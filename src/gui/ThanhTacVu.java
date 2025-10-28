package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.KhachHang_DAO;
import dao.KhuVuc_DAO;
import dao.LoaiBan_DAO; // Added import for LoaiBan_DAO
import dao.LoaiKhachHang_DAO;
import dao.PhieuDatBan_DAO;
import dao.TaiKhoan_DAO;
import entity.Ban;
import entity.KhachHang; // Đã thêm import
import entity.PhieuDatBan;
import entity.TaiKhoan;

public class ThanhTacVu extends JFrame {
    private static String phanQuyen = null;
    private static ThanhTacVu instance;
    private JMenuBar menuBar;
    private NutBottom btnHome;
    private NutBottom btnQuanLy;
    private JPanel bottomBar;
    private JLabel lblQL;

    public static void setPhanQuyen(String quyen) {
        phanQuyen = quyen;
    }

    public static void resetInstance() {
        instance = null;
        phanQuyen = null;
    }

    public static ThanhTacVu getInstance() {
        if (instance == null) {
            instance = new ThanhTacVu();
        }
        return instance;
    }

    public ThanhTacVu() {
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

        JMenuItem monAn = new JMenuItem("Thực đơn");
        monAn.setFont(fontItem);
        monAn.setIcon(taoIcon("img/thucdon.png", 30, 30));

        JMenuItem khuVuc = new JMenuItem("Khu Vực");
        khuVuc.setFont(fontItem);
        khuVuc.setIcon(taoIcon("img/khuvuc.png", 30, 30));
        
        JMenuItem ban = new JMenuItem("Bàn");
        ban.setFont(fontItem);
        ban.setIcon(taoIcon("img/ban.png", 30, 30));
        
        JMenuItem hoaDon = new JMenuItem("Hóa Đơn");
        hoaDon.setFont(fontItem);
        hoaDon.setIcon(taoIcon("img/hoadon.png", 30, 30));
        
        danhMuc.add(ban);
        danhMuc.add(monAn);
        danhMuc.add(hoaDon);
        if ("QuanLy".equals(phanQuyen)) {
            danhMuc.add(khuVuc);
        }

        menuBar.add(danhMuc);
        menuBar.add(Box.createHorizontalStrut(30));
        
        // Cập nhật
        JMenu capNhat = createHoverableMenu("Cập Nhật");
        capNhat.setIcon(taoIcon("img/capnhat.png", 30, 30));
        capNhat.setFont(fontMenu);
        
        JMenuItem khachHang = new JMenuItem("Khách Hàng");
        khachHang.setFont(fontItem);
        khachHang.setIcon(taoIcon("img/khachhang.png", 30, 30));
        
        JMenuItem khuyenMai = new JMenuItem("Khuyến Mãi");
        khuyenMai.setFont(fontItem);
        khuyenMai.setIcon(taoIcon("img/khuyenmai.png", 30, 30));

        JMenuItem nhanVien = new JMenuItem("Nhân Viên");
        nhanVien.setFont(fontItem);
        nhanVien.setIcon(taoIcon("img/nhanvien.png", 30, 30));
        
        capNhat.add(khachHang);
        if ("QuanLy".equals(phanQuyen)) {
            capNhat.add(khuyenMai);
            capNhat.add(nhanVien);
        }
        
        menuBar.add(capNhat);
        menuBar.add(Box.createHorizontalStrut(30));

        // Xử Lý
        JMenu xuLy = createHoverableMenu("Xử Lý");
        xuLy.setIcon(taoIcon("img/xuly.png", 30, 30));
        xuLy.setFont(fontMenu);

        JMenuItem dban = new JMenuItem("Đặt Bàn");
        dban.setFont(fontItem);
        dban.setIcon(taoIcon("img/ban.png", 30, 30));

        JMenuItem thanhToan = new JMenuItem("Thanh Toán");
        thanhToan.setFont(fontItem);
        thanhToan.setIcon(taoIcon("img/hoadon.png", 30, 30));
        
        xuLy.add(dban);
        xuLy.add(thanhToan);

        menuBar.add(xuLy);
        menuBar.add(Box.createHorizontalStrut(20));

        // Tìm Kiếm
        JMenu timKiem = createHoverableMenu("Tìm Kiếm");
        timKiem.setIcon(taoIcon("img/timkiem.png", 30, 30));
        timKiem.setFont(fontMenu);

        JMenuItem tban = new JMenuItem("Bàn");
        tban.setFont(fontItem);
        tban.setIcon(taoIcon("img/ban.png", 30, 30));
        
        JMenuItem tmon = new JMenuItem("Món ăn");
        tmon.setFont(fontItem);
        tmon.setIcon(taoIcon("img/thucdon.png", 30, 30));
        
        JMenuItem tkh = new JMenuItem("Khách Hàng");
        tkh.setFont(fontItem);
        tkh.setIcon(taoIcon("img/khachhang.png", 30, 30));
        
        timKiem.add(tban);
        timKiem.add(tmon);
        timKiem.add(tkh);
        
        menuBar.add(timKiem);
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
            
            theoNgay.addActionListener(e -> {
                new FrmThongKeChinh(1).setVisible(true); 
                dispose(); 
            });

            monBanChay.addActionListener(e -> {
                new FrmThongKeChinh(0).setVisible(true); 
                dispose(); 
            });

            theoThang.addActionListener(e -> {
                new FrmThongKeChinh(2).setVisible(true);
                dispose(); 
            });

            theoNam.addActionListener(e -> {
                new FrmThongKeChinh(3).setVisible(true);
                dispose(); 
            });

            menuBar.add(thongKe);
            menuBar.add(Box.createHorizontalStrut(30));
        }

