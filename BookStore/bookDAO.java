package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class bookDAO {
	public static long BookidCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select nvl(max(book_id), 0) + 1 as cnt from book";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getLong("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn, rs);
		}

		return 0;
	}

	// 책 입력
	public boolean BookInsert(bookDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "insert into book (book_id, title, price, publisher, pub_date) values (?,?,?,?,?)";

		try {
			dto.validate();
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getBookId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setInt(3, dto.getPrice());
			pstmt.setString(4, dto.getPublisher());
			if (dto.getPubDate() != null) {
				pstmt.setDate(5, new java.sql.Date(dto.getPubDate().getTime()));
			} else {
				pstmt.setNull(5, java.sql.Types.DATE);
			}
			pstmt.executeUpdate();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn);
		}

		return false;
	}

	// 출력(목록용) - book 테이블
	public List<bookDTO> BookList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<bookDTO> list = new ArrayList<bookDTO>();

		String sql = "select * from book order by book_id asc";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bookDTO dto = new bookDTO();
				dto.setBookId(rs.getLong("book_id"));
				dto.setTitle(rs.getString("title"));
				dto.setPrice(rs.getInt("price"));
				dto.setPublisher(rs.getString("publisher"));
				dto.setPubDate(rs.getDate("pub_date"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn, rs);
		}

		return list;
	}

	// 수정 - book 테이블
	public boolean BookUpdate(bookDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "update book set title = ?, price = ?, publisher = ?, pub_date = ? where book_id = ?";

		try {
			dto.validate();
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setInt(2, dto.getPrice());
			pstmt.setString(3, dto.getPublisher());
			if (dto.getPubDate() != null) {
				pstmt.setDate(4, new java.sql.Date(dto.getPubDate().getTime()));
			} else {
				pstmt.setNull(4, java.sql.Types.DATE);
			}
			pstmt.setLong(5, dto.getBookId());
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn);
		}
		return false;
	}

	// 삭제 - book 테이블
	public boolean BookDelete(long bookId) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "delete from book where book_id = ?";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bookId);
			return pstmt.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn);
		}

		return false;
	}

	// 검색(북 아이디)
	public bookDTO BookSearchId(long bookId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from book where book_id = ?";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bookId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				bookDTO dto = new bookDTO();
				dto.setBookId(rs.getLong("book_id"));
				dto.setTitle(rs.getString("title"));
				dto.setPrice(rs.getInt("price"));
				dto.setPublisher(rs.getString("publisher"));
				dto.setPubDate(rs.getDate("pub_date"));
				return dto;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn, rs);
		}

		return null;
	}

	// 검색(제목)
	public List<bookDTO> BookSearchTitle(String search) {
		List<bookDTO> list1 = BookList();
		List<bookDTO> list2 = new ArrayList<bookDTO>();

		if (search == null) return list2;

		for (bookDTO a : list1) {
			if (a.getTitle() != null && a.getTitle().contains(search)) {
				list2.add(a);
			}
		}

		return list2;
	}
}


