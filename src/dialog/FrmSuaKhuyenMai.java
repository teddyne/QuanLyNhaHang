//package dialog;
//
//import dao.KhuyenMai_DAO;
//import dao.LoaiKhuyenMai_DAO;
//import entity.KhuyenMai;
//import entity.LoaiKhuyenMai;
//
//import javax.swing.*;
//
//import connectSQL.ConnectSQL;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.SQLException;
//
//public class FrmSuaKhuyenMai extends JFrame {
//    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu, txtTrangThai;
//    private JComboBox<String> cmbLoaiKM, cmbDoiTuongApDung;
//    private JSpinner spTuNgay, spDenNgay;
//    private JButton btnLuu, btnHuy;
//    private KhuyenMai_DAO kmDAO;
//    private JPanel pNorth;
//    private JLabel lblTieuDe;
//    private JLabel lblMa, lblTen, lblLoai, lblGiaTri, lblTuNgay, lblDenNgay, lblTrangThai, lblDonHangTu, lblDoiTuong, lblMon1, lblMon2, lblMonTang, lblGhiChu;
//
//    private Connection con = ConnectSQL.getConnection();
//
//    public FrmSuaKhuyenMai(KhuyenMai km) {
//        setTitle("Sửa Khuyến Mãi");
//        setSize(770, 600);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        kmDAO = new KhuyenMai_DAO();
//
//        // Panel tiêu đề
//        pNorth = new JPanel();
//        pNorth.add(lblTieuDe = new JLabel("SỬA KHUYẾN MÃI"));
//        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 24));
//        lblTieuDe.setForeground(Color.WHITE);
//        pNorth.setBackground(new Color(169, 55, 68));
//        add(pNorth, BorderLayout.NORTH);
//
//        // Panel chính với BoxLayout
//        JPanel pMain = new JPanel();
//        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
//        pMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Font và kích thước
//        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
//        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 16);
//        Dimension fieldSize = new Dimension(200, 30);
//        Dimension labelSize = new Dimension(110, 30);
//
//        // Dòng 1: Mã KM
//        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblMa = new JLabel("Mã KM:");
//        lblMa.setFont(labelFont);
//        lblMa.setPreferredSize(labelSize);
//        p1.add(lblMa);
//        txtMaKM = new JTextField(18);
//        txtMaKM.setFont(fieldFont);
//        txtMaKM.setPreferredSize(fieldSize);
//        txtMaKM.setEnabled(false);
//        txtMaKM.setText(km.getMaKM());
//        p1.add(txtMaKM);
//        pMain.add(p1);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 2: Tên KM
//        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblTen = new JLabel("Tên KM:");
//        lblTen.setFont(labelFont);
//        lblTen.setPreferredSize(labelSize);
//        p2.add(lblTen);
//        txtTenKM = new JTextField(18);
//        txtTenKM.setFont(fieldFont);
//        txtTenKM.setPreferredSize(fieldSize);
//        txtTenKM.setText(km.getTenKM());
//        p2.add(txtTenKM);
//        pMain.add(p2);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 3: Loại KM + Giá trị
//        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblLoai = new JLabel("Loại KM:");
//        lblLoai.setFont(labelFont);
//        lblLoai.setPreferredSize(labelSize);
//        p3.add(lblLoai);
//        cmbLoaiKM = new JComboBox<>();
//        loadLoaiKM();
//        cmbLoaiKM.setFont(fieldFont);
//        cmbLoaiKM.setPreferredSize(fieldSize);
//        p3.add(cmbLoaiKM);
//        p3.add(Box.createHorizontalStrut(40));
//        lblGiaTri = new JLabel("Giá trị:");
//        lblGiaTri.setFont(labelFont);
//        lblGiaTri.setPreferredSize(labelSize);
//        p3.add(lblGiaTri);
//        txtGiaTri = new JTextField(18);
//        txtGiaTri.setFont(fieldFont);
//        txtGiaTri.setPreferredSize(fieldSize);
//        txtGiaTri.setText(String.valueOf(km.getGiaTri()));
//        p3.add(txtGiaTri);
//        pMain.add(p3);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 4: Từ ngày + Đến ngày
//        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblTuNgay = new JLabel("Từ ngày:");
//        lblTuNgay.setFont(labelFont);
//        lblTuNgay.setPreferredSize(labelSize);
//        p4.add(lblTuNgay);
//        spTuNgay = new JSpinner(new SpinnerDateModel());
//        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay, "yyyy-MM-dd"));
//        spTuNgay.setFont(fieldFont);
//        spTuNgay.setPreferredSize(fieldSize);
//        spTuNgay.setValue(km.getNgayBatDau());
//        p4.add(spTuNgay);
//        p4.add(Box.createHorizontalStrut(40));
//        lblDenNgay = new JLabel("Đến ngày:");
//        lblDenNgay.setFont(labelFont);
//        lblDenNgay.setPreferredSize(labelSize);
//        p4.add(lblDenNgay);
//        spDenNgay = new JSpinner(new SpinnerDateModel());
//        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay, "yyyy-MM-dd"));
//        spDenNgay.setFont(fieldFont);
//        spDenNgay.setPreferredSize(fieldSize);
//        spDenNgay.setValue(km.getNgayKetThuc());
//        p4.add(spDenNgay);
//        pMain.add(p4);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 5: Trạng thái + Đối tượng
//        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblTrangThai = new JLabel("Trạng thái:");
//        lblTrangThai.setFont(labelFont);
//        lblTrangThai.setPreferredSize(labelSize);
//        p5.add(lblTrangThai);
//        txtTrangThai = new JTextField(18);
//        txtTrangThai.setFont(fieldFont);
//        txtTrangThai.setPreferredSize(fieldSize);
//        txtTrangThai.setText(km.getTrangThai());
//        p5.add(txtTrangThai);
//        p5.add(Box.createHorizontalStrut(40));
//        lblDoiTuong = new JLabel("Đối tượng:");
//        lblDoiTuong.setFont(labelFont);
//        lblDoiTuong.setPreferredSize(labelSize);
//        p5.add(lblDoiTuong);
//        cmbDoiTuongApDung = new JComboBox<>(new String[]{"Tất cả", "Khách thường", "Thành viên"});
//        cmbDoiTuongApDung.setFont(fieldFont);
//        cmbDoiTuongApDung.setPreferredSize(fieldSize);
//        cmbDoiTuongApDung.setSelectedItem(km.getDoiTuongApDung() != null ? km.getDoiTuongApDung() : "Tất cả");
//        p5.add(cmbDoiTuongApDung);
//        pMain.add(p5);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 6: Đơn hàng từ
//        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblDonHangTu = new JLabel("Đơn hàng từ:");
//        lblDonHangTu.setFont(labelFont);
//        lblDonHangTu.setPreferredSize(labelSize);
//        p6.add(lblDonHangTu);
//        txtDonHangTu = new JTextField(18);
//        txtDonHangTu.setFont(fieldFont);
//        txtDonHangTu.setPreferredSize(fieldSize);
//        txtDonHangTu.setText(String.valueOf(km.getDonHangTu()));
//        p6.add(txtDonHangTu);
//        pMain.add(p6);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 7: Món 1 + Món 2
//        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblMon1 = new JLabel("Món 1:");
//        lblMon1.setFont(labelFont);
//        lblMon1.setPreferredSize(labelSize);
//        p7.add(lblMon1);
//        txtMon1 = new JTextField(18);
//        txtMon1.setFont(fieldFont);
//        txtMon1.setPreferredSize(fieldSize);
//        txtMon1.setText(km.getMon1() != null ? km.getMon1() : "");
//        p7.add(txtMon1);
//        p7.add(Box.createHorizontalStrut(40));
//        lblMon2 = new JLabel("Món 2:");
//        lblMon2.setFont(labelFont);
//        lblMon2.setPreferredSize(labelSize);
//        p7.add(lblMon2);
//        txtMon2 = new JTextField(18);
//        txtMon2.setFont(fieldFont);
//        txtMon2.setPreferredSize(fieldSize);
//        txtMon2.setText(km.getMon2() != null ? km.getMon2() : "");
//        p7.add(txtMon2);
//        pMain.add(p7);
//        pMain.add(Box.createVerticalStrut(10));
//
//        // Dòng 8: Món tặng + Ghi chú
//        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        lblMonTang = new JLabel("Món tặng:");
//        lblMonTang.setFont(labelFont);
//        lblMonTang.setPreferredSize(labelSize);
//        p8.add(lblMonTang);
//        txtMonTang = new JTextField(18);
//        txtMonTang.setFont(fieldFont);
//        txtMonTang.setPreferredSize(fieldSize);
//        txtMonTang.setText(km.getMonTang() != null ? km.getMonTang() : "");
//        p8.add(txtMonTang);
//        p8.add(Box.createHorizontalStrut(40));
//        lblGhiChu = new JLabel("Ghi chú:");
//        lblGhiChu.setFont(labelFont);
//        lblGhiChu.setPreferredSize(labelSize);
//        p8.add(lblGhiChu);
//        txtGhiChu = new JTextField(18);
//        txtGhiChu.setFont(fieldFont);
//        txtGhiChu.setPreferredSize(fieldSize);
//        txtGhiChu.setText(km.getGhiChu() != null ? km.getGhiChu() : "");
//        p8.add(txtGhiChu);
//        pMain.add(p8);
//        pMain.add(Box.createVerticalStrut(20));
//
//        // Dòng 9: Nút
//        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);
//        Dimension buttonSize = new Dimension(100, 35);
//        btnLuu = taoNut("Lưu", new Color(46, 204, 113), buttonSize, buttonFont);
//        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
//        p9.add(btnHuy);
//        p9.add(Box.createHorizontalStrut(20));
//        p9.add(btnLuu);
//        pMain.add(p9);
//
//        add(pMain, BorderLayout.CENTER);
//
//        pMain.setBackground(Color.WHITE);
//        p1.setBackground(Color.WHITE);
//        p2.setBackground(Color.WHITE);
//        p3.setBackground(Color.WHITE);
//        p4.setBackground(Color.WHITE);
//        p5.setBackground(Color.WHITE);
//        p6.setBackground(Color.WHITE);
//        p7.setBackground(Color.WHITE);
//        p8.setBackground(Color.WHITE);
//        p9.setBackground(Color.WHITE);
//        // Tải dữ liệu ban đầu
//        LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
//        String tenLoai = loaiDAO.getMaLoaiByTenLoai(km.getMaLoai());
//        cmbLoaiKM.setSelectedItem(tenLoai != null ? tenLoai : "");
//
//        // Thêm ItemListener cho cmbLoaiKM để xử lý ràng buộc
//        cmbLoaiKM.addItemListener(e -> {
//            if (e.getStateChange() == ItemEvent.SELECTED) {
//                capNhatTrangThaiField();
//            }
//        });
//
//        // Cập nhật trạng thái ban đầu
//        capNhatTrangThaiField();
//
//        // Sự kiện nút
//        btnHuy.addActionListener(e -> dispose());
//        btnLuu.addActionListener(e -> luuKhuyenMai());
//
//        // Tải danh sách loại khuyến mãi
//        loadLoaiKM();
//
//        setVisible(true);
//    }
//
//    private void capNhatTrangThaiField() {
//        String loai = cmbLoaiKM.getSelectedItem() != null ? cmbLoaiKM.getSelectedItem().toString() : "";
//        if (loai.equals("Tặng món")) {
//            txtGiaTri.setEnabled(false);
//            txtDonHangTu.setEnabled(false);
//            txtMon1.setEnabled(true);
//            txtMon2.setEnabled(true);
//            txtMonTang.setEnabled(true);
//            txtGiaTri.setText("");
//            txtDonHangTu.setText("");
//        } else {
//            txtGiaTri.setEnabled(true);
//            txtDonHangTu.setEnabled(true);
//            txtMon1.setEnabled(false);
//            txtMon2.setEnabled(false);
//            txtMonTang.setEnabled(false);
//            txtMon1.setText("");
//            txtMon2.setText("");
//            txtMonTang.setText("");
//        }
//    }
//
//    private JButton taoNut(String text, Color baseColor, Dimension size, Font font) {
//        JButton btn = new JButton(text);
//        btn.setFont(font);
//        btn.setPreferredSize(size);
//        btn.setForeground(Color.WHITE);
//        btn.setBackground(baseColor);
//        btn.setFocusPainted(false);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setContentAreaFilled(false);
//        btn.setOpaque(true);
//
//        btn.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                btn.setBackground(baseColor.darker());
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                btn.setBackground(baseColor);
//            }
//        });
//
//        return btn;
//    }
//
//    private void loadLoaiKM() {
//        try {
//            cmbLoaiKM.removeAllItems();
//            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
//            for (LoaiKhuyenMai loai : loaiDAO.getAllLoaiKhuyenMai()) {
//                cmbLoaiKM.addItem(loai.getTenLoai());
//            }
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Lỗi tải loại KM: " + ex.getMessage());
//        }
//    }
//
//    private void luuKhuyenMai() {
//        try {
//            Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
//            Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());
//            if (kt.before(bd)) {
//                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
//                return;
//            }
//
//            // Lấy maLoai từ tenLoai
//            String tenLoai = cmbLoaiKM.getSelectedItem().toString();
//            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
//            String maLoai = loaiDAO.getMaLoaiByTenLoai(tenLoai);
//
//            if (maLoai == null) {
//                JOptionPane.showMessageDialog(this, "Loại khuyến mãi không hợp lệ!");
//                return;
//            }
//
//            double giaTri = 0;
//            double donHangTu = 0;
//            String mon1 = txtMon1.getText().isEmpty() ? null : txtMon1.getText();
//            String mon2 = txtMon2.getText().isEmpty() ? null : txtMon2.getText();
//            String monTang = txtMonTang.getText().isEmpty() ? null : txtMonTang.getText();
//
//            if (!tenLoai.equals("Tặng món")) {
//                try {
//                    giaTri = Double.parseDouble(txtGiaTri.getText());
//                    donHangTu = txtDonHangTu.getText().isEmpty() ? 0 : Double.parseDouble(txtDonHangTu.getText());
//                    mon1 = null;
//                    mon2 = null;
//                    monTang = null;
//                } catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(this, "Giá trị và đơn hàng từ phải là số!");
//                    return;
//                }
//            } else {
//                if (monTang == null || monTang.trim().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Món tặng không được để trống khi loại là Tặng món!");
//                    return;
//                }
//            }
//
//            KhuyenMai km = new KhuyenMai(
//                    txtMaKM.getText(),
//                    txtTenKM.getText(),
//                    maLoai,
//                    giaTri,
//                    bd,
//                    kt,
//                    txtTrangThai.getText(),
//                    cmbDoiTuongApDung.getSelectedItem().toString(),
//                    donHangTu,
//                    mon1,
//                    mon2,
//                    monTang,
//                    txtGhiChu.getText().isEmpty() ? null : txtGhiChu.getText()
//            );
//
//            if (kmDAO.suaKhuyenMai(km)) {
//                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
//                dispose();
//            } else {
//                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
//            }
////        } catch (SQLException e) {
////            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage());
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
//        }
//    }
//}

