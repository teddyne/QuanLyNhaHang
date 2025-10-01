package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class FrmKhachHang extends JFrame {
    private JTextField txtMa, txtTen, txtPhone, txtEmail, txtCCCD;
    private DefaultTableModel tableModel;
    private JTable table;

    public FrmKhachHang() {
        initUI();
        setTitle("Quản lý khách hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(main);

        // Header
        JLabel lblHeader = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(178, 41, 41));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblHeader.setPreferredSize(new Dimension(0, 78));
        lblHeader.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        main.add(lblHeader, BorderLayout.NORTH);

        // --- Form + nút ---
        JPanel topPanel = new JPanel(new BorderLayout(12, 12));
        topPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Form GridBag
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);

        JLabel lblInfoTitle = new JLabel("Thông tin khách hàng");
        lblInfoTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblInfoTitle, gbc);
        gbc.gridwidth = 1;

        // Mã KH
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblMa = new JLabel("Mã khách hàng");
        lblMa.setFont(labelFont);
        formPanel.add(lblMa, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtMa = new JTextField(20);
        txtMa.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtMa.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtMa, gbc);

        // Tên KH
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblTen = new JLabel("Tên khách hàng");
        lblTen.setFont(labelFont);
        formPanel.add(lblTen, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTen = new JTextField(20);
        txtTen.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtTen.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtTen, gbc);

        // SĐT
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblPhone = new JLabel("Số điện thoại");
        lblPhone.setFont(labelFont);
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        txtPhone.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtPhone.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(labelFont);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtEmail.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtEmail, gbc);

        // CCCD
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblCCCD = new JLabel("CCCD");
        lblCCCD.setFont(labelFont);
        formPanel.add(lblCCCD, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtCCCD = new JTextField(20);
        txtCCCD.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtCCCD.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtCCCD, gbc);

        topPanel.add(formPanel, BorderLayout.CENTER);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(180, 200));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalGlue());

        RoundedButton btnThem = new RoundedButton("Thêm");
        btnThem.addActionListener(e -> themKhachHang());
        btnThem.setBackground(new Color(88, 214, 141));
        buttonPanel.add(centered(btnThem));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        RoundedButton btnXoa = new RoundedButton("Xóa");
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnXoa.setBackground(new Color(255, 204, 51));
        buttonPanel.add(centered(btnXoa));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        RoundedButton btnSua = new RoundedButton("Sửa");
        btnSua.addActionListener(e -> suaKhachHang());
        btnSua.setBackground(new Color(229, 80, 80));
        buttonPanel.add(centered(btnSua));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        RoundedButton btnSearch = new RoundedButton("Tìm kiếm");
        btnSearch.addActionListener(e -> timKiemKhachHang());
        btnSearch.setBackground(new Color(52, 152, 219));
        buttonPanel.add(centered(btnSearch));

        buttonPanel.add(Box.createVerticalGlue());

        topPanel.add(buttonPanel, BorderLayout.EAST);
        main.add(topPanel, BorderLayout.CENTER);

        // --- Bảng khách hàng ---
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(new EmptyBorder(12, 8, 8, 8));
        bottomPanel.setPreferredSize(new Dimension(800, 320));

        JLabel lblListTitle = new JLabel("Danh sách khách hàng");
        lblListTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        bottomPanel.add(lblListTitle, BorderLayout.NORTH);

        String[] cols = {"Mã KH", "Tên KH", "Số điện thoại", "CCCD", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane sc = new JScrollPane(table);
        bottomPanel.add(sc, BorderLayout.CENTER);

        main.add(bottomPanel, BorderLayout.SOUTH);

        // Click bảng -> fill form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtMa.setText(table.getValueAt(r, 0).toString());
                    txtTen.setText(table.getValueAt(r, 1).toString());
                    txtPhone.setText(table.getValueAt(r, 2).toString());
                    txtCCCD.setText(table.getValueAt(r, 3).toString());
                    txtEmail.setText(table.getValueAt(r, 4).toString());
                }
            }
        });
    }

    // Tìm kiếm khách hàng
    private void timKiemKhachHang() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập mã hoặc tên KH:");
        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String ma = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String ten = tableModel.getValueAt(i, 1).toString().toLowerCase();
                if (ma.contains(keyword) || ten.contains(keyword)) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào.");
        }
    }

    // helper
    private Component centered(Component c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    // Thêm KH
    private void themKhachHang() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String phone = txtPhone.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String email = txtEmail.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và tên KH không được để trống.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{ma, ten, phone, cccd, email});
        clearForm();
    }

    // Xóa KH
    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(row);
                clearForm();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn 1 khách hàng để xóa.");
        }
    }

    // Sửa KH
    private void suaKhachHang() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            tableModel.setValueAt(txtMa.getText().trim(), row, 0);
            tableModel.setValueAt(txtTen.getText().trim(), row, 1);
            tableModel.setValueAt(txtPhone.getText().trim(), row, 2);
            tableModel.setValueAt(txtCCCD.getText().trim(), row, 3);
            tableModel.setValueAt(txtEmail.getText().trim(), row, 4);
            JOptionPane.showMessageDialog(this, "Đã cập nhật.");
        } else {
            JOptionPane.showMessageDialog(this, "Chọn 1 khách hàng để sửa.");
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
    }

    // --- Nút bo tròn đồng kích thước ---
    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setBackground(new Color(100, 150, 200));
            setBorder(new EmptyBorder(8, 20, 8, 20));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width, height, 20, 20);

            g2.setColor(getBackground().darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, width - 3, height - 3, 20, 20);

            FontMetrics fm = g2.getFontMetrics();
            int x = (width - fm.stringWidth(getText())) / 2;
            int y = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(160, 45); // ép tất cả nút cùng size
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new FrmKhachHang().setVisible(true));
    }
}
