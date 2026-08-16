package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class bookInventoryDAO {
	public boolean BookInventoryInsert(bookInventoryDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String sql1 = "insert into book (book_id, title, price, publisher, pub_date) values (?,?,?,?,?)";
		String sql2 = "insert into inventory (inventory_id, book_id, qty) values (?,?,?)";

		try {
			dto.validate();
			conn = DBmanager.getInstance();
			if (conn == null) return false;
			conn.setAutoCommit(false);

			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setLong(1, dto.getBookId());
			pstmt1.setString(2, dto.getTitle());
			pstmt1.setInt(3, dto.getPrice());
			pstmt1.setString(4, dto.getPublisher());
			if (dto.getPubDate() != null) {
				pstmt1.setDate(5, new java.sql.Date(dto.getPubDate().getTime()));
			} else {
				pstmt1.setNull(5, java.sql.Types.DATE);
			}
			pstmt1.executeUpdate();

			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setLong(1, dto.getInventoryId());
			pstmt2.setLong(2, dto.getBookId());
			pstmt2.setInt(3, dto.getQty());
			pstmt2.executeUpdate();

			conn.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			restoreAutoCommit(conn);
			DBmanager.clos(pstmt1, pstmt2, conn);
		}
		return false;
	}

	// 출력(목록용) - book + inventory 조인
	public List<bookInventoryDTO> BookInventoryList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<bookInventoryDTO> list = new ArrayList<bookInventoryDTO>();

		String sql = "select b.book_id, b.title, b.price, b.publisher, b.pub_date, i.inventory_id, i.qty "
				+ "from book b "
				+ "join inventory i "
				+ "on b.book_id = i.book_id "
				+ "order by b.book_id asc";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bookInventoryDTO dto = new bookInventoryDTO();
				dto.setBookId(rs.getLong("book_id"));
				dto.setTitle(rs.getString("title"));
				dto.setPrice(rs.getInt("price"));
				dto.setPublisher(rs.getString("publisher"));
				dto.setPubDate(rs.getDate("pub_date"));
				dto.setInventoryId(rs.getLong("inventory_id"));
				dto.setQty(rs.getInt("qty"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn, rs);
		}

		return list;
	}

	// 동시 수정
	public boolean BookInventoryUpdate(bookInventoryDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String sql1 = "update book set title = ?, price = ?, publisher = ?, pub_date = ? where book_id = ?";
		String sql2 = "update inventory set qty = ? where inventory_id = ?";

		try {
			dto.validate();
			conn = DBmanager.getInstance();
			if (conn == null) return false;
			conn.setAutoCommit(false);

			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, dto.getTitle());
			pstmt1.setInt(2, dto.getPrice());
			pstmt1.setString(3, dto.getPublisher());
			if (dto.getPubDate() != null) {
				pstmt1.setDate(4, new java.sql.Date(dto.getPubDate().getTime()));
			} else {
				pstmt1.setNull(4, java.sql.Types.DATE);
			}
			pstmt1.setLong(5, dto.getBookId());
			pstmt1.executeUpdate();

			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, dto.getQty());
			pstmt2.setLong(2, dto.getInventoryId());
			pstmt2.executeUpdate();

			conn.commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			restoreAutoCommit(conn);
			DBmanager.clos(pstmt1, pstmt2, conn);
		}
		return false;
	}

	/** 기존 코드 호환용 (메서드명 오타 유지) */
	public boolean BookInventoryUpdaet(bookInventoryDTO dto) {
		return BookInventoryUpdate(dto);
	}

	// 동시 삭제 (자식 → 부모 순서)
	public boolean BookInventoryDelete(long bookId) {
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String sql1 = "delete from inventory where book_id = ?";
		String sql2 = "delete from book where book_id = ?";

		try {
			conn = DBmanager.getInstance();
			if (conn == null) return false;
			conn.setAutoCommit(false);

			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setLong(1, bookId);
			pstmt1.executeUpdate();

			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setLong(1, bookId);
			int result = pstmt2.executeUpdate();

			conn.commit();
			return result > 0;

		} catch (Exception e) {
			e.printStackTrace();
			rollback(conn);
		} finally {
			restoreAutoCommit(conn);
			DBmanager.clos(pstmt1, pstmt2, conn);
		}

		return false;
	}

	// 검색(북 아이디)
	public bookInventoryDTO BookSearchId(long bookId) {
		List<bookInventoryDTO> list = BookInventoryList();

		for (bookInventoryDTO a : list) {
			if (a.getBookId() != null && a.getBookId() == bookId) {
				return a;
			}
		}

		return null;
	}

	// 검색(제목)
	public List<bookInventoryDTO> BookSearchTitle(String search) {
		List<bookInventoryDTO> list1 = BookInventoryList();
		List<bookInventoryDTO> list2 = new ArrayList<bookInventoryDTO>();

		if (search == null) return list2;

		for (bookInventoryDTO a : list1) {
			if (a.getTitle() != null && a.getTitle().contains(search)) {
				list2.add(a);
			}
		}

		return list2;
	}

	// ===== 내부 유틸 =====
	private void rollback(Connection conn) {
		if (conn == null) return;
		try {
			conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restoreAutoCommit(Connection conn) {
		if (conn == null) return;
		try {
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
