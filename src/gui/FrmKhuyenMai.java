//package gui;
//
//import dao.KhuyenMai_DAO;
//import entity.KhuyenMai;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.Date;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//public class FrmKhuyenMai extends JFrame implements ActionListener, MouseListener{
//
//    private final KhuyenMai_DAO kmDAO;
//    private final JTable tblKhuyenMai = new JTable();
//    private final DefaultTableModel modelKhuyenMai;
//
//    private final JButton btnThem;
//    private final JButton btnSua;
//    private final JButton btnTraCuu;
//    private final JButton btnTatCa;
//    private final JButton btnLamMoi;
//	private JTextField txtMaKM;
//	private JTextField txtTenKM;
//	private JPanel pLeft;
//	private JPanel pBox;
//	private JPanel pNorth;
//	private JTextField txtMon, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu;
//	private JComboBox<String> cmbLoaiKM, cmbTrangThai, cmbDoiTuongApDung;
//	private JSpinner spTuNgay, spDenNgay;
//
//
//    public FrmKhuyenMai() {
//        setTitle("Quản Lý Khuyến Mãi");
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        kmDAO = new KhuyenMai_DAO();
//
//        setJMenuBar (CustomMenu.getInstance().getJMenuBar());
//        CustomMenu customMenu = CustomMenu.getInstance();
//        add(customMenu.getBottomBar(), BorderLayout.SOUTH);
//
//        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
//        
//        JPanel pNorth = new JPanel(new BorderLayout());
//        pNorth.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//
//        JPanel pLeft = new JPanel();
//        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
//        pLeft.setBorder(BorderFactory.createTitledBorder("Bộ lọc tra cứu khuyến mãi"));
//
//        // Mỗi dòng thông tin
//        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        p1.add(new JLabel("Mã KM: "));
//        txtMaKM = new JTextField(15);
//        p1.add(txtMaKM);
//        
//        p1.add(new JLabel("Món ăn: "));
//        txtMon = new JTextField(15);
//        p1.add(txtMon);
//
//        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        cmbLoaiKM = new JComboBox<>(new String[]{"Phần trăm","Giảm trực tiếp","Sinh nhật","Tặng món"});
//        cmbTrangThai = new JComboBox<>(new String[]{"Đang áp dụng","Sắp áp dụng","Hết hạn"});
//        p2.add(new JLabel("Loại KM: "));
//        p2.add(cmbLoaiKM);
//        p2.add(new JLabel("Trạng thái: "));
//        p2.add(cmbTrangThai);
//
//        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        spTuNgay = new JSpinner(new SpinnerDateModel());
//        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay,"dd/MM/yyyy"));
//        spDenNgay = new JSpinner(new SpinnerDateModel());
//        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay,"dd/MM/yyyy"));
//        p3.add(new JLabel("Từ ngày: "));
//        p3.add(spTuNgay);
//        p3.add(new JLabel("Đến ngày: "));
//        p3.add(spDenNgay);
//
//        // Thêm vào pLeft
//        pLeft.add(p1);
//        pLeft.add(p2);
//        pLeft.add(p3);
//
//        JPanel pRight = new JPanel();
//        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
//        pRight.setBorder(BorderFactory.createEmptyBorder(0,10,0,0)); // cách pLeft 10px
//
//        Font btnFont = new Font("Times New Roman", Font.BOLD, 16);
//
//        btnThem = new JButton("Thêm");
//        btnSua = new JButton("Sửa");
//        btnTraCuu = new JButton("Tra cứu");
//        btnTatCa = new JButton("Tất cả");
//        btnLamMoi = new JButton("Làm mới");
//        tblKhuyenMai.addMouseListener(this);
//
//        // Thiết lập cùng kích thước
//        Dimension btnSize = new Dimension(120, 40);
//        for (JButton btn : new JButton[]{btnThem,btnSua,btnTraCuu,btnTatCa,btnLamMoi}) {
//            btn.setMaximumSize(btnSize);
//            btn.setAlignmentX(Component.CENTER_ALIGNMENT); // căn giữa
//            btn.setFont(btnFont);
//            pRight.add(btn);
//            pRight.add(Box.createVerticalStrut(10)); // khoảng cách giữa các nút
//        }
//
//        pNorth.add(pLeft, BorderLayout.CENTER); // bộ lọc bên trái
//        pNorth.add(pRight, BorderLayout.EAST);  // nút thao tác bên phải
//        add(pNorth, BorderLayout.NORTH);
//
//        btnThem.addActionListener(this);
//        btnSua.addActionListener(this);
//        btnTraCuu.addActionListener(this);
//        btnTatCa.addActionListener(this);
//        btnLamMoi.addActionListener(this);
//
//        
//        //Bảng dữ liệu
//        String[] columns = {"Mã KM","Tên KM","Loại","Giá trị","Ngày BD","Ngày KT","Trạng thái","Đối tượng","Đơn hàng từ","Món1","Món2","Món tặng","Ghi chú"};
//        modelKhuyenMai = new DefaultTableModel(columns,0);
//        tblKhuyenMai = new JTable(modelKhuyenMai);
//        tblKhuyenMai.setRowHeight(25);
//        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
//
//        JPanel tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách Khuyến Mãi"));
//        tablePanel.add(scroll, BorderLayout.CENTER);
//        add(tablePanel, BorderLayout.CENTER);
//
//        // Load dữ liệu từ database
//        loadDanhSachKhuyenMai();
//    }
//
//    
//	private void loadDanhSachKhuyenMai() {
//			// TODO Auto-generated method stub
//		modelKhuyenMai.setRowCount(0); // xóa dữ liệu cũ
//	    List<KhuyenMai> list = kmDAO.layDanhSachKhuyenMai(); // lấy danh sách từ DAO
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	    
//	    for (KhuyenMai km : list) {
//	        modelKhuyenMai.addRow(new Object[]{
//	            km.getMaKM(),
//	            km.getTenKM(),
//	            km.getLoaiKM(),
//	            km.getGiaTri(),
//	            km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
//	            km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
//	            km.getTrangThai(),
//	            km.getDoiTuongApDung(),
//	            km.getDonHangTu(),
//	            km.getMon1(),
//	            km.getMon2(),
//	            km.getMonTang(),
//	            km.getGhiChu()
//	        });
//	    }
//	}
//
//
//    private boolean kiemTraNgay(Date bd, Date kt) {
//        if (kt.before(bd)) {
//            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//        return true;
//    }
//
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//	    Object src = e.getSource();
//	    if (src == btnThem) {
//	        themKhuyenMai();
//	    } else if (src == btnSua) {
//	        suaKhuyenMai();
//	    } else if (src == btnTraCuu) {
//	        traCuuKhuyenMai();
//	    } else if (src == btnTatCa) {
//	        xemTatCaKhuyenMai();
//	    } else if (src == btnLamMoi) {
//	        lamMoiForm();
//	    }
//	}
//
//	
//    private void lamMoiForm() {
//		// TODO Auto-generated method stub
//    	txtMaKM.setText(kmDAO.taoMaKhuyenMaiMoi()); // giả sử DAO có tạo mã mới
//        txtTenKM.setText("");
//        spTuNgay.setValue(new java.util.Date());
//        spDenNgay.setValue(new java.util.Date());
//	}
//
//	private void traCuuKhuyenMai() {
//	    String ma = txtMaKM.getText().trim();
//	    String mon = txtMon.getText().trim();
//	    String loai = cmbLoaiKM.getSelectedItem().toString();
//	    String trangThai = cmbTrangThai.getSelectedItem().toString();
//
//	    List<KhuyenMai> list = kmDAO.traCuuKhuyenMai(ma, mon, loai, trangThai);
//	    modelKhuyenMai.setRowCount(0);
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//	    for (KhuyenMai km : list) {
//	        modelKhuyenMai.addRow(new Object[]{
//	            km.getMaKM(),
//	            km.getTenKM(),
//	            km.getLoaiKM(),
//	            km.getGiaTri(),
//	            km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
//	            km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
//	            km.getTrangThai(),
//	            km.getDoiTuongApDung(),
//	            km.getDonHangTu(),
//	            km.getMon1(),
//	            km.getMon2(),
//	            km.getMonTang(),
//	            km.getGhiChu()
//	        });
//	    }
//	}
//
//	private void xemTatCaKhuyenMai() {
//	    loadDanhSachKhuyenMai();
//	}
//
//
//
//	private void suaKhuyenMai() {
//	    int row = tblKhuyenMai.getSelectedRow();
//	    if (row < 0) {
//	        JOptionPane.showMessageDialog(this, "Chọn khuyến mãi cần sửa!");
//	        return;
//	    }
//
//	    try {
//	        Date bd = new Date(((java.util.Date)spTuNgay.getValue()).getTime());
//	        Date kt = new Date(((java.util.Date)spDenNgay.getValue()).getTime());
//	        if (!kiemTraNgay(bd, kt)) return;
//
//	        KhuyenMai km = new KhuyenMai(
//	            txtMaKM.getText(),
//	            txtTenKM.getText(),
//	            cmbLoaiKM.getSelectedItem().toString(),
//	            Double.parseDouble(txtGiaTri.getText()),
//	            bd,
//	            kt,
//	            cmbTrangThai.getSelectedItem().toString(),
//	            cmbDoiTuongApDung.getSelectedItem().toString(),
//	            Double.parseDouble(txtDonHangTu.getText()),
//	            txtMon1.getText(),
//	            txtMon2.getText(),
//	            txtMonTang.getText(),
//	            txtGhiChu.getText()
//	        );
//
//	        if (kmDAO.suaKhuyenMai(km)) {
//	            JOptionPane.showMessageDialog(this,"Sửa thành công!");
//	            loadDanhSachKhuyenMai();
//	            lamMoiForm();
//	        } else {
//	            JOptionPane.showMessageDialog(this,"Sửa thất bại!");
//	        }
//
//	    } catch (NumberFormatException e) {
//	        JOptionPane.showMessageDialog(this,"Giá trị và Đơn hàng từ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//	    }
//	}
//
//
//
//	private void themKhuyenMai() {
//	    try {
//	        Date bd = new Date(((java.util.Date)spTuNgay.getValue()).getTime());
//	        Date kt = new Date(((java.util.Date)spDenNgay.getValue()).getTime());
//	        if (!kiemTraNgay(bd, kt)) return;
//
//	        KhuyenMai km = new KhuyenMai(
//	            txtMaKM.getText(),
//	            txtTenKM.getText(),
//	            cmbLoaiKM.getSelectedItem().toString(),
//	            Double.parseDouble(txtGiaTri.getText()), // thêm txtGiaTri
//	            bd,
//	            kt,
//	            cmbTrangThai.getSelectedItem().toString(),
//	            cmbDoiTuongApDung.getSelectedItem().toString(),
//	            Double.parseDouble(txtDonHangTu.getText()), // thêm txtDonHangTu
//	            txtMon1.getText(),
//	            txtMon2.getText(),
//	            txtMonTang.getText(),
//	            txtGhiChu.getText()
//	        );
//
//	        if (kmDAO.themKhuyenMai(km)) {
//	            JOptionPane.showMessageDialog(this,"Thêm thành công!");
//	            loadDanhSachKhuyenMai();
//	            lamMoiForm();
//	        } else {
//	            JOptionPane.showMessageDialog(this,"Thêm thất bại!");
//	        }
//
//	    } catch (NumberFormatException e) {
//	        JOptionPane.showMessageDialog(this,"Giá trị và Đơn hàng từ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//	    }
//	}
//
//
//
//	public static void main(String[] args) {
//        new FrmKhuyenMai().setVisible(true);
//    }
//
//
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		// TODO Auto-generated method stub
//		int row = tblKhuyenMai.getSelectedRow();
//        if(row >= 0){
//            txtMaKM.setText(modelKhuyenMai.getValueAt(row,0).toString());
//            txtTenKM.setText(modelKhuyenMai.getValueAt(row,1).toString());
//            cmbLoaiKM.setSelectedItem(modelKhuyenMai.getValueAt(row,2));
//            txtGiaTri.setText(modelKhuyenMai.getValueAt(row,3).toString());
//            spTuNgay.setValue(java.sql.Date.valueOf(modelKhuyenMai.getValueAt(row,4).toString()));
//            spDenNgay.setValue(java.sql.Date.valueOf(modelKhuyenMai.getValueAt(row,5).toString()));
//            cmbTrangThai.setSelectedItem(modelKhuyenMai.getValueAt(row,6));
//            cmbDoiTuongApDung.setSelectedItem(modelKhuyenMai.getValueAt(row,7));
//            txtDonHangTu.setText(modelKhuyenMai.getValueAt(row,8).toString());
//            txtMon1.setText(modelKhuyenMai.getValueAt(row,9).toString());
//            txtMon2.setText(modelKhuyenMai.getValueAt(row,10).toString());
//            txtMonTang.setText(modelKhuyenMai.getValueAt(row,11).toString());
//            txtGhiChu.setText(modelKhuyenMai.getValueAt(row,12).toString());
//        }
//	}
//
//
//	@Override
//	public void mousePressed(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void mouseExited(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//}

