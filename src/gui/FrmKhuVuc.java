package gui;

import dao.KhuVuc_DAO;
import entity.KhuVuc;
import connectSQL.ConnectSQL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmKhuVuc extends ThanhTacVu {
    private final KhuVuc_DAO khuDAO;
    private final DefaultTableModel model;
    private final JTable tblKhuVuc;
    private final JTextField txtMaKhuVuc, txtTenKhuVuc, txtSoLuongBan, txtTrangThai;
    private final JButton btnLuu, btnXoa, btnTraCuu, btnLamMoi;
    private final Consumer<Void> refreshCallback;

    public FrmKhuVuc() throws SQLException {
        this(null, null);
    }

    public FrmKhuVuc(Consumer<Void> refreshCallback) throws SQLException {
        this(null, refreshCallback);
    }

    public FrmKhuVuc(KhuVuc_DAO khuDAO, Consumer<Void> refreshCallback) throws SQLException {
        super();
        setTitle("Quản Lý Khu Vực");
        Connection conn = ConnectSQL.getConnection();
        if (khuDAO == null) {
            if (conn == null) throw new SQLException("Không thể kết nối cơ sở dữ liệu");
            this.khuDAO = new KhuVuc_DAO(conn);
        } else {
            this.khuDAO = khuDAO;
        }
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
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHU VỰC");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Thông Tin Khu Vực", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
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

        // === Mã khu vực ===
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMaKhuVuc = new JLabel("Mã khu vực");
        lblMaKhuVuc.setFont(labelFont);
        fieldsPanel.add(lblMaKhuVuc, gbc);

        txtMaKhuVuc = new JTextField(15);
        txtMaKhuVuc.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtMaKhuVuc, gbc);

        // === Tên khu vực ===
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTenKhuVuc = new JLabel("Tên khu vực");
        lblTenKhuVuc.setFont(labelFont);
        fieldsPanel.add(lblTenKhuVuc, gbc);

        txtTenKhuVuc = new JTextField(15);
        txtTenKhuVuc.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtTenKhuVuc, gbc);

        // === Số lượng bàn ===
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblSoLuongBan = new JLabel("Số lượng bàn");
        lblSoLuongBan.setFont(labelFont);
        fieldsPanel.add(lblSoLuongBan, gbc);

        txtSoLuongBan = new JTextField(15);
        txtSoLuongBan.setFont(fieldFont);
        txtSoLuongBan.setEditable(false);
        gbc.gridx = 1;
        fieldsPanel.add(txtSoLuongBan, gbc);

        // Spacer giữa hai cột
        gbc.weightx = 0.5;
        for (int y = 0; y <= 2; y++) {
            gbc.gridx = 2; gbc.gridy = y;
            fieldsPanel.add(Box.createHorizontalStrut(50), gbc);
        }
        gbc.weightx = 1.0;

        // === Trạng thái ===
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTrangThai = new JLabel("Trạng thái");
        lblTrangThai.setFont(labelFont);
        fieldsPanel.add(lblTrangThai, gbc);

        txtTrangThai = new JTextField(15);
        txtTrangThai.setFont(fieldFont);
        gbc.gridx = 1;
        fieldsPanel.add(txtTrangThai, gbc);

        // Panel nút thao tác – 4 nút
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

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

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng danh sách khu vực
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Khu Vực", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] columns = {"Mã khu vực", "Tên khu vực", "Số lượng bàn", "Trạng thái"};
        model = new DefaultTableModel(columns, 0);
        tblKhuVuc = new JTable(model);
        tblKhuVuc.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKhuVuc.setRowHeight(30);
        tablePanel.add(new JScrollPane(tblKhuVuc), BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu
        loadData();

        // Sự kiện các nút
        btnLuu.addActionListener(e -> luuKhuVuc());
        btnXoa.addActionListener(e -> xoaKhuVuc());
        btnTraCuu.addActionListener(e -> traCuuKhuVuc());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        // Click vào bảng để điền dữ liệu và khóa mã
        tblKhuVuc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhuVuc.getSelectedRow();
                if (row >= 0) {
                    txtMaKhuVuc.setEditable(false);
                    txtMaKhuVuc.setText((String) model.getValueAt(row, 0));
                    txtTenKhuVuc.setText((String) model.getValueAt(row, 1));
                    txtSoLuongBan.setText(model.getValueAt(row, 2).toString());
                    txtTrangThai.setText((String) model.getValueAt(row, 3));
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

    private void loadData() {
        model.setRowCount(0);
        try {
            List<KhuVuc> list = khuDAO.getAll();
            for (KhuVuc k : list) {
                model.addRow(new Object[]{
                    k.getMaKhuVuc(),
                    k.getTenKhuVuc(),
                    k.getSoLuongBan(),
                    k.getTrangThai()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void luuKhuVuc() {
        String ma = txtMaKhuVuc.getText().trim();
        String ten = txtTenKhuVuc.getText().trim();
        String trangThai = txtTrangThai.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || trangThai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!trangThai.equals("Hoạt động") && !trangThai.equals("Ẩn")) {
            JOptionPane.showMessageDialog(this, "Trạng thái phải là 'Hoạt động' hoặc 'Ẩn'!");
            return;
        }

        try {
            int soLuongBan = khuDAO.countBan(ma);
            KhuVuc khuVuc = new KhuVuc(ma, ten, soLuongBan, trangThai);

            if (txtMaKhuVuc.isEditable()) {
                // Thêm mới
                if (khuDAO.getByMa(ma) != null) {
                    JOptionPane.showMessageDialog(this, "Mã khu vực đã tồn tại!");
                    return;
                }
                khuDAO.them(khuVuc);
                JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
            } else {
                // Cập nhật
                khuDAO.capNhat(khuVuc);
                int row = tblKhuVuc.getSelectedRow();
                if (row >= 0) {
                    model.setValueAt(ten, row, 1);
                    model.setValueAt(soLuongBan, row, 2);
                    model.setValueAt(trangThai, row, 3);
                }
                JOptionPane.showMessageDialog(this, "Cập nhật khu vực thành công!");
            }

            if (refreshCallback != null) refreshCallback.accept(null);
            loadData();
            lamMoiForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu khu vực: " + ex.getMessage());
        }
    }

    private void xoaKhuVuc() {
        int row = tblKhuVuc.getSelectedRow();
        if (row >= 0) {
            String maKhuVuc = (String) model.getValueAt(row, 0);
            try {
                if (khuDAO.checkTrung(maKhuVuc)) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa khu vực vì vẫn còn bàn đang sử dụng!");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn xóa khu vực " + maKhuVuc + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    khuDAO.xoaKhuVuc(maKhuVuc);
                    loadData();
                    if (refreshCallback != null) refreshCallback.accept(null);
                    JOptionPane.showMessageDialog(this, "Xóa khu vực thành công!");
                    lamMoiForm();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa khu vực: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để xóa!");
        }
    }

    private void traCuuKhuVuc() {
        String ma = txtMaKhuVuc.getText().trim();
        String ten = txtTenKhuVuc.getText().trim();
        String trangThai = txtTrangThai.getText().trim();

        model.setRowCount(0);
        try {
            List<KhuVuc> list = khuDAO.traCuuKhuVuc(ma, ten, trangThai);
            for (KhuVuc k : list) {
                model.addRow(new Object[]{
                    k.getMaKhuVuc(),
                    k.getTenKhuVuc(),
                    k.getSoLuongBan(),
                    k.getTrangThai()
                });
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khu vực nào phù hợp!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tra cứu: " + ex.getMessage());
        }
    }

    private void lamMoiForm() {
        txtMaKhuVuc.setEditable(true);
        txtMaKhuVuc.setText("");
        txtTenKhuVuc.setText("");
        txtSoLuongBan.setText("0");
        txtTrangThai.setText("");
        tblKhuVuc.clearSelection();
        loadData();
    }

    public static void main(String[] args) {
        UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new FrmKhuVuc().setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Không thể kết nối cơ sở dữ liệu: " + e.getMessage(),
                        "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}