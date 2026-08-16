package bookstore;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class mainframe extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;

	private JComboBox<String> cbxSearch;
	private JTextField tfSearch; // 검색
	private JButton btnSearch; // 검색

	private JTextField tfBookId; // 도서 코드
	private JTextField tfTitle; // 도서 제목
	private JTextField tfPrice; // 가격
	private JTextField tfPub; // 출판사	
	private JTextField tfPubDate; // 출판일
	private JTextField tfInvId; // 재고 ID
	private JTextField tfQty; // 재고 수량

	private JButton btnInsert; // 추가
	private JButton btnUpdate; // 수정
	private JButton btnDelete; // 삭제
	private JButton btnReset; 

	// 테이블
	private DefaultTableModel tableModel;
	private JTable bookTable;

	// DAO
	private final bookInventoryDAO biDao = new bookInventoryDAO();

	// 날짜 표시 형식
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public mainframe() {
		initUI();
	}

	private void initUI() {
		setTitle("AOBA Book Store Book Manager DEV.ver");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createPanel();
		createComponent();
		addComponent();
		event();
		setVisible(true);

		checkConnection();
		loadTable();
	}

	/** 시작 시 DB 접속 확인 */
	private void checkConnection() {
		if (!DBmanager.isConnectable()) {
			JOptionPane.showMessageDialog(this,
					"DB에 접속할 수 없습니다.\n"
					+ "1) Oracle 서비스 실행 여부\n"
					+ "2) DBmanager.java의 URL / 계정 / 비밀번호\n"
					+ "3) ojdbc jar의 Build Path 등록 여부\n"
					+ "위 3가지를 확인해주세요.",
					"DB 연결 실패", JOptionPane.ERROR_MESSAGE);
		}
	}

	// 패널 생성
	private void createPanel() {
		setLayout(new BorderLayout());
		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		topPanel.setBorder(BorderFactory.createTitledBorder("검색"));

		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		bottomPanel = new JPanel(new BorderLayout());
	}

	private void createComponent() {
		// 검색창 + 콤보박스
		String[] searchop = { "전체", "도서ID", "도서명", "출판사", "가격", "출판일", "재고ID", "재고" };
		cbxSearch = new JComboBox<String>(searchop);
		tfSearch = new JTextField(20);
		btnSearch = new JButton("검색");

		// 테이블
		String[] columnNames = { "도서ID", "도서명", "출판사", "가격", "출판일", "재고ID", "재고" };

		tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		bookTable = new JTable(tableModel);
		// 순서 변경방지
		bookTable.getTableHeader().setReorderingAllowed(false);
		bookTable.setRowHeight(25);

		// 하단 CUD
		tfBookId = new JTextField();
		tfBookId.setEditable(false);
		tfTitle = new JTextField();
		tfPrice = new JTextField();
		tfPub = new JTextField();
		tfPubDate = new JTextField();
		tfInvId = new JTextField();
		tfInvId.setEditable(false);
		tfQty = new JTextField();

		btnInsert = new JButton("도서 등록");
		btnUpdate = new JButton("수정");
		btnDelete = new JButton("도서 삭제");
		btnReset = new JButton("초기화");
	}

	private void addComponent() {
		// 상단 패널
		topPanel.add(new JLabel("검색 조건 : "));
		topPanel.add(cbxSearch);
		topPanel.add(tfSearch);
		topPanel.add(btnSearch);

		centerPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

		JPanel inputFormPanel = new JPanel(new GridBagLayout());
		inputFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 1.0;
		inputFormPanel.add(new JLabel("도서ID:", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfBookId, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("제목 : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfTitle, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("출판사 : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfPub, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("가격 : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfPrice, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("출판일 : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfPubDate, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("재고ID : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 10);
		gbc.weightx = 1.0;
		inputFormPanel.add(tfInvId, gbc);

		gbc.insets = new Insets(0, 0, 0, 4);
		gbc.weightx = 0.0;
		inputFormPanel.add(new JLabel("재고 : ", JLabel.CENTER), gbc);

		gbc.insets = new Insets(0, 0, 0, 0); // 마지막은 넣지않기
		gbc.weightx = 1.0;
		inputFormPanel.add(tfQty, gbc);

		JPanel buttomFlowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		buttomFlowPanel.add(btnInsert);
		buttomFlowPanel.add(btnUpdate);
		buttomFlowPanel.add(btnDelete);
		buttomFlowPanel.add(btnReset);

		bottomPanel.add(inputFormPanel, BorderLayout.CENTER);
		bottomPanel.add(buttomFlowPanel, BorderLayout.SOUTH);

		add(topPanel, BorderLayout.NORTH); // 검색
		add(centerPanel, BorderLayout.CENTER); // 목록
		add(bottomPanel, BorderLayout.SOUTH); // 입력란 + 버튼
	}

	// 이벤트 처리
	private void event() {
		// 테이블 데이터 선택
		bookTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectRw = bookTable.getSelectedRow();
				if (selectRw != -1) {
					tfBookId.setText(str(tableModel.getValueAt(selectRw, 0)));
					tfTitle.setText(str(tableModel.getValueAt(selectRw, 1)));
					tfPub.setText(str(tableModel.getValueAt(selectRw, 2)));
					tfPrice.setText(str(tableModel.getValueAt(selectRw, 3)));
					tfPubDate.setText(str(tableModel.getValueAt(selectRw, 4)));
					tfInvId.setText(str(tableModel.getValueAt(selectRw, 5)));
					tfQty.setText(str(tableModel.getValueAt(selectRw, 6)));
				}
			}
		});

		// 등록
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertBook();
			}
		});

		// 수정
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateBook();
			}
		});

		// 삭제
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteBook();
			}
		});

		btnSearch.addActionListener(e -> search());
		tfSearch.addActionListener(e -> search());
		btnReset.addActionListener(e -> {
			clearFields();
			tfSearch.setText("");
			cbxSearch.setSelectedIndex(0);
			loadTable();
		});
	}

	// ================= CRUD =================

	/** 도서 + 재고 동시 등록 */
	private void insertBook() {
		String title = tfTitle.getText().trim();
		String price = tfPrice.getText().trim();
		String publisher = tfPub.getText().trim();
		String pubDate = tfPubDate.getText().trim();
		String qty = tfQty.getText().trim();

		if (title.isEmpty() || price.isEmpty() || publisher.isEmpty() || pubDate.isEmpty() || qty.isEmpty()) {
			JOptionPane.showMessageDialog(this, "모든 입력란을 작성해 주세요.", "입력 경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			bookInventoryDTO dto = new bookInventoryDTO();
			dto.setTitle(title);
			dto.setPrice(Integer.parseInt(price));
			dto.setPublisher(publisher);
			try {
				dto.setPubDate(Date.valueOf(pubDate));
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("출판일은 YYYY-MM-DD 형식으로 입력해주세요");
			}
			dto.setQty(Integer.parseInt(qty));

			// PK 자동 채번
			dto.setBookId(bookDAO.BookidCount());
			dto.setInventoryId(inventoryDAO.InventoryCount());

			dto.validate();

			if (biDao.BookInventoryInsert(dto)) {
				JOptionPane.showMessageDialog(this, "등록 완료");
				clearFields();
				loadTable();
			} else {
				JOptionPane.showMessageDialog(this, "등록 실패 (콘솔 로그를 확인하세요)", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해주세요");
		} catch (IllegalArgumentException ex) {
			String ms = (ex.getMessage() != null) ? ex.getMessage() : "Wrong input data";
			JOptionPane.showMessageDialog(this, ms, "input Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 도서 + 재고 동시 수정 */
	private void updateBook() {
		String bookId = tfBookId.getText().trim();
		String invId = tfInvId.getText().trim();

		if (bookId.isEmpty() || invId.isEmpty()) {
			JOptionPane.showMessageDialog(this, "수정할 도서를 목록에서 먼저 선택해주세요.", "선택 필요", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String title = tfTitle.getText().trim();
		String price = tfPrice.getText().trim();
		String publisher = tfPub.getText().trim();
		String pubDate = tfPubDate.getText().trim();
		String qty = tfQty.getText().trim();

		if (title.isEmpty() || price.isEmpty() || publisher.isEmpty() || pubDate.isEmpty() || qty.isEmpty()) {
			JOptionPane.showMessageDialog(this, "모든 입력란을 작성해 주세요.", "입력 경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			bookInventoryDTO dto = new bookInventoryDTO();
			dto.setBookId(Long.parseLong(bookId));
			dto.setInventoryId(Long.parseLong(invId));
			dto.setTitle(title);
			dto.setPrice(Integer.parseInt(price));
			dto.setPublisher(publisher);
			try {
				dto.setPubDate(Date.valueOf(pubDate));
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("출판일은 YYYY-MM-DD 형식으로 입력해주세요");
			}
			dto.setQty(Integer.parseInt(qty));

			dto.validate();

			if (biDao.BookInventoryUpdate(dto)) {
				JOptionPane.showMessageDialog(this, "수정 완료");
				clearFields();
				loadTable();
			} else {
				JOptionPane.showMessageDialog(this, "수정 실패 (콘솔 로그를 확인하세요)", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해주세요");
		} catch (IllegalArgumentException ex) {
			String ms = (ex.getMessage() != null) ? ex.getMessage() : "Wrong input data";
			JOptionPane.showMessageDialog(this, ms, "input Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 도서 + 재고 동시 삭제 */
	private void deleteBook() {
		String bookId = tfBookId.getText().trim();

		if (bookId.isEmpty()) {
			JOptionPane.showMessageDialog(this, "삭제할 도서를 목록에서 먼저 선택해주세요.", "선택 필요", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int answer = JOptionPane.showConfirmDialog(this,
				"도서ID " + bookId + " 을(를) 재고와 함께 삭제할까요?",
				"삭제 확인", JOptionPane.YES_NO_OPTION);

		if (answer != JOptionPane.YES_OPTION) return;

		try {
			if (biDao.BookInventoryDelete(Long.parseLong(bookId))) {
				JOptionPane.showMessageDialog(this, "삭제 완료");
				clearFields();
				loadTable();
			} else {
				JOptionPane.showMessageDialog(this, "삭제 실패 (콘솔 로그를 확인하세요)", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "도서ID가 올바르지 않습니다.");
		}
	}

	// ================= 목록 / 검색 =================

	/** 전체 목록 조회 */
	private void loadTable() {
		renderTable(biDao.BookInventoryList());
	}

	/** 조회 결과를 테이블에 표시 */
	private void renderTable(List<bookInventoryDTO> list) {
		tableModel.setRowCount(0);

		for (bookInventoryDTO dto : list) {
			tableModel.addRow(new Object[] {
					dto.getBookId(),
					dto.getTitle(),
					dto.getPublisher(),
					dto.getPrice(),
					(dto.getPubDate() != null) ? sdf.format(dto.getPubDate()) : "",
					dto.getInventoryId(),
					dto.getQty()
			});
		}
	}

	/** 조건 검색 (DB 조회 후 조건에 맞는 행만 표시) */
	private void search() {
		String category = (String) cbxSearch.getSelectedItem();
		String keyword = tfSearch.getText().trim();

		if (keyword.isEmpty()) {
			loadTable();
			return;
		}

		List<bookInventoryDTO> all = biDao.BookInventoryList();
		List<bookInventoryDTO> result = new ArrayList<bookInventoryDTO>();

		for (bookInventoryDTO dto : all) {
			String target;

			if ("도서ID".equals(category)) {
				target = str(dto.getBookId());
			} else if ("도서명".equals(category)) {
				target = str(dto.getTitle());
			} else if ("출판사".equals(category)) {
				target = str(dto.getPublisher());
			} else if ("가격".equals(category)) {
				target = str(dto.getPrice());
			} else if ("출판일".equals(category)) {
				target = (dto.getPubDate() != null) ? sdf.format(dto.getPubDate()) : "";
			} else if ("재고ID".equals(category)) {
				target = str(dto.getInventoryId());
			} else if ("재고".equals(category)) {
				target = str(dto.getQty());
			} else { // 전체
				target = str(dto.getBookId()) + str(dto.getTitle()) + str(dto.getPublisher())
						+ str(dto.getPrice())
						+ ((dto.getPubDate() != null) ? sdf.format(dto.getPubDate()) : "")
						+ str(dto.getInventoryId()) + str(dto.getQty());
			}

			if (target.contains(keyword)) {
				result.add(dto);
			}
		}

		renderTable(result);

		if (result.isEmpty()) {
			JOptionPane.showMessageDialog(this, "검색 결과가 없음");
		}
	}

	// ================= 유틸 =================

	private void clearFields() {
		tfBookId.setText("");
		tfTitle.setText("");
		tfPub.setText("");
		tfPrice.setText("");
		tfPubDate.setText("");
		tfInvId.setText("");
		tfQty.setText("");
		bookTable.clearSelection();
	}

	private String str(Object o) {
		return (o == null) ? "" : o.toString();
	}

}
