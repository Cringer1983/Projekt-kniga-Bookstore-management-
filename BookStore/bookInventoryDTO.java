package bookstore;

import java.util.Date;

public class bookInventoryDTO {
	private Long bookId; //책 아이디
    private String title; //책 제목
    private int price; //가격
    private String publisher; //출판사
    private Date pubDate; //출반 일지
    private Long inventoryId; //재고 아이디
    private int qty; //재고 수량

    public bookInventoryDTO() {}

    public bookInventoryDTO(Long bookId, String title, int price, String publisher, Date pubDate, Long inventoryId, int qty) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.inventoryId = inventoryId;
        this.qty = qty;
    }

    /**
     * DB 저장/수정 전 유효성 검사 (Validation)
     */
    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("도서 제목은 필수 입력값입니다.");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("도서 제목은 최대 200자까지 입력 가능합니다. (현재: " + title.length() + "자)");
        }
        if (price == 0 || price < 0) {
            throw new IllegalArgumentException("가격은 null이거나 음수일 수 없습니다.");
        }
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("출판사는 필수 입력값입니다.");
        }
        if (publisher.length() > 100) {
            throw new IllegalArgumentException("출판사명은 최대 100자까지 입력 가능합니다. (현재: " + publisher.length() + "자)");
        }
        if (pubDate == null) {
            throw new IllegalArgumentException("출간일은 필수 입력값입니다.");
        }
        if (qty != 0 && qty < 0) {
            throw new IllegalArgumentException("재고 수량은 음수일 수 없습니다.");
        }
    }

    // Getter & Setter
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Date getPubDate() { return pubDate; }
    public void setPubDate(Date pubDate) { this.pubDate = pubDate; }

    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    @Override
    public String toString() {
        return "bookInventoryDTO{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", publisher='" + publisher + '\'' +
                ", pubDate=" + pubDate +
                ", inventoryId=" + inventoryId +
                ", qty=" + qty +
                '}';
    }
}