package gui;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmKhuyenMai extends JFrame implements ActionListener, MouseListener {

    private final KhuyenMai_DAO kmDAO;
    private final JTable tblKhuyenMai;
    private final DefaultTableModel modelKhuyenMai;

    private final JButton btnThem;
    private final JButton btnSua;
    private final JButton btnTraCuu;
    private final JButton btnTatCa;
    private final JButton btnLamMoi;

    // Các field dùng cho form và lọc
    private JTextField txtMaKM, txtTenKM, txtMon, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu;
    private JComboBox<String> cmbLoaiKM, cmbTrangThai, cmbDoiTuongApDung;
    private JSpinner spTuNgay, spDenNgay;
    private JCheckBox chkTuNgay, chkDenNgay;
	private JPanel pNorth;
	private JLabel lblTieuDe;
	private JLabel lblMa;
	private JLabel lblMon;
	private JLabel lblLoai;
	private JLabel lblTrangThai;
	private JPanel pMain;

    public FrmKhuyenMai() {
        setTitle("Quản Lý Khuyến Mãi");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        kmDAO = new KhuyenMai_DAO();

        // Menu
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);
        pMain = new JPanel();
        
        
        pNorth = new JPanel();
		pNorth.add(lblTieuDe = new JLabel("DANH SÁCH KHUYẾN MÃI"));
		Font fo = new Font("Times new Roman", Font.BOLD, 26);
		lblTieuDe.setFont(fo);
		lblTieuDe.setForeground(Color.WHITE);
		Color c = new Color(169, 55, 68);
		pNorth.setBackground(c);
		add(pNorth, BorderLayout.NORTH);
		
        // Panel phía trên (form + nút)
        JPanel pCenter = new JPanel(new BorderLayout());

        // Panel filter bên trái
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
		pLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Thông Tin Tài Khoản", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));

        // ======== Dòng 1: Mã KM + Món ========
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtMaKM = new JTextField(25);
        txtMon = new JTextField(25);
        p1.add(Box.createHorizontalStrut(30));

        p1.add(lblMa = new JLabel("Mã khuyến mãi:   "));
        p1.add(txtMaKM);
        p1.add(Box.createHorizontalStrut(50));

        p1.add(lblMon = new JLabel("Món ăn:     "));
        p1.add(txtMon);

        
        // Loại KM + Trạng thái
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbLoaiKM = new JComboBox<>(new String[]{"Tất cả","Phần trăm","Giảm trực tiếp","Sinh nhật","Tặng món"});
        cmbTrangThai = new JComboBox<>(new String[]{"Tất cả","Đang áp dụng","Sắp áp dụng","Hết hạn"});
        p2.add(Box.createHorizontalStrut(30));

        p2.add(lblLoai = new JLabel("Loại khuyến mãi: "));
        p2.add(cmbLoaiKM);
        p2.add(Box.createHorizontalStrut(50));

        p2.add(lblTrangThai = new JLabel("Trạng thái: "));
        p2.add(cmbTrangThai);

        // Ngày bắt đầu / kết thúc với checkbox
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(Box.createHorizontalStrut(30));
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay,"dd/MM/yyyy"));
        
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay,"dd/MM/yyyy"));
        
        chkTuNgay = new JCheckBox("Từ ngày: ");
        chkDenNgay = new JCheckBox("Đến ngày: ");
        p3.add(chkTuNgay); p3.add(spTuNgay);
        p3.add(Box.createHorizontalStrut(20));
        p3.add(chkDenNgay); p3.add(spDenNgay);
 
		Font foBoLoc = new Font("Times new Roman", Font.BOLD, 22);
		txtMaKM.setFont(foBoLoc); 
		lblMa.setFont(foBoLoc); 
		lblMon.setFont(foBoLoc); 
		txtMon.setFont(foBoLoc);
		lblLoai.setFont(foBoLoc); 
		lblTrangThai.setFont(foBoLoc); 
		cmbLoaiKM.setFont(foBoLoc);
		chkTuNgay.setFont(foBoLoc); 
		chkDenNgay.setFont(foBoLoc);
		spTuNgay.setFont(foBoLoc); 
		spDenNgay.setFont(foBoLoc);
		cmbLoaiKM.setPreferredSize(new Dimension(450, 30));// rộng cao
		cmbTrangThai.setFont(foBoLoc);
		cmbTrangThai.setPreferredSize(new Dimension(450, 30));
		spTuNgay.setPreferredSize(new Dimension(173, 30));
		spDenNgay.setPreferredSize(new Dimension(173, 30));

        pLeft.add(p1); pLeft.add(p2); pLeft.add(p3);

        pLeft.add(p1);
        pLeft.add(p2);
        pLeft.add(p3);

        // Panel nút thao tác bên phải
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));//trên trái dưới phải

        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnTraCuu = new JButton("Tra cứu");
        btnTatCa = new JButton("Tất cả");
        btnLamMoi = new JButton("Làm mới");
        //btnSua.setPreferredSize(new Dimension(150, 50));

        Dimension btnSize = new Dimension(150, 50);
        for (JButton btn : new JButton[]{btnThem, btnSua, btnTraCuu, btnTatCa, btnLamMoi}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(btnFont);
            pRight.add(btn);
            pRight.add(Box.createVerticalStrut(10));
            btn.addActionListener(this);
        }

        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);
        add(pCenter, BorderLayout.CENTER);

        
        
        // ======== Bảng dữ liệu ========
        String[] columns = {"Mã khuyến mãi", "Tên khuyến mãi", "Loại", "Giá trị", "Ngày bắt đầu", "Ngày kết thúc",
                "Trạng thái", "Đối tượng", "Đơn hàng từ", "Món 1", "Món 2", "Món tặng", "Ghi chú"};
        modelKhuyenMai = new DefaultTableModel(columns, 0);
        tblKhuyenMai = new JTable(modelKhuyenMai);
        tblKhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKhuyenMai.setRowHeight(30);
        tblKhuyenMai.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Thông Tin Tài Khoản", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.add(scroll, BorderLayout.CENTER);
        //add(tablePanel, BorderLayout.SOUTH);

        
        pMain.setLayout(new BorderLayout());
        pMain.add(pCenter, BorderLayout.NORTH);   // form và nút ở trên
        pMain.add(tablePanel, BorderLayout.CENTER); // bảng chiếm hết phần còn lại
        add(pMain, BorderLayout.CENTER); //giữa của header với footer

       
        
        // Khởi tạo các field form chi tiết KM62, 64, 194
        txtTenKM = new JTextField(15);
        txtGiaTri = new JTextField(10);
        txtDonHangTu = new JTextField(10);
        txtMon1 = new JTextField(15);
        txtMon2 = new JTextField(15);
        txtMonTang = new JTextField(15);
        txtGhiChu = new JTextField(30);
        cmbDoiTuongApDung = new JComboBox<>(new String[]{"Tất cả", "Khách hàng thân thiết", "Khách lẻ"});

        // Load dữ liệu
        loadDanhSachKhuyenMai();
    }

    private void loadDanhSachKhuyenMai() {
        modelKhuyenMai.setRowCount(0);
        List<KhuyenMai> list = kmDAO.layDanhSachKhuyenMai();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (KhuyenMai km : list) {
            modelKhuyenMai.addRow(new Object[]{
                    km.getMaKM(),
                    km.getTenKM(),
                    km.getLoaiKM(),
                    km.getGiaTri(),
                    km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
                    km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
                    km.getTrangThai(),
                    km.getDoiTuongApDung(),
                    km.getDonHangTu(),
                    km.getMon1(),
                    km.getMon2(),
                    km.getMonTang(),
                    km.getGhiChu()
            });
        }
    }

    private boolean kiemTraNgay(Date bd, Date kt) {
        if (kt.before(bd)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src == btnThem) {
            new FrmThemKhuyenMai();
            loadDanhSachKhuyenMai();
        } else if(src == btnSua) {
            int row = tblKhuyenMai.getSelectedRow();
            if(row < 0){
                JOptionPane.showMessageDialog(this,"Chọn khuyến mãi cần sửa!");
                return;
            }
            String maKM = modelKhuyenMai.getValueAt(row,0).toString();
            KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM); // DAO trả về 1 KhuyenMai
            new FrmSuaKhuyenMai(km);
            loadDanhSachKhuyenMai();
        }
        else if (src == btnTraCuu) 
        	traCuuKhuyenMai();
        else if (src == btnTatCa) 
        	xemTatCaKhuyenMai();
        else if (src == btnLamMoi) 
        	lamMoiForm();
    }

    private void lamMoiForm() {
    	txtMaKM.setText("");
        txtMon.setText("");
        
        // ComboBox về "Tất cả"
        cmbLoaiKM.setSelectedIndex(0);
        cmbTrangThai.setSelectedIndex(0);

        // CheckBox ngày bỏ check
        chkTuNgay.setSelected(false);
        chkDenNgay.setSelected(false);
    }

    private void traCuuKhuyenMai() {
        String ma = txtMaKM.getText().trim();
        String mon = txtMon.getText().trim();

        String loai = cmbLoaiKM.getSelectedItem().toString();
        if (loai.equals("Tất cả")) loai = null;

        String trangThai = cmbTrangThai.getSelectedItem().toString();
        if (trangThai.equals("Tất cả")) trangThai = null;

        Date tuNgayVal = chkTuNgay.isSelected() ? new Date(((java.util.Date) spTuNgay.getValue()).getTime()) : null;
        Date denNgayVal = chkDenNgay.isSelected() ? new Date(((java.util.Date) spDenNgay.getValue()).getTime()) : null;

        List<KhuyenMai> list = kmDAO.traCuuKhuyenMai(ma, mon, loai, trangThai, tuNgayVal, denNgayVal);
        modelKhuyenMai.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (KhuyenMai km : list) {
            modelKhuyenMai.addRow(new Object[]{
                km.getMaKM(), km.getTenKM(), km.getLoaiKM(), km.getGiaTri(),
                km.getNgayBatDau()!=null ? sdf.format(km.getNgayBatDau()) : "",
                km.getNgayKetThuc()!=null ? sdf.format(km.getNgayKetThuc()) : "",
                km.getTrangThai(), km.getDoiTuongApDung(), km.getDonHangTu(),
                km.getMon1(), km.getMon2(), km.getMonTang(), km.getGhiChu()
            });
        }
    }
 


    private void xemTatCaKhuyenMai() {
        loadDanhSachKhuyenMai();
    }

    private void suaKhuyenMai() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khuyến mãi cần sửa!");
            return;
        }

        try {
            Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
            Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());
            if (!kiemTraNgay(bd, kt)) return;

            KhuyenMai km = new KhuyenMai(
                    txtMaKM.getText(),
                    txtTenKM.getText(),
                    cmbLoaiKM.getSelectedItem().toString(),
                    Double.parseDouble(txtGiaTri.getText()),
                    bd,
                    kt,
                    cmbTrangThai.getSelectedItem().toString(),
                    cmbDoiTuongApDung.getSelectedItem().toString(),
                    Double.parseDouble(txtDonHangTu.getText()),
                    txtMon1.getText(),
                    txtMon2.getText(),
                    txtMonTang.getText(),
                    txtGhiChu.getText()
            );

            if (kmDAO.suaKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadDanhSachKhuyenMai();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị và Đơn hàng từ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themKhuyenMai() {
        try {
            Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
            Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());
            if (!kiemTraNgay(bd, kt)) return;

            KhuyenMai km = new KhuyenMai(
                    txtMaKM.getText(),
                    txtTenKM.getText(),
                    cmbLoaiKM.getSelectedItem().toString(),
                    Double.parseDouble(txtGiaTri.getText()),
                    bd,
                    kt,
                    cmbTrangThai.getSelectedItem().toString(),
                    cmbDoiTuongApDung.getSelectedItem().toString(),
                    Double.parseDouble(txtDonHangTu.getText()),
                    txtMon1.getText(),
                    txtMon2.getText(),
                    txtMonTang.getText(),
                    txtGhiChu.getText()
            );

            if (kmDAO.themKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDanhSachKhuyenMai();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị và đơn hàng từ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblKhuyenMai.getSelectedRow();
        if (row >= 0) {
            txtMaKM.setText(modelKhuyenMai.getValueAt(row, 0).toString());
            txtTenKM.setText(modelKhuyenMai.getValueAt(row, 1).toString());
            cmbLoaiKM.setSelectedItem(modelKhuyenMai.getValueAt(row, 2));
            txtGiaTri.setText(modelKhuyenMai.getValueAt(row, 3).toString());

            String ngayBD = modelKhuyenMai.getValueAt(row, 4).toString();
            String ngayKT = modelKhuyenMai.getValueAt(row, 5).toString();
            spTuNgay.setValue(!ngayBD.isEmpty() ? java.sql.Date.valueOf(ngayBD) : new java.util.Date());
            spDenNgay.setValue(!ngayKT.isEmpty() ? java.sql.Date.valueOf(ngayKT) : new java.util.Date());

            cmbTrangThai.setSelectedItem(modelKhuyenMai.getValueAt(row, 6));
            cmbDoiTuongApDung.setSelectedItem(modelKhuyenMai.getValueAt(row, 7));
            txtDonHangTu.setText(modelKhuyenMai.getValueAt(row, 8).toString());
            txtMon1.setText(modelKhuyenMai.getValueAt(row, 9).toString());
            txtMon2.setText(modelKhuyenMai.getValueAt(row, 10).toString());
            txtMonTang.setText(modelKhuyenMai.getValueAt(row, 11).toString());
            txtGhiChu.setText(modelKhuyenMai.getValueAt(row, 12).toString());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    public static void main(String[] args) {
        new FrmKhuyenMai().setVisible(true);
    }
}
