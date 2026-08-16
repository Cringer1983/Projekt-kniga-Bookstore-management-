package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class inventoryDAO {
	public static long InventoryCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		
		String sql = "select nvl(max(inventory_id), 0) + 1 as cnt from inventory";

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

		// 재고 입력
	public boolean InventoryInsert(inventoryDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "insert into inventory (inventory_id, book_id, qty) values (?,?,?)";
		try {
			dto.validate();
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getInventoryId());
			pstmt.setLong(2, dto.getBookId());
			pstmt.setInt(3, dto.getQty());
			pstmt.executeUpdate();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn);
		}

		return false;
	}

		// 출력(목록용) - inventory 테이블
	public List<inventoryDTO> InventoryList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<inventoryDTO> list = new ArrayList<inventoryDTO>();

		String sql = "select * from inventory order by inventory_id asc";

		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				inventoryDTO dto = new inventoryDTO();
				dto.setInventoryId(rs.getLong("inventory_id"));
				dto.setBookId(rs.getLong("book_id"));
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

		// 수정 - inventory 테이블
	public boolean InventoryUpdate(inventoryDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update inventory set qty = ? where inventory_id = ?";
		try {
			conn = DBmanager.getInstance();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getQty());
			pstmt.setLong(2, dto.getInventoryId());
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBmanager.clos(pstmt, conn);
		}
		return false;
	}

		/** 기존 코드 호환용 (메서드명 오타 유지) */
	public boolean InventoryUpdaet(inventoryDTO dto) {
		return InventoryUpdate(dto);
	}

	// 삭제 - inventory 테이블
	public boolean InventoryDelete(long bookId) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "delete from inventory where book_id = ?";

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

		// 검색(재고 아이디)
	public inventoryDTO InventorySearchId(long inventoryId) {
		List<inventoryDTO> list = InventoryList();
			for (inventoryDTO a : list) {
			if (a.getInventoryId() != null && a.getInventoryId() == inventoryId) {
				return a;
			}
		}
		return null;
	}
}

