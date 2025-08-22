package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrmTrangChu extends JFrame {
    private JPanel sidebar, contentPanel;
    private CardLayout cardLayout;
	private JPanel pNor;
	private JLabel lbltieude;

    public FrmTrangChu() {
        setTitle("Phần mềm Quản Lý Nhà Hàng");
        setSize(1560, 1340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        pNor = new JPanel(new BorderLayout());
        pNor.add(lbltieude = new JLabel("QUẢN LÝ NHÀ HÀNG SEN VÀNG"));
        lbltieude.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lbltieude.setForeground(Color.white);
        lbltieude.setHorizontalAlignment(SwingConstants.LEFT);
        lbltieude.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); 
        pNor.setBackground(new Color(70, 130, 180));
        add(pNor, BorderLayout.NORTH);
        
        // ===== Sidebar Menu =====
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Các nút menu
        JButton btnTrangChu = createMenuButton("Trang Chủ", "img/nha.png");
        JButton btnNhanVien = createMenuButton("Nhân Viên", "img/taiKhoan.png");
        JButton btnBan      = createMenuButton("Bàn", "img/ban.png");
        JButton btnThucDon  = createMenuButton("Thực Đơn", "img/thucdon.png");
        JButton btnKhuyenMai = createMenuButton("Khuyến mãi","img/khuyenmai.png");
        JButton btnKhach    = createMenuButton("Khách Hàng", "img/taiKhoan.png");
        JButton btnKho      = createMenuButton("Kho", "img/kho.png");
        JButton btnHoaDon   = createMenuButton("Hóa Đơn", "img/hoadon.png");
        JButton btnThongKe   = createMenuButton("Báo Cáo", "img/thongke.png");
        JButton btnDangXuat = createMenuButton("Đăng Xuất", "img/dangxuat.png");

        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnTrangChu);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnNhanVien);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnThucDon);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnKhuyenMai);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnKhach);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnKho);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnHoaDon);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnThongKe);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnDangXuat);

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createPage("Trang Chủ"), "Trang Chủ");
        
        add(contentPanel, BorderLayout.CENTER);

        btnTrangChu.addActionListener(e -> cardLayout.show(contentPanel, "Trang Chủ"));
        btnNhanVien.addActionListener(e ->{
        	new FrmNhanVien().setVisible(true);
        	dispose();
        });
        btnBan.addActionListener(e ->{
        	new FrmBan().setVisible(true);
        	dispose();
        });
        btnThucDon.addActionListener(e ->{
        	new FrmThucDon().setVisible(true);
        	dispose();
        });
        btnKhuyenMai.addActionListener(e ->{
        	new FrmKhuyenMai().setVisible(true);
        	dispose();
        });
        btnKhach.addActionListener(e ->{
        	new FrmKhachHang().setVisible(true);
        	dispose();
        });
        btnKho.addActionListener(e ->{
        	new FrmKho().setVisible(true);
        	dispose();
        });
        btnHoaDon.addActionListener(e ->{
        	new FrmHoaDon().setVisible(true);
        	dispose();
        });
        btnThongKe.addActionListener(e ->{
        	new FrmThongKe().setVisible(true);
        	dispose();
        });
        btnDangXuat.addActionListener(e -> {
            new FrmDangNhap().setVisible(true);
            dispose();
        });
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 22));
        btn.setBackground(Color.white);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);

        btn.setPreferredSize(new Dimension(300, 80));
        btn.setMaximumSize(new Dimension(300, 80));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true), 
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon: " + iconPath);
        }

        Color defaultColor = Color.white;
        Color hoverColor = new Color(220, 220, 220);
        Color selectedColor = new Color(173, 216, 230);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(defaultColor)) { 
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(selectedColor)) {
                    btn.setBackground(defaultColor);
                }
            }
        });

        btn.addActionListener(e -> {
            for (Component c : sidebar.getComponents()) {
                if (c instanceof JButton) {
                    c.setBackground(defaultColor);
                }
            }
            btn.setBackground(selectedColor);
        });

        return btn;
    }


    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(title, JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmTrangChu().setVisible(true));
    }
}
