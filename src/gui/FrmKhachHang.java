package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmKhachHang extends ThanhTacVu {
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private DefaultTableModel modelKH;
    private JTable tblKH;
    private JTextField txtMaKH, txtTenKH, txtPhone, txtEmail, txtCCCD;
    // THAY ĐỔI 1: Khai báo JComboBox thay cho JRadioButton
    private JComboBox<String> cbLoaiKH;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTraCuu;

    public FrmKhachHang() {
        super();
        setTitle("Quản Lý Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        initUI();
        loadDataToTable();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(169, 55, 68));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        Border lineBorder = BorderFactory.createLineBorder(new Color(165, 42, 42), 2);
        inputPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Thông Tin Khách Hàng",
                0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        inputPanel.setPreferredSize(new Dimension(0, 250));

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
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Mã khách hàng") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        txtMaKH = new JTextField(15);
        txtMaKH.setFont(fieldFont);
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(230, 230, 230));
        fieldsPanel.add(txtMaKH, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Tên khách hàng") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        txtTenKH = new JTextField(15);
        txtTenKH.setFont(fieldFont);
        fieldsPanel.add(txtTenKH, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        fieldsPanel.add(new JLabel("CCCD") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 1;
        txtCCCD = new JTextField(15);
        txtCCCD.setFont(fieldFont);
        fieldsPanel.add(txtCCCD, gbc);

        // --- Spacer giữa 2 cột ---
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.5;
        fieldsPanel.add(Box.createHorizontalStrut(50), gbc);
        gbc.weightx = 1.0;

        // --- Cột 2 ---
        gbc.gridx = 3; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Số điện thoại") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 4;
        txtPhone = new JTextField(15);
        txtPhone.setFont(fieldFont);
        fieldsPanel.add(txtPhone, gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Email") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 4;
        txtEmail = new JTextField(15);
        txtEmail.setFont(fieldFont);
        fieldsPanel.add(txtEmail, gbc);

        // --- THAY ĐỔI 2: Tạo JComboBox ---
        gbc.gridx = 3; gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Loại khách hàng") {{ setFont(labelFont); }}, gbc);
        gbc.gridx = 4;
        cbLoaiKH = new JComboBox<>(new String[]{"Khách thường", "Thành viên"});
        cbLoaiKH.setFont(fieldFont);
        cbLoaiKH.setBackground(Color.WHITE);
        fieldsPanel.add(cbLoaiKH, gbc);
        
        // --- Panel nút thao tác ---
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Dimension buttonSize = new Dimension(150, 50);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);

        btnThem = taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont);
        btnSua = taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);
        btnLamMoi = taoNut("Làm mới", new Color(149, 165, 166), buttonSize, buttonFont);
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), buttonSize, buttonFont);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnTraCuu);
        buttonPanel.add(new JLabel());

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng danh sách khách hàng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(lineBorder,
                "Danh Sách Khách Hàng", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã KH", "Tên KH", "Số điện thoại", "CCCD", "Email", "Loại KH"};
        modelKH = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKH = new JTable(modelKH);
        tblKH.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKH.setRowHeight(30);
        tablePanel.add(new JScrollPane(tblKH), BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // --- Sự kiện ---
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> clearForm());
        btnTraCuu.addActionListener(e -> traCuuKhachHang());

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

                    // THAY ĐỔI 3: Thiết lập giá trị cho ComboBox khi click vào bảng
                    String loaiKH = modelKH.getValueAt(row, 5).toString();
                    cbLoaiKH.setSelectedItem(loaiKH);
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
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(baseColor.darker()); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); }
        });
        return btn;
    }

    private void loadDataToTable() {
        modelKH.setRowCount(0);
        for (KhachHang kh : khachHangDAO.getAllKhachHang()) {
            modelKH.addRow(new Object[]{
                kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                kh.getCccd(), kh.getEmail(), kh.getLoaiKH()
            });
        }
    }

    private void themKhachHang() {
        try {
            String ten = txtTenKH.getText().trim();
            String phone = txtPhone.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String email = txtEmail.getText().trim();
            // THAY ĐỔI 4: Lấy giá trị từ ComboBox
            String loaiKH = (String) cbLoaiKH.getSelectedItem();

            if (ten.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng và số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KhachHang kh = new KhachHang(ten, phone, cccd, email, loaiKH);
            if (khachHangDAO.themKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Số điện thoại hoặc CCCD có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi dữ liệu đầu vào", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void suaKhachHang() {
        int row = tblKH.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để sửa thông tin.");
            return;
        }

        try {
            String ma = txtMaKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String phone = txtPhone.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String email = txtEmail.getText().trim();
            // THAY ĐỔI 5: Lấy giá trị từ ComboBox
            String loaiKH = (String) cbLoaiKH.getSelectedItem();

            if (ten.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng và số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KhachHang kh = new KhachHang(ma, ten, phone, cccd, email, loaiKH);
            if (khachHangDAO.suaKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Không tìm thấy khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi dữ liệu đầu vào", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void xoaKhachHang() {
        int row = tblKH.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String maKH = (String) modelKH.getValueAt(row, 0);
                if (khachHangDAO.xoaKhachHang(maKH)) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa.");
        }
    }

    private void traCuuKhachHang() {
        String keyword = txtTenKH.getText().trim();
        if (keyword.isEmpty()) keyword = txtPhone.getText().trim();
        if (keyword.isEmpty()) keyword = txtCCCD.getText().trim();
        if (keyword.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên, SĐT hoặc CCCD để tra cứu.");
             return;
        }

        List<KhachHang> ketQua = khachHangDAO.timKiemKhachHang(keyword);
        modelKH.setRowCount(0);
        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp.");
        } else {
            for (KhachHang kh : ketQua) {
                modelKH.addRow(new Object[]{
                    kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                    kh.getCccd(), kh.getEmail(), kh.getLoaiKH()
                });
            }
        }
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        // THAY ĐỔI 6: Reset ComboBox về lựa chọn đầu tiên
        cbLoaiKH.setSelectedIndex(0);
        tblKH.clearSelection();
        loadDataToTable();
    }

    public static void main(String[] args) {
         try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new FrmKhachHang().setVisible(true));
    }
}