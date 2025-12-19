package dialog;

import dao.LoaiMon_DAO;
import entity.LoaiMon;
import gui.FrmThucDon;
import connectSQL.ConnectSQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FrmLoaiMon extends JFrame {

    private JTextField txtMaLoai, txtTenLoai;
    private JButton btnThem, btnSua, btnXoa;
    private JTable table;
    private DefaultTableModel model;
    private LoaiMon_DAO dao;

    public FrmLoaiMon() {
        setTitle("Quản Lý Loại Món");
        setSize(600, 400); // giống FrmLoaiBan
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        dao = new LoaiMon_DAO(ConnectSQL.getConnection());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // ==================== PANEL NHẬP LIỆU ====================
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        // Mã loại món
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMaLoai = new JLabel("Mã loại món");
        lblMaLoai.setFont(labelFont);
        inputPanel.add(lblMaLoai, gbc);

        txtMaLoai = new JTextField(15);
        txtMaLoai.setFont(fieldFont);
        txtMaLoai.setEditable(false);
        txtMaLoai.setBackground(new Color(245, 245, 245));
        gbc.gridx = 1;
        inputPanel.add(txtMaLoai, gbc);

        // Tên loại món
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTenLoai = new JLabel("Tên loại món");
        lblTenLoai.setFont(labelFont);
        inputPanel.add(lblTenLoai, gbc);

        txtTenLoai = new JTextField(25);
        txtTenLoai.setFont(fieldFont);
        gbc.gridx = 1;
        inputPanel.add(txtTenLoai, gbc);

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Tạo nút giống hệt FrmLoaiBan nhưng KHÔNG dùng FrmDangNhap
        btnThem = taoNut("Thêm", new Color(46, 204, 113));
        btnSua = taoNut("Sửa", new Color(52, 152, 219));
        btnXoa = taoNut("Xóa", new Color(231, 76, 60));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // ==================== BẢNG DANH SÁCH ====================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Loại Món",
                0, 0,
                new Font("Times New Roman", Font.BOLD, 24)));

        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(new String[]{"Mã loại", "Tên loại", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        table.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // ==================== THÊM VÀO MAIN ====================
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ==================== TẢI DỮ LIỆU & SỰ KIỆN ====================
        loadDanhSach();

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> an());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaLoai.setText(model.getValueAt(row, 0).toString());
                    txtTenLoai.setText(model.getValueAt(row, 1).toString());
                }
            }
        });

        setVisible(true);
    }

    // Phương thức tạo nút giống hệt FrmLoaiBan (copy nguyên từ code cũ của bạn)
    private JButton taoNut(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(100, 35)); // giống FrmLoaiBan
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    // Các hàm chức năng giữ nguyên 100% như code cũ của bạn
    private void loadDanhSach() {
        model.setRowCount(0);
        try {
            List<LoaiMon> list = dao.getAllLoaiMon();
            for (LoaiMon lm : list) {
                model.addRow(new Object[]{
                    lm.getMaLoai(),
                    lm.getTenLoai(),
                    lm.getTrangThaiText()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu loại món!");
        }
    }

    private void them() {
        String ten = txtTenLoai.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại món!");
            return;
        }
        LoaiMon lm = new LoaiMon(null, ten, 1);
        if (dao.themLoaiMon(lm)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadDanhSach();
            txtTenLoai.setText("");
            refreshThucDon();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void sua() {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập tên loại!");
            return;
        }
        LoaiMon lm = new LoaiMon(ma, ten);
        if (dao.capNhatLoaiMon(lm)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDanhSach();
            refreshThucDon();
        }
    }

    private void an() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại món để xóa!");
            return;
        }
        String ma = model.getValueAt(row, 0).toString();
        String ten = model.getValueAt(row, 1).toString();

        if (JOptionPane.showConfirmDialog(this,
                "Xóa loại món \"" + ten + "\"?\nCác món thuộc loại này sẽ không hiển thị!",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.anLoaiMon(ma)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadDanhSach();
                refreshThucDon();
            }
        }
    }

    private void refreshThucDon() {
        Component parent = getOwner();
        while (parent != null) {
            if (parent instanceof FrmThucDon) {
                ((FrmThucDon) parent).refreshLoaiAndData();
                break;
            }
            parent = parent.getParent();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLoaiMon());
    }
}