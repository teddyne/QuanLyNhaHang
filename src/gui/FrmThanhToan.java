package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.HoaDon_DAO;

import java.awt.*;
import java.text.DecimalFormat;

public class FrmThanhToan extends JFrame {
    private DecimalFormat df = new DecimalFormat("#,##0 VND");
	private JLabel lblTieuDe;
	private JLabel lblKH;
	private JTextField txtKhachHang;
	private JLabel lblSDT;
	private JTextField txtSDT;
	private JLabel lblNgayLap;
	private JTextField txtNgayLap;
	private JLabel lblBan;
	private JTextField txtBan;
	private JLabel lblNV;
	private JTextField txtNV;
	private JTable table;
	private JButton btnQuayLai;
	private JButton btnHuy;
	private JButton btnXacNhan;
	private JPanel pTop;
	private JPanel pMain;
	private JPanel pBottom;
	private JLabel lblTongCong;
	private JTextField txtTongCong;
	private JLabel lblTongHD;
	private JTextField txtTongHD;
	private JLabel lblKM;
	private JTextField txtKM;
	private JLabel lblCoc;
	private JTextField txtCoc;
	private JLabel lblVAT;
	private JTextField txtVAT;
	private JLabel lblTienThanhToan;
	private JTextField txtTienThanhToan;
	private JLabel lblTienNhan;
	private JTextField txtTienNhan;
	private JLabel lblThua;
	private JTextField txtTienThua;

    public FrmThanhToan() {
        setTitle("Thanh toán");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // ==== THÔNG TIN ====
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
        Font foTxt = new Font("Times New Roman", Font.PLAIN, 18);

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(lblKH = new JLabel("Khách hàng:"));
        txtKhachHang = new JTextField(15);
        p1.add(txtKhachHang);
        p1.add(Box.createHorizontalStrut(25));
        p1.add(lblSDT = new JLabel("SĐT khách:"));
        txtSDT = new JTextField(15);
        p1.add(txtSDT);
        pTop.add(p1);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(lblNgayLap = new JLabel("Ngày lập: "));
        txtNgayLap = new JTextField(15);
        p2.add(txtNgayLap);
        p2.add(Box.createHorizontalStrut(35));
        p2.add(lblBan = new JLabel("Bàn: "));
        txtBan = new JTextField(15);
        p2.add(txtBan);
        pTop.add(p2);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(lblNV = new JLabel("Nhân viên: "));
        txtNV = new JTextField(15);
        p3.add(txtNV);
        pTop.add(p3);

        // === Gán font ===
        lblKH.setFont(foBoLoc);
        lblSDT.setFont(foBoLoc);
        lblNgayLap.setFont(foBoLoc);
        lblBan.setFont(foBoLoc);
        lblNV.setFont(foBoLoc);

        txtKhachHang.setFont(foTxt);
        txtSDT.setFont(foTxt);
        txtNgayLap.setFont(foTxt);
        txtBan.setFont(foTxt);
        txtNV.setFont(foTxt);

        // === Căn đều kích thước nhãn ===
        lblNgayLap.setPreferredSize(lblKH.getPreferredSize());
        lblNV.setPreferredSize(lblKH.getPreferredSize());
        lblBan.setPreferredSize(lblSDT.getPreferredSize());

        // ==== BẢNG MÓN ĂN ====
        String[] colNames = {"STT", "Món", "Số lượng", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        table.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(table);

        // ==== TỔNG HÓA ĐƠN ====
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        pBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.add(lblTongCong = new JLabel("Tổng cộng: "));
        txtTongCong = new JTextField(15);
        p4.add(txtTongCong);
        p4.add(Box.createHorizontalStrut(30));
        p4.add(lblTongHD = new JLabel("Tổng hóa đơn: "));
        txtTongHD = new JTextField(15);
        p4.add(txtTongHD);
        pBottom.add(p4);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(lblKM = new JLabel("Khuyến mãi: "));
        txtKM = new JTextField(15);
        p5.add(txtKM);
        p5.add(Box.createHorizontalStrut(30));
        p5.add(lblCoc = new JLabel("Đã cọc: "));
        txtCoc = new JTextField(15);
        p5.add(txtCoc);
        pBottom.add(p5);
        
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p6.add(lblVAT = new JLabel("Thuế VAT: "));
        txtVAT = new JTextField(15);
        txtVAT.setText("8%");
        p6.add(txtVAT);
        p6.add(Box.createHorizontalStrut(25));
        p6.add(lblTienThanhToan = new JLabel("Tiền thanh toán: "));
        txtTienThanhToan = new JTextField(15);
        p6.add(txtTienThanhToan);
        pBottom.add(p6);
        
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p7.add(lblTienNhan = new JLabel("Tiền khách đưa:"));
        txtTienNhan = new JTextField(15);
        p7.add(txtTienNhan);
        pBottom.add(p7);
        
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p8.add(lblThua = new JLabel("Tiền thừa: "));
        txtTienThua = new JTextField(15);
        p8.add(txtTienThua);
        pBottom.add(p8);

        // Gán font cho phần tổng
        lblTongCong.setFont(foBoLoc);
        lblTongHD.setFont(foBoLoc);
        lblKM.setFont(foBoLoc);
        lblCoc.setFont(foBoLoc);
        lblVAT.setFont(foBoLoc);
        lblTienThanhToan.setFont(foBoLoc);
        lblTienNhan.setFont(foBoLoc);
        lblThua.setFont(foBoLoc);

        txtTongCong.setFont(foTxt);
        txtTongHD.setFont(foTxt);
        txtKM.setFont(foTxt);
        txtCoc.setFont(foTxt);
        txtVAT.setFont(foTxt);
        txtTienThanhToan.setFont(foTxt);
        txtTienNhan.setFont(foTxt);
        txtTienThua.setFont(foTxt);
        
        lblTongCong.setPreferredSize(lblTienNhan.getPreferredSize());
        lblKM.setPreferredSize(lblTienNhan.getPreferredSize());
        lblVAT.setPreferredSize(lblTienNhan.getPreferredSize());
        lblThua.setPreferredSize(lblTienNhan.getPreferredSize());
        
        lblTongHD.setPreferredSize(lblTienThanhToan.getPreferredSize());
        lblCoc.setPreferredSize(lblTienThanhToan.getPreferredSize());
        
        // === Ghép các phần vào pMain ===
        pMain.add(pTop, BorderLayout.NORTH);
        pMain.add(scroll, BorderLayout.CENTER);
        pMain.add(pBottom, BorderLayout.SOUTH);

        add(pMain, BorderLayout.CENTER);

        // === Nút chức năng ===
        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnQuayLai = new JButton("Quay lại");
        btnHuy = new JButton("Hủy");
        btnXacNhan = new JButton("Xác nhận thanh toán");

        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBackground(new Color(169, 55, 68));

        btnHuy.setForeground(Color.WHITE);
        btnHuy.setBackground(new Color(210, 201, 74));

        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(new Color(102, 210, 74));

        Font foBtn = new Font("Times New Roman", Font.BOLD, 18);
        btnQuayLai.setFont(foBtn);
        btnHuy.setFont(foBtn);
        btnXacNhan.setFont(foBtn);

        pSouth.add(btnQuayLai);
        pSouth.add(Box.createHorizontalStrut(235));
        pSouth.add(btnHuy);
        pSouth.add(btnXacNhan);
        add(pSouth, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
        new FrmThanhToan().setVisible(true);
    }
}