package dialog;

import dao.KhuyenMai_DAO;
import dao.LoaiKhuyenMai_DAO;
import dao.MonAn_DAO;
import entity.KhuyenMai;
import entity.LoaiKhuyenMai;
import entity.MonAn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectSQL.ConnectSQL;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;


public class FrmSuaKhuyenMai extends JFrame {
    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu, txtTrangThai;
    private JComboBox<String> cmbLoaiKM, cmbDoiTuongApDung;
    private JSpinner spTuNgay, spDenNgay;
    private JButton btnLuu, btnHuy, btnLamMoi;
    private KhuyenMai_DAO kmDAO;
    private JPanel pNorth, pMain, p1, p2, p3, p4, p5, p6, p7, p8, p9;
    private JLabel lblTieuDe;
    private JLabel lblMa, lblTen, lblLoai, lblGiaTri, lblTuNgay, lblDenNgay, lblTrangThai, lblDonHangTu, lblDoiTuong, lblMon1, lblMon2, lblMonTang, lblGhiChu;

    private Connection con = ConnectSQL.getConnection();
	private DefaultTableModel modelDieuKien;
	private JTable tblDieuKien;
	private JButton btnThemDieuKien;
	private DefaultTableModel modelMonTang;
	private JTable tblMonTang;
	private JButton btnThemMonTang;
	private JPanel pTangMon;
	private JLabel lblGiamToiDa;
	private JTextField txtGiamToiDa;
	private MonAn_DAO monDAO;
	private JSplitPane splitTangMon;
	private JPanel panelRight;
	private JButton btnXoaDieuKien;
	private JButton btnXoaMonTang;
	private KhuyenMai khuyenMai;
	private boolean chiTiet = false;


