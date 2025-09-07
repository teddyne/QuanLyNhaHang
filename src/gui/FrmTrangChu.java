package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class FrmTrangChu extends JFrame {

    private JPanel pNor, pCen;

    // Khai báo các nút
    private RoundedButton btnBan;
    private RoundedButton btnNhanVien;
    private RoundedButton btnThucDon;
    private RoundedButton btnKhuyenMai;
    private RoundedButton btnKhach;
    private RoundedButton btnKho;
    private RoundedButton btnHoaDon;
    private RoundedButton btnThongKe;
    private RoundedButton btnDangXuat;

	private JPanel pSou;

	private JButton btnHome;

	private JButton btnQuanLy;

    public FrmTrangChu() {
        setTitle("Quản Lý Nhà Hàng Vang");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Vùng north chứa banner
        pNor = new JPanel(new BorderLayout());
        pNor.setBackground(Color.WHITE);
        ImageIcon banner = new ImageIcon("img/banner.png");
        Image img = banner.getImage().getScaledInstance(1140, 300, Image.SCALE_SMOOTH);
        JLabel lblBanner = new JLabel(new ImageIcon(img));
        lblBanner.setHorizontalAlignment(SwingConstants.CENTER);
        pNor.add(lblBanner, BorderLayout.CENTER);

        add(pNor, BorderLayout.NORTH);

        //Vùng center chứa các nút 
        pCen = new JPanel(new GridLayout(3, 3, 30, 30));
        pCen.setBackground(Color.white);
        pCen.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));

        // Tạo nút
        btnBan = new RoundedButton("Bàn", "img/ban.png");
        btnNhanVien = new RoundedButton("Nhân Viên", "img/nhanvien.png");
        btnThucDon = new RoundedButton("Thực Đơn", "img/thucdon.png");
        btnKhuyenMai = new RoundedButton("Khuyến Mãi", "img/khuyenmai.png");
        btnKhach = new RoundedButton("Khách Hàng", "img/khachhang.png");
        btnKho = new RoundedButton("Kho", "img/kho.png");
        btnHoaDon = new RoundedButton("Hóa Đơn", "img/hoadon.png");
        btnThongKe = new RoundedButton("Thống Kê", "img/thongke.png");
        btnDangXuat = new RoundedButton("Đăng Xuất", "img/dangxuat.png");

        pCen.add(btnBan);
        pCen.add(btnNhanVien);
        pCen.add(btnThucDon);
        pCen.add(btnKhuyenMai);
        pCen.add(btnKhach);
        pCen.add(btnKho);
        pCen.add(btnHoaDon);
        pCen.add(btnThongKe);
        pCen.add(btnDangXuat);

        add(pCen, BorderLayout.CENTER);
        
        // Vùng South
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(245, 245, 245));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 180, 0, 0));

        // Icon địa chỉ
        ImageIcon iconAddress = new ImageIcon("img/diachi.png");
        Image imgAddr = iconAddress.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblAddress = new JLabel("Đại học Công Nghiệp Tp HCM", new ImageIcon(imgAddr), JLabel.LEFT);
        lblAddress.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblAddress.setForeground(Color.DARK_GRAY);

        // Icon liên hệ
        ImageIcon iconPhone = new ImageIcon("img/lienhe.png");
        Image imgPhone = iconPhone.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblPhone = new JLabel("Nhóm 9_PTUD", new ImageIcon(imgPhone), JLabel.LEFT);
        lblPhone.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblPhone.setForeground(Color.DARK_GRAY);

        infoPanel.add(lblAddress);
        infoPanel.add(lblPhone);
        bottomBar.add(infoPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 190));

        BottomButton btnHome = new BottomButton("Trang chủ", "img/home.png");
        BottomButton btnQuanLy = new BottomButton("Thoát", "img/quanly.png");

        btnHome.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnQuanLy.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttonPanel.add(btnHome);
        buttonPanel.add(btnQuanLy);

        bottomBar.add(buttonPanel, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);

        btnNhanVien.addActionListener(e -> {
            new FrmNhanVien().setVisible(true);
            dispose();
        });
        btnBan.addActionListener(e -> {
            try {
				new FrmBan().setVisible(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            dispose();
        });
        btnThucDon.addActionListener(e -> {
            new FrmThucDon().setVisible(true);
            dispose();
        });
        btnKhuyenMai.addActionListener(e -> {
            new FrmKhuyenMai().setVisible(true);
            dispose();
        });
        btnKhach.addActionListener(e -> {
            new FrmKhachHang().setVisible(true);
            dispose();
        });
        btnKho.addActionListener(e -> {
            new FrmKho().setVisible(true);
            dispose();
        });
        btnHoaDon.addActionListener(e -> {
            new FrmHoaDon().setVisible(true);
            dispose();
        });
        btnThongKe.addActionListener(e -> {
            new FrmThongKe().setVisible(true);
            dispose();
        });
        btnDangXuat.addActionListener(e -> {
            new FrmDangNhap().setVisible(true);
            dispose();
        });
        btnHome.addActionListener(e -> {
            new FrmTrangChu().setVisible(true);
            dispose();
        });
        btnQuanLy.addActionListener(e -> {
        	int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // tạo bo tròn
    class RoundedButton extends JButton {
        private int cornerRadius = 25;

        public RoundedButton(String text, String iconPath) {
            super(text);
            setFont(new Font("Times New Roman", Font.BOLD, 22));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBackground(Color.WHITE);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setVerticalTextPosition(SwingConstants.CENTER);
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Không tìm thấy icon: " + iconPath);
            }

            setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

            // Hiệu ứng khi hover (chuột chạm)
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(255, 204, 204));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(Color.WHITE);
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.setColor(new Color(160, 160, 160));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    class BottomButton extends JButton {
        public BottomButton(String text, String iconPath) {
            super(text);

            setFont(new Font("Times New Roman", Font.BOLD, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(false);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setVerticalTextPosition(SwingConstants.CENTER);
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Không tìm thấy icon: " + iconPath);
            }

            setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setForeground(new Color(200, 0, 0));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setForeground(Color.BLACK);
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmTrangChu().setVisible(true));
    }
}
