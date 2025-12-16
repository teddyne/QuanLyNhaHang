package gui;

import dao.KhuyenMai_DAO;
import dialog.FrmLoaiKhuyenMai;
import dialog.FrmSuaKhuyenMai;
import dialog.FrmThemKhuyenMai;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
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
    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtDonHangTu, txtGhiChu;
    private JComboBox<String> cmbLoaiKM, cmbTrangThai, cmbDoiTuongApDung;
    private JSpinner spTuNgay, spDenNgay;
    private JCheckBox chkTuNgay, chkDenNgay;
	private JPanel pNorth;
	private JLabel lblTieuDe;
	private JLabel lblMa;
	private JLabel lblLoai;
	private JLabel lblTrangThai;
	private JPanel pMain;
	private JTextField txtTrangThai;
	private JButton btnThemLoai;
    private Connection con = ConnectSQL.getConnection();
	private JLabel lblDoiTuong;
	private JTextField txtDoiTuong;


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
		pNorth.setBackground(new Color(169, 55, 68));
		add(pNorth, BorderLayout.NORTH);
		
        // Panel phía trên (form + nút)
        JPanel pCenter = new JPanel(new BorderLayout());
        // Panel filter bên trái
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
		pLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Bộ Lọc Khuyến Mãi", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));

		
        // ======== Dòng 1: Mã KM + Món ========
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtMaKM = new JTextField(20);
        txtDoiTuong = new JTextField(20);
        p1.add(Box.createHorizontalStrut(25));

        p1.add(lblMa = new JLabel("Mã khuyến mãi:   "));
        p1.add(txtMaKM);
        p1.add(Box.createHorizontalStrut(100));

        p1.add(lblDoiTuong = new JLabel("Đối tượng:   "));
        p1.add(txtDoiTuong);

        
        // Loại KM + Trạng thái
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbLoaiKM = new JComboBox<>();
        loadLoaiKhuyenMai();

        txtTrangThai = new JTextField(20);
        p2.add(Box.createHorizontalStrut(25));

        p2.add(lblLoai = new JLabel("Loại khuyến mãi: "));
        p2.add(cmbLoaiKM);
        p2.add(Box.createHorizontalStrut(100));

        p2.add(lblTrangThai = new JLabel("Trạng thái:   "));
        p2.add(txtTrangThai);

        
        // Ngày bắt đầu / kết thúc với checkbox
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(Box.createHorizontalStrut(20));
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay,"dd/MM/yyyy"));
        
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay,"dd/MM/yyyy"));
        
        chkTuNgay = new JCheckBox("Từ ngày:            ");
        chkDenNgay = new JCheckBox("Đến ngày: ");
        p3.add(chkTuNgay); p3.add(spTuNgay);
        p3.add(Box.createHorizontalStrut(100));
        p3.add(chkDenNgay); p3.add(spDenNgay);
 
		Font foBoLoc = new Font("Times new Roman", Font.BOLD, 22);
		Font foTxt = new Font("Times new Roman", Font.PLAIN, 22);

		txtMaKM.setFont(foTxt); 
		lblMa.setFont(foBoLoc); 
		lblDoiTuong.setFont(foBoLoc); 
		txtDoiTuong.setFont(foTxt);
		lblLoai.setFont(foBoLoc); 
		lblTrangThai.setFont(foBoLoc); 
		cmbLoaiKM.setFont(foTxt);
		chkTuNgay.setFont(foBoLoc); 
		chkDenNgay.setFont(foBoLoc);
		spTuNgay.setFont(foTxt); 
		spDenNgay.setFont(foTxt);
		cmbLoaiKM.setPreferredSize(new Dimension(320, 30));// rộng cao
		txtTrangThai.setFont(foTxt);
		spTuNgay.setPreferredSize(new Dimension(320, 30));
		spDenNgay.setPreferredSize(new Dimension(320, 30));

        pLeft.add(p1);
        pLeft.add(p2);
        pLeft.add(p3);
        
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.X_AXIS));
        pRight.setBackground(Color.WHITE);

        JPanel col1 = new JPanel();
        JPanel col2 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        col1.setBackground(Color.WHITE);
        col2.setBackground(Color.WHITE);
        
        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension btnSize = new Dimension(150, 50);

        btnThem   = taoNut("Thêm", new Color(46, 204, 113), btnSize, btnFont);
        btnSua    = taoNut("Sửa", new Color(52, 152, 219), btnSize, btnFont);
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), btnSize, btnFont);
        btnTatCa  = taoNut("Tất cả", new Color(231, 76, 60), btnSize, btnFont);
        btnLamMoi = taoNut("Làm mới", new Color(255, 193, 7), btnSize, btnFont);
        btnThemLoai = taoNut("Thêm loại", new Color(149, 165, 166), btnSize, btnFont);

        // Cột 1
        col1.add(btnThem);
        col1.add(Box.createVerticalStrut(10));
        col1.add(btnTraCuu);
        col1.add(Box.createVerticalStrut(10));
        col1.add(btnLamMoi);

        // Cột 2
        col2.add(btnSua);
        col2.add(Box.createVerticalStrut(10));
        col2.add(btnTatCa);
        col2.add(Box.createVerticalStrut(10));
        col2.add(btnThemLoai);
        
        col1.setAlignmentY(Component.TOP_ALIGNMENT);
        col2.setAlignmentY(Component.TOP_ALIGNMENT);
        Dimension colSize = new Dimension(170, 200);
        col1.setPreferredSize(colSize);
        col2.setPreferredSize(colSize);

        pRight.add(Box.createHorizontalStrut(5));
        pRight.add(Box.createVerticalStrut(30));
        pRight.add(col1);
        pRight.add(col2);

        for (JButton btn : new JButton[]{btnThem, btnSua, btnTraCuu, btnTatCa, btnLamMoi, btnThemLoai}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
        }

        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);
        add(pCenter, BorderLayout.CENTER);
        
        
        // ======== Bảng dữ liệu ========
        String[] columns = {
        	    "Mã KM",
        	    "Tên khuyến mãi",
        	    "Loại",
        	    "Ngày bắt đầu",
        	    "Ngày kết thúc",
        	    "Trạng thái",
        	    "Đối tượng áp dụng",
        	    "Ghi chú"
        	};
        
        modelKhuyenMai = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // KHÔNG cho sửa
            }
        };

        tblKhuyenMai = new JTable(modelKhuyenMai);
        tblKhuyenMai.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKhuyenMai.setRowHeight(30);
        tblKhuyenMai.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
	    scroll.getViewport().setBackground(Color.WHITE); // nền bên trong bảng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Danh Sách Khuyến Mãi", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);
		tablePanel.add(scroll, BorderLayout.CENTER);

        
        pMain.setLayout(new BorderLayout());
        pMain.add(pCenter, BorderLayout.NORTH);   // form và nút ở trên
        pMain.add(tablePanel, BorderLayout.CENTER); // bảng chiếm hết phần còn lại
        add(pMain, BorderLayout.CENTER); //giữa của header với footer

        //Màu
        pMain.setBackground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.WHITE);
        p3.setBackground(Color.WHITE);
        pLeft.setBackground(Color.WHITE);
        pRight.setBackground(Color.WHITE);
        pCenter.setBackground(Color.WHITE);
       
        loadDanhSachKhuyenMai();
    }
    
    private void loadDanhSachKhuyenMai() {
        modelKhuyenMai.setRowCount(0);
        List<KhuyenMai> list = kmDAO.layDanhSachKhuyenMai();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (KhuyenMai km : list) {
            modelKhuyenMai.addRow(new Object[]{
                km.getMaKM(),
                km.getTenKM(),
                km.getMaLoai(),
                km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
                km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
                km.getTrangThai(),
                km.getDoiTuongApDung(),
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
            FrmThemKhuyenMai frm = new FrmThemKhuyenMai();
//            frm.setVisible(true);       // <---- QUAN TRỌNG
            loadDanhSachKhuyenMai();
        }

        if(src == btnSua) {
            int row = tblKhuyenMai.getSelectedRow();
            if(row < 0){
                JOptionPane.showMessageDialog(this,"Chọn khuyến mãi cần sửa!");
                return;
            }
            String maKM = modelKhuyenMai.getValueAt(row,0).toString();
            KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
            new FrmSuaKhuyenMai(km).setVisible(true);
            loadDanhSachKhuyenMai();
        }
        
        if (src == btnTraCuu) 
        	traCuuKhuyenMai();
        if (src == btnTatCa) 
        	loadDanhSachKhuyenMai();
        if (src == btnLamMoi) {
        	lamMoi();
        	//loadDanhSachKhuyenMai();
        }
        
        if (src == btnThemLoai) {
            FrmLoaiKhuyenMai frm = new FrmLoaiKhuyenMai();
            frm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadLoaiKhuyenMai();
                }
            });
            frm.setVisible(true);
        }
        
