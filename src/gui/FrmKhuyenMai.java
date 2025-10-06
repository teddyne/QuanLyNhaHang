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
        
        pMain = new JPanel();//chứa tất cả nd trong trang
        
        pNorth = new JPanel();
		pNorth.add(lblTieuDe = new JLabel("DANH SÁCH KHUYẾN MÃI"));
		Font fo = new Font("Times new Roman", Font.BOLD, 28);
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
		pLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Bộ Lọc Khuyến Mãi", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));

        // ======== Dòng 1: Mã KM + Món ========
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtMaKM = new JTextField(28);
        txtMon = new JTextField(28);
        p1.add(Box.createHorizontalStrut(25));

        p1.add(lblMa = new JLabel("Mã khuyến mãi:   "));
        p1.add(txtMaKM);
        p1.add(Box.createHorizontalStrut(100));

        p1.add(lblMon = new JLabel("Món ăn:     "));
        p1.add(txtMon);

        
        // Loại KM + Trạng thái
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbLoaiKM = new JComboBox<>(new String[]{"Tất cả","Phần trăm","Giảm trực tiếp","Sinh nhật","Tặng món"});
        cmbTrangThai = new JComboBox<>(new String[]{"Tất cả","Đang áp dụng","Sắp áp dụng","Hết hạn"});
        p2.add(Box.createHorizontalStrut(25));

        p2.add(lblLoai = new JLabel("Loại khuyến mãi: "));
        p2.add(cmbLoaiKM);
        p2.add(Box.createHorizontalStrut(100));

        p2.add(lblTrangThai = new JLabel("Trạng thái: "));
        p2.add(cmbTrangThai);

        // Ngày bắt đầu / kết thúc với checkbox
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(Box.createHorizontalStrut(20));
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
		Font foTxt = new Font("Times new Roman", Font.PLAIN, 22);

		txtMaKM.setFont(foTxt); 
		lblMa.setFont(foBoLoc); 
		lblMon.setFont(foBoLoc); 
		txtMon.setFont(foTxt);
		lblLoai.setFont(foBoLoc); 
		lblTrangThai.setFont(foBoLoc); 
		cmbLoaiKM.setFont(foTxt);
		chkTuNgay.setFont(foBoLoc); 
		chkDenNgay.setFont(foBoLoc);
		spTuNgay.setFont(foTxt); 
		spDenNgay.setFont(foTxt);
		cmbLoaiKM.setPreferredSize(new Dimension(450, 30));// rộng cao
		cmbTrangThai.setFont(foTxt);
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

        Font btnFont = new Font("Times New Roman", Font.BOLD, 22);
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnTraCuu = new JButton("Tra cứu");
        btnTatCa = new JButton("Tất cả");
        btnLamMoi = new JButton("Làm mới");


        Dimension btnSize = new Dimension(180, 50);
        for (JButton btn : new JButton[]{btnThem, btnSua, btnTraCuu, btnTatCa, btnLamMoi}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(btnFont);
            pRight.add(btn);
            pRight.add(Box.createVerticalStrut(10));
            btn.addActionListener(this);
        }
        
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(102, 210, 74));
        btnSua.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(216, 154, 161));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(210, 201, 74));
        btnTraCuu.setForeground(Color.WHITE);
        btnTraCuu.setBackground(new Color(62, 64, 194));
        btnTatCa.setForeground(Color.WHITE);
        btnTatCa.setBackground(new Color(169, 55, 68));

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
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Danh Sách Khuyến Mãi", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
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