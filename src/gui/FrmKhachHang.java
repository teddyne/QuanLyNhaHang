package gui;

import dao.KhachHang_DAO;
import entity.KhachHang;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class FrmKhachHang extends JFrame {
    private JTextField txtMa, txtTen, txtPhone, txtEmail, txtCCCD;
    private JRadioButton rbThuong, rbThanhVien;
    private ButtonGroup bgLoaiKH;
    private DefaultTableModel tableModel;
    private JTable table;
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();

    public FrmKhachHang() {
        initUI();
        setTitle("Quản lý khách hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);
        loadDataToTable();
    }

    private void initUI() {
    	JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Header
        JLabel lblHeader = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(178, 41, 41));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 26));
        lblHeader.setPreferredSize(new Dimension(0, 50));
        lblHeader.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

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
        Font labelFont = new Font("Times New Roman", Font.BOLD, 18);

        JLabel lblInfoTitle = new JLabel("Thông tin khách hàng");
        lblInfoTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
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
        txtMa.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtMa.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtMa, gbc);

        // Tên KH
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblTen = new JLabel("Tên khách hàng");
        lblTen.setFont(labelFont);
        formPanel.add(lblTen, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTen = new JTextField(20);
        txtTen.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        txtTen.setBorder(new LineBorder(Color.GRAY, 1));
        formPanel.add(txtTen, gbc);

        // SĐT
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblPhone = new JLabel("Số điện thoại");
        lblPhone.setFont(labelFont);
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        txtPhone.setFont(new Font("Times New Roman", Font.PLAIN, 16));
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
        
        // Loại khách hàng
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblLoaiKH = new JLabel("Loại khách hàng");
        lblLoaiKH.setFont(labelFont);
        formPanel.add(lblLoaiKH, gbc);
        
        rbThuong = new JRadioButton("Khách thường");
        rbThuong.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        rbThuong.setSelected(true); // Default selection
        rbThuong.setBackground(Color.WHITE);

        rbThanhVien = new JRadioButton("Thành viên");
        rbThanhVien.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        rbThanhVien.setBackground(Color.WHITE);

        bgLoaiKH = new ButtonGroup();
        bgLoaiKH.add(rbThuong);
        bgLoaiKH.add(rbThanhVien);

        JPanel pLoaiKH = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pLoaiKH.setBackground(Color.WHITE);
        pLoaiKH.add(rbThuong);
        pLoaiKH.add(Box.createHorizontalStrut(20)); // Add some space
        pLoaiKH.add(rbThanhVien);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(pLoaiKH, gbc);

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
        

        // --- Bảng khách hàng ---
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(new EmptyBorder(12, 8, 8, 8));

        JLabel lblListTitle = new JLabel("Danh sách khách hàng");
        lblListTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        bottomPanel.add(lblListTitle, BorderLayout.NORTH);

        String[] cols = {"Mã KH", "Tên KH", "Số điện thoại", "CCCD", "Email", "Loại KH"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane sc = new JScrollPane(table);
        bottomPanel.add(sc, BorderLayout.CENTER);

     // --- Ghép Form và Bảng bằng JSplitPane ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setResizeWeight(0.1); // Tỷ lệ phân chia không gian ban đầu 
        splitPane.setBorder(null);
        mainPanel.add(splitPane, BorderLayout.CENTER); // Thêm splitPane vào CENTER

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
                    
                    String loaiKH = table.getValueAt(r, 5).toString();
                    if(loaiKH.equalsIgnoreCase("Thành viên")) {
                        rbThanhVien.setSelected(true);
                    } else {
                        rbThuong.setSelected(true);
                    }
                }
            }
        });
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        try {
            List<KhachHang> dsKhachHang = khachHangDAO.getAllKhachHang();
            for (KhachHang kh : dsKhachHang) {
                tableModel.addRow(new Object[]{
                    kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                    kh.getCccd(), kh.getEmail(), kh.getLoaiKH() 
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }
    
    private KhachHang createKhachHangFromForm() throws IllegalArgumentException {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String phone = txtPhone.getText().trim();
        String cccd = txtCCCD.getText().trim();
        String email = txtEmail.getText().trim();
    
        String loaiKH = rbThanhVien.isSelected() ? "Thành viên" : "Khách thường";

        return new KhachHang(ma, ten, phone, cccd, email, loaiKH);
    }
    
    private void themKhachHang() {
        try {
            KhachHang kh = createKhachHangFromForm();

            if (khachHangDAO.themKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Mã khách hàng có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi dữ liệu đầu vào", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu khi thêm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void suaKhachHang() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để sửa thông tin.");
            return;
        }

        try {
            KhachHang kh = createKhachHangFromForm();
            
            if (khachHangDAO.suaKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Không tìm thấy khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi dữ liệu đầu vào", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu khi cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

 // Tìm kiếm khách hàng
    private void timKiemKhachHang() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập mã hoặc tên khách hàng cần tìm:");
        
        // Nếu người dùng nhấn Cancel hoặc đóng dialog, keyword sẽ là null
        if (keyword == null) {
            return; 
        }

        // Nếu người dùng không nhập gì và nhấn OK, có thể tải lại toàn bộ danh sách
        if (keyword.trim().isEmpty()) {
            loadDataToTable();
            return;
        }

        try {
            java.util.List<KhachHang> ketQua = khachHangDAO.timKiemKhachHang(keyword);
            
            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào phù hợp.");
            } else {
                // Xóa dữ liệu cũ và hiển thị kết quả tìm kiếm
                tableModel.setRowCount(0);
                for (KhachHang kh : ketQua) {
                    tableModel.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getTenKH(),
                        kh.getSdt(),
                        kh.getCccd(),
                        kh.getEmail(),
                        kh.getLoaiKH()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu khi tìm kiếm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // helper
    private Component centered(Component c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }
    
 // Xóa KH
    private void xoaKhachHang() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Lấy mã khách hàng từ cột đầu tiên của hàng được chọn
                String maKH = (String) tableModel.getValueAt(row, 0);
                try {
                    if (khachHangDAO.xoaKhachHang(maKH)) {
                        JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                        loadDataToTable(); // Tải lại dữ liệu sau khi xóa
                        clearForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu khi xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xóa.");
        }
    }
    
    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        rbThuong.setSelected(true);
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