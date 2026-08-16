package bookstore;
import java.util.Date;

public class bookDTO {
	private Long bookId;//북 아이디
    private String title;//책 제목
    private int price;//책 가격
    private String publisher;//출판사
    private Date pubDate;//출반 일자

    public bookDTO() {}

    public bookDTO(Long bookId, String title, int price, String publisher, Date pubDate) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.publisher = publisher;
        this.pubDate = pubDate;
    }

    /**
     * DB 저장/수정 전 유효성 검사 (Validation)
     * @throws IllegalArgumentException 제약조건 위반 시 예외 발생
     */
    public void validate() {
        // title 검증 (NOT NULL & VARCHAR2(200))
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("도서 제목은 필수 입력값입니다.");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("도서 제목은 최대 200자까지 입력 가능합니다. (현재: " + title.length() + "자)");
        }

        // price 검증 (NOT NULL & NUMBER >= 0)
        if (price == 0) {
            throw new IllegalArgumentException("가격은 필수 입력값입니다.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다. (입력값: " + price + ")");
        }

        // publisher 검증 (NOT NULL & VARCHAR2(100))
        if (publisher == null || publisher.trim().isEmpty()) {
            throw new IllegalArgumentException("출판사는 필수 입력값입니다.");
        }
        if (publisher.length() > 100) {
            throw new IllegalArgumentException("출판사명은 최대 100자까지 입력 가능합니다. (현재: " + publisher.length() + "자)");
        }

        // pubDate 검증 (NOT NULL)
        if (pubDate == null) {
            throw new IllegalArgumentException("출간일은 필수 입력값입니다.");
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

    @Override
    public String toString() {
        return "bookDTO{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", publisher='" + publisher + '\'' +
                ", pubDate=" + pubDate +
                '}';
    }

}
