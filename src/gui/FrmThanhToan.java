//package gui;
//
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableModel;
//
//import dao.HoaDon_DAO;
//
//import java.awt.*;
//import java.text.DecimalFormat;
//
//public class FrmThanhToan extends JFrame {
//    private DecimalFormat df = new DecimalFormat("#,##0 VND");
//	private JLabel lblTieuDe;
//	private JLabel lblKH;
//	private JTextField txtKhachHang;
//	private JLabel lblSDT;
//	private JTextField txtSDT;
//	private JLabel lblNgayLap;
//	private JTextField txtNgayLap;
//	private JLabel lblBan;
//	private JTextField txtBan;
//	private JLabel lblNV;
//	private JTextField txtNV;
//	private JTable table;
//	private JButton btnQuayLai;
//	private JButton btnHuy;
//	private JButton btnXacNhan;
//	private JPanel pTop;
//	private JPanel pMain;
//	private JPanel pBottom;
//	private JLabel lblTongCong;
//	private JTextField txtTongCong;
//	private JLabel lblTongHD;
//	private JTextField txtTongHD;
//	private JLabel lblKM;
//	private JTextField txtKM;
//	private JLabel lblCoc;
//	private JTextField txtCoc;
//	private JLabel lblVAT;
//	private JTextField txtVAT;
//	private JLabel lblTienThanhToan;
//	private JTextField txtTienThanhToan;
//	private JLabel lblTienNhan;
//	private JTextField txtTienNhan;
//	private JLabel lblThua;
//	private JTextField txtTienThua;
//
//    public FrmThanhToan() {
//        setTitle("Thanh toán");
//        setSize(800, 700);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // === Tiêu đề ===
//        JPanel pNorth = new JPanel();
//        pNorth.add(lblTieuDe = new JLabel("THANH TOÁN"));
//        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 26));
//        lblTieuDe.setForeground(Color.WHITE);
//        pNorth.setBackground(new Color(169, 55, 68));
//        add(pNorth, BorderLayout.NORTH);
//
//        // === Panel chính ===
//        JPanel pMain = new JPanel(new BorderLayout());
//        pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // ==== THÔNG TIN ====
//        JPanel pTop = new JPanel();
//        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
//
//        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
//        Font foTxt = new Font("Times New Roman", Font.PLAIN, 18);
//
//        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p1.add(lblKH = new JLabel("Khách hàng:"));
//        txtKhachHang = new JTextField(15);
//        p1.add(txtKhachHang);
//        p1.add(Box.createHorizontalStrut(25));
//        p1.add(lblSDT = new JLabel("SĐT khách:"));
//        txtSDT = new JTextField(15);
//        p1.add(txtSDT);
//        pTop.add(p1);
//
//        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p2.add(lblNgayLap = new JLabel("Ngày lập: "));
//        txtNgayLap = new JTextField(15);
//        p2.add(txtNgayLap);
//        p2.add(Box.createHorizontalStrut(35));
//        p2.add(lblBan = new JLabel("Bàn: "));
//        txtBan = new JTextField(15);
//        p2.add(txtBan);
//        pTop.add(p2);
//
//        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p3.add(lblNV = new JLabel("Nhân viên: "));
//        txtNV = new JTextField(15);
//        p3.add(txtNV);
//        pTop.add(p3);
//
//        // === Gán font ===
//        lblKH.setFont(foBoLoc);
//        lblSDT.setFont(foBoLoc);
//        lblNgayLap.setFont(foBoLoc);
//        lblBan.setFont(foBoLoc);
//        lblNV.setFont(foBoLoc);
//
//        txtKhachHang.setFont(foTxt);
//        txtSDT.setFont(foTxt);
//        txtNgayLap.setFont(foTxt);
//        txtBan.setFont(foTxt);
//        txtNV.setFont(foTxt);
//
//        // === Căn đều kích thước nhãn ===
//        lblNgayLap.setPreferredSize(lblKH.getPreferredSize());
//        lblNV.setPreferredSize(lblKH.getPreferredSize());
//        lblBan.setPreferredSize(lblSDT.getPreferredSize());
//
//        // ==== BẢNG MÓN ĂN ====
//        String[] colNames = {"STT", "Món", "Số lượng", "Đơn giá", "Thành tiền"};
//        DefaultTableModel model = new DefaultTableModel(colNames, 0);
//        table = new JTable(model);
//        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
//        table.setRowHeight(30);
//
//        JScrollPane scroll = new JScrollPane(table);
//
//        // ==== TỔNG HÓA ĐƠN ====
//        JPanel pBottom = new JPanel();
//        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
//        pBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
//
//        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p4.add(lblTongCong = new JLabel("Tổng cộng: "));
//        txtTongCong = new JTextField(15);
//        p4.add(txtTongCong);
//        p4.add(Box.createHorizontalStrut(30));
//        p4.add(lblTongHD = new JLabel("Tổng hóa đơn: "));
//        txtTongHD = new JTextField(15);
//        p4.add(txtTongHD);
//        pBottom.add(p4);
//
//        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p5.add(lblKM = new JLabel("Khuyến mãi: "));
//        txtKM = new JTextField(15);
//        p5.add(txtKM);
//        p5.add(Box.createHorizontalStrut(30));
//        p5.add(lblCoc = new JLabel("Đã cọc: "));
//        txtCoc = new JTextField(15);
//        p5.add(txtCoc);
//        pBottom.add(p5);
//        
//        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p6.add(lblVAT = new JLabel("Thuế VAT: "));
//        txtVAT = new JTextField(15);
//        txtVAT.setText("8%");
//        p6.add(txtVAT);
//        p6.add(Box.createHorizontalStrut(25));
//        p6.add(lblTienThanhToan = new JLabel("Tiền thanh toán: "));
//        txtTienThanhToan = new JTextField(15);
//        p6.add(txtTienThanhToan);
//        pBottom.add(p6);
//        
//        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p7.add(lblTienNhan = new JLabel("Tiền khách đưa:"));
//        txtTienNhan = new JTextField(15);
//        p7.add(txtTienNhan);
//        pBottom.add(p7);
//        
//        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p8.add(lblThua = new JLabel("Tiền thừa: "));
//        txtTienThua = new JTextField(15);
//        p8.add(txtTienThua);
//        pBottom.add(p8);
//
//        // Gán font cho phần tổng
//        lblTongCong.setFont(foBoLoc);
//        lblTongHD.setFont(foBoLoc);
//        lblKM.setFont(foBoLoc);
//        lblCoc.setFont(foBoLoc);
//        lblVAT.setFont(foBoLoc);
//        lblTienThanhToan.setFont(foBoLoc);
//        lblTienNhan.setFont(foBoLoc);
//        lblThua.setFont(foBoLoc);
//
//        txtTongCong.setFont(foTxt);
//        txtTongHD.setFont(foTxt);
//        txtKM.setFont(foTxt);
//        txtCoc.setFont(foTxt);
//        txtVAT.setFont(foTxt);
//        txtTienThanhToan.setFont(foTxt);
//        txtTienNhan.setFont(foTxt);
//        txtTienThua.setFont(foTxt);
//        
//        lblTongCong.setPreferredSize(lblTienNhan.getPreferredSize());
//        lblKM.setPreferredSize(lblTienNhan.getPreferredSize());
//        lblVAT.setPreferredSize(lblTienNhan.getPreferredSize());
//        lblThua.setPreferredSize(lblTienNhan.getPreferredSize());
//        
//        lblTongHD.setPreferredSize(lblTienThanhToan.getPreferredSize());
//        lblCoc.setPreferredSize(lblTienThanhToan.getPreferredSize());
//        
//        // === Ghép các phần vào pMain ===
//        pMain.add(pTop, BorderLayout.NORTH);
//        pMain.add(scroll, BorderLayout.CENTER);
//        pMain.add(pBottom, BorderLayout.SOUTH);
//
//        add(pMain, BorderLayout.CENTER);
//
//        // === Nút chức năng ===
//        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        btnQuayLai = new JButton("Quay lại");
//        btnHuy = new JButton("Hủy");
//        btnXacNhan = new JButton("Xác nhận thanh toán");
//
//        btnQuayLai.setForeground(Color.WHITE);
//        btnQuayLai.setBackground(new Color(169, 55, 68));
//
//        btnHuy.setForeground(Color.WHITE);
//        btnHuy.setBackground(new Color(210, 201, 74));
//
//        btnXacNhan.setForeground(Color.WHITE);
//        btnXacNhan.setBackground(new Color(102, 210, 74));
//
//        Font foBtn = new Font("Times New Roman", Font.BOLD, 18);
//        btnQuayLai.setFont(foBtn);
//        btnHuy.setFont(foBtn);
//        btnXacNhan.setFont(foBtn);
//
//        pSouth.add(btnQuayLai);
//        pSouth.add(Box.createHorizontalStrut(235));
//        pSouth.add(btnHuy);
//        pSouth.add(btnXacNhan);
//        add(pSouth, BorderLayout.SOUTH);
//    }
//    
//    public static void main(String[] args) {
//        new FrmThanhToan().setVisible(true);
//    }
//}
//
//package gui;
//
//import dao.HoaDon_DAO;
//import dao.KhuyenMai_DAO;
//import entity.KhuyenMai;
//import connectSQL.ConnectSQL;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class FrmThanhToan extends JFrame implements ActionListener {
//
//    private JLabel lblTieuDe;
//    private JLabel lblKH, lblSDT, lblNgayLap, lblBan, lblNV;
//    private JLabel valKH, valSDT, valNgayLap, valBan, valNV;
//    private JTable table;
//    private DefaultTableModel model;
//    private JLabel lblTongCong, lblTongHD, lblKM, lblCoc, lblVAT, lblTienThanhToan, lblTienNhan, lblThua;
//    private JLabel valTongCong, valTongHD, valCoc, valVAT, valTienThanhToan, valThua;
//    private JTextField txtTienNhan;
//    private JComboBox<KhuyenMai> cmbKhuyenMai;
//    private JButton btnQuayLai, btnHuy, btnXacNhan;
//    private DecimalFormat df = new DecimalFormat("#,##0 VND");
//    private HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
//    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
//    private double tongCong; // Tổng cộng trước VAT và khuyến mãi
//    private double giamGia; // Giá trị giảm từ khuyến mãi
//
//    public FrmThanhToan(String maHD) {
//        this();
//        loadDuLieuHoaDon(maHD);
//        loadChiTietHoaDon(maHD);
//    }
//
//    public FrmThanhToan() {
//        setTitle("Thanh toán");
//        setSize(900, 700);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // === Tiêu đề ===
//        JPanel pNorth = new JPanel();
//        pNorth.add(lblTieuDe = new JLabel("THANH TOÁN"));
//        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 26));
//        lblTieuDe.setForeground(Color.WHITE);
//        pNorth.setBackground(new Color(169, 55, 68));
//        add(pNorth, BorderLayout.NORTH);
//
//        // === Panel chính ===
//        JPanel pMain = new JPanel(new BorderLayout());
//        pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // === Hàng trên: Bảng món ăn ===
//        String[] colNames = {"STT", "Món", "Số lượng", "Đơn giá", "Thành tiền"};
//        model = new DefaultTableModel(colNames, 0);
//        table = new JTable(model);
//        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
//        table.setRowHeight(30);
//        JScrollPane scroll = new JScrollPane(table);
//        //scroll.setPreferredSize(new Dimension(0, 250)); // Tăng chiều cao bảng lên 450px
//        pMain.add(scroll, BorderLayout.CENTER);
//
//        // === Hàng dưới: 2 cột (Thông tin và Tổng hóa đơn) ===
//        JPanel pBottom = new JPanel(new GridLayout(1, 2, 10, 10));
//        //pBottom.setPreferredSize(new Dimension(0, 180)); // Giới hạn chiều cao pBottom
//        //pBottom.setMinimumSize(new Dimension(0, 180)); // Đảm bảo không bị thu nhỏ quá mức
//
//        // === Cột trái: Thông tin hóa đơn ===
//        JPanel pLeft = new JPanel();
//        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
//        pLeft.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
//
//        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
//        Font foTxt = new Font("Times New Roman", Font.PLAIN, 18);
//
//        // Khách hàng
//        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblKH = new JLabel("Khách hàng:");
//        lblKH.setFont(foBoLoc);
//        lblKH.setPreferredSize(new Dimension(110, 30));
//        valKH = new JLabel("");
//        valKH.setFont(foTxt);
//        p1.add(lblKH);
//        p1.add(valKH);
//        pLeft.add(p1);
//
//        // SĐT khách
//        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblSDT = new JLabel("SĐT khách:");
//        lblSDT.setFont(foBoLoc);
//        lblSDT.setPreferredSize(new Dimension(110, 30));
//        valSDT = new JLabel("");
//        valSDT.setFont(foTxt);
//        p2.add(lblSDT);
//        p2.add(valSDT);
//        pLeft.add(p2);
//
//        // Ngày lập
//        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblNgayLap = new JLabel("Ngày lập:");
//        lblNgayLap.setFont(foBoLoc);
//        lblNgayLap.setPreferredSize(new Dimension(110, 30));
//        valNgayLap = new JLabel("");
//        valNgayLap.setFont(foTxt);
//        p3.add(lblNgayLap);
//        p3.add(valNgayLap);
//        pLeft.add(p3);
//
//        // Bàn
//        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblBan = new JLabel("Bàn:");
//        lblBan.setFont(foBoLoc);
//        lblBan.setPreferredSize(new Dimension(110, 30));
//        valBan = new JLabel("");
//        valBan.setFont(foTxt);
//        p4.add(lblBan);
//        p4.add(valBan);
//        pLeft.add(p4);
//
//        // Nhân viên
//        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblNV = new JLabel("Nhân viên:");
//        lblNV.setFont(foBoLoc);
//        lblNV.setPreferredSize(new Dimension(110, 30));
//        valNV = new JLabel("");
//        valNV.setFont(foTxt);
//        p5.add(lblNV);
//        p5.add(valNV);
//        pLeft.add(p5);
//        pLeft.add(Box.createVerticalGlue()); // Giảm không gian dư
//
//        // === Cột phải: Tổng hóa đơn ===
//        JPanel pRight = new JPanel();
//        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
//        pRight.setBorder(BorderFactory.createTitledBorder("Tổng hóa đơn"));
//
//        // Tổng cộng
//        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblTongCong = new JLabel("Tổng cộng:");
//        lblTongCong.setFont(foBoLoc);
//        lblTongCong.setPreferredSize(new Dimension(150, 30));
//        valTongCong = new JLabel("");
//        valTongCong.setFont(foTxt);
//        p6.add(lblTongCong);
//        p6.add(valTongCong);
//        pRight.add(p6);
//
//        // Khuyến mãi
//        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblKM = new JLabel("Khuyến mãi:");
//        lblKM.setFont(foBoLoc);
//        lblKM.setPreferredSize(new Dimension(150, 30));
//        cmbKhuyenMai = new JComboBox<>();
//        cmbKhuyenMai.setFont(foTxt);
//        cmbKhuyenMai.setPreferredSize(new Dimension(200, 30));
//        cmbKhuyenMai.addActionListener(this);
//        p7.add(lblKM);
//        p7.add(cmbKhuyenMai);
//        pRight.add(p7);
//
//        // Đã cọc
//        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p8.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblCoc = new JLabel("Đã cọc:");
//        lblCoc.setFont(foBoLoc);
//        lblCoc.setPreferredSize(new Dimension(150, 30));
//        valCoc = new JLabel("");
//        valCoc.setFont(foTxt);
//        p8.add(lblCoc);
//        p8.add(valCoc);
//        pRight.add(p8);
//
//        // Thuế VAT
//        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p9.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblVAT = new JLabel("Thuế VAT:");
//        lblVAT.setFont(foBoLoc);
//        lblVAT.setPreferredSize(new Dimension(150, 30));
//        valVAT = new JLabel("8%");
//        valVAT.setFont(foTxt);
//        p9.add(lblVAT);
//        p9.add(valVAT);
//        pRight.add(p9);
//
//        // Tổng hóa đơn
//        JPanel p10 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p10.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblTongHD = new JLabel("Tổng hóa đơn:");
//        lblTongHD.setFont(foBoLoc);
//        lblTongHD.setPreferredSize(new Dimension(150, 30));
//        valTongHD = new JLabel("");
//        valTongHD.setFont(foTxt);
//        p10.add(lblTongHD);
//        p10.add(valTongHD);
//        pRight.add(p10);
//
//        // Tiền thanh toán
//        JPanel p11 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p11.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblTienThanhToan = new JLabel("Tiền thanh toán:");
//        lblTienThanhToan.setFont(foBoLoc);
//        lblTienThanhToan.setPreferredSize(new Dimension(150, 30));
//        valTienThanhToan = new JLabel("");
//        valTienThanhToan.setFont(foTxt);
//        p11.add(lblTienThanhToan);
//        p11.add(valTienThanhToan);
//        pRight.add(p11);
//
//        // Tiền khách đưa
//        JPanel p12 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p12.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblTienNhan = new JLabel("Tiền khách đưa:");
//        lblTienNhan.setFont(foBoLoc);
//        lblTienNhan.setPreferredSize(new Dimension(150, 30));
//        txtTienNhan = new JTextField(15);
//        txtTienNhan.setFont(foTxt);
//        txtTienNhan.setPreferredSize(new Dimension(200, 30));
//        txtTienNhan.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                tinhTienThua();
//            }
//        });
//        p12.add(lblTienNhan);
//        p12.add(txtTienNhan);
//        pRight.add(p12);
//        pRight.add(Box.createVerticalStrut(5));
//
//        // Tiền thừa
//        JPanel p13 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
//        p13.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
//        lblThua = new JLabel("Tiền thừa:");
//        lblThua.setFont(foBoLoc);
//        lblThua.setPreferredSize(new Dimension(150, 30));
//        valThua = new JLabel("");
//        valThua.setFont(foTxt);
//        p13.add(lblThua);
//        p13.add(valThua);
//        pRight.add(p13);
//        pRight.add(Box.createVerticalGlue()); // Giảm không gian dư
//
//        // Ghép vào pBottom
//        pBottom.add(pLeft);
//        pBottom.add(pRight);
//        pMain.add(pBottom, BorderLayout.SOUTH);
//
//        // Ghép vào frame
//        add(pMain, BorderLayout.CENTER);
//
//        // === Nút chức năng ===
//        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        btnQuayLai = new JButton("Quay lại");
//        btnHuy = new JButton("Hủy");
//        btnXacNhan = new JButton("Xác nhận thanh toán");
//
//        btnQuayLai.setForeground(Color.WHITE);
//        btnQuayLai.setBackground(new Color(169, 55, 68));
//        btnHuy.setForeground(Color.WHITE);
//        btnHuy.setBackground(new Color(210, 201, 74));
//        btnXacNhan.setForeground(Color.WHITE);
//        btnXacNhan.setBackground(new Color(102, 210, 74));
//
//        Font foBtn = new Font("Times New Roman", Font.BOLD, 18);
//        btnQuayLai.setFont(foBtn);
//        btnHuy.setFont(foBtn);
//        btnXacNhan.setFont(foBtn);
//
//        btnQuayLai.addActionListener(this);
//        btnHuy.addActionListener(this);
//        btnXacNhan.addActionListener(this);
//
//        pSouth.add(btnQuayLai);
//        pSouth.add(Box.createHorizontalStrut(235));
//        pSouth.add(btnHuy);
//        pSouth.add(btnXacNhan);
//        add(pSouth, BorderLayout.SOUTH);
//    }
//    
//    private double tinhGiamGia(KhuyenMai km) {
//        if (km == null) return 0;
//        if (km.getMaLoai().equals("Phần trăm")) {
//            return tongCong * (km.getGiaTri() / 100);
//        } else if (km.getMaLoai().equals("Giảm trực tiếp")) {
//            return km.getGiaTri();
//        } else if (km.getMaLoai().equals("Tặng món") && km.getMonTang() != null) {
//            String sql = "SELECT donGia FROM MonAn WHERE maMon = ?";
//            try (Connection con = ConnectSQL.getConnection();
//                 PreparedStatement ps = con.prepareStatement(sql)) {
//                ps.setString(1, km.getMonTang());
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        return rs.getDouble("donGia");
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return 0;
//    }
//
//    // === Tính tiền thừa ===
//    private void tinhTienThua() {
//        try {
//            double tienKhachDua = Double.parseDouble(txtTienNhan.getText().trim());
//            double tienSauGiam = tongCong + (tongCong * 0.08) - giamGia;
//            double tienThua = tienKhachDua - tienSauGiam;
//            valThua.setText(df.format(tienThua));
//            valTienThanhToan.setText(df.format(tienSauGiam));
//        } catch (NumberFormatException e) {
//            valThua.setText(df.format(0));
//        }
//    }
//
//    // === HÀM LOAD DỮ LIỆU HÓA ĐƠN ===
//    private void loadDuLieuHoaDon(String maHD) {
//        Object[] thongTin = hoaDonDAO.layThongTinHoaDon(maHD);
//        if (thongTin == null) {
//            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn " + maHD);
//            return;
//        }
//
//        valKH.setText((String) thongTin[0]);
//        valSDT.setText((String) thongTin[1]);
//        valBan.setText((String) thongTin[2]);
//        valNV.setText((String) thongTin[3]);
//        valNgayLap.setText(thongTin[4].toString());
//        valCoc.setText(df.format(0));
//
//        tongCong = (double) thongTin[6];
//        valTongCong.setText(df.format(tongCong));
//
//        double vat = tongCong * 0.08;
//        valVAT.setText("8%");
//        giamGia = 0;
//        double tongHD = tongCong + vat - giamGia;
//        valTongHD.setText(df.format(tongHD));
//        valTienThanhToan.setText(df.format(tongHD));
//        txtTienNhan.setText(String.format("%.0f", tongHD));
//        valThua.setText(df.format(0));
//    }
//
//    // === HÀM LOAD CHI TIẾT HÓA ĐƠN ===
//    private void loadChiTietHoaDon(String maHD) {
//        List<Object[]> dsCT = hoaDonDAO.layChiTietHoaDon(maHD);
//        model.setRowCount(0);
//        int stt = 1;
//        for (Object[] row : dsCT) {
//            model.addRow(new Object[]{
//                    stt++,
//                    row[2],
//                    row[0],
//                    df.format(row[3]),
//                    df.format(row[4])
//            });
//        }
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        Object source = e.getSource();
//        if (source == btnQuayLai || source == btnHuy) {
//            dispose();
//        } else if (source == btnXacNhan) {
//            try {
//                double tienKhachDua = Double.parseDouble(txtTienNhan.getText().trim());
//                double tienSauGiam = tongCong + (tongCong * 0.08) - giamGia;
//                if (tienKhachDua < tienSauGiam) {
//                    JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
//                String maKM = selectedKM != null ? selectedKM.getMaKM() : null;
//                // Cập nhật hóa đơn với mã khuyến mãi (nếu có)
//                // hoaDonDAO.capNhatThanhToan(maHD, maKM, tienKhachDua); // Cần thêm phương thức này trong HoaDon_DAO
//                JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                dispose();
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } else if (source == cmbKhuyenMai) {
//            KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
//            giamGia = tinhGiamGia(selectedKM);
//            double tongHD = tongCong + (tongCong * 0.08) - giamGia;
//            valTongHD.setText(df.format(tongHD));
//            valTienThanhToan.setText(df.format(tongHD));
//            txtTienNhan.setText(String.format("%.0f", tongHD));
//            tinhTienThua();
//        }
//    }
//
//    public static void main(String[] args) {
//        ConnectSQL.getInstance().connect();
//        new FrmThanhToan("HD0001").setVisible(true);
//    }
//}
package gui;

import dao.HoaDon_DAO;
import dao.KhuyenMai_DAO;
import dao.LoaiKhuyenMai_DAO;
import entity.KhuyenMai;
import connectSQL.ConnectSQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FrmThanhToan extends JFrame implements ActionListener {

    private JLabel lblTieuDe;
    private JLabel lblKH, lblSDT, lblNgayLap, lblBan, lblNV;
    private JLabel valKH, valSDT, valNgayLap, valBan, valNV;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongCong, lblTongHD, lblKM, lblCoc, lblVAT, lblTienThanhToan, lblTienNhan, lblThua, lblPhuThu;
    private JLabel valTongCong, valTongHD, valCoc, valVAT, valTienThanhToan, valThua;
    private JTextField txtTienNhan, txtPhuThu;
    private JCheckBox chkPhuThu;
    private JComboBox<KhuyenMai> cmbKhuyenMai;
    private JButton btnQuayLai, btnHuy, btnXacNhan;
    private DecimalFormat df = new DecimalFormat("#,##0 VND");
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
    private double tongCong; // Tổng cộng trước VAT và khuyến mãi
    private double giamGia; // Giá trị giảm từ khuyến mãi
    private double phuThu; // Số tiền phụ thu
	private JLabel lblDonViTien;
	private JLabel lblDonVi;

    public FrmThanhToan(String maHD) {
        this();
        loadDuLieuHoaDon(maHD);
        loadChiTietHoaDon(maHD);
    }

    public FrmThanhToan() {
        setTitle("Thanh toán");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Tiêu đề ===
        JPanel pNorth = new JPanel();
        pNorth.add(lblTieuDe = new JLabel("THANH TOÁN"));
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 26));
        lblTieuDe.setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        add(pNorth, BorderLayout.NORTH);

        // === Panel chính ===
        JPanel pMain = new JPanel(new BorderLayout());
        pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colNames = {"STT", "Món", "Số lượng", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(colNames, 0);
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
	    scroll.getViewport().setBackground(Color.WHITE);
        pMain.add(scroll, BorderLayout.CENTER);

        // === Hàng dưới: 2 cột (Thông tin và Tổng hóa đơn) ===
        JPanel pBottom = new JPanel(new GridLayout(1, 2, 10, 10));
        lblDonViTien = new JLabel("VND");
        lblDonVi = new JLabel("VND");

        // === Cột trái: Thông tin hóa đơn ===
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Thông tin hóa đơn", 0, 0, new Font("Times New Roman", Font.BOLD, 22)));

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
        Font foTxt = new Font("Times New Roman", Font.PLAIN, 18);
        lblDonViTien.setFont(foTxt);
        lblDonVi.setFont(foTxt);

        // Khách hàng
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblKH = new JLabel("Khách hàng:");
        lblKH.setFont(foBoLoc);
        lblKH.setPreferredSize(new Dimension(110, 30));
        valKH = new JLabel("");
        valKH.setFont(foTxt);
        p1.add(lblKH);
        p1.add(valKH);
        pLeft.add(p1);

        // SĐT khách
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblSDT = new JLabel("SĐT khách:");
        lblSDT.setFont(foBoLoc);
        lblSDT.setPreferredSize(new Dimension(110, 30));
        valSDT = new JLabel("");
        valSDT.setFont(foTxt);
        p2.add(lblSDT);
        p2.add(valSDT);
        pLeft.add(p2);

        // Ngày lập
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblNgayLap = new JLabel("Ngày lập:");
        lblNgayLap.setFont(foBoLoc);
        lblNgayLap.setPreferredSize(new Dimension(110, 30));
        valNgayLap = new JLabel("");
        valNgayLap.setFont(foTxt);
        p3.add(lblNgayLap);
        p3.add(valNgayLap);
        pLeft.add(p3);

        // Bàn
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblBan = new JLabel("Bàn:");
        lblBan.setFont(foBoLoc);
        lblBan.setPreferredSize(new Dimension(110, 30));
        valBan = new JLabel("");
        valBan.setFont(foTxt);
        p4.add(lblBan);
        p4.add(valBan);
        pLeft.add(p4);

        // Nhân viên
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblNV = new JLabel("Nhân viên:");
        lblNV.setFont(foBoLoc);
        lblNV.setPreferredSize(new Dimension(110, 30));
        valNV = new JLabel("");
        valNV.setFont(foTxt);
        p5.add(lblNV);
        p5.add(valNV);
        pLeft.add(p5);
        pLeft.add(Box.createVerticalGlue());

        // === Cột phải: Tổng hóa đơn ===
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Tổng hóa đơn", 0, 0, new Font("Times New Roman", Font.BOLD, 22)));

        // Tổng cộng
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTongCong = new JLabel("Tổng cộng:");
        lblTongCong.setFont(foBoLoc);
        lblTongCong.setPreferredSize(new Dimension(150, 30));
        valTongCong = new JLabel("");
        valTongCong.setFont(foTxt);
        p6.add(lblTongCong);
        p6.add(valTongCong);
        pRight.add(p6);

        // Khuyến mãi
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblKM = new JLabel("Khuyến mãi:");
        lblKM.setFont(foBoLoc);
        lblKM.setPreferredSize(new Dimension(150, 30));
        cmbKhuyenMai = new JComboBox<>();
        cmbKhuyenMai.setFont(foTxt);
        cmbKhuyenMai.setPreferredSize(new Dimension(215, 30));
        cmbKhuyenMai.addActionListener(this);
        p7.add(lblKM);
        p7.add(cmbKhuyenMai);
        pRight.add(p7);

        // Phụ thu
        JPanel pPhuThu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        pPhuThu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblPhuThu = new JLabel("Phụ thu:");
        lblPhuThu.setFont(foBoLoc);
        lblPhuThu.setPreferredSize(new Dimension(150, 30));
        chkPhuThu = new JCheckBox();
        chkPhuThu.setFont(foTxt);
        txtPhuThu = new JTextField(15);
        txtPhuThu.setFont(foTxt);
        txtPhuThu.setPreferredSize(new Dimension(200, 30));
        txtPhuThu.setEnabled(false); // Ban đầu vô hiệu hóa

        pPhuThu.add(lblPhuThu);
        pPhuThu.add(txtPhuThu);
        pPhuThu.add(lblDonVi);
        pPhuThu.add(chkPhuThu);
        pRight.add(pPhuThu);

        // Sự kiện cho checkbox Phụ thu
        chkPhuThu.addItemListener(e -> {
            txtPhuThu.setEnabled(chkPhuThu.isSelected());
            if (!chkPhuThu.isSelected()) {
                txtPhuThu.setText("");
                phuThu = 0;
            } else {
                try {
                    phuThu = txtPhuThu.getText().isEmpty() ? 0 : Double.parseDouble(txtPhuThu.getText().trim());
                } catch (NumberFormatException ex) {
                    phuThu = 0;
                }
            }
            tinhTienThua(); // Cập nhật tiền thừa khi thay đổi trạng thái phụ thu
        });

        // Sự kiện nhập liệu cho txtPhuThu
        txtPhuThu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    phuThu = chkPhuThu.isSelected() && !txtPhuThu.getText().isEmpty() ? Double.parseDouble(txtPhuThu.getText().trim()) : 0;
                } catch (NumberFormatException ex) {
                    phuThu = 0;
                }
                tinhTienThua(); // Cập nhật tiền thừa khi nhập phụ thu
            }
        });

        // Đã cọc
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p8.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblCoc = new JLabel("Đã cọc:");
        lblCoc.setFont(foBoLoc);
        lblCoc.setPreferredSize(new Dimension(150, 30));
        valCoc = new JLabel("");
        valCoc.setFont(foTxt);
        p8.add(lblCoc);
        p8.add(valCoc);
        pRight.add(p8);

        // Thuế VAT
        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p9.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblVAT = new JLabel("Thuế VAT:");
        lblVAT.setFont(foBoLoc);
        lblVAT.setPreferredSize(new Dimension(150, 30));
        valVAT = new JLabel("8%");
        valVAT.setFont(foTxt);
        p9.add(lblVAT);
        p9.add(valVAT);
        pRight.add(p9);

        // Tổng hóa đơn
        JPanel p10 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p10.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTongHD = new JLabel("Tổng hóa đơn:");
        lblTongHD.setFont(foBoLoc);
        lblTongHD.setPreferredSize(new Dimension(150, 30));
        valTongHD = new JLabel("");
        valTongHD.setFont(foTxt);
        p10.add(lblTongHD);
        p10.add(valTongHD);
        pRight.add(p10);

        // Tiền thanh toán
        JPanel p11 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p11.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTienThanhToan = new JLabel("Tiền thanh toán:");
        lblTienThanhToan.setFont(foBoLoc);
        lblTienThanhToan.setPreferredSize(new Dimension(150, 30));
        valTienThanhToan = new JLabel("");
        valTienThanhToan.setFont(foTxt);
        p11.add(lblTienThanhToan);
        p11.add(valTienThanhToan);
        pRight.add(p11);

        // Tiền khách đưa
        JPanel p12 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p12.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTienNhan = new JLabel("Tiền khách đưa:");
        lblTienNhan.setFont(foBoLoc);
        lblTienNhan.setPreferredSize(new Dimension(150, 30));
        txtTienNhan = new JTextField(15);
        txtTienNhan.setFont(foTxt);
        txtTienNhan.setPreferredSize(new Dimension(200, 30));
        txtTienNhan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTienThua();
            }
        });
        p12.add(lblTienNhan);
        p12.add(txtTienNhan);
        p12.add(lblDonViTien);
        pRight.add(p12);

        // Tiền thừa
        JPanel p13 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p13.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblThua = new JLabel("Tiền thừa:");
        lblThua.setFont(foBoLoc);
        lblThua.setPreferredSize(new Dimension(150, 30));
        valThua = new JLabel("");
        valThua.setFont(foTxt);
        p13.add(lblThua);
        p13.add(valThua);
        pRight.add(p13);
        pRight.add(Box.createVerticalGlue());

        pBottom.add(pLeft);
        pBottom.add(pRight);
        pMain.add(pBottom, BorderLayout.SOUTH);
        add(pMain, BorderLayout.CENTER);

        // === Nút chức năng ===
        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);
        Dimension btnSize = new Dimension(150, 50);
        Dimension buttonSize = new Dimension(100, 35);
        
        btnQuayLai = taoNut("Quay lại", new Color(255, 193, 7), buttonSize, buttonFont);
        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
        btnXacNhan = taoNut("Xác nhận", new Color(46, 204, 113), buttonSize, buttonFont);
        
        for (JButton btn : new JButton[]{btnQuayLai, btnHuy, btnXacNhan}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
        }
        pSouth.add(btnQuayLai);
        pSouth.add(Box.createHorizontalStrut(350));
        pSouth.add(btnHuy);
        pSouth.add(Box.createHorizontalStrut(20));
        pSouth.add(btnXacNhan);
        add(pSouth, BorderLayout.SOUTH);

        pMain.setBackground(Color.WHITE);
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.WHITE);
        p3.setBackground(Color.WHITE);
        p4.setBackground(Color.WHITE);
        p5.setBackground(Color.WHITE);
        p6.setBackground(Color.WHITE);
        p7.setBackground(Color.WHITE);
        p8.setBackground(Color.WHITE);
        p9.setBackground(Color.WHITE);
        p10.setBackground(Color.WHITE);
        p11.setBackground(Color.WHITE);
        p12.setBackground(Color.WHITE);
        p13.setBackground(Color.WHITE);
        pPhuThu.setBackground(Color.WHITE);
        pBottom.setBackground(Color.WHITE);
        pLeft.setBackground(Color.WHITE);
        pRight.setBackground(Color.WHITE);
        pMain.setBackground(Color.WHITE);
        pSouth.setBackground(Color.WHITE);
        // Load danh sách khuyến mãi
        loadKhuyenMai();
    }

    private void loadKhuyenMai() {
        try {
            List<KhuyenMai> dsKM = kmDAO.layDanhSachKhuyenMai();
            Collections.sort(dsKM, Comparator.comparing(KhuyenMai::getTenKM));
            cmbKhuyenMai.addItem(null); // Thêm tùy chọn không chọn khuyến mãi
            for (KhuyenMai km : dsKM) {
                cmbKhuyenMai.addItem(km);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khuyến mãi: " + e.getMessage());
        }
    }

    private double tinhGiamGia(KhuyenMai km) {
        if (km == null) return 0;
        String maLoai = km.getMaLoai();
        try {
            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
            String tenLoai = loaiDAO.getMaLoaiByTenLoai(maLoai);
            if (tenLoai.equals("Phần trăm")) {
                return tongCong * (km.getGiaTri() / 100);
            } else if (tenLoai.equals("Giảm trực tiếp")) {
                return km.getGiaTri();
            } else if (tenLoai.equals("Tặng món") && km.getMonTang() != null) {
                String sql = "SELECT donGia FROM MonAn WHERE maMon = ?";
                try (Connection con = ConnectSQL.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, km.getMonTang());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getDouble("donGia");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tính giảm giá: " + e.getMessage());
        }
        return 0;
    }

    private void tinhTienThua() {
        try {
            double tienKhachDua = txtTienNhan.getText().isEmpty() ? 0 : Double.parseDouble(txtTienNhan.getText().trim());
            double vat = tongCong * 0.08;
            double tienSauGiam = tongCong + vat - giamGia + phuThu;
            double tienThua = tienKhachDua - tienSauGiam;
            valTongHD.setText(df.format(tienSauGiam));
            valTienThanhToan.setText(df.format(tienSauGiam));
            valThua.setText(df.format(tienThua < 0 ? 0 : tienThua));
            txtTienNhan.setText(String.format("%.0f", tienSauGiam));
        } catch (NumberFormatException e) {
            valTongHD.setText(df.format(tongCong + (tongCong * 0.08) - giamGia + phuThu));
            valTienThanhToan.setText(df.format(tongCong + (tongCong * 0.08) - giamGia + phuThu));
            valThua.setText(df.format(0));
        }
    }

    private void loadDuLieuHoaDon(String maHD) {
        Object[] thongTin = hoaDonDAO.layThongTinHoaDon(maHD);
        if (thongTin == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn " + maHD);
            return;
        }

        valKH.setText((String) thongTin[0]);
        valSDT.setText((String) thongTin[1]);
        valBan.setText((String) thongTin[2]);
        valNV.setText((String) thongTin[3]);
        valNgayLap.setText(thongTin[4].toString());
        valCoc.setText(df.format(0));

        tongCong = (double) thongTin[6];
        valTongCong.setText(df.format(tongCong));

        double vat = tongCong * 0.08;
        giamGia = 0;
        phuThu = 0;
        double tongHD = tongCong + vat - giamGia + phuThu;
        valTongHD.setText(df.format(tongHD));
        valTienThanhToan.setText(df.format(tongHD));
        txtTienNhan.setText(String.format("%.0f", tongHD));
        valThua.setText(df.format(0));
    }

    private void loadChiTietHoaDon(String maHD) {
        List<Object[]> dsCT = hoaDonDAO.layChiTietHoaDon(maHD);
        model.setRowCount(0);
        int stt = 1;
        for (Object[] row : dsCT) {
            model.addRow(new Object[]{
                    stt++,
                    row[1],
                    row[2],
                    df.format(row[3]),
                    df.format(row[4])
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnQuayLai || source == btnHuy) {
            dispose();
        } else if (source == btnXacNhan) {
            try {
                double tienKhachDua = Double.parseDouble(txtTienNhan.getText().trim());
                double vat = tongCong * 0.08;
                double tienSauGiam = tongCong + vat - giamGia + phuThu;
                if (tienKhachDua < tienSauGiam) {
                    JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
                String maKM = selectedKM != null ? selectedKM.getMaKM() : null;
                // Cập nhật hóa đơn với mã khuyến mãi và phụ thu (nếu có)
                // hoaDonDAO.capNhatThanhToan(maHD, maKM, tienKhachDua, phuThu); // Cần thêm phương thức này trong HoaDon_DAO
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == cmbKhuyenMai) {
            KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
            giamGia = tinhGiamGia(selectedKM);
            tinhTienThua();
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

        // Hiệu ứng hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
    	UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 14));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        ConnectSQL.getInstance().connect();
        new FrmThanhToan("HD0001").setVisible(true);
    }
}