	public FrmSuaKhuyenMai(KhuyenMai km) {
	    this.khuyenMai = km;
	    initUI(); // toàn bộ code giao diện giống y chang
	    loadDuLieu();
	}

	public FrmSuaKhuyenMai(KhuyenMai km, boolean chiTiet) {
	    this.khuyenMai = km;
	    this.chiTiet = chiTiet;
	    initUI();
	    loadDuLieu();
	    if (chiTiet) {
	        chuyenSangChiTiet();
	    }
	}
	
    public void  initUI() {
        setTitle("Sửa Khuyến Mãi");
        setSize(770, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        kmDAO = new KhuyenMai_DAO();
        monDAO = new MonAn_DAO(con);

        // Panel tiêu đề
        pNorth = new JPanel();
        pNorth.add(lblTieuDe = new JLabel("SỬA KHUYẾN MÃI"));
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTieuDe.setForeground(Color.WHITE);
        add(pNorth, BorderLayout.NORTH);

        // Panel chính với BoxLayout
        pMain = new JPanel();
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Font và kích thước
        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(200, 30);
        Dimension labelSize = new Dimension(110, 30);

        // Dòng 1: Mã KM
        p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblMa = new JLabel("Mã KM:");
        lblMa.setFont(labelFont);
        lblMa.setPreferredSize(labelSize);
        p1.add(lblMa);
        txtMaKM = new JTextField(18);
        txtMaKM.setFont(fieldFont);
        txtMaKM.setPreferredSize(fieldSize);
        txtMaKM.setEnabled(false);
        p1.add(txtMaKM);
        pMain.add(p1);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 2: Tên KM
        p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTen = new JLabel("Tên KM:");
        lblTen.setFont(labelFont);
        lblTen.setPreferredSize(labelSize);
        p2.add(lblTen);
        txtTenKM = new JTextField(18);
        txtTenKM.setFont(fieldFont);
        txtTenKM.setPreferredSize(fieldSize);
        p2.add(txtTenKM);
        p2.add(Box.createHorizontalStrut(40));

        lblGiamToiDa = new JLabel("Giảm tối đa:");
        lblGiamToiDa.setFont(labelFont);
        lblGiamToiDa.setPreferredSize(labelSize);
        p2.add(lblGiamToiDa);
        txtGiamToiDa = new JTextField(18);
        txtGiamToiDa.setFont(fieldFont);
        txtGiamToiDa.setPreferredSize(fieldSize);
        p2.add(txtGiamToiDa);
        pMain.add(p2);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 3: Loại KM + Giá trị
        p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblLoai = new JLabel("Loại KM:");
        lblLoai.setFont(labelFont);
        lblLoai.setPreferredSize(labelSize);
        p3.add(lblLoai);
        cmbLoaiKM = new JComboBox<>();
        loadLoaiKM();
        cmbLoaiKM.setFont(fieldFont);
        cmbLoaiKM.setPreferredSize(fieldSize);
        p3.add(cmbLoaiKM);
        p3.add(Box.createHorizontalStrut(40));
        
        lblGiaTri = new JLabel("Giá trị:");
        lblGiaTri.setFont(labelFont);
        lblGiaTri.setPreferredSize(labelSize);
        p3.add(lblGiaTri);
        txtGiaTri = new JTextField(18);
        txtGiaTri.setFont(fieldFont);
        txtGiaTri.setPreferredSize(fieldSize);
        p3.add(txtGiaTri);
        pMain.add(p3);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 4: Từ ngày + Đến ngày
        p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(labelFont);
        lblTuNgay.setPreferredSize(labelSize);
        p4.add(lblTuNgay);
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay, "yyyy-MM-dd"));
        spTuNgay.setFont(fieldFont);
        spTuNgay.setPreferredSize(fieldSize);
        spTuNgay.setValue(new java.util.Date()); // Đặt ngày hiện tại
        p4.add(spTuNgay);
        p4.add(Box.createHorizontalStrut(40));
        
        lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setFont(labelFont);
        lblDenNgay.setPreferredSize(labelSize);
        p4.add(lblDenNgay);
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay, "yyyy-MM-dd"));
        spDenNgay.setFont(fieldFont);
        spDenNgay.setPreferredSize(fieldSize);
        spDenNgay.setValue(new java.util.Date()); // Đặt ngày hiện tại
        p4.add(spDenNgay);
        pMain.add(p4);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 5: Trạng thái + Đối tượng
        p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setPreferredSize(labelSize);
        p5.add(lblTrangThai);
        txtTrangThai = new JTextField(18);
        txtTrangThai.setFont(fieldFont);
        txtTrangThai.setPreferredSize(fieldSize);
        p5.add(txtTrangThai);
        p5.add(Box.createHorizontalStrut(40));
        
        lblDoiTuong = new JLabel("Đối tượng:");
        lblDoiTuong.setFont(labelFont);
        lblDoiTuong.setPreferredSize(labelSize);
        p5.add(lblDoiTuong);
        cmbDoiTuongApDung = new JComboBox<>(new String[]{"Tất cả", "Khách thường", "Thành viên"});
        cmbDoiTuongApDung.setFont(fieldFont);
        cmbDoiTuongApDung.setPreferredSize(fieldSize);
        p5.add(cmbDoiTuongApDung);
        pMain.add(p5);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 6: Đơn hàng từ
        p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblDonHangTu = new JLabel("Đơn hàng từ:");
        lblDonHangTu.setFont(labelFont);
        lblDonHangTu.setPreferredSize(labelSize);
        p6.add(lblDonHangTu);
        txtDonHangTu = new JTextField(18);
        txtDonHangTu.setFont(fieldFont);
        txtDonHangTu.setPreferredSize(fieldSize);
        p6.add(txtDonHangTu);
        p6.add(Box.createHorizontalStrut(40));

        lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(labelFont);
        lblGhiChu.setPreferredSize(labelSize);
        p6.add(lblGhiChu);
        txtGhiChu = new JTextField(18);
        txtGhiChu.setFont(fieldFont);
        txtGhiChu.setPreferredSize(fieldSize);
        p6.add(txtGhiChu);
        pMain.add(p6);
        pMain.add(Box.createVerticalStrut(10));
        
        pTangMon = taoPanelTangMon();
        pMain.add(pTangMon);
        pTangMon.setVisible(false); // Ẩn mặc định


        // Dòng 9: Nút
        p9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(100, 35);
        btnLuu = taoNut("Lưu", new Color(46, 204, 113), buttonSize, buttonFont);
        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
        btnLamMoi = taoNut("Làm mới", new Color(52, 152, 219), buttonSize, buttonFont);
        p9.add(btnHuy);
        p9.add(Box.createHorizontalStrut(20));
        p9.add(btnLamMoi);
        p9.add(Box.createHorizontalStrut(20));
        p9.add(btnLuu);
        pMain.add(p9);

        // Thiết lập màu sắc
        pMain.setBackground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.WHITE);
        p3.setBackground(Color.WHITE);
        p4.setBackground(Color.WHITE);
        p5.setBackground(Color.WHITE);
        p6.setBackground(Color.WHITE);
        p9.setBackground(Color.WHITE);

        JPanel panelLeft = new JPanel(new BorderLayout());
        panelLeft.add(pMain, BorderLayout.CENTER);

        panelRight = new JPanel(new BorderLayout());
        panelRight.setPreferredSize(new Dimension(330, 0));
        panelRight.setBackground(Color.WHITE);
        pTangMon = taoPanelTangMon();
        panelRight.add(pTangMon, BorderLayout.CENTER);
        panelRight.setVisible(false); // ẨN BAN ĐẦU
        
        add(panelLeft, BorderLayout.CENTER);
        add(panelRight, BorderLayout.EAST);

        //Thêm ItemListener cho cmbLoaiKM để xử lý ràng buộc
        cmbLoaiKM.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                capNhatTrangThaiField();
            }
        });

        capNhatTrangThaiField();
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> luuKhuyenMai());
        btnLamMoi.addActionListener(e -> lamMoi());
        loadLoaiKM();
        setVisible(true);
    }
    
    private void loadDuLieu() {
        txtMaKM.setText(khuyenMai.getMaKM());
        txtMaKM.setEnabled(false);

        txtTenKM.setText(khuyenMai.getTenKM());
        txtGiaTri.setText(String.valueOf(khuyenMai.getGiaTri()));
        txtDonHangTu.setText(String.valueOf(khuyenMai.getDonHangTu()));
        txtGiamToiDa.setText(String.valueOf(khuyenMai.getGiamToiDa()));

        spTuNgay.setValue(khuyenMai.getNgayBatDau());
        spDenNgay.setValue(khuyenMai.getNgayKetThuc());

        txtTrangThai.setText(khuyenMai.getTrangThai());
        txtGhiChu.setText(khuyenMai.getGhiChu());
        cmbDoiTuongApDung.setSelectedItem(khuyenMai.getDoiTuongApDung());

        // set loại KM theo tên
        LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
        String tenLoai = loaiDAO.getTenLoaiByMaLoai(khuyenMai.getMaLoai());
        cmbLoaiKM.setSelectedItem(tenLoai);

        // nếu là TẶNG MÓN
        if ("Tặng món".equals(tenLoai)) {
            panelRight.setVisible(true);
            setSize(1100, 600);
            modelDieuKien.setRowCount(0);
            modelMonTang.setRowCount(0);
            
            for (Object[] row : kmDAO.layMonTheoKhuyenMai(khuyenMai.getMaKM(), "Dieu_kien")) {
                modelDieuKien.addRow(row);
            }
            for (Object[] row : kmDAO.layMonTheoKhuyenMai(khuyenMai.getMaKM(), "Tang")) {
                modelMonTang.addRow(row);
            }
        }
    }
   
    private void xoaDongDangChon(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa món này?",
        		"Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
        }
    }

    private void capNhatTrangThaiField() {
        String loai = cmbLoaiKM.getSelectedItem().toString();
        boolean laTangMon = loai.equals("Tặng món");
        txtGiaTri.setEnabled(!laTangMon);
        txtDonHangTu.setEnabled(!laTangMon);
        txtGiamToiDa.setEnabled(!laTangMon);
        
        panelRight.setVisible(laTangMon);
        if (laTangMon) {
            setSize(1100, 600);
        } else {
            setSize(770, 600);
        }
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

    private void loadLoaiKM() {
        try {
            cmbLoaiKM.removeAllItems();
            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
            for (LoaiKhuyenMai loai : loaiDAO.getAllLoaiKhuyenMai()) {
                cmbLoaiKM.addItem(loai.getTenLoai());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải loại KM: " + ex.getMessage());
        }
    }

    private void luuKhuyenMai() {
        try {
            Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
            Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());

            if (kt.before(bd)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            String tenKM = txtTenKM.getText().trim();
            if (tenKM.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên khuyến mãi không được để trống!");
                return;
            }
            String tenLoai = cmbLoaiKM.getSelectedItem().toString();
            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
            String maLoai = loaiDAO.getMaLoaiByTenLoai(tenLoai);

            if (maLoai == null) {
                JOptionPane.showMessageDialog(this, "Loại khuyến mãi không hợp lệ!");
                return;
            }
            
            boolean laTangMon = tenLoai.equals("Tặng món");
            boolean laGiamPhanTram = tenLoai.equals("Phần trăm");
            
            double giaTri = 0;
            double donHangTu = 0;
            double giamToiDa = 0;

            if (!laTangMon) {
                try {
                    if (laGiamPhanTram) {
                        if (txtGiamToiDa.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Giảm tối đa không được để trống!");
                            return;
                        }
                        giamToiDa = Double.parseDouble(txtGiamToiDa.getText().trim());
                    }
                    giaTri = Double.parseDouble(txtGiaTri.getText().trim());
                    donHangTu = Double.parseDouble(txtDonHangTu.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá trị không hợp lệ!");
                    return;
                }
            } else {
                // TẶNG MÓN
                if (modelDieuKien.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Phải có ít nhất 1 món điều kiện!");
                    return;
                }
                if (modelMonTang.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Phải có ít nhất 1 món tặng!");
                    return;
                }
            }
            //CẬP NHẬT KHUYẾN MÃI
            KhuyenMai km = new KhuyenMai(
                txtMaKM.getText(),
                tenKM,
                maLoai,
                giaTri,
                donHangTu,
                giamToiDa,
                bd,
                kt,
                null, // trạng thái cho DAO tự xử lý
                cmbDoiTuongApDung.getSelectedItem().toString(),
                txtGhiChu.getText().trim().isEmpty() ? null : txtGhiChu.getText().trim()
            );
            if (!kmDAO.suaKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thất bại!");
                return;
            }
            //XÓA + LƯU LẠI MÓN
            if (laTangMon) {
                kmDAO.xoaKhuyenMaiMonTheoMaKM(km.getMaKM());
                // MÓN ĐIỀU KIỆN
                for (int i = 0; i < modelDieuKien.getRowCount(); i++) {
                    kmDAO.themKhuyenMaiMon(
                        km.getMaKM(),
                        modelDieuKien.getValueAt(i, 0).toString(),
                        "Dieu_kien",
                        Integer.parseInt(modelDieuKien.getValueAt(i, 2).toString())
                    );
                }
                // MÓN TẶNG
                for (int i = 0; i < modelMonTang.getRowCount(); i++) {
                    kmDAO.themKhuyenMaiMon(
                        km.getMaKM(),
                        modelMonTang.getValueAt(i, 0).toString(),
                        "Tang",
                        Integer.parseInt(modelMonTang.getValueAt(i, 2).toString())
                    );
                }
            }
            JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void lamMoi() {
    	txtMaKM.setText(khuyenMai.getMaKM());
    	txtMaKM.setEnabled(false);
        txtTenKM.setText("");
        txtGiaTri.setText("");
        txtDonHangTu.setText("");
        txtMon1.setText("");
        txtMon2.setText("");
        txtMonTang.setText("");
        txtGhiChu.setText("");
        txtTrangThai.setText("");
        // Đặt lại giá trị mặc định cho JComboBox và JSpinner
        if (cmbLoaiKM.getItemCount() > 0) {
            cmbLoaiKM.setSelectedIndex(0);
        }
        cmbDoiTuongApDung.setSelectedItem("Tất cả");
        spTuNgay.setValue(new java.util.Date());
        spDenNgay.setValue(new java.util.Date());
        capNhatTrangThaiField();
    }
        
    //GD chọn món    
    private void moDialogChonMon(DefaultTableModel modelThemVao) {
        JDialog dlg = new JDialog(this, "Chọn món", true);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        //Center
        DefaultTableModel modelMon = new DefaultTableModel(new String[]{"Mã món", "Tên món", "Đơn giá"}, 0);
        JTable tblMon = new JTable(modelMon);
        tblMon.setRowHeight(30);
        tblMon.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        // Header
        JTableHeader header = tblMon.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 15));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setOpaque(true);

        //LOAD MÓN TỪ DATABASE
        List<MonAn> dsMon = monDAO.getAllMonAn();
        dsMon.sort(Comparator.comparingInt(m -> Integer.parseInt(m.getMaMon().substring(3))));
        for (MonAn mon : dsMon) {
            modelMon.addRow(new Object[]{
                mon.getMaMon(),
                mon.getTenMon(),
                mon.getDonGia()
            });
        }
        dlg.add(new JScrollPane(tblMon), BorderLayout.CENTER);

        //South
        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setFont(new Font("Times New Roman", Font.BOLD, 14));
        pSouth.add(lblSoLuong);

        JSpinner spSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spSoLuong.setPreferredSize(new Dimension(60, 30));
        spSoLuong.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        pSouth.add(spSoLuong);

        JButton btnChon = new JButton("Chọn");
        btnChon.setBackground(new Color(46, 204, 113));
        btnChon.setForeground(Color.WHITE);
        btnChon.setFocusPainted(false);
        btnChon.setBorderPainted(false);
        btnChon.setOpaque(true);
        btnChon.setFont(new Font("Times New Roman", Font.BOLD, 14));

        pSouth.add(btnChon);
        dlg.add(pSouth, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        btnChon.addActionListener(e -> {
            int row = tblMon.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng chọn 1 món!");
                return;
            }
            String maMon = modelMon.getValueAt(row, 0).toString();
            String tenMon = modelMon.getValueAt(row, 1).toString();
            int soLuong = (int) spSoLuong.getValue();
            modelThemVao.addRow(new Object[]{maMon, tenMon, soLuong});
            dlg.dispose();
        });
        dlg.setVisible(true);
    }

    //GD khi chọn loại tặng món
    private JPanel taoPanelTangMon() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        Font titleFont = new Font("Times New Roman", Font.BOLD, 16);
        Font tableFont = new Font("Times New Roman", Font.PLAIN, 14);
        Font headerFont = new Font("Times New Roman", Font.BOLD, 14);
        //MÓN ĐIỀU KIỆN
        JPanel pDieuKien = new JPanel(new BorderLayout(5, 5));
        pDieuKien.setBackground(Color.WHITE);
        JLabel lblDK = new JLabel("Món điều kiện");
        lblDK.setFont(titleFont);

        modelDieuKien = new DefaultTableModel(new String[]{"Mã món", "Tên món", "Số lượng"}, 0);
        tblDieuKien = new JTable(modelDieuKien);
        tblDieuKien.setFont(tableFont);
        tblDieuKien.getTableHeader().setFont(headerFont);
        tblDieuKien.setRowHeight(28);

        JScrollPane spDieuKien = new JScrollPane(tblDieuKien);
        JPanel pDKBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pDKBtn.setBackground(Color.WHITE);
        
        btnThemDieuKien = taoNut("Thêm món điều kiện", new Color(46, 204, 113), 
        		new Dimension(180, 28), new Font("Times New Roman", Font.BOLD, 14));
        btnXoaDieuKien = taoNut("Xóa món", new Color(231, 76, 60), 
        		new Dimension(140, 28), new Font("Times New Roman", Font.BOLD, 14));

        pDKBtn.add(btnXoaDieuKien);
        pDKBtn.add(btnThemDieuKien);
        pDieuKien.add(lblDK, BorderLayout.NORTH);
        pDieuKien.add(spDieuKien, BorderLayout.CENTER);
        pDieuKien.add(pDKBtn, BorderLayout.SOUTH);

        //MÓN TẶNG
        JPanel pMonTang = new JPanel(new BorderLayout(5, 5));
        pMonTang.setBackground(Color.WHITE);
        JLabel lblTang = new JLabel("Món tặng");
        lblTang.setFont(titleFont);

        modelMonTang = new DefaultTableModel(new String[]{"Mã món", "Tên món", "Số lượng"}, 0);
        tblMonTang = new JTable(modelMonTang);
        tblMonTang.setFont(tableFont);
        tblMonTang.getTableHeader().setFont(headerFont);
        tblMonTang.setRowHeight(28);

        JScrollPane spMonTang = new JScrollPane(tblMonTang);
        JPanel pTangBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pTangBtn.setBackground(Color.WHITE);
        
        btnThemMonTang = taoNut("Thêm món tặng", new Color(46, 204, 113), 
        		new Dimension(180, 28), new Font("Times New Roman", Font.BOLD, 14));
        btnXoaMonTang = taoNut("Xóa món", new Color(231, 76, 60), 
        		new Dimension(140, 28), new Font("Times New Roman", Font.BOLD, 14));
        
        pTangBtn.add(btnXoaMonTang);
        pTangBtn.add(btnThemMonTang);
        pMonTang.add(lblTang, BorderLayout.NORTH);
        pMonTang.add(spMonTang, BorderLayout.CENTER);
        pMonTang.add(pTangBtn, BorderLayout.SOUTH);

        splitTangMon = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pDieuKien, pMonTang);
        splitTangMon.setResizeWeight(0.5); // mỗi bảng 50%
        splitTangMon.setDividerSize(0);
        splitTangMon.setBorder(null);
        panel.add(splitTangMon, BorderLayout.CENTER);

        btnThemDieuKien.addActionListener(e -> moDialogChonMon(modelDieuKien));
        btnThemMonTang.addActionListener(e -> moDialogChonMon(modelMonTang));
        btnXoaDieuKien.addActionListener(e -> xoaDongDangChon(tblDieuKien, modelDieuKien));
        btnXoaMonTang.addActionListener(e -> xoaDongDangChon(tblMonTang, modelMonTang));
        return panel;
    }
    
    private void chuyenSangChiTiet() {
        lblTieuDe.setText("CHI TIẾT KHUYẾN MÃI");
        textFieldToLabel(txtMaKM);
        textFieldToLabel(txtTenKM);
        textFieldToLabel(txtGiaTri);
        textFieldToLabel(txtDonHangTu);
        textFieldToLabel(txtGiamToiDa);
        textFieldToLabel(txtTrangThai);
        textFieldToLabel(txtGhiChu);

        // Disable chỉnh sửa bảng
        tblDieuKien.setDefaultEditor(Object.class, null);
        tblMonTang.setDefaultEditor(Object.class, null);
        anTatCaNutTruNutDong(this.getContentPane());
        btnHuy.setText("Đóng");
    }
    
    private void anTatCaNutTruNutDong(Container c) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                // Giữ nút Đóng (btnHuy)
                if (btn != btnHuy) {
                    btn.setVisible(false);
                }
            }
            if (comp instanceof Container) {
                anTatCaNutTruNutDong((Container) comp);
            }
        }
    }

    private void textFieldToLabel(JTextField txt) {
        txt.setEditable(false);
        txt.setEnabled(true);
        txt.setBorder(null);
        txt.setOpaque(false);
        txt.setBackground(null);
        txt.setFocusable(false);
        txt.setCaretColor(new Color(0,0,0,0));
    }
    
}

