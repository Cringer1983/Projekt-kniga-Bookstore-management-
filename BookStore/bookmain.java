package bookstore;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class bookmain {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {

		Connection conn = DBmanager.getInstance();
		bookInventoryDAO dao = new bookInventoryDAO();
		Scanner sc = new Scanner(System.in);

		if (conn == null) {
			System.out.println("DB 접속 실패 - DBmanager 설정과 ojdbc jar 등록을 확인하세요.");
			sc.close();
			return;
		}

		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("접속 성공");
		boolean tf = true;

		while (tf) {
			System.out.print("메뉴<[1]입력[2]출력[3]검색[4]수정[5]삭제[0]종료> : ");
			String menu = sc.nextLine().trim();

			if ("1".equals(menu)) {
				long bid = bookDAO.BookidCount();
				System.out.println("북 ID : " + bid);

				System.out.print("제목 : ");
				String tit = sc.nextLine().trim();
				System.out.print("가격 : ");
				String mn = sc.nextLine().trim();
				System.out.print("출판사 : ");
				String pb = sc.nextLine().trim();
				System.out.print("출시 일자(yyyy-MM-dd) : ");
				String ynd = sc.nextLine().trim();

				long iid = inventoryDAO.InventoryCount();
				System.out.println("재고 ID : " + iid);
				System.out.print("재고 : ");
				String qty = sc.nextLine().trim();

				try {
					Date dateResult = SDF.parse(ynd);

					bookInventoryDTO dto = new bookInventoryDTO();
					dto.setBookId(bid);
					dto.setTitle(tit);
					dto.setPrice(Integer.parseInt(mn));
					dto.setPublisher(pb);
					dto.setPubDate(dateResult);
					dto.setInventoryId(iid);
					dto.setQty(Integer.parseInt(qty));

					System.out.println(dao.BookInventoryInsert(dto) ? "입력완료" : "입력실패");
				} catch (Exception e) {
					System.out.println("입력값 오류 : " + e.getMessage());
				}

			} else if ("2".equals(menu)) {
				List<bookInventoryDTO> list = dao.BookInventoryList();
				if (list.isEmpty()) {
					System.out.println("등록된 도서가 없습니다.");
				}
				for (bookInventoryDTO a : list) {
					System.out.println(a);
				}

			} else if ("3".equals(menu)) {
				System.out.print("검색방법(1, 북 ID /2, 제목) : ");
				String s = sc.nextLine().trim();

				if ("1".equals(s)) {
					System.out.print("ID : ");
					try {
						long bid = Long.parseLong(sc.nextLine().trim());
						System.out.println(dao.BookSearchId(bid));
					} catch (Exception e) {
						System.out.println("숫자를 입력하세요.");
					}
				} else {
					System.out.print("제목 : ");
					String title = sc.nextLine().trim();
					List<bookInventoryDTO> list = dao.BookSearchTitle(title);
					if (list.isEmpty()) {
						System.out.println("검색 결과가 없습니다.");
					}
					for (bookInventoryDTO a : list) {
						System.out.println(a);
					}
				}

			} else if ("4".equals(menu)) {
				System.out.print("북 ID : ");
				String bidStr = sc.nextLine().trim();
				System.out.print("제목 : ");
				String tit = sc.nextLine().trim();
				System.out.print("가격 : ");
				String mn = sc.nextLine().trim();
				System.out.print("출판사 : ");
				String pb = sc.nextLine().trim();
				System.out.print("출시 일자(yyyy-MM-dd) : ");
				String ynd = sc.nextLine().trim();
				System.out.print("재고 ID : ");
				String iidStr = sc.nextLine().trim();
				System.out.print("재고 : ");
				String qty = sc.nextLine().trim();

				try {
					Date dateResult = SDF.parse(ynd);

					bookInventoryDTO dto = new bookInventoryDTO();
					dto.setBookId(Long.parseLong(bidStr));
					dto.setTitle(tit);
					dto.setPrice(Integer.parseInt(mn));
					dto.setPublisher(pb);
					dto.setPubDate(dateResult);
					dto.setInventoryId(Long.parseLong(iidStr));
					dto.setQty(Integer.parseInt(qty));

					System.out.println(dao.BookInventoryUpdate(dto) ? "수정완료" : "수정실패");
				} catch (Exception e) {
					System.out.println("입력값 오류 : " + e.getMessage());
				}

			} else if ("5".equals(menu)) {
				System.out.print("북 ID : ");
				try {
					long bid = Long.parseLong(sc.nextLine().trim());
					System.out.println(dao.BookInventoryDelete(bid) ? "삭제완료" : "삭제실패");
				} catch (Exception e) {
					System.out.println("숫자를 입력하세요.");
				}

			} else if ("0".equals(menu)) {
				System.out.println("프로그램을 종료합니다.");
				tf = false;

			} else {
				System.out.println("숫자를 다시 입력하세요.");
			}
		}

		sc.close();
	}
}
