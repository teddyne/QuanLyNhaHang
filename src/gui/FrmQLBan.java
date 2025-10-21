package gui;
import dao.Ban_DAO;
import dao.KhuVuc_DAO;
import dao.LoaiBan_DAO;
import entity.Ban;
import entity.KhuVuc;
import entity.LoaiBan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connectSQL.ConnectSQL;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmQLBan extends ThanhTacVu {
    private final Ban_DAO banDAO;
    private final KhuVuc_DAO khuVucDAO;
    private final LoaiBan_DAO loaiBanDAO;
    private final DefaultTableModel modelBan;
    private final JTable tblBan;
    private final JTextField txtMaBan, txtGhiChu, txtSoCho, txtTrangThai;
    private final JComboBox<String> cbKhuVuc;
    private final JComboBox<LoaiBan> cbLoaiBan;
    private final JButton btnThem, btnXoa, btnSua, btnLamMoi, btnLoaiBan, btnTraCuu;
    private final Consumer<Void> refreshCallback;

    public FrmQLBan(Ban_DAO banDAO, KhuVuc_DAO khuVucDAO, LoaiBan_DAO loaiBanDAO, Consumer<Void> refreshCallback) throws SQLException {
        super();
        setTitle("Quản Lý Bàn");
        ConnectSQL.getInstance().connect();
        this.banDAO = banDAO;
        this.khuVucDAO = khuVucDAO;
        this.loaiBanDAO = loaiBanDAO;
        this.refreshCallback = refreshCallback;

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Panel tiêu đề
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(169, 55, 68));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ BÀN");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2), "Thông Tin Bàn",
                0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        inputPanel.setPreferredSize(new Dimension(0, 220));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        // Cột 1
        // Mã bàn
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaBan = new JLabel("Mã bàn");
        lblMaBan.setFont(labelFont);
        fieldsPanel.add(lblMaBan, gbc);
        txtMaBan = new JTextField(15);
        txtMaBan.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtMaBan, gbc);

        // Khu vực
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblKhuVuc = new JLabel("Khu vực");
        lblKhuVuc.setFont(labelFont);
        fieldsPanel.add(lblKhuVuc, gbc);
        cbKhuVuc = new JComboBox<>();
        cbKhuVuc.setFont(fieldFont);
        cbKhuVuc.addItem("Tất cả");
        try {
            List<KhuVuc> khuVucs = khuVucDAO.getAll();
            for (KhuVuc k : khuVucs) {
                cbKhuVuc.addItem(k.getMaKhuVuc() + " - " + k.getTenKhuVuc());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khu vực: " + ex.getMessage());
        }
        gbc.gridx = 1;
        fieldsPanel.add(cbKhuVuc, gbc);

        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblTrangThai = new JLabel("Trạng thái");
        lblTrangThai.setFont(labelFont);
        fieldsPanel.add(lblTrangThai, gbc);
        
        txtTrangThai = new JTextField(15);
        txtTrangThai.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtTrangThai, gbc);

        // Spacer giữa cột
        gbc.weightx = 0.5;
        for (int y = 0; y <= 2; y++) {
            gbc.gridx = 2;
            gbc.gridy = y;
            fieldsPanel.add(Box.createHorizontalStrut(50), gbc);
        }
        gbc.weightx = 1.0;

        // Cột 2
        // Loại bàn
        gbc.gridx = 3;
        gbc.gridy = 0;
        JLabel lblLoaiBan = new JLabel("Loại bàn");
        lblLoaiBan.setFont(labelFont);
        fieldsPanel.add(lblLoaiBan, gbc);
        
        cbLoaiBan = new JComboBox<>();
        LoaiBan tatCa = new LoaiBan();
        tatCa.setMaLoai("");
        tatCa.setTenLoai("Tất cả");
        cbLoaiBan.addItem(tatCa);
        try {
            List<LoaiBan> loaiBanList = loaiBanDAO.getAll();
            for (LoaiBan lb : loaiBanList) {
                cbLoaiBan.addItem(lb);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách loại bàn: " + ex.getMessage());
        }
        cbLoaiBan.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(cbLoaiBan, gbc);

        // Ghi chú
        gbc.gridx = 3;
        gbc.gridy = 1;
        JLabel lblGhiChu = new JLabel("Ghi chú");
        lblGhiChu.setFont(labelFont);
        fieldsPanel.add(lblGhiChu, gbc);
        txtGhiChu = new JTextField(15);
        txtGhiChu.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtGhiChu, gbc);

        // Số chỗ ngồi
        gbc.gridx = 3;
        gbc.gridy = 2;
        JLabel lblSoCho = new JLabel("Số chỗ ngồi");
        lblSoCho.setFont(labelFont);
        fieldsPanel.add(lblSoCho, gbc);
        txtSoCho = new JTextField(15);
        txtSoCho.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtSoCho, gbc);
        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Dimension buttonSize = new Dimension(150, 50);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);

        // === Khởi tạo nút ===
        btnThem = taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont);       // xanh lá
        btnSua = taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);         // xanh dương
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);          // đỏ
        btnLamMoi = taoNut("Làm mới", new Color(149, 165, 166), buttonSize, buttonFont); // xám nhạt
        btnLoaiBan = taoNut("Loại Bàn", new Color(255, 193, 7), buttonSize, buttonFont); // vàng
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), buttonSize, buttonFont);  // tím

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnLoaiBan);
        buttonPanel.add(btnTraCuu);

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng danh sách bàn
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Bàn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã bàn", "Khu vực", "Trạng thái", "Ghi chú", "Số chỗ ngồi", "Loại bàn"};
        modelBan = new DefaultTableModel(columns, 0);
        tblBan = new JTable(modelBan);
        tblBan.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblBan.setRowHeight(30);
        JScrollPane scrollBan = new JScrollPane(tblBan);
        tablePanel.add(scrollBan, BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu
        loadData();

        // Sự kiện nút
        btnThem.addActionListener(e -> themBan());
        btnSua.addActionListener(e -> luuBan());
        btnXoa.addActionListener(e -> xoaBan());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnLoaiBan.addActionListener(e -> {
            try {
                new FrmLoaiBan(loaiBanDAO, v -> loadLoaiBan()).setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        btnTraCuu.addActionListener(e -> traCuuBan());

        // Sự kiện bảng
        tblBan.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            int row = tblBan.getSelectedRow();
	            if (row >= 0) {
	                txtMaBan.setText((String) tblBan.getValueAt(row, 0));
	                String khuVuc = (String) modelBan.getValueAt(row, 1);
	                try {
	                    List<KhuVuc> khuVucs = khuVucDAO.getAll();
	                    String tenKhuVuc = (String) tblBan.getValueAt(row, 1);
	                    for (KhuVuc kv : khuVucs) {
	                        if (kv.getTenKhuVuc().equals(tenKhuVuc)) {
	                            cbKhuVuc.setSelectedItem(kv.getMaKhuVuc() + " - " + kv.getTenKhuVuc());
	                            break;
	                        }
	                    }
	                    List<LoaiBan> loaiBanList = loaiBanDAO.getAll();
	                    String tenLoai = (String) tblBan.getValueAt(row, 5);
	                    for (LoaiBan lb : loaiBanList) {
	                        if (lb.getTenLoai().equals(tenLoai)) {
	                            cbLoaiBan.setSelectedItem(lb);
	                            break;
	                        }
	                    }
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(FrmQLBan.this, "Lỗi khi lấy dữ liệu khu vực hoặc loại bàn!");
	                }
	                txtTrangThai.setText(modelBan.getValueAt(row, 2).toString());
	                txtGhiChu.setText((String) modelBan.getValueAt(row, 3));
	                txtSoCho.setText(modelBan.getValueAt(row, 4).toString());
	                cbLoaiBan.setSelectedItem(modelBan.getValueAt(row, 5));
	            }
	        }
	    });
    }

    private JButton taoNut(String text, Color baseColor, Dimension size, Font font) {
	    JButton btn = new JButton(text);
	    btn.setFont(font);
	    btn.setPreferredSize(size);
	    btn.setForeground(Color.WHITE);
	    btn.setBackground(baseColor);
	    btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        
	    // Hiệu ứng hover
	    btn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btn.setBackground(baseColor.darker());
	        }
	
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btn.setBackground(baseColor);
	        }
	    });
	
	    return btn;
	}
    
	private void loadLoaiBan() {
	    cbLoaiBan.removeAllItems();
	    LoaiBan tatCa = new LoaiBan();
	    tatCa.setMaLoai("");
	    tatCa.setTenLoai("Tất cả");
	    cbLoaiBan.addItem(tatCa);
	    try {
	        List<LoaiBan> loaiBans = loaiBanDAO.getAll();
	        for (LoaiBan lb : loaiBans) {
	            cbLoaiBan.addItem(lb);
	        }
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "Lỗi tải danh sách loại bàn: " + ex.getMessage());
	    }
	}

	private void loadData() {
	    modelBan.setRowCount(0);
	    try {
	        List<Ban> list = banDAO.getAll();
	        List<LoaiBan> loaiBanList = loaiBanDAO.getAll();
	        for (Ban b : list) {
	            if (!b.getTrangThai().equals("Ẩn")) {
	                String tenLoai = loaiBanList.stream()
	                    .filter(lb -> lb.getMaLoai().equals(b.getMaLoai()))
	                    .findFirst()
	                    .map(LoaiBan::getTenLoai)
	                    .orElse("Không xác định");
	                modelBan.addRow(new Object[]{
	                        b.getMaBan(),
	                        b.getTenKhuVuc(),
	                        b.getTrangThai(),
	                        b.getGhiChu(),
	                        b.getSoChoNgoi(),
	                        tenLoai
	                });
	            }
	        }
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	}

	private void traCuuBan() {
	    String maBan = txtMaBan.getText().trim();
	    String maKhuVuc = cbKhuVuc.getSelectedIndex() > 0 ? cbKhuVuc.getSelectedItem().toString().split(" - ")[0] : "";
	    String trangThai = txtTrangThai.getText().trim();
	    LoaiBan selectedLoaiBan = (LoaiBan) cbLoaiBan.getSelectedItem();
	    String maLoai = selectedLoaiBan != null && selectedLoaiBan.getMaLoai() != null ? selectedLoaiBan.getMaLoai() : "";
	
	    modelBan.setRowCount(0);
	    try {
	        List<Ban> list = banDAO.traCuuBan(maBan, maKhuVuc, trangThai, maLoai);
	        List<LoaiBan> loaiBanList = loaiBanDAO.getAll();
	        for (Ban b : list) {
	            String tenLoai = loaiBanList.stream()
	                .filter(lb -> lb.getMaLoai().equals(b.getMaLoai()))
	                .findFirst()
	                .map(LoaiBan::getTenLoai)
	                .orElse("Không xác định");
	            modelBan.addRow(new Object[]{
	                    b.getMaBan(),
	                    b.getTenKhuVuc(),
	                    b.getTrangThai(),
	                    b.getGhiChu(),
	                    b.getSoChoNgoi(),
	                    tenLoai
	            });
	        }
	        if (list.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy bàn phù hợp!");
	        }
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "Lỗi tra cứu bàn: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	}
    
    private void themBan() {
        txtMaBan.setEditable(true);
        txtMaBan.setText("B" + System.currentTimeMillis());
        txtGhiChu.setText("");
        txtSoCho.setText("");
        cbKhuVuc.setSelectedIndex(0);
        txtTrangThai.setText("");
        cbLoaiBan.setSelectedIndex(0);
    }

    private void xoaBan() {
        int selectedRow = tblBan.getSelectedRow();
        if (selectedRow >= 0) {
            String maBan = (String) tblBan.getValueAt(selectedRow, 0);
            try {
                // Kiểm tra xem bàn có đang được đặt hoặc phục vụ không
                java.util.Date today = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());
                java.sql.Time sqlTime = new java.sql.Time(today.getTime());
                if (banDAO.checkTrung(maBan, sqlDate, sqlTime)) {
                    JOptionPane.showMessageDialog(this, "Không thể ẩn bàn " + maBan + " vì đang có lịch đặt hoặc phục vụ!");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn ẩn bàn " + maBan + "?", "Xác nhận ẩn", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    banDAO.anBan(maBan);
                    loadData();
                    JOptionPane.showMessageDialog(this, "Ẩn bàn thành công!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi ẩn bàn: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để ẩn!");
        }
    }
    
	private void luuBan() {
	    String maBan = txtMaBan.getText().trim();
	    String maKhuVuc = cbKhuVuc.getSelectedIndex() > 0 ? cbKhuVuc.getSelectedItem().toString().split(" - ")[0] : "";
	    int soCho = 0;
	    try {
	        soCho = Integer.parseInt(txtSoCho.getText().trim());
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Số chỗ phải là số nguyên!");
	        return;
	    }
	    String ghiChu = txtGhiChu.getText().trim();
	    LoaiBan selectedLoaiBan = (LoaiBan) cbLoaiBan.getSelectedItem();
	    if (selectedLoaiBan == null || selectedLoaiBan.getMaLoai() == null || selectedLoaiBan.getMaLoai().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn loại bàn hợp lệ!");
	        return;
	    }
	    String maLoai = selectedLoaiBan.getMaLoai();
	
	    if (maBan.isEmpty() || maKhuVuc.isEmpty() || maLoai.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
	        return;
	    }
	
	    Ban ban = new Ban();
	    ban.setMaBan(maBan);
	    ban.setMaKhuVuc(maKhuVuc);
	    ban.setMaLoai(maLoai);
	    ban.setSoChoNgoi(soCho);
	    ban.setGhiChu(ghiChu);
	    ban.setTrangThai("Trống");
	
	    try {
	        int selectedRow = tblBan.getSelectedRow();
	        if (selectedRow >= 0) {
	            // Cập nhật bàn
	            banDAO.capNhatBan(ban);
	            JOptionPane.showMessageDialog(this, "Cập nhật bàn thành công!");
	        } else {
	            // Thêm mới bàn
	            banDAO.themBan(ban);
	            JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
	        }
	        loadData();
	        lamMoiForm();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi lưu bàn: " + ex.getMessage());
	    }
	}
	
    private void lamMoiForm() {
        txtMaBan.setEditable(true);
        txtMaBan.setText("");
        txtGhiChu.setText("");
        txtSoCho.setText("");
        cbKhuVuc.setSelectedIndex(0);
        txtTrangThai.setText("");
        cbLoaiBan.setSelectedIndex(0);
        loadData();
    }

    public static void main(String[] args) {
    	 UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        SwingUtilities.invokeLater(() -> {
            try {
            	
                Connection conn = ConnectSQL.getConnection();
                Ban_DAO banDAO = new Ban_DAO(conn);
                KhuVuc_DAO khuVucDAO = new KhuVuc_DAO(conn);
                LoaiBan_DAO loaiBanDAO = new LoaiBan_DAO(conn);
                new FrmQLBan(banDAO, khuVucDAO, loaiBanDAO, null).setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}