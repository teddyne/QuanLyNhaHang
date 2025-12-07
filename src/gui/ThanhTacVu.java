package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.KhachHang_DAO;
import dao.KhuVuc_DAO;
import dao.LoaiBan_DAO; // Added import for LoaiBan_DAO
import dao.LoaiKhachHang_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import dao.TaiKhoan_DAO;
import entity.Ban;
import entity.KhachHang; // Đã thêm import
import entity.MonAn;
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
        
        JMenuItem khachHang = new JMenuItem("Khách Hàng");
        khachHang.setFont(fontItem);
        khachHang.setIcon(taoIcon("img/khachhang.png", 30, 30));

        JMenuItem monAn = new JMenuItem("Thực đơn");
        monAn.setFont(fontItem);
        monAn.setIcon(taoIcon("img/thucdon.png", 30, 30));
        
        JMenuItem khuyenMai = new JMenuItem("Khuyến Mãi");
        khuyenMai.setFont(fontItem);
        khuyenMai.setIcon(taoIcon("img/khuyenmai.png", 30, 30));
        
        JMenuItem ban = new JMenuItem("Bàn");
        ban.setFont(fontItem);
        ban.setIcon(taoIcon("img/ban.png", 30, 30));   

        JMenuItem khuVuc = new JMenuItem("Khu Vực");
        khuVuc.setFont(fontItem);
        khuVuc.setIcon(taoIcon("img/khuvuc.png", 30, 30));

        JMenuItem nhanVien = new JMenuItem("Nhân Viên");
        nhanVien.setFont(fontItem);
        nhanVien.setIcon(taoIcon("img/nhanvien.png", 30, 30));
        
        danhMuc.add(khachHang);
        danhMuc.add(monAn);
        danhMuc.add(ban);

        if ("QuanLy".equals(phanQuyen)) {
            danhMuc.add(khuyenMai);
            danhMuc.add(khuVuc);
            danhMuc.add(nhanVien);
        }

        menuBar.add(danhMuc);
        menuBar.add(Box.createHorizontalStrut(30));        
       
        // Xử Lý
        JMenu xuLy = createHoverableMenu("Xử Lý");
        xuLy.setIcon(taoIcon("img/xuly.png", 30, 30));
        xuLy.setFont(fontMenu);

        JMenuItem dban = new JMenuItem("Đặt Bàn");
        dban.setFont(fontItem);
        dban.setIcon(taoIcon("img/ban.png", 30, 30));

        JMenuItem hoaDon = new JMenuItem("Hóa Đơn");
        hoaDon.setFont(fontItem);
        hoaDon.setIcon(taoIcon("img/hoadon.png", 30, 30));
        
        xuLy.add(dban);
        xuLy.add(hoaDon);

        menuBar.add(xuLy);
        menuBar.add(Box.createHorizontalStrut(30));

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
        tmon.addActionListener(e -> {
            try {
                moFormGoiYTenMon();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form tìm món: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JMenuItem tkh = new JMenuItem("Khách Hàng");
        tkh.setFont(fontItem);
        tkh.setIcon(taoIcon("img/khachhang.png", 30, 30));
        
        timKiem.add(tban);
        timKiem.add(tmon);
        timKiem.add(tkh);
        
        menuBar.add(timKiem);
        menuBar.add(Box.createHorizontalStrut(30));

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

 // =============================================
 // TÌM MÓN ĂN THEO TÊN (giống FrmDatMon)
 // =============================================
// private void moFormTimMonAn() throws SQLException {
//     JDialog dlg = new JDialog(this, "Tìm Món Ăn", true);
//     dlg.setLayout(new BorderLayout());
//     dlg.setSize(900, 600);
//     dlg.setLocationRelativeTo(this);
//     dlg.setResizable(false);
//
//     Connection conn = ConnectSQL.getConnection();
//     MonAn_DAO monAnDAO = new MonAn_DAO(conn);
//
//     // === HEADER: Tìm kiếm ===
//     JPanel pnlHeader = new JPanel(new BorderLayout());
//     pnlHeader.setBackground(new Color(169, 55, 68));
//     pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
//
//     JLabel lblTitle = new JLabel("TÌM KIẾM MÓN ĂN", SwingConstants.CENTER);
//     lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));
//     lblTitle.setForeground(Color.WHITE);
//     pnlHeader.add(lblTitle, BorderLayout.CENTER);
//
//     JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//     pnlSearch.setBackground(new Color(169, 55, 68));
//     JLabel lblTim = new JLabel("Tên món:");
//     lblTim.setFont(new Font("Times New Roman", Font.BOLD, 20));
//     lblTim.setForeground(Color.WHITE);
//
//     JTextField txtTim = new JTextField(25);
//     txtTim.setFont(new Font("Times New Roman", Font.PLAIN, 20));
//
//     JButton btnTim = new JButton("Tìm");
//     kieuNut(btnTim, new Color(102, 210, 74));
//     btnTim.setPreferredSize(new Dimension(100, 40));
//
//     pnlSearch.add(lblTim);
//     pnlSearch.add(txtTim);
//     pnlSearch.add(btnTim);
//     pnlHeader.add(pnlSearch, BorderLayout.SOUTH);
//
//     dlg.add(pnlHeader, BorderLayout.NORTH);
//
//     // === BẢNG KẾT QUẢ ===
//     String[] cols = {"Mã món", "Tên món", "Loại món", "Giá", "Trạng thái"};
//     DefaultTableModel model = new DefaultTableModel(cols, 0);
//     JTable tblKetQua = new JTable(model);
//     tblKetQua.setRowHeight(40);
//     tblKetQua.setFont(new Font("Times New Roman", Font.PLAIN, 18));
//     tblKetQua.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
//     tblKetQua.getTableHeader().setBackground(new Color(169, 55, 68));
//
//     JScrollPane scroll = new JScrollPane(tblKetQua);
//     scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//     dlg.add(scroll, BorderLayout.CENTER);
//
//     // === NÚT ĐÓNG ===
//     JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//     pnlBottom.setBackground(Color.WHITE);
//     JButton btnDong = new JButton("Đóng");
//     kieuNut(btnDong, new Color(231, 76, 60));
//     btnDong.setPreferredSize(new Dimension(120, 40));
//     btnDong.addActionListener(e -> dlg.dispose());
//     pnlBottom.add(btnDong);
//     dlg.add(pnlBottom, BorderLayout.SOUTH);
//
//     // === HÀNH ĐỘNG TÌM KIẾM ===
//     Runnable timKiem = () -> {
//         String keyword = txtTim.getText().trim().toLowerCase();
//         model.setRowCount(0);
//
//         List<MonAn> dsMon = monAnDAO.getAllMonAn();
//		 for (MonAn mon : dsMon) {
//		     if (keyword.isEmpty() || mon.getTenMon().toLowerCase().contains(keyword)) {
//		         String trangThai = mon.isTrangThai() ? "Còn món" : "Hết món";
//		         model.addRow(new Object[]{
//		             mon.getMaMon(),
//		             mon.getTenMon(),
//		             mon.getLoaiMon().getTenLoai(),
//		             String.format("%,.0f đ", mon.getDonGia()),
//		             trangThai
//		         });
//		     }
//		 }
//		 if (model.getRowCount() == 0) {
//		     JOptionPane.showMessageDialog(dlg, "Không tìm thấy món nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//		 }
//     };
//
//     btnTim.addActionListener(e -> timKiem.run());
//     txtTim.addKeyListener(new KeyAdapter() {
//         @Override
//         public void keyPressed(KeyEvent e) {
//             if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                 timKiem.run();
//             }
//         }
//     });
//
//     // Tải tất cả khi mở
//     timKiem.run();
//
//     dlg.setVisible(true);
// }
    // TÌM MÓN ĂN THEO TÊN (giống FrmDatMon)
    
    private void moFormGoiYTenMon() throws SQLException {
        JDialog dlg = new JDialog(this, "Tìm Món Ăn", true);
        dlg.setSize(900, 650);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);
        dlg.setLayout(new BorderLayout());

        // ===================== GIỮ NGUYÊN GIAO DIỆN ĐẸP CỦA BẠN =====================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(169, 55, 68));
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("TÌM KIẾM MÓN ĂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlSearch.setBackground(new Color(169, 55, 68));

        JTextField txtTim = new JTextField(40);
        txtTim.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        txtTim.setPreferredSize(new Dimension(550, 50));

        pnlSearch.add(new JLabel("Nhập tên món:", JLabel.RIGHT) {{
            setFont(new Font("Times New Roman", Font.BOLD, 22));
            setForeground(Color.WHITE);
        }});
        pnlSearch.add(txtTim);
        pnlHeader.add(pnlSearch, BorderLayout.SOUTH);
        dlg.add(pnlHeader, BorderLayout.NORTH);

        // JList + Model
        DefaultListModel<String> modelList = new DefaultListModel<>();
        JList<String> listGoiY = new JList<>(modelList);
        listGoiY.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        listGoiY.setFixedCellHeight(55);
        listGoiY.setSelectionBackground(new Color(169, 55, 68));
        listGoiY.setSelectionForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listGoiY);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(169, 55, 68), 4));
        dlg.add(scroll, BorderLayout.CENTER);

        // DAO – giữ nguyên như bạn
        MonAn_DAO monAnDAO = new MonAn_DAO(ConnectSQL.getConnection());
        List<MonAn> dsMonHienTai = new ArrayList<>(); // lưu món hiện tại

        // ===================== TỐI ƯU TÌM KIẾM NHƯ KIỂU NHỎ GỌN TRƯỚC ĐÓ =====================
        Timer timer = new Timer(300, e -> {
            String text = txtTim.getText().trim();
            modelList.clear();
            dsMonHienTai.clear();

            if (text.isEmpty()) {
                modelList.addElement("← Nhập tên món để tìm kiếm...");
                return;
            }

            new SwingWorker<List<MonAn>, Void>() {
                @Override
                protected List<MonAn> doInBackground() throws Exception {
                    return monAnDAO.timKiemMonAn(text); // giữ nguyên hàm cũ của bạn
                }

                @Override
                protected void done() {
                    try {
                        List<MonAn> ds = get();
                        if (ds.isEmpty()) {
                            modelList.addElement("→ Không tìm thấy món nào");
                        } else {
                            dsMonHienTai.addAll(ds);
                            for (MonAn mon : ds) {
                                modelList.addElement(mon.getTenMon());
                            }
                        }
                    } catch (Exception ex) {
                        modelList.addElement("Lỗi kết nối CSDL");
                    }
                }
            }.execute();
        });
        timer.setRepeats(false);

        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timer.restart(); }
            public void removeUpdate(DocumentEvent e) { timer.restart(); }
            public void changedUpdate(DocumentEvent e) { timer.restart(); }
        });

        // Double click – lấy đúng món theo index (không sợ trùng tên)
        listGoiY.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listGoiY.getSelectedIndex();
                    if (index >= 0 && index < dsMonHienTai.size()) {
                        MonAn mon = dsMonHienTai.get(index);
                        moFormHienThiThongTinMon(mon);
                        SwingUtilities.invokeLater(() -> dlg.dispose());
                    }
                }
            }
        });

        // Enter chọn món đầu
        txtTim.addActionListener(e -> {
            if (modelList.size() > 0 && !modelList.get(0).startsWith("→") && !modelList.get(0).startsWith("←")) {
                listGoiY.setSelectedIndex(0);
                listGoiY.dispatchEvent(new MouseEvent(listGoiY, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, 0, 0, 2, false));
            }
        });

        // Nút đóng
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBorder(new EmptyBorder(10, 20, 15, 20));
        JButton btnDong = new JButton("Đóng");
        kieuNut(btnDong, new Color(231, 76, 60));
        btnDong.setPreferredSize(new Dimension(140, 45));
        btnDong.addActionListener(e -> dlg.dispose());
        pnlBottom.add(btnDong);
        dlg.add(pnlBottom, BorderLayout.SOUTH);

        // Khởi đầu
        modelList.addElement("← Nhập tên món để tìm kiếm...");
        dlg.setVisible(true);
    }

       private static final Color MAU_DO_RUOU = new Color(169, 55, 68);
       private static final Color MAU_HONG = new Color(241, 200, 204);
       
       private void moFormHienThiThongTinMon(MonAn mon) {
           if (mon == null) return;

           // --- Dialog ---
           JDialog dlg = new JDialog(this, "Thông tin món", true);
           dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
           dlg.getContentPane().setBackground(Color.WHITE);
           dlg.setLayout(new BorderLayout(10, 10));

           // --- Panel chính với border đỏ ---
           JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
           pnlMain.setBackground(Color.WHITE);
           pnlMain.setBorder(BorderFactory.createLineBorder(MAU_DO_RUOU, 3));

           // --- Ảnh món ---
           JLabel lblAnh = new JLabel();
           lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
           lblAnh.setPreferredSize(new Dimension(180, 140));
           String duongDan = mon.getAnhMon() != null && !mon.getAnhMon().isEmpty() ? mon.getAnhMon() : "img/placeholder.png";
           datAnhChoLabel(lblAnh, duongDan, 180, 140);
           lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
           pnlMain.add(lblAnh, BorderLayout.NORTH);

           // --- Thông tin món ---
           JPanel pnlTT = new JPanel(new GridBagLayout());
           pnlTT.setBackground(Color.WHITE);
           GridBagConstraints gbc = new GridBagConstraints();
           gbc.insets = new Insets(4, 6, 4, 6);
           gbc.fill = GridBagConstraints.HORIZONTAL;

           int row = 0;

           // Mã món
           gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
           JLabel lblMa = new JLabel("<html><u>" + mon.getMaMon() + "</u></html>");
           lblMa.setFont(new Font("Times New Roman", Font.BOLD, 16));
           pnlTT.add(lblMa, gbc);

           // Tên món
           row++;
           gbc.gridy = row;
           JLabel lblTen = new JLabel(mon.getTenMon(), SwingConstants.CENTER);
           lblTen.setFont(new Font("Times New Roman", Font.BOLD, 20));
           pnlTT.add(lblTen, gbc);

           // Loại món
           row++;
           gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
           JLabel lblLoaiLabel = new JLabel("Loại món:");
           lblLoaiLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
           pnlTT.add(lblLoaiLabel, gbc);
           gbc.gridx = 1;
           JLabel lblLoai = new JLabel(mon.getLoaiMon().getTenLoai());
           lblLoai.setFont(new Font("Times New Roman", Font.PLAIN, 16));
           pnlTT.add(lblLoai, gbc);

           // Trạng thái
           row++;
           gbc.gridx = 0; gbc.gridy = row;
           JLabel lblTrangThaiLabel = new JLabel("Trạng thái:");
           lblTrangThaiLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
           pnlTT.add(lblTrangThaiLabel, gbc);
           gbc.gridx = 1;
           JLabel lblTrangThai = new JLabel(mon.isTrangThai() ? "Còn món" : "Hết món");
           lblTrangThai.setFont(new Font("Times New Roman", Font.PLAIN, 16));
           lblTrangThai.setForeground(mon.isTrangThai() ? Color.GREEN.darker() : Color.RED);
           pnlTT.add(lblTrangThai, gbc);

           // Mô tả
           row++;
           gbc.gridx = 0; gbc.gridy = row;
           JLabel lblMoTaLabel = new JLabel("Mô tả:");
           lblMoTaLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
           pnlTT.add(lblMoTaLabel, gbc);
           gbc.gridx = 1;
           JLabel lblMoTa = new JLabel("<html><i>" + (mon.getMoTa() != null ? mon.getMoTa() : "") + "</i></html>");
           lblMoTa.setFont(new Font("Times New Roman", Font.PLAIN, 16));
           pnlTT.add(lblMoTa, gbc);

           // Giá bán
           row++;
           gbc.gridx = 1; gbc.gridy = row; gbc.anchor = GridBagConstraints.EAST;
           JLabel lblGia = new JLabel(String.format("%,.0f đ", mon.getDonGia()));
           lblGia.setFont(new Font("Times New Roman", Font.BOLD, 18));
           lblGia.setForeground(Color.RED.darker());
           pnlTT.add(lblGia, gbc);

           // Cho pnlTT vào JScrollPane để scroll khi cần
           JScrollPane scrollTT = new JScrollPane(pnlTT);
           scrollTT.setBorder(null);
           scrollTT.setBackground(Color.WHITE);
           pnlMain.add(scrollTT, BorderLayout.CENTER);

           dlg.add(pnlMain, BorderLayout.CENTER);

           // --- Panel nút ---
           JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
           pnlButtons.setBackground(Color.WHITE);

           JButton btnDong = new JButton("Đóng");
           kieuNut(btnDong, new Color(231, 76, 60));
           btnDong.setPreferredSize(new Dimension(140, 45));
           btnDong.setFont(new Font("Times New Roman", Font.BOLD, 18));
           btnDong.addActionListener(e -> dlg.dispose());

           pnlButtons.add(btnDong);
           dlg.add(pnlButtons, BorderLayout.SOUTH);

           dlg.setSize(300, 480);           
           dlg.setLocationRelativeTo(this);
           dlg.setVisible(true);
       }



       private void datAnhChoLabel(JLabel lbl, String path, int w, int h) {
           try {
               if (path != null && !path.isEmpty()) {
                   File file = new File(path);
                   if (file.exists()) {
                       ImageIcon icon = new ImageIcon(new ImageIcon(path)
                               .getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
                       lbl.setIcon(icon);
                       return;
                   }
               }
               lbl.setIcon(null);
               lbl.setText("");
               lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
           } catch (Exception e) {
               lbl.setText("");
               lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
           }
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
        dlg.setSize(500, 400);
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

//        if (!trangThai.equals("Trống")){
//            try {
//                java.util.Date today = new java.util.Date();
//                List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(ban.getMaBan(), new java.sql.Date(today.getTime()));
//                if (!list.isEmpty()) {
//                    PhieuDatBan pdb = list.get(0);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 5;
//                    gbc.gridwidth = 2;
//                    JLabel lblDatBan = new JLabel("Thông tin đặt bàn:");
//                    lblDatBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
//                    pnlThongTin.add(lblDatBan, gbc);
//
//                    gbc.gridwidth = 1;
//                    gbc.gridy = 6;
//                    JLabel lblTenKhachTitle = new JLabel("Tên khách:");
//                    lblTenKhachTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblTenKhachTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblTenKhachValue = new JLabel(pdb.getTenKhach());
//                    lblTenKhachValue.setFont(fontValue);
//                    pnlThongTin.add(lblTenKhachValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 7;
//                    JLabel lblSoDienThoaiTitle = new JLabel("Số điện thoại:");
//                    lblSoDienThoaiTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblSoDienThoaiTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblSoDienThoaiValue = new JLabel(pdb.getSoDienThoai() != null ? pdb.getSoDienThoai() : "Không có");
//                    lblSoDienThoaiValue.setFont(fontValue);
//                    pnlThongTin.add(lblSoDienThoaiValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 8;
//                    JLabel lblSoNguoiTitle = new JLabel("Số người:");
//                    lblSoNguoiTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblSoNguoiTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblSoNguoiValue = new JLabel(String.valueOf(pdb.getSoNguoi()));
//                    lblSoNguoiValue.setFont(fontValue);
//                    pnlThongTin.add(lblSoNguoiValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 9;
//                    JLabel lblGioDenTitle = new JLabel("Giờ đến:");
//                    lblGioDenTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblGioDenTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblGioDenValue = new JLabel(pdb.getGioDen().toString());
//                    lblGioDenValue.setFont(fontValue);
//                    pnlThongTin.add(lblGioDenValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 10;
//                    JLabel lblGhiChuTitle = new JLabel("Ghi chú:");
//                    lblGhiChuTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblGhiChuTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblGhiChuValue = new JLabel(pdb.getGhiChu() != null ? pdb.getGhiChu() : "Không có");
//                    lblGhiChuValue.setFont(fontValue);
//                    pnlThongTin.add(lblGhiChuValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 11;
//                    JLabel lblTienCocTitle = new JLabel("Tiền cọc:");
//                    lblTienCocTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblTienCocTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblTienCocValue = new JLabel(String.format("%,.0f VNĐ", pdb.getTienCoc()));
//                    lblTienCocValue.setFont(fontValue);
//                    pnlThongTin.add(lblTienCocValue, gbc);
//
//                    gbc.gridx = 0;
//                    gbc.gridy = 12;
//                    JLabel lblGhiChuCocTitle = new JLabel("Ghi chú cọc:");
//                    lblGhiChuCocTitle.setFont(fontLabel);
//                    pnlThongTin.add(lblGhiChuCocTitle, gbc);
//                    gbc.gridx = 1;
//                    JLabel lblGhiChuCocValue = new JLabel(pdb.getGhiChuCoc() != null ? pdb.getGhiChuCoc() : "Không có");
//                    lblGhiChuCocValue.setFont(fontValue);
//                    pnlThongTin.add(lblGhiChuCocValue, gbc);
//                }
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(dlg, "Lỗi khi lấy thông tin phiếu đặt bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        }

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
        dlg.setSize(600, 350);
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
            boolean hasPhucVu = list.stream().anyMatch(d -> "Phục vụ".equals(d.getTrangThai()));
            if (hasPhucVu) return "Đang phục vụ";
            boolean hasDat = list.stream().anyMatch(d -> "Đặt".equals(d.getTrangThai()));
            if (hasDat) return "Đã đặt";
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

    //Thanh dưới
    private JPanel taoBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(245, 245, 245));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 180, 0, 0));

        ImageIcon iconAddress = new ImageIcon("img/diachi.png");
        Image imgAddr = iconAddress.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblAddress = new JLabel("Nhà Hàng Vang_Gò Vấp_TP.HCM", new ImageIcon(imgAddr), JLabel.LEFT);
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
    public static void kieuNut(JButton button, Color baseColor) {
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