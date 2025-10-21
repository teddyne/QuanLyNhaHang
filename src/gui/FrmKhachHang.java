package gui;

import dao.KhachHang_DAO;
import dao.LoaiKhachHang_DAO;
import entity.KhachHang;
import entity.LoaiKhachHang;
import connectSQL.ConnectSQL;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class FrmKhachHang extends ThanhTacVu {
    private final KhachHang_DAO khachHangDAO;
    private final LoaiKhachHang_DAO loaiKH_DAO;
    private final Consumer<Void> refreshCallback;

    private DefaultTableModel modelKH;
    private JTable tblKH;
    private JTextField txtMaKH, txtTenKH, txtPhone, txtEmail, txtCCCD;
    private JComboBox<String> cbLoaiKH;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTraCuu, btnLoaiKH;

    private List<LoaiKhachHang> dsLoaiKH;

    
    public FrmKhachHang(KhachHang_DAO khachHangDAO, LoaiKhachHang_DAO loaiKH_DAO, Consumer<Void> refreshCallback) {
        super();

        try {
            ConnectSQL.getInstance().connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.khachHangDAO = khachHangDAO;
        this.loaiKH_DAO = loaiKH_DAO;
        this.refreshCallback = refreshCallback;
        this.dsLoaiKH = new ArrayList<>();

        setTitle("Quản Lý Khách Hàng");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);


        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Panel tiêu đề
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(169, 55, 68));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2), "Thông Tin Khách Hàng",
                0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        inputPanel.setPreferredSize(new Dimension(0, 220)); // Tăng chiều cao một chút cho 3 hàng

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        // --- Cột 1 ---
        // Mã khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaKH = new JLabel("Mã khách hàng");
        lblMaKH.setFont(labelFont);
        fieldsPanel.add(lblMaKH, gbc);
        txtMaKH = new JTextField(15);
        txtMaKH.setFont(fieldFont);
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(230, 230, 230));
        gbc.gridx = 1;
        fieldsPanel.add(txtMaKH, gbc);

        // Tên khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenKH = new JLabel("Tên khách hàng");
        lblTenKH.setFont(labelFont);
        fieldsPanel.add(lblTenKH, gbc);
        txtTenKH = new JTextField(15);
        txtTenKH.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtTenKH, gbc);

        // CCCD
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblCCCD = new JLabel("CCCD");
        lblCCCD.setFont(labelFont);
        fieldsPanel.add(lblCCCD, gbc);
        txtCCCD = new JTextField(15);
        txtCCCD.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtCCCD, gbc);

        // Spacer giữa cột
        gbc.weightx = 0.5;
        for (int y = 0; y <= 2; y++) {
            gbc.gridx = 2;
            gbc.gridy = y;
            fieldsPanel.add(Box.createHorizontalStrut(50), gbc);
        }
        gbc.weightx = 1.0;

        // --- Cột 2 ---
        // Số điện thoại
        gbc.gridx = 3;
        gbc.gridy = 0;
        JLabel lblPhone = new JLabel("Số điện thoại");
        lblPhone.setFont(labelFont);
        fieldsPanel.add(lblPhone, gbc);
        txtPhone = new JTextField(15);
        txtPhone.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 3;
        gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(labelFont);
        fieldsPanel.add(lblEmail, gbc);
        txtEmail = new JTextField(15);
        txtEmail.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtEmail, gbc);

        // Loại khách hàng
        gbc.gridx = 3;
        gbc.gridy = 2;
        JLabel lblLoaiKH = new JLabel("Loại khách hàng");
        lblLoaiKH.setFont(labelFont);
        fieldsPanel.add(lblLoaiKH, gbc);
        cbLoaiKH = new JComboBox<>();
        cbLoaiKH.setFont(fieldFont);
        cbLoaiKH.setBackground(Color.WHITE);
        loadLoaiKhachHangData(); // Tải dữ liệu cho ComboBox
        gbc.gridx = 4;
        fieldsPanel.add(cbLoaiKH, gbc);

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Dimension buttonSize = new Dimension(150, 50);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);

        // === Khởi tạo nút ===
        btnThem = taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont);
        btnSua = taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);
        btnLamMoi = taoNut("Làm mới", new Color(149, 165, 166), buttonSize, buttonFont);
        btnLoaiKH = taoNut("Loại KH", new Color(255, 193, 7), buttonSize, buttonFont);
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), buttonSize, buttonFont);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnLoaiKH);
        buttonPanel.add(btnTraCuu);

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng danh sách khách hàng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Khách Hàng", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã KH", "Tên KH", "Số điện thoại", "CCCD", "Email", "Loại KH"};
        modelKH = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKH = new JTable(modelKH);
        tblKH.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKH.setRowHeight(30);
        
        JScrollPane scrollKH = new JScrollPane(tblKH);
        tablePanel.add(scrollKH, BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // === Gán sự kiện ===
        addEventListeners();

        // === Tải dữ liệu ===
        loadDataToTable();
    }

    /**
     * Gán các trình xử lý sự kiện cho các thành phần.
     */
    private void addEventListeners() {
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> clearForm());
        btnTraCuu.addActionListener(e -> traCuuKhachHang());

        btnLoaiKH.addActionListener(e -> {
            try {
            
                Consumer<Void> callback = v -> {
                    System.out.println("Callback được gọi: Tải lại ComboBox và Bảng");
                    
                    loadLoaiKhachHangData(); 
                    
                    loadDataToTable();      
                };

                FrmLoaiKhachHang frmLoai = new FrmLoaiKhachHang(this, loaiKH_DAO, callback);
                
                frmLoai.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form Loại Khách Hàng: " + ex.getMessage());
            }
        });

        tblKH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKH.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(modelKH.getValueAt(row, 0).toString());
                    txtTenKH.setText(modelKH.getValueAt(row, 1).toString());
                    txtPhone.setText(modelKH.getValueAt(row, 2).toString());
                    txtCCCD.setText(modelKH.getValueAt(row, 3) != null ? modelKH.getValueAt(row, 3).toString() : "");
                    txtEmail.setText(modelKH.getValueAt(row, 4) != null ? modelKH.getValueAt(row, 4).toString() : "");
                    cbLoaiKH.setSelectedItem(modelKH.getValueAt(row, 5).toString());
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

    // ============================= LOGIC XỬ LÝ DỮ LIỆU ===============================

    private void loadLoaiKhachHangData() {
        cbLoaiKH.removeAllItems();
        cbLoaiKH.addItem("Tất cả");
        try {
            this.dsLoaiKH = loaiKH_DAO.getAll();
            for (LoaiKhachHang lkh : dsLoaiKH) {
                cbLoaiKH.addItem(lkh.getTenLoaiKH());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu loại khách hàng!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataToTable() {
        modelKH.setRowCount(0);
        try {
            for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
                String tenLoaiKH = getTenLoaiKH(kh.getMaLoaiKH());
                modelKH.addRow(new Object[]{
                        kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                        kh.getCccd(), kh.getEmail(), tenLoaiKH
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khách hàng!", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themKhachHang() {
        if (!validateInput()) return;
        try {
            String ten = txtTenKH.getText().trim();
            String phone = txtPhone.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String email = txtEmail.getText().trim();
            String tenLoaiKH_selected = (String) cbLoaiKH.getSelectedItem();
            
            if (tenLoaiKH_selected.equals("Tất cả")) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại khách hàng hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            String maLoaiKH = getMaLoaiKH(tenLoaiKH_selected);

            KhachHang kh = new KhachHang(ten, phone, cccd, email, maLoaiKH);

            if (khachHangDAO.themKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                if (refreshCallback != null) {
                    refreshCallback.accept(null);
                }
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Số điện thoại hoặc CCCD có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhachHang() {
        int row = tblKH.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;
        try {
            String maKH = txtMaKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String phone = txtPhone.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String email = txtEmail.getText().trim();
            String tenLoaiKH_selected = (String) cbLoaiKH.getSelectedItem();
            
            if (tenLoaiKH_selected.equals("Tất cả")) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại khách hàng hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            String maLoaiKH = getMaLoaiKH(tenLoaiKH_selected);

            KhachHang kh = new KhachHang(maKH, ten, phone, cccd, email, maLoaiKH);

            if (khachHangDAO.suaKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");
                if (refreshCallback != null) {
                    refreshCallback.accept(null);
                }
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa khách hàng: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKhachHang() {
        int row = tblKH.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maKH = (String) modelKH.getValueAt(row, 0);
            try {
                if (khachHangDAO.xoaKhachHang(maKH)) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    if (refreshCallback != null) {
                        refreshCallback.accept(null);
                    }
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại. Có thể khách hàng này đã có hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa khách hàng: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void traCuuKhachHang() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên, SĐT hoặc CCCD để tìm:", "Tra Cứu Khách Hàng", JOptionPane.INFORMATION_MESSAGE);
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }
        try {

            List<KhachHang> ketQua = khachHangDAO.timKiemKhachHang(keyword.trim());
            modelKH.setRowCount(0);
            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp.");
            } else {
                for (KhachHang kh : ketQua) {
                    String tenLoaiKH = getTenLoaiKH(kh.getMaLoaiKH());
                    modelKH.addRow(new Object[]{
                            kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                            kh.getCccd(), kh.getEmail(), tenLoaiKH
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tra cứu khách hàng: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        if (cbLoaiKH.getItemCount() > 0) {
            cbLoaiKH.setSelectedIndex(0);
        }
        tblKH.clearSelection();
        txtTenKH.requestFocus();
        loadDataToTable();
    }

    private boolean validateInput() {
        String ten = txtTenKH.getText().trim();
        String sdt = txtPhone.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!sdt.matches("^0[0-9]{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // --- CÁC HÀM TRỢ GIÚP CHUYỂN ĐỔI DỮ LIỆU ---
    private String getTenLoaiKH(String maLoaiKH) {
        if (maLoaiKH == null) return "Không xác định";
        return dsLoaiKH.stream()
                .filter(lkh -> lkh.getMaLoaiKH().equalsIgnoreCase(maLoaiKH))
                .map(LoaiKhachHang::getTenLoaiKH)
                .findFirst()
                .orElse("Không xác định");
    }

    private String getMaLoaiKH(String tenLoaiKH) {
        if (tenLoaiKH == null) return null;
        return dsLoaiKH.stream()
                .filter(lkh -> lkh.getTenLoaiKH().equalsIgnoreCase(tenLoaiKH))
                .map(LoaiKhachHang::getMaLoaiKH)
                .findFirst()
                .orElse(null);
    }
    
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));

        SwingUtilities.invokeLater(() -> {
            try {
                ConnectSQL.getInstance().connect();
                Connection conn = ConnectSQL.getConnection();

                KhachHang_DAO khachHangDAO = new KhachHang_DAO(conn);
                LoaiKhachHang_DAO loaiKH_DAO = new LoaiKhachHang_DAO(conn);

                new FrmKhachHang(khachHangDAO, loaiKH_DAO, null).setVisible(true);
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu. Không thể mở ứng dụng.");
            }
        });
    }
}