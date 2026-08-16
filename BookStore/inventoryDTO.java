package bookstore;

public class inventoryDTO {
	private Long inventoryId;//재고 아이디
    private Long bookId;//북 아이디
    private int qty;//재고 수량

    public inventoryDTO() {}

    public inventoryDTO(Long inventoryId, Long bookId, int qty) {
        this.inventoryId = inventoryId;
        this.bookId = bookId;
        this.qty = qty;
    }

    /**
     * DB 저장/수정 전 유효성 검사 (Validation)
     */
    public void validate() {
        if (bookId == null) {
            throw new IllegalArgumentException("참조할 도서 ID(bookId)는 필수 입력값입니다.");
        }
        if (qty == 0) {
            throw new IllegalArgumentException("재고 수량은 필수 입력값입니다.");
        }
        if (qty < 0) {
            throw new IllegalArgumentException("재고 수량은 음수일 수 없습니다. (입력값: " + qty + ")");
        }
    }

    // Getter & Setter
    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    @Override
    public String toString() {
        return "inventoryDTO{" +
                "inventoryId=" + inventoryId +
                ", bookId=" + bookId +
                ", qty=" + qty +
                '}';
    }
}