//        if (src == btnChiTiet) {
//            int row = tblKhuyenMai.getSelectedRow();
//            if (row < 0) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi!");
//                return;
//            }
//            String maKM = modelKhuyenMai.getValueAt(row, 0).toString();
//            KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
//        	new FrmSuaKhuyenMai(km, true).setVisible(true);
//            
//        }

    }

    //load (cập nhật) lên combobox
    private void loadLoaiKhuyenMai() {
        try {
            cmbLoaiKM.removeAllItems();
            cmbLoaiKM.addItem("Tất cả");
            
            dao.LoaiKhuyenMai_DAO loaiDAO = new dao.LoaiKhuyenMai_DAO();
            List<entity.LoaiKhuyenMai> list = loaiDAO.getAllLoaiKhuyenMai();
            for (entity.LoaiKhuyenMai loai : list) {
                cmbLoaiKM.addItem(loai.getTenLoai());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách loại khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
	private void lamMoi() {
    	txtMaKM.setText("");
        txtDoiTuong.setText("");
        txtTrangThai.setText("");
        // ComboBox về "Tất cả"
        cmbLoaiKM.setSelectedIndex(0);
        // CheckBox ngày bỏ check
        chkTuNgay.setSelected(false);
        chkDenNgay.setSelected(false);
    }

    private void traCuuKhuyenMai() {
        String ma = txtMaKM.getText().trim();
        String doiTuong = txtDoiTuong.getText().trim();
        String trangThai = txtTrangThai.getText().trim();
        String loai = cmbLoaiKM.getSelectedItem().toString();
        if (loai.equals("Tất cả")) loai = null;
        Date tuNgayVal = chkTuNgay.isSelected() ? new Date(((java.util.Date) spTuNgay.getValue()).getTime()) : null;
        Date denNgayVal = chkDenNgay.isSelected() ? new Date(((java.util.Date) spDenNgay.getValue()).getTime()) : null;

        List<KhuyenMai> list = kmDAO.traCuuKhuyenMai(ma, doiTuong, loai, trangThai, tuNgayVal, denNgayVal);
        modelKhuyenMai.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (KhuyenMai km : list) {
            modelKhuyenMai.addRow(new Object[]{
            		km.getMaKM(),
    	            km.getTenKM(),
    	            km.getMaLoai(),
    	            km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
    	            km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
    	            km.getTrangThai(),
    	            km.getDoiTuongApDung(),
    	            km.getGhiChu()
            });
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

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblKhuyenMai.getSelectedRow();
        if (row < 0) return;
        
        if (e.getClickCount() == 2) {
            String maKM = modelKhuyenMai.getValueAt(row, 0).toString();
        	KhuyenMai km = kmDAO.layKhuyenMaiTheoMa(maKM);
        	new FrmSuaKhuyenMai(km, true).setVisible(true);
            return;
        }
        
        try {
            txtMaKM.setText(modelKhuyenMai.getValueAt(row, 0).toString());
            cmbLoaiKM.setSelectedItem(modelKhuyenMai.getValueAt(row, 2).toString());
            txtTrangThai.setText(modelKhuyenMai.getValueAt(row, 5).toString());
            txtDoiTuong.setText(modelKhuyenMai.getValueAt(row, 6).toString());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String ngayBDStr = modelKhuyenMai.getValueAt(row, 3).toString();
            String ngayKTStr = modelKhuyenMai.getValueAt(row, 4).toString();

            if (!ngayBDStr.isEmpty()) {
                java.util.Date d1 = sdf.parse(ngayBDStr);
                spTuNgay.setValue(d1);
                chkTuNgay.setSelected(true);
            }

            if (!ngayKTStr.isEmpty()) {
                java.util.Date d2 = sdf.parse(ngayKTStr);
                spDenNgay.setValue(d2);
                chkDenNgay.setSelected(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hiển thị ngày khuyến mãi!");
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
        UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 14));
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new FrmKhuyenMai().setVisible(true);
        });
    }

}