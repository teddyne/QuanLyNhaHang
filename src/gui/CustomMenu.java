package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import connectSQL.ConnectSQL;
import entity.TaiKhoan;

public class CustomMenu extends JFrame {
    private static String phanQuyen = null;
    private static CustomMenu instance;
    private JMenuBar menuBar;
    private NutBottom btnHome;
    private NutBottom btnQuanLy;
    private JPanel bottomBar;
    private JLabel lblQL; // Lưu lblQL để cập nhật sau

    public static void setPhanQuyen(String quyen) {
        phanQuyen = quyen;
    }

    public static void resetInstance() {
        instance = null; // Đặt lại instance để tạo mới CustomMenu
        phanQuyen = null; // Đặt lại phanQuyen
    }

    public static CustomMenu getInstance() {
        if (instance == null) {
            instance = new CustomMenu();
        }
        return instance;
    }

    public CustomMenu() {
        setTitle("Quản Lý Nhà Hàng Vang");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(245, 245, 245));
        menuBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        menuBar.setPreferredSize(new Dimension(0, 60));

        Font fontMenu = new Font("Times New Roman", Font.BOLD, 22);
        Font fontItem = new Font("Times New Roman", Font.BOLD, 20);

        // Hệ Thống
        JMenu heThong = createHoverableMenu("Hệ Thống");
        heThong.setIcon(taoIcon("img/hethong.png", 30, 30));
        heThong.setFont(fontMenu);

        JMenuItem trangChu = new JMenuItem("Trang Chủ");
        trangChu.setFont(fontItem);
        trangChu.setIcon(taoIcon("img/home.png", 30, 30));

        JMenuItem taiKhoan = new JMenuItem("Tài Khoản");
        taiKhoan.setFont(fontItem);
        taiKhoan.setIcon(taoIcon("img/quanlytaikhoan.png", 30, 30));

