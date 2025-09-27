package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import connectSQL.ConnectSQL;

public class FrmTrangChu extends JFrame {
    private JPanel pNor, pCen;
    private RoundedButton btnBan;
    private RoundedButton btnNhanVien;
    private RoundedButton btnThucDon;
    private RoundedButton btnKhuyenMai;
    private RoundedButton btnKhach;
    private RoundedButton btnKho;
    private RoundedButton btnHoaDon;
    private RoundedButton btnThongKe;
    private RoundedButton btnDangXuat;
    private RoundedButton btnTaiKhoan;
    private JPanel pSou;
    private JButton btnHome;
    private JButton btnQuanLy;
    private static String phanQuyen = null;

    public static void setPhanQuyen(String quyen) {
        phanQuyen = quyen;
    }

    public static void resetPhanQuyen() {
        phanQuyen = null;
    }

    public FrmTrangChu() {
        setTitle("Quản Lý Nhà Hàng Vang");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(CustomMenu.getInstance().getJMenuBar());
        CustomMenu customMenu = CustomMenu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);

        pCen = new JPanel(new GridLayout(3, 3, 30, 30));
        pCen.setBackground(Color.white);
        pCen.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));

        btnBan = new RoundedButton("Bàn", "img/ban.png");
        btnNhanVien = new RoundedButton("Nhân Viên", "img/nhanvien.png");
        btnThucDon = new RoundedButton("Thực Đơn", "img/thucdon.png");
        btnKhuyenMai = new RoundedButton("Khuyến Mãi", "img/khuyenmai.png");
        btnKhach = new RoundedButton("Khách Hàng", "img/khachhang.png");
        btnHoaDon = new RoundedButton("Hóa Đơn", "img/hoadon.png");
        btnThongKe = new RoundedButton("Thống Kê", "img/thongke.png");
        btnTaiKhoan = new RoundedButton("Tài khoản", "img/quanlytaikhoan.png");
        btnDangXuat = new RoundedButton("Đăng Xuất", "img/dangxuat.png");

        pCen.add(btnBan);
        pCen.add(btnThucDon);
        pCen.add(btnHoaDon);

        if ("QuanLy".equals(phanQuyen)) {
            pCen.add(btnNhanVien);
            pCen.add(btnKhuyenMai);
            pCen.add(btnKhach);
            pCen.add(btnThongKe);
            pCen.add(btnTaiKhoan);
        } else if ("LeTan".equals(phanQuyen)) {
            pCen.add(btnKhach);
        }

        pCen.add(btnDangXuat);

        add(pCen, BorderLayout.CENTER);

        btnBan.addActionListener(e -> {
            try {
                new FrmBan().setVisible(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            dispose();
        });

        btnNhanVien.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                new FrmNhanVien().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnThucDon.addActionListener(e -> {
            new FrmThucDon().setVisible(true);
            dispose();
        });

        btnKhuyenMai.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                new FrmKhuyenMai().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnKhach.addActionListener(e -> {
            new FrmKhachHang().setVisible(true);
            dispose();
        });

        btnHoaDon.addActionListener(e -> {
            new FrmHoaDon().setVisible(true);
            dispose();
        });

        btnThongKe.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                new FrmThongKe().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnTaiKhoan.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                try {
                    Connection conn = ConnectSQL.getConnection();
                    new FrmTaiKhoan().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể mở quản lý tài khoản: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDangXuat.addActionListener(e -> {
            new FrmDangNhap().setVisible(true);
            dispose();
        });
    }

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