        menuBar.add(Box.createHorizontalGlue());
        String userLabel = (TaiKhoan_DAO.getCurrentTaiKhoan() != null)
                ? "Người dùng: " + TaiKhoan_DAO.getCurrentTaiKhoan().getHoTen()
                : "Người dùng: Không xác định";
        lblQL = new JLabel(userLabel);
        lblQL.setIcon(taoIcon("img/nguoidung.png", 30, 30));
        lblQL.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblQL.setForeground(Color.DARK_GRAY);
        menuBar.add(lblQL);

        setJMenuBar(menuBar);

        bottomBar = taoBottomBar();
        add(bottomBar, BorderLayout.SOUTH);

        // Xử lý sự kiện menu
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
        	TaiKhoan_DAO.resetCurrentTaiKhoan();
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

        dban.addActionListener(e -> {
            try {
				new FrmBan().setVisible(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            dispose();
        });
        
        khuVuc.addActionListener(e -> {
            try {
                Connection conn = ConnectSQL.getConnection();
                if (conn != null) {
                    KhuVuc_DAO khuDAO = new KhuVuc_DAO(conn);
                    new FrmKhuVuc(khuDAO, null).setExtendedState(JFrame.MAXIMIZED_BOTH);
                    new FrmKhuVuc(khuDAO, null).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở quản lý khu vực: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        ban.addActionListener(e -> {
            try {
                Connection conn = ConnectSQL.getConnection();
                Ban_DAO banDAO = new Ban_DAO(conn);
                KhuVuc_DAO khuVucDAO = new KhuVuc_DAO(conn);
                LoaiBan_DAO loaiBanDAO = new LoaiBan_DAO(conn); // Initialize LoaiBan_DAO
                new FrmQLBan(banDAO, khuVucDAO, loaiBanDAO, null).setVisible(true);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở quản lý bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        monAn.addActionListener(e -> {
            try {
				new FrmThucDon().setVisible(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            dispose();
        });

        hoaDon.addActionListener(e -> {
            new FrmHoaDon().setVisible(true);
            dispose();
        });

        khachHang.addActionListener(e -> {
            try {
                Connection conn = ConnectSQL.getConnection();
                KhachHang_DAO khachHangDAO = new KhachHang_DAO(conn);
                LoaiKhachHang_DAO loaiKH_DAO = new LoaiKhachHang_DAO(conn);
                
                new FrmKhachHang(khachHangDAO, loaiKH_DAO, null).setVisible(true);
                dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở quản lý khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
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

        tban.addActionListener(e -> {
            try {
                moFormNhapMaBan();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form tìm kiếm bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // ==================================================================
        // PHẦN CODE MỚI ĐƯỢC THÊM CHO TÌM KIẾM KHÁCH HÀNG
        // ==================================================================
        tkh.addActionListener(e -> {
            try {
                moFormNhapSDTKhachHang();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form tìm kiếm khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void moFormNhapMaBan() throws SQLException {
	    JDialog dlg = new JDialog(this, "Nhập Mã Bàn", false);
	    dlg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	    dlg.setSize(500, 120);
	    dlg.setLocationRelativeTo(this);
	    dlg.setResizable(false);
	
	    Connection conn = ConnectSQL.getConnection();
	    Ban_DAO banDAO = new Ban_DAO(conn);
	    PhieuDatBan_DAO phieuDatBanDAO = new PhieuDatBan_DAO(conn);
	
	    JLabel lblMaBan = new JLabel("Mã bàn:");
	    lblMaBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
	
	    JTextField txtMaBan = new JTextField(15);
	    txtMaBan.setFont(new Font("Times New Roman", Font.PLAIN, 22));
	    
	    JButton btnTim = new JButton("Tìm");
        kieuNut(btnTim, new Color(102, 210, 74));
	    btnTim.setForeground(Color.WHITE);
	    btnTim.setFocusPainted(false);
	    btnTim.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	
	    dlg.add(lblMaBan);
	    dlg.add(txtMaBan);
	    dlg.add(btnTim);
	
	    btnTim.addActionListener(e -> {
	        String maBan = txtMaBan.getText().trim();
	        if (maBan.isEmpty()) {
	            JOptionPane.showMessageDialog(dlg, "Vui lòng nhập mã bàn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	
	        try {
	            Ban ban = banDAO.getBanByMa(maBan);
	            if (ban == null) {
	                JOptionPane.showMessageDialog(dlg, "Không tìm thấy bàn với mã: " + maBan, "Thông báo", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	
	            moFormHienThiThongTinBan(ban, phieuDatBanDAO);
	            dlg.dispose();
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(dlg, "Lỗi khi tìm kiếm bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	
	    txtMaBan.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                btnTim.doClick();
	            }
	        }
	    });
	
	    dlg.setVisible(true);
	}

    private void moFormHienThiThongTinBan(Ban ban, PhieuDatBan_DAO phieuDatBanDAO) {
        JDialog dlg = new JDialog(this, "Thông Tin Bàn", false);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.getContentPane().setBackground(Color.white);
        dlg.setSize(600, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        pnlThongTin.setBackground(Color.WHITE);
        pnlThongTin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(128, 0, 0), 2), "Thông tin bàn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 24)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 20);

        String trangThai = layTrangThaiHienTai(ban.getMaBan(), phieuDatBanDAO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaBanTitle = new JLabel("Mã bàn:");
        lblMaBanTitle.setFont(fontLabel);
        pnlThongTin.add(lblMaBanTitle, gbc);
        gbc.gridx = 1;
        JLabel lblMaBanValue = new JLabel(ban.getMaBan());
        lblMaBanValue.setFont(fontValue);
        pnlThongTin.add(lblMaBanValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTrangThaiTitle = new JLabel("Trạng thái:");
        lblTrangThaiTitle.setFont(fontLabel);
        pnlThongTin.add(lblTrangThaiTitle, gbc);
        gbc.gridx = 1;
        JLabel lblTrangThaiValue = new JLabel(trangThai);
        lblTrangThaiValue.setFont(fontValue);
        pnlThongTin.add(lblTrangThaiValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblLoaiBanTitle = new JLabel("Loại bàn:");
        lblLoaiBanTitle.setFont(fontLabel);
        pnlThongTin.add(lblLoaiBanTitle, gbc);
        gbc.gridx = 1;
        JLabel lblLoaiBanValue = new JLabel(ban.getMaBan());
        lblLoaiBanValue.setFont(fontValue);
        pnlThongTin.add(lblLoaiBanValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblSoChoNgoiTitle = new JLabel("Số chỗ ngồi:");
        lblSoChoNgoiTitle.setFont(fontLabel);
        pnlThongTin.add(lblSoChoNgoiTitle, gbc);
        gbc.gridx = 1;
        JLabel lblSoChoNgoiValue = new JLabel(String.valueOf(ban.getSoChoNgoi()));
        lblSoChoNgoiValue.setFont(fontValue);
        pnlThongTin.add(lblSoChoNgoiValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblKhuVucTitle = new JLabel("Khu vực:");
        lblKhuVucTitle.setFont(fontLabel);
        pnlThongTin.add(lblKhuVucTitle, gbc);
        gbc.gridx = 1;
        JLabel lblKhuVucValue = new JLabel(ban.getTenKhuVuc());
        lblKhuVucValue.setFont(fontValue);
        pnlThongTin.add(lblKhuVucValue, gbc);

        if (!trangThai.equals("Trống")) {
            try {
                java.util.Date today = new java.util.Date();
                List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(ban.getMaBan(), new java.sql.Date(today.getTime()));
                if (!list.isEmpty()) {
                    PhieuDatBan pdb = list.get(0);

                    gbc.gridx = 0;
                    gbc.gridy = 5;
                    gbc.gridwidth = 2;
                    JLabel lblDatBan = new JLabel("Thông tin đặt bàn:");
                    lblDatBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
                    pnlThongTin.add(lblDatBan, gbc);

                    gbc.gridwidth = 1;
                    gbc.gridy = 6;
                    JLabel lblTenKhachTitle = new JLabel("Tên khách:");
                    lblTenKhachTitle.setFont(fontLabel);
                    pnlThongTin.add(lblTenKhachTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblTenKhachValue = new JLabel(pdb.getTenKhach());
                    lblTenKhachValue.setFont(fontValue);
                    pnlThongTin.add(lblTenKhachValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    JLabel lblSoDienThoaiTitle = new JLabel("Số điện thoại:");
                    lblSoDienThoaiTitle.setFont(fontLabel);
                    pnlThongTin.add(lblSoDienThoaiTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblSoDienThoaiValue = new JLabel(pdb.getSoDienThoai() != null ? pdb.getSoDienThoai() : "Không có");
                    lblSoDienThoaiValue.setFont(fontValue);
                    pnlThongTin.add(lblSoDienThoaiValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 8;
                    JLabel lblSoNguoiTitle = new JLabel("Số người:");
                    lblSoNguoiTitle.setFont(fontLabel);
                    pnlThongTin.add(lblSoNguoiTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblSoNguoiValue = new JLabel(String.valueOf(pdb.getSoNguoi()));
                    lblSoNguoiValue.setFont(fontValue);
                    pnlThongTin.add(lblSoNguoiValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 9;
                    JLabel lblGioDenTitle = new JLabel("Giờ đến:");
                    lblGioDenTitle.setFont(fontLabel);
                    pnlThongTin.add(lblGioDenTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblGioDenValue = new JLabel(pdb.getGioDen().toString());
                    lblGioDenValue.setFont(fontValue);
                    pnlThongTin.add(lblGioDenValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 10;
                    JLabel lblGhiChuTitle = new JLabel("Ghi chú:");
                    lblGhiChuTitle.setFont(fontLabel);
                    pnlThongTin.add(lblGhiChuTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblGhiChuValue = new JLabel(pdb.getGhiChu() != null ? pdb.getGhiChu() : "Không có");
                    lblGhiChuValue.setFont(fontValue);
                    pnlThongTin.add(lblGhiChuValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 11;
                    JLabel lblTienCocTitle = new JLabel("Tiền cọc:");
                    lblTienCocTitle.setFont(fontLabel);
                    pnlThongTin.add(lblTienCocTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblTienCocValue = new JLabel(String.format("%,.0f VNĐ", pdb.getTienCoc()));
                    lblTienCocValue.setFont(fontValue);
                    pnlThongTin.add(lblTienCocValue, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 12;
                    JLabel lblGhiChuCocTitle = new JLabel("Ghi chú cọc:");
                    lblGhiChuCocTitle.setFont(fontLabel);
                    pnlThongTin.add(lblGhiChuCocTitle, gbc);
                    gbc.gridx = 1;
                    JLabel lblGhiChuCocValue = new JLabel(pdb.getGhiChuCoc() != null ? pdb.getGhiChuCoc() : "Không có");
                    lblGhiChuCocValue.setFont(fontValue);
                    pnlThongTin.add(lblGhiChuCocValue, gbc);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dlg, "Lỗi khi lấy thông tin phiếu đặt bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        JScrollPane scrollThongTin = new JScrollPane(pnlThongTin);
        scrollThongTin.setBorder(null);
        dlg.add(scrollThongTin, BorderLayout.CENTER);

        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnDiChuyen = new JButton("Quản lý đặt bàn");
        kieuNut(btnDiChuyen,new Color(55, 212, 23));
        btnDiChuyen.setForeground(Color.WHITE);
        btnDiChuyen.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlButtons.add(btnDiChuyen);

        JButton btnDong = new JButton("Đóng");
        kieuNut(btnDong, new Color(236, 66, 48));
        btnDong.setForeground(Color.WHITE);
        btnDong.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlButtons.add(btnDong);

        dlg.add(pnlButtons, BorderLayout.SOUTH);

        btnDong.addActionListener(e -> dlg.dispose());

        btnDiChuyen.addActionListener(e -> {
            try {
				new FrmBan().setVisible(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dlg.dispose();
        });

        // Hiển thị dialog
        dlg.setVisible(true);
    }
    
   
    private void moFormNhapSDTKhachHang() throws SQLException {
	    JDialog dlg = new JDialog(this, "Nhập Mã Khách Hàng", false);
	    dlg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	    dlg.setSize(600, 120);
	    dlg.setLocationRelativeTo(this);
	    dlg.setResizable(false);
	
	    Connection conn = ConnectSQL.getConnection();
	    KhachHang_DAO khachHangDAO = new KhachHang_DAO(conn);
	
	    JLabel lblSdt = new JLabel("Số điện thoại:");
	    lblSdt.setFont(new Font("Times New Roman", Font.BOLD, 22));
	
	    JTextField txtSdt = new JTextField(15);
	    txtSdt.setFont(new Font("Times New Roman", Font.PLAIN, 22));
	    
	    JButton btnTim = new JButton("Tìm");
        kieuNut(btnTim, new Color(102, 210, 74));
	    btnTim.setForeground(Color.WHITE);
	    btnTim.setFocusPainted(false);
	    btnTim.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	
	    dlg.add(lblSdt);
	    dlg.add(txtSdt);
	    dlg.add(btnTim);
	
	    btnTim.addActionListener(e -> {
	        String sdt = txtSdt.getText().trim();
	        if (sdt.isEmpty()) {
	            JOptionPane.showMessageDialog(dlg, "Vui lòng nhập Số điện thoại khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	
	        try {
	            KhachHang kh = khachHangDAO.timKhachHangTheoSDT(sdt); 
	            if (kh == null) {
	                JOptionPane.showMessageDialog(dlg, "Không tìm thấy khách hàng với Số điện thoại: " + sdt, "Thông báo", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	
	            moFormHienThiThongTinKhachHang(kh);
	            dlg.dispose();
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(dlg, "Lỗi khi tìm kiếm khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	
	    txtSdt.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                btnTim.doClick();
	            }
	        }
	    });
	
	    dlg.setVisible(true);
	}

    
	private void moFormHienThiThongTinKhachHang(KhachHang kh) {
        JDialog dlg = new JDialog(this, "Thông Tin Khách Hàng", false);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.getContentPane().setBackground(Color.white);
        dlg.setSize(600, 350); // Kích thước nhỏ hơn vì ít thông tin hơn
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        pnlThongTin.setBackground(Color.WHITE);
        pnlThongTin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(128, 0, 0), 2), "Thông tin khách hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 24)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaKHTitle = new JLabel("Mã khách hàng:");
        lblMaKHTitle.setFont(fontLabel);
        pnlThongTin.add(lblMaKHTitle, gbc);
        gbc.gridx = 1;
        JLabel lblMaKHValue = new JLabel(kh.getMaKH());
        lblMaKHValue.setFont(fontValue);
        pnlThongTin.add(lblMaKHValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenKHTitle = new JLabel("Tên khách hàng:");
        lblTenKHTitle.setFont(fontLabel);
        pnlThongTin.add(lblTenKHTitle, gbc);
        gbc.gridx = 1;
        JLabel lblTenKHValue = new JLabel(kh.getTenKH());
        lblTenKHValue.setFont(fontValue);
        pnlThongTin.add(lblTenKHValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblSoDienThoaiTitle = new JLabel("Số điện thoại:");
        lblSoDienThoaiTitle.setFont(fontLabel);
        pnlThongTin.add(lblSoDienThoaiTitle, gbc);
        gbc.gridx = 1;
        JLabel lblSoDienThoaiValue = new JLabel(kh.getSdt());
        lblSoDienThoaiValue.setFont(fontValue);
        pnlThongTin.add(lblSoDienThoaiValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblLoaiKHTitle = new JLabel("Loại khách hàng:");
        lblLoaiKHTitle.setFont(fontLabel);
        pnlThongTin.add(lblLoaiKHTitle, gbc);
        gbc.gridx = 1;
        
        JLabel lblLoaiKHValue = new JLabel(kh.getloaiKH()); 
        lblLoaiKHValue.setFont(fontValue);
        pnlThongTin.add(lblLoaiKHValue, gbc);

        JScrollPane scrollThongTin = new JScrollPane(pnlThongTin);
        scrollThongTin.setBorder(null);
        dlg.add(scrollThongTin, BorderLayout.CENTER);

        // Panel nút
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnDiChuyen = new JButton("Quản lý khách hàng");
        kieuNut(btnDiChuyen, new Color(55, 212, 23));
        btnDiChuyen.setForeground(Color.WHITE);
        btnDiChuyen.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlButtons.add(btnDiChuyen);

        JButton btnDong = new JButton("Đóng");
        kieuNut(btnDong, new Color(236, 66, 48));
        btnDong.setForeground(Color.WHITE);
        btnDong.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlButtons.add(btnDong);

        dlg.add(pnlButtons, BorderLayout.SOUTH);

        btnDong.addActionListener(e -> dlg.dispose());

        btnDiChuyen.addActionListener(e -> {
            try {
                Connection conn = ConnectSQL.getConnection();
                KhachHang_DAO khachHangDAO = new KhachHang_DAO(conn);
                LoaiKhachHang_DAO loaiKH_DAO = new LoaiKhachHang_DAO(conn);
                
                new FrmKhachHang(khachHangDAO, loaiKH_DAO, null).setVisible(true);
                dlg.dispose(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Lỗi khi mở quản lý khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dlg.setVisible(true);
    }
    

    private String layTrangThaiHienTai(String maBan, PhieuDatBan_DAO phieuDatBanDAO) {
        try {
            java.util.Date today = new java.util.Date();
            List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(maBan, new java.sql.Date(today.getTime()));
            boolean hasPhucVu = list.stream().anyMatch(d -> "Đang phục vụ".equals(d.getTrangThai()));
            if (hasPhucVu) return "Đang phục vụ";
            boolean hasDat = list.stream().anyMatch(d -> "Đặt".equals(d.getTrangThai()));
            if (hasDat) return "Đặt";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Trống";
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
    private void kieuNut(JButton button, Color baseColor) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setBackground(baseColor);

        Color hoverColor = baseColor.darker();
        Color clickColor = baseColor.darker();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(hoverColor);
            }
        });
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
        SwingUtilities.invokeLater(() -> {
            new FrmTrangChu().setVisible(true);
        });
    }
}