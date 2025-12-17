package gui;

import connectSQL.ConnectSQL;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import entity.LichSuDangNhap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmTaiKhoan extends ThanhTacVu {
    private final TaiKhoan_DAO taiKhoanDAO;
    private final JTable tblTaiKhoan;
    private final JTable tblLichSuDangNhap;
    private final DefaultTableModel modelTaiKhoan;
    private final DefaultTableModel modelLichSuDangNhap;
    private final JTextField txtMaTaiKhoan;
    private final JTextField txtSoDienThoai;
    private final JPasswordField txtMatKhau;
    private final JTextField txtMaNhanVien;
    private final JComboBox<String> cmbPhanQuyen;
    private final JTextField txtHoTen;
    private final JButton btnLuu, btnXoa, btnTraCuu, btnLamMoi;
    private final JCheckBox chkGanNhat, chkNhieuNhat;

    public FrmTaiKhoan() throws SQLException {
        super();
        setTitle("Quản Lý Tài Khoản");
        Connection conn = ConnectSQL.getConnection();
        taiKhoanDAO = new TaiKhoan_DAO(conn);

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
        JLabel lblTieuDe = new JLabel("QUẢN LÝ TÀI KHOẢN");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2), "Thông Tin Tài Khoản",
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
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMaTaiKhoan = new JLabel("Mã tài khoản");
        lblMaTaiKhoan.setFont(labelFont);
        fieldsPanel.add(lblMaTaiKhoan, gbc);
        txtMaTaiKhoan = new JTextField(15);
        txtMaTaiKhoan.setEditable(false);
        txtMaTaiKhoan.setFont(fieldFont);
        txtMaTaiKhoan.setText(taiKhoanDAO.taoMaTaiKhoanMoi());
        gbc.gridx = 1;
        fieldsPanel.add(txtMaTaiKhoan, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblSoDienThoai = new JLabel("Số điện thoại");
        lblSoDienThoai.setFont(labelFont);
        fieldsPanel.add(lblSoDienThoai, gbc);
        txtSoDienThoai = new JTextField(15);
        txtSoDienThoai.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtSoDienThoai, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(labelFont);
        fieldsPanel.add(lblMatKhau, gbc);
        txtMatKhau = new JPasswordField(15);
        txtMatKhau.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtMatKhau, gbc);

        // Spacer
        gbc.weightx = 0.5;
        for (int y = 0; y <= 2; y++) {
            gbc.gridx = 2; gbc.gridy = y;
            fieldsPanel.add(Box.createHorizontalStrut(50), gbc);
        }
        gbc.weightx = 1.0;

        // Cột 2
        gbc.gridx = 3; gbc.gridy = 0;
        JLabel lblMaNhanVien = new JLabel("Mã nhân viên");
        lblMaNhanVien.setFont(labelFont);
        fieldsPanel.add(lblMaNhanVien, gbc);
        txtMaNhanVien = new JTextField(15);
        txtMaNhanVien.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtMaNhanVien, gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        JLabel lblPhanQuyen = new JLabel("Phân quyền");
        lblPhanQuyen.setFont(labelFont);
        fieldsPanel.add(lblPhanQuyen, gbc);
        cmbPhanQuyen = new JComboBox<>(new String[]{"QuanLy", "LeTan"});
        cmbPhanQuyen.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(cmbPhanQuyen, gbc);

        gbc.gridx = 3; gbc.gridy = 2;
        JLabel lblHoTen = new JLabel("Họ tên");
        lblHoTen.setFont(labelFont);
        fieldsPanel.add(lblHoTen, gbc);
        txtHoTen = new JTextField(15);
        txtHoTen.setEditable(false);
        txtHoTen.setFont(fieldFont);
        gbc.gridx = 4;
        fieldsPanel.add(txtHoTen, gbc);

        // Panel nút thao tác - 4 nút
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        Dimension buttonSize = new Dimension(160, 50);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);

        btnLuu = taoNut("Lưu", new Color(46, 204, 113), buttonSize, buttonFont);         // xanh lá
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);         // đỏ
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), buttonSize, buttonFont); // tím
        btnLamMoi = taoNut("Làm mới", new Color(149, 165, 166), buttonSize, buttonFont); // xám

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnTraCuu);
        buttonPanel.add(btnLamMoi);

        // Panel lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        filterPanel.setBackground(Color.WHITE);
        JLabel lblLoc = new JLabel("  Lọc");
        lblLoc.setFont(new Font("Times New Roman", Font.BOLD, 22));
        chkGanNhat = new JCheckBox("Gần nhất");
        chkGanNhat.setBackground(Color.WHITE);
        chkGanNhat.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        chkGanNhat.setFocusPainted(false);
        chkGanNhat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkNhieuNhat = new JCheckBox("Nhiều nhất");
        chkNhieuNhat.setBackground(Color.WHITE);
        chkNhieuNhat.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        chkNhieuNhat.setFocusPainted(false);
        chkNhieuNhat.setCursor(new Cursor(Cursor.HAND_CURSOR));

        filterPanel.add(lblLoc);
        filterPanel.add(chkGanNhat);
        filterPanel.add(chkNhieuNhat);

        // Gộp nút + lọc
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(buttonPanel, BorderLayout.CENTER);
        rightPanel.add(filterPanel, BorderLayout.SOUTH);

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(rightPanel, BorderLayout.EAST);

        // Bảng tài khoản
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Tài Khoản", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] taiKhoanColumns = {"Mã tài khoản", "Số điện thoại", "Mật khẩu", "Mã nhân viên", "Phân quyền", "Họ tên"};
        modelTaiKhoan = new DefaultTableModel(taiKhoanColumns, 0);
        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblTaiKhoan.setRowHeight(30);
        tablePanel.add(new JScrollPane(tblTaiKhoan), BorderLayout.CENTER);

        // Bảng lịch sử đăng nhập
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Lịch Sử Đăng Nhập", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        historyPanel.setBackground(Color.WHITE);

        String[] lichSuColumns = {"Mã lịch sử", "Mã tài khoản", "Thời gian đăng nhập", "Trạng thái"};
        modelLichSuDangNhap = new DefaultTableModel(lichSuColumns, 0);
        tblLichSuDangNhap = new JTable(modelLichSuDangNhap);
        tblLichSuDangNhap.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblLichSuDangNhap.setRowHeight(30);
        historyPanel.add(new JScrollPane(tblLichSuDangNhap), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, historyPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Load dữ liệu
        loadDanhSachTaiKhoan();

        // Sự kiện
        btnLuu.addActionListener(e -> luuTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        btnTraCuu.addActionListener(e -> traCuuTaiKhoan());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        chkGanNhat.addActionListener(e -> {
            chkNhieuNhat.setSelected(false); // không cho chọn cả 2
            if (chkGanNhat.isSelected()) {
                try {
                    locDangNhapGanNhat();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi lọc gần nhất!");
                }
            } else {
                loadDanhSachTaiKhoan();
            }
        });

        chkNhieuNhat.addActionListener(e -> {
            chkGanNhat.setSelected(false);
            if (chkNhieuNhat.isSelected()) {
                locDangNhapNhieuNhat();
            } else {
                loadDanhSachTaiKhoan();
            }
        });

        tblTaiKhoan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblTaiKhoan.getSelectedRow();
                if (row >= 0) {
                    txtMaTaiKhoan.setText(modelTaiKhoan.getValueAt(row, 0).toString());
                    txtSoDienThoai.setText(modelTaiKhoan.getValueAt(row, 1).toString());
                    txtMatKhau.setText(modelTaiKhoan.getValueAt(row, 2).toString());
                    txtMaNhanVien.setText(modelTaiKhoan.getValueAt(row, 3).toString());
                    cmbPhanQuyen.setSelectedItem(modelTaiKhoan.getValueAt(row, 4).toString());
                    txtHoTen.setText(modelTaiKhoan.getValueAt(row, 5).toString());
                    try {
                        loadLichSuDangNhap(modelTaiKhoan.getValueAt(row, 0).toString());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
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

    private void loadDanhSachTaiKhoan() {
        modelTaiKhoan.setRowCount(0);
        List<TaiKhoan> dsTaiKhoan = taiKhoanDAO.layDanhSachTaiKhoan();
        for (TaiKhoan tk : dsTaiKhoan) {
            modelTaiKhoan.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getSoDienThoai(),
                    tk.getMatKhau(),
                    tk.getMaNhanVien(),
                    tk.getPhanQuyen(),
                    tk.getHoTen()
            });
        }
    }

    private void loadLichSuDangNhap(String maTaiKhoan) throws SQLException {
        modelLichSuDangNhap.setRowCount(0);
        List<LichSuDangNhap> dsLichSu = taiKhoanDAO.layLichSuDangNhap(maTaiKhoan);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (LichSuDangNhap ls : dsLichSu) {
            modelLichSuDangNhap.addRow(new Object[]{
                    ls.getMaLichSu(),
                    ls.getMaTaiKhoan(),
                    sdf.format(ls.getThoiGianDangNhap()),
                    ls.isTrangThai() ? "Thành công" : "Thất bại"
            });
        }
    }

    private void luuTaiKhoan() {
        try {
            TaiKhoan tk = new TaiKhoan();
            tk.setMaTaiKhoan(txtMaTaiKhoan.getText().trim());
            tk.setSoDienThoai(txtSoDienThoai.getText().trim());
            tk.setMatKhau(new String(txtMatKhau.getPassword()));
            tk.setMaNhanVien(txtMaNhanVien.getText().trim());
            tk.setPhanQuyen(cmbPhanQuyen.getSelectedItem().toString());
            tk.setHoTen(txtHoTen.getText().trim());

            int selectedRow = tblTaiKhoan.getSelectedRow();
            if (selectedRow >= 0) {
                // Cập nhật
                if (taiKhoanDAO.suaTaiKhoan(tk)) {
                    JOptionPane.showMessageDialog(this, "Sửa tài khoản thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa tài khoản thất bại!");
                    return;
                }
            } else {
                // Thêm mới
                if (taiKhoanDAO.themTaiKhoan(tk)) {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!");
                    return;
                }
            }
            loadDanhSachTaiKhoan();
            lamMoiForm();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void xoaTaiKhoan() {
        String maTaiKhoan = txtMaTaiKhoan.getText().trim();
        if (maTaiKhoan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa tài khoản " + maTaiKhoan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (taiKhoanDAO.xoaTaiKhoan(maTaiKhoan)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadDanhSachTaiKhoan();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại!");
            }
        }
    }

    private void traCuuTaiKhoan() {
        String keyword = JOptionPane.showInputDialog(this, 
            "Nhập số điện thoại hoặc mã nhân viên để tra cứu:", 
            "Tra cứu tài khoản", JOptionPane.QUESTION_MESSAGE);
        if (keyword == null || keyword.trim().isEmpty()) return;

        modelTaiKhoan.setRowCount(0);
        List<TaiKhoan> ketQua = taiKhoanDAO.traCuuTaiKhoan(keyword.trim());
        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản nào phù hợp!");
        } else {
            for (TaiKhoan tk : ketQua) {
                modelTaiKhoan.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getSoDienThoai(),
                    tk.getMatKhau(),
                    tk.getMaNhanVien(),
                    tk.getPhanQuyen(),
                    tk.getHoTen()
                });
            }
            JOptionPane.showMessageDialog(this, "Tìm thấy " + ketQua.size() + " tài khoản phù hợp!");
        }
    }

    private void lamMoiForm() {
        txtMaTaiKhoan.setText(taiKhoanDAO.taoMaTaiKhoanMoi());
        txtSoDienThoai.setText("");
        txtMatKhau.setText("");
        txtMaNhanVien.setText("");
        cmbPhanQuyen.setSelectedIndex(0);
        txtHoTen.setText("");
        modelLichSuDangNhap.setRowCount(0);
        tblTaiKhoan.clearSelection();
        chkGanNhat.setSelected(false);
        chkNhieuNhat.setSelected(false);
        loadDanhSachTaiKhoan();
    }

    private void locDangNhapGanNhat() throws SQLException {
        TaiKhoan tk = taiKhoanDAO.layTaiKhoanDangNhapGanNhat();
        if (tk != null) {
            modelTaiKhoan.setRowCount(0);
            modelTaiKhoan.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getSoDienThoai(),
                    tk.getMatKhau(),
                    tk.getMaNhanVien(),
                    tk.getPhanQuyen(),
                    tk.getHoTen()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu đăng nhập nào!");
            modelTaiKhoan.setRowCount(0);
        }
    }

    private void locDangNhapNhieuNhat() {
        TaiKhoan tk = taiKhoanDAO.layTaiKhoanDangNhapNhieuNhat();
        if (tk != null) {
            modelTaiKhoan.setRowCount(0);
            modelTaiKhoan.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tk.getSoDienThoai(),
                    tk.getMatKhau(),
                    tk.getMaNhanVien(),
                    tk.getPhanQuyen(),
                    tk.getHoTen()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu đăng nhập nào!");
            modelTaiKhoan.setRowCount(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            try {
                new FrmTaiKhoan().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}