        JMenuItem dangXuat = new JMenuItem("Đăng Xuất");
        dangXuat.setFont(fontItem);
        dangXuat.setIcon(taoIcon("img/dangxuat.png", 30, 30));
        dangXuat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));

        JMenuItem thoat = new JMenuItem("Thoát");
        thoat.setFont(fontItem);
        thoat.setIcon(taoIcon("img/quanly.png", 30, 30));
        thoat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        heThong.add(trangChu);
        if ("QuanLy".equals(phanQuyen)) {
            heThong.add(taiKhoan);
        }
        heThong.add(dangXuat);
        heThong.add(thoat);

        menuBar.add(heThong);
        menuBar.add(Box.createHorizontalStrut(30));

        // Danh Mục
        JMenu danhMuc = createHoverableMenu("Danh Mục");
        danhMuc.setIcon(taoIcon("img/danhmuc.png", 30, 30));
        danhMuc.setFont(fontMenu);

        JMenuItem monAn = new JMenuItem("Món Ăn");
        monAn.setFont(fontItem);
        monAn.setIcon(taoIcon("img/thucdon.png", 30, 30));

        JMenuItem hoaDon = new JMenuItem("Hóa Đơn");
        hoaDon.setFont(fontItem);
        hoaDon.setIcon(taoIcon("img/hoadon.png", 30, 30));

        JMenuItem khachHang = new JMenuItem("Khách Hàng");
        khachHang.setFont(fontItem);
        khachHang.setIcon(taoIcon("img/khachhang.png", 30, 30));

        JMenuItem nhanVien = new JMenuItem("Nhân Viên");
        nhanVien.setFont(fontItem);
        nhanVien.setIcon(taoIcon("img/nhanvien.png", 30, 30));

        JMenuItem khuyenMai = new JMenuItem("Khuyến Mãi");
        khuyenMai.setFont(fontItem);
        khuyenMai.setIcon(taoIcon("img/khuyenmai.png", 30, 30));

        danhMuc.add(monAn);
        danhMuc.add(hoaDon);
        danhMuc.add(khachHang);
        if ("QuanLy".equals(phanQuyen)) {
            danhMuc.add(nhanVien);
            danhMuc.add(khuyenMai);
        }

        menuBar.add(danhMuc);
        menuBar.add(Box.createHorizontalStrut(30));

        // Xử Lý
        JMenu xuLy = createHoverableMenu("Xử Lý");
        xuLy.setIcon(taoIcon("img/xuly.png", 30, 30));
        xuLy.setFont(fontMenu);

        JMenuItem ban = new JMenuItem("Bàn");
        ban.setFont(fontItem);
        ban.setIcon(taoIcon("img/ban.png", 30, 30));

        xuLy.add(ban);

        menuBar.add(xuLy);
        menuBar.add(Box.createHorizontalStrut(20));

        if ("QuanLy".equals(phanQuyen)) {
            JMenu thongKe = createHoverableMenu("Thống Kê");
            thongKe.setIcon(taoIcon("img/thongke.png", 30, 30));
            thongKe.setFont(fontMenu);

            JMenuItem monBanChay = new JMenuItem("Món Bán Chạy");
            monBanChay.setFont(fontItem);
            monBanChay.setIcon(taoIcon("img/thongke.png", 30, 30));

            JMenu doanhThu = createHoverableMenu("Doanh thu");
            doanhThu.setIcon(taoIcon("img/thongkemenu.png", 30, 30));
            doanhThu.setFont(fontItem);

            JMenuItem theoNgay = new JMenuItem("Theo Ngày");
            theoNgay.setFont(fontItem);
            theoNgay.setIcon(taoIcon("img/thongke.png", 30, 30));

            JMenuItem theoThang = new JMenuItem("Theo Tháng");
            theoThang.setFont(fontItem);
            theoThang.setIcon(taoIcon("img/thongke.png", 30, 30));

            JMenuItem theoNam = new JMenuItem("Theo Năm");
            theoNam.setFont(fontItem);
            theoNam.setIcon(taoIcon("img/thongke.png", 30, 30));

            doanhThu.add(theoNgay);
            doanhThu.add(theoThang);
            doanhThu.add(theoNam);

            thongKe.add(monBanChay);
            thongKe.add(doanhThu);

            menuBar.add(thongKe);
            menuBar.add(Box.createHorizontalStrut(30));
        }

        menuBar.add(Box.createHorizontalGlue());
        String userLabel = (FrmDangNhap.getCurrentTaiKhoan() != null)
                ? "Người dùng: " + FrmDangNhap.getCurrentTaiKhoan().getHoTen()
                : "Người dùng: Không xác định";
        lblQL = new JLabel(userLabel);
        lblQL.setIcon(taoIcon("img/nguoidung.png", 30, 30));
        lblQL.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblQL.setForeground(Color.DARK_GRAY);
        menuBar.add(lblQL);

        setJMenuBar(menuBar);

        bottomBar = taoBottomBar();
        add(bottomBar, BorderLayout.SOUTH);

        trangChu.addActionListener(e -> {
            new FrmTrangChu().setVisible(true);
            dispose();
        });

        taiKhoan.addActionListener(e -> {
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

        dangXuat.addActionListener(e -> {
            FrmDangNhap.resetCurrentTaiKhoan();
            resetInstance();
            new FrmDangNhap().setVisible(true);
            dispose();
        });

        thoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        ban.addActionListener(e -> {
            try {
                new FrmBan().setVisible(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            dispose();
        });

        monAn.addActionListener(e -> {
            new FrmThucDon().setVisible(true);
            dispose();
        });

        hoaDon.addActionListener(e -> {
            new FrmHoaDon().setVisible(true);
            dispose();
        });

        khachHang.addActionListener(e -> {
            new FrmKhachHang().setVisible(true);
            dispose();
        });

        nhanVien.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                new FrmNhanVien().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        khuyenMai.addActionListener(e -> {
            if ("QuanLy".equals(phanQuyen)) {
                new FrmKhuyenMai().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JMenu createHoverableMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.doClick();
            }
        });
        return menu;
    }

    private JPanel taoBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(245, 245, 245));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 180, 0, 0));

        ImageIcon iconAddress = new ImageIcon("img/diachi.png");
        Image imgAddr = iconAddress.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblAddress = new JLabel("Đại học Công Nghiệp Tp HCM", new ImageIcon(imgAddr), JLabel.LEFT);
        lblAddress.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblAddress.setForeground(Color.DARK_GRAY);

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

        btnHome = new NutBottom("Trang chủ", "img/home.png");
        btnQuanLy = new NutBottom("Thoát", "img/quanly.png");

        btnHome.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnQuanLy.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttonPanel.add(btnHome);
        buttonPanel.add(btnQuanLy);

        bottomBar.add(buttonPanel, BorderLayout.EAST);

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

        return bottomBar;
    }

    public JPanel getBottomBar() {
        return bottomBar;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    private ImageIcon taoIcon(String duongDan, int chieuRong, int chieuCao) {
        try {
            ImageIcon icon = new ImageIcon(duongDan);
            Image hinhAnh = icon.getImage().getScaledInstance(chieuRong, chieuCao, Image.SCALE_SMOOTH);
            return new ImageIcon(hinhAnh);
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon: " + duongDan);
            return null;
        }
    }

    class NutBottom extends JButton {
        private Color mauNenMacDinh = new Color(245, 245, 245);
        private Color mauHover = new Color(255, 106, 106);

        public NutBottom(String vanBan, String duongDanHinh) {
            super(vanBan);
            setFont(new Font("Times New Roman", Font.BOLD, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setBackground(mauNenMacDinh);
            setOpaque(true);

            try {
                ImageIcon icon = new ImageIcon(duongDanHinh);
                Image hinh = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(hinh));
            } catch (Exception e) {
                System.out.println("Không tìm thấy icon: " + duongDanHinh);
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    setForeground(mauHover);
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    setBackground(mauNenMacDinh);
                    setForeground(Color.BLACK);
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmTrangChu().setVisible(true));
    }
}