package gui;

import dao.NhanVien_DAO;
import entity.NhanVien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import connectSQL.ConnectSQL;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FrmNhanVien extends ThanhTacVu {
    private final NhanVien_DAO dao;
    private final DefaultTableModel modelNhanVien;
    private final JTable tblNhanVien;

    // Form fields
    private JTextField txtMaNV, txtHoTen, txtCCCD, txtSDT, txtEmail, txtChucVu;
    private JDateChooser dcNgaySinh;
    private JComboBox<String> cmbGioiTinh, cmbTrangThai;
    private JLabel lblAnhNV;
    private String duongDanAnh = "default_avatar.png";

    // Buttons
    private JButton btnLuu, btnXoa, btnTraCuu, btnLamMoi;

    private Connection con = ConnectSQL.getConnection();

    public FrmNhanVien() {
        super();
        setTitle("Quản Lý Nhân Viên");

        this.dao = new NhanVien_DAO(con);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Panel tiêu đề
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(169, 55, 68));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Thông Tin Nhân Viên",
                0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        inputPanel.setPreferredSize(new Dimension(0, 280));

        // Left panel: Ảnh nhân viên
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel pnlAnh = new JPanel();
        pnlAnh.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 1), "Ảnh nhân viên"));
        pnlAnh.setPreferredSize(new Dimension(140, 180));
        pnlAnh.setBackground(Color.WHITE);

        lblAnhNV = new JLabel();
        lblAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnhNV.setPreferredSize(new Dimension(120, 150));
        setAnhChoLabel(lblAnhNV, duongDanAnh);
        pnlAnh.add(lblAnhNV);

        // Click để chọn ảnh
        lblAnhNV.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn ảnh nhân viên");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image files", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(FrmNhanVien.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    duongDanAnh = fileChooser.getSelectedFile().getAbsolutePath();
                    setAnhChoLabel(lblAnhNV, duongDanAnh);
                }
            }
        });

        leftPanel.add(pnlAnh, BorderLayout.NORTH);

        // Right panel: Các field
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        int row = 0;

        // Mã NV
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel lblMaNV = new JLabel("Mã NV");
        lblMaNV.setFont(labelFont);
        fieldsPanel.add(lblMaNV, gbc);
        gbc.gridx = 1;
        txtMaNV = new JTextField(20);
        txtMaNV.setFont(fieldFont);
        txtMaNV.setEditable(false);
        fieldsPanel.add(txtMaNV, gbc);
        row++;

        // Họ tên
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblHoTen = new JLabel("Họ tên");
        lblHoTen.setFont(labelFont);
        fieldsPanel.add(lblHoTen, gbc);
        gbc.gridx = 1;
        txtHoTen = new JTextField(15);
        txtHoTen.setFont(fieldFont);
        fieldsPanel.add(txtHoTen, gbc);
        row++;

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblNgaySinh = new JLabel("Ngày sinh");
        lblNgaySinh.setFont(labelFont);
        fieldsPanel.add(lblNgaySinh, gbc);
        gbc.gridx = 1;
        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("dd/MM/yyyy");
        dcNgaySinh.setFont(fieldFont);
        fieldsPanel.add(dcNgaySinh, gbc);
        row++;

        // Giới tính
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGioiTinh = new JLabel("Giới tính");
        lblGioiTinh.setFont(labelFont);
        fieldsPanel.add(lblGioiTinh, gbc);
        gbc.gridx = 1;
        cmbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cmbGioiTinh.setFont(fieldFont);
        fieldsPanel.add(cmbGioiTinh, gbc);
        row++;

        // Trạng thái
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTrangThai = new JLabel("Trạng thái");
        lblTrangThai.setFont(labelFont);
        fieldsPanel.add(lblTrangThai, gbc);
        gbc.gridx = 1;
        cmbTrangThai = new JComboBox<>(new String[]{"Đang làm việc", "Ngừng làm việc"});
        cmbTrangThai.setFont(fieldFont);
        fieldsPanel.add(cmbTrangThai, gbc);

        // Cột 2
        gbc.gridx = 2; gbc.gridy = 0;
        JLabel lblCCCD = new JLabel("CCCD");
        lblCCCD.setFont(labelFont);
        fieldsPanel.add(lblCCCD, gbc);
        gbc.gridx = 3;
        txtCCCD = new JTextField(12);
        txtCCCD.setFont(fieldFont);
        fieldsPanel.add(txtCCCD, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        JLabel lblSDT = new JLabel("SĐT");
        lblSDT.setFont(labelFont);
        fieldsPanel.add(lblSDT, gbc);
        gbc.gridx = 3;
        txtSDT = new JTextField(12);
        txtSDT.setFont(fieldFont);
        fieldsPanel.add(txtSDT, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(labelFont);
        fieldsPanel.add(lblEmail, gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(15);
        txtEmail.setFont(fieldFont);
        fieldsPanel.add(txtEmail, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        JLabel lblChucVu = new JLabel("Chức vụ");
        lblChucVu.setFont(labelFont);
        fieldsPanel.add(lblChucVu, gbc);
        gbc.gridx = 3;
        txtChucVu = new JTextField(12);
        txtChucVu.setFont(fieldFont);
        fieldsPanel.add(txtChucVu, gbc);

        // Gộp left + fields
        JPanel mainInputPanel = new JPanel(new BorderLayout(20, 0));
        mainInputPanel.setBackground(Color.WHITE);
        mainInputPanel.add(leftPanel, BorderLayout.WEST);
        mainInputPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Panel nút - 4 nút
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Dimension buttonSize = new Dimension(150, 20);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);

        btnLuu = taoNut("Lưu", new Color(46, 204, 113), buttonSize, buttonFont);
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), buttonSize, buttonFont);
        btnLamMoi = taoNut("Làm mới", new Color(149, 165, 166), buttonSize, buttonFont);

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnTraCuu);
        buttonPanel.add(btnLamMoi);

        inputPanel.add(mainInputPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng danh sách
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Nhân Viên", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã NV", "Tên NV", "Ngày sinh", "Giới tính", "CCCD", "SDT", "Email", "Chức vụ", "Trạng thái"};
        modelNhanVien = new DefaultTableModel(columns, 0);
        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setRowHeight(30);
        tblNhanVien.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tablePanel.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu
        loadNhanVien();

        // Sự kiện
        btnLuu.addActionListener(e -> luuNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnTraCuu.addActionListener(e -> traCuuNhanVien());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        // Click bảng
        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText((String) modelNhanVien.getValueAt(row, 0));
                    txtHoTen.setText((String) modelNhanVien.getValueAt(row, 1));

                    try {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate ld = LocalDate.parse(modelNhanVien.getValueAt(row, 2).toString(), dtf);
                        dcNgaySinh.setDate(Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    } catch (Exception ex) {
                        dcNgaySinh.setDate(null);
                    }

                    cmbGioiTinh.setSelectedItem(modelNhanVien.getValueAt(row, 3).toString());
                    txtCCCD.setText((String) modelNhanVien.getValueAt(row, 4));
                    txtSDT.setText((String) modelNhanVien.getValueAt(row, 5));
                    txtEmail.setText((String) modelNhanVien.getValueAt(row, 6));
                    txtChucVu.setText((String) modelNhanVien.getValueAt(row, 7));
                    cmbTrangThai.setSelectedItem(modelNhanVien.getValueAt(row, 8).toString());

                    // Load ảnh
                    NhanVien nv = dao.timNhanVienTheoMa(txtMaNV.getText());
                    if (nv != null && nv.getAnhNV() != null && !nv.getAnhNV().isEmpty()) {
                        duongDanAnh = nv.getAnhNV();
                    } else {
                        duongDanAnh = "default_avatar.png";
                    }
                    setAnhChoLabel(lblAnhNV, duongDanAnh);
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

    private void setAnhChoLabel(JLabel lbl, String duongDan) {
        try {
            ImageIcon icon = new ImageIcon(duongDan);
            Image img = icon.getImage();
            int maxW = 120, maxH = 150;
            float ratio = (float) img.getWidth(null) / img.getHeight(null);
            int newW = maxW;
            int newH = (int) (maxW / ratio);
            if (newH > maxH) {
                newH = maxH;
                newW = (int) (maxH * ratio);
            }
            Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lbl.setIcon(null);
            lbl.setText("No Image");
        }
    }

    private void loadNhanVien() {
        modelNhanVien.setRowCount(0);
        List<NhanVien> list = dao.getAllNhanVien();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (NhanVien nv : list) {
            if (!nv.isTrangThai()) continue;
            modelNhanVien.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    nv.getNgaySinh() != null ? nv.getNgaySinh().format(dtf) : "",
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getEmail(),
                    nv.getChucVu(),
                    nv.isTrangThai() ? "Đang làm việc" : "Ngừng làm việc"
            });
        }
    }

    private boolean kiemTraDuLieu() {
        if (txtHoTen.getText().trim().isEmpty() || dcNgaySinh.getDate() == null ||
                txtCCCD.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() || txtChucVu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if (!txtCCCD.getText().matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD phải đúng 12 chữ số!");
            return false;
        }
        if (!txtSDT.getText().matches("^(03|05|07|08|09)\\d{8}$")) {
            JOptionPane.showMessageDialog(this, "SĐT phải 10 số, bắt đầu bằng 03,05,07,08,09!");
            return false;
        }
        if (!txtEmail.getText().matches("^\\w+@\\w+\\.\\w+$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            return false;
        }
        return true;
    }

    private NhanVien layNhanVienTuForm() {
        Date date = dcNgaySinh.getDate();
        LocalDate ngaySinh = date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        return new NhanVien(
                txtMaNV.getText().trim(),
                txtHoTen.getText().trim(),
                duongDanAnh,
                ngaySinh,
                cmbGioiTinh.getSelectedItem().equals("Nam"),
                txtCCCD.getText().trim(),
                txtEmail.getText().trim(),
                txtSDT.getText().trim(),
                cmbTrangThai.getSelectedItem().equals("Đang làm việc"),
                txtChucVu.getText().trim()
        );
    }

    private void luuNhanVien() {
        if (!kiemTraDuLieu()) return;

        NhanVien nv = layNhanVienTuForm();
        int selectedRow = tblNhanVien.getSelectedRow();

        try {
            if (selectedRow >= 0) {
                // Cập nhật
                if (dao.capNhatNhanVien(nv)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                    return;
                }
            } else {
                // Thêm mới
                nv.setMaNhanVien(taoMaNhanVien());
                if (dao.themNhanVien(nv)) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Có thể CCCD/SĐT/Email đã tồn tại.");
                    return;
                }
            }
            loadNhanVien();
            lamMoiForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void xoaNhanVien() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa!");
            return;
        }
        String ma = (String) modelNhanVien.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.anNhanVien(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                loadNhanVien();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void traCuuNhanVien() {
        String tuKhoaTen = txtHoTen.getText().trim().toLowerCase();
        String tuKhoaChucVu = txtChucVu.getText().trim().toLowerCase();

        modelNhanVien.setRowCount(0);
        List<NhanVien> list = dao.getAllNhanVien();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean found = false;

        for (NhanVien nv : list) {
            if (!nv.isTrangThai()) continue;

            boolean match = true;
            if (!tuKhoaTen.isEmpty() && !nv.getHoTen().toLowerCase().contains(tuKhoaTen)) match = false;
            if (!tuKhoaChucVu.isEmpty() && !nv.getChucVu().toLowerCase().contains(tuKhoaChucVu)) match = false;

            if (match) {
                modelNhanVien.addRow(new Object[]{
                        nv.getMaNhanVien(),
                        nv.getHoTen(),
                        nv.getNgaySinh() != null ? nv.getNgaySinh().format(dtf) : "",
                        nv.isGioiTinh() ? "Nam" : "Nữ",
                        nv.getCccd(),
                        nv.getSdt(),
                        nv.getEmail(),
                        nv.getChucVu(),
                        nv.isTrangThai() ? "Đang làm việc" : "Ngừng làm việc"
                });
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp!");
        }
    }

    private void lamMoiForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        dcNgaySinh.setDate(null);
        txtCCCD.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtChucVu.setText("");
        cmbGioiTinh.setSelectedIndex(0);
        cmbTrangThai.setSelectedIndex(0);
        tblNhanVien.clearSelection();
        duongDanAnh = "default_avatar.png";
        setAnhChoLabel(lblAnhNV, duongDanAnh);
        loadNhanVien();
    }

    private String taoMaNhanVien() {
        List<NhanVien> list = dao.getAllNhanVien();
        int max = 0;
        for (NhanVien nv : list) {
            try {
                int num = Integer.parseInt(nv.getMaNhanVien().replaceAll("\\D", ""));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }
        return String.format("NV%04d", max + 1);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            try {
                FrmNhanVien frame = new FrmNhanVien();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}