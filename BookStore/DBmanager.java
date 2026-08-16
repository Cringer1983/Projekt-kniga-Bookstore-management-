package bookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBmanager {

	// ===== DB 접속 설정 =====
		private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
		private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
		private static final String USER = "jsl28";
		private static final String PASSWORD = "1234";
		// ========================

		public static Connection getInstance() {
			Connection conn = null;

			try {
				Class.forName(DRIVER);
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (ClassNotFoundException e) {
				System.err.println("[DBmanager] ojdbc 드라이버를 찾을 수 없습니다. lib 폴더의 jar를 Build Path에 추가했는지 확인하세요.");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return conn;
		}

		public static void clos(PreparedStatement pstmt, Connection conn) {
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void clos(PreparedStatement pstmt, Connection conn, ResultSet rs) {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			clos(pstmt, conn);
		}

		/** 트랜잭션용 - PreparedStatement 2개를 함께 정리 */
		public static void clos(PreparedStatement pstmt1, PreparedStatement pstmt2, Connection conn) {
			try {
				if (pstmt1 != null) pstmt1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			clos(pstmt2, conn);
		}

		/** 연결 가능 여부 확인용 */
		public static boolean isConnectable() {
			Connection conn = getInstance();
			if (conn == null) return false;
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

}
