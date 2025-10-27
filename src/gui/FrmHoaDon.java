package gui;

import dao.HoaDon_DAO;
import entity.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class FrmHoaDon extends JFrame implements ActionListener, MouseListener{
    private HoaDon_DAO hdDAO;
	private JTextField txtMaHD;
	private JTextField txtTenNV;
	private JLabel lblMa;
	private JLabel lblTenNV;
	private JTextField txtTenKH;
	private JSpinner spNgayLap;
	private JLabel lblTenKH;
	private JCheckBox chkNgayLap;
	private JTextField txtSdt;
	private JTextField txtBan;
	private JLabel lblSdt;
	private JLabel lblBan;
	private JButton btnChiTiet;
	private JButton btnLamMoi;
	private JButton btnTraCuu;
	private JButton btnTatCa;
	private DefaultTableModel modelHoaDon;
	private JTable tblHoaDon;
	private Container pCenter;
    

    public FrmHoaDon() {
        setTitle("Quản Lý Hóa Đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Connection con = ConnectSQL.getConnection();
        hdDAO = new HoaDon_DAO(con);
        
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);
        
        JPanel pNorth = new JPanel();
        JLabel lblTieuDe = new JLabel("DANH SÁCH HÓA ĐƠN");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        pNorth.add(lblTieuDe);
        add(pNorth, BorderLayout.NORTH);

        JPanel pMain = new JPanel(new BorderLayout());
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165,42,42),2),"Bộ Lọc Hóa Đơn",0,0,new Font("Times New Roman", Font.BOLD, 24)));

        // dòng 1: mã HD + KH
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtMaHD = new JTextField(20);
        txtTenNV = new JTextField(20);
        p1.add(Box.createHorizontalStrut(25));

        p1.add(lblMa = new JLabel("Mã hóa đơn: "));
        p1.add(txtMaHD);
        p1.add(Box.createHorizontalStrut(100));

        p1.add(lblTenNV = new JLabel("Tên nhân viên: "));
        p1.add(txtTenNV);

        
        // Loại tên khách + ngày lập
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTenKH = new JTextField(20);
        spNgayLap = new JSpinner(new SpinnerDateModel());
        spNgayLap.setEditor(new JSpinner.DateEditor(spNgayLap,"dd/MM/yyyy"));
        p2.add(Box.createHorizontalStrut(25));

        p2.add(lblTenKH = new JLabel("Tên khách:    "));
        p2.add(txtTenKH);
        p2.add(Box.createHorizontalStrut(100));

        chkNgayLap = new JCheckBox("Ngày lập:      ");
        p2.add(chkNgayLap);
        p2.add(spNgayLap);

        // sđt khách + bàn
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSdt = new JTextField(20);
        txtBan = new JTextField(20);
        p3.add(Box.createHorizontalStrut(25));

        p3.add(lblSdt = new JLabel("SĐT khách:   "));
        p3.add(txtSdt);
        p3.add(Box.createHorizontalStrut(100));

        p3.add(lblBan = new JLabel("Bàn:                  "));
        p3.add(txtBan);
        
		Font foBoLoc = new Font("Times new Roman", Font.BOLD, 22);
		Font foTxt = new Font("Times new Roman", Font.PLAIN, 22);

		txtMaHD.setFont(foTxt); 
		lblMa.setFont(foBoLoc); 
		lblTenNV.setFont(foBoLoc); 
		txtTenNV.setFont(foTxt);
		lblTenKH.setFont(foBoLoc); 
		txtTenKH.setFont(foTxt);
		chkNgayLap.setFont(foBoLoc); 
		spNgayLap.setFont(foTxt); 
		spNgayLap.setPreferredSize(new Dimension(210, 30));
		lblSdt.setFont(foBoLoc); 
		txtSdt.setFont(foTxt);
		lblBan.setFont(foBoLoc); 
		txtBan.setFont(foTxt);
		
        pLeft.add(p1); pLeft.add(p2); pLeft.add(p3);

        
        
        // ==== Buttons ====
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

        // ====== Tạo nút ======
        btnChiTiet = taoNut("Chi tiết", new Color(255, 193, 7), btnSize, btnFont);
        btnTraCuu  = taoNut("Tra cứu", new Color(121, 89, 229), btnSize, btnFont);
        btnTatCa   = taoNut("Tất cả", new Color(46, 204, 113), btnSize, btnFont);
        btnLamMoi  = taoNut("Làm mới", new Color(231, 76, 60), btnSize, btnFont);

        // ====== Thêm vào cột ======
        // Cột 1
        col1.add(btnChiTiet);
        col1.add(Box.createVerticalStrut(20));
        col1.add(btnTraCuu);

        // Cột 2
        col2.add(btnTatCa);
        col2.add(Box.createVerticalStrut(20));
        col2.add(btnLamMoi);

        // ====== Căn chỉnh ======
        col1.setAlignmentY(Component.TOP_ALIGNMENT);
        col2.setAlignmentY(Component.TOP_ALIGNMENT);

        Dimension colSize = new Dimension(170, 200);
        col1.setPreferredSize(colSize);
        col2.setPreferredSize(colSize);

        pRight.add(Box.createHorizontalStrut(5));
        pRight.add(Box.createVerticalStrut(30));

        pRight.add(col1);
        pRight.add(col2);

        //Gán ActionListener cho tất cả nút
        for (JButton btn : new JButton[]{btnChiTiet, btnTraCuu, btnTatCa, btnLamMoi}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
        }

        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);
        add(pCenter, BorderLayout.CENTER);

        // ==== Table ====
        String[] cols = {"Mã hóa đơn","Bàn","Ngày lập","Khách hàng","SĐT khách","Nhân viên","Khuyến mãi","Tổng tiền"};
        modelHoaDon = new DefaultTableModel(cols,0);
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(28);
        tblHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblHoaDon.setDefaultEditor(Object.class, null); //ko cho chỉnh sửa, cho phép bắt sự kiện double-click
        tblHoaDon.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
	    scroll.getViewport().setBackground(Color.WHITE); // nền bên trong bảng
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165,42,42),2),"Danh Sách Hóa Đơn",0,0,new Font("Times New Roman", Font.BOLD, 24)));

        pMain.add(pCenter, BorderLayout.NORTH);
        pMain.add(scroll, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);
        
        pMain.setBackground(Color.WHITE);
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.WHITE);
        p3.setBackground(Color.WHITE);
        pLeft.setBackground(Color.WHITE);
        pRight.setBackground(Color.WHITE);
        pCenter.setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        
        loadDanhSachHoaDon();
    }

    private void loadDanhSachHoaDon() {
        modelHoaDon.setRowCount(0); // xóa dòng cũ
        List<Object[]> ds = hdDAO.layDanhSachHoaDonDayDu();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(Object[] row : ds) {
            modelHoaDon.addRow(new Object[]{
                    row[0], // Mã HĐ
                    row[1], // Bàn
                    sdf.format(row[2]), // Ngày lập
                    row[3], // Tên KH
                    row[4], // SĐT KH
                    row[5], // Nhân viên
                    row[6], // Khuyến mãi
                    row[7]  // Tổng tiền
            });
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == btnChiTiet)
			chiTietHoaDon();
		if (o == btnLamMoi)
			lamMoi();
		if (o == btnTatCa)
		    loadDanhSachHoaDon();
		if (o == btnTraCuu)
			traCuuHoaDon();
	}
	

    private void traCuuHoaDon() {
    	String maHD = txtMaHD.getText().trim();
	    String tenKH = txtTenKH.getText().trim();
	    String sdt = txtSdt.getText().trim();
	    String tenNV = txtTenNV.getText().trim();
	    String maBan = txtBan.getText().trim();
	    Date ngayLap = null;
	    if (chkNgayLap.isSelected()) {
	        java.util.Date utilDate = (java.util.Date) spNgayLap.getValue();
	        ngayLap = new java.sql.Date(utilDate.getTime());
	    }

	    List<Object[]> ds = hdDAO.timKiemHoaDon(maHD, tenKH, sdt, tenNV, maBan, ngayLap);
	    modelHoaDon.setRowCount(0);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	    for (Object[] row : ds) {
	        modelHoaDon.addRow(new Object[]{
	                row[0],
	                row[1],
	                sdf.format(row[2]),
	                row[3],
	                row[4],
	                row[5],
	                row[6],
	                row[7]
	        });
	    }

	    if (ds.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào phù hợp!");
	    }		
	}

	private void lamMoi() {
    	txtMaHD.setText("");
 	    txtTenNV.setText("");
 	    txtTenKH.setText("");
 	    txtSdt.setText("");
 	    txtBan.setText("");
 	    chkNgayLap.setSelected(false);		
	}

	private void chiTietHoaDon() {
    	int row = tblHoaDon.getSelectedRow();
    	if (row == -1) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa đơn!");
	        return;
    	}
	    String maHD = modelHoaDon.getValueAt(row, 0).toString();
	    new FrmChiTietHoaDon(maHD).setVisible(true);	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	    int row = tblHoaDon.getSelectedRow();
	    if (row >= 0) {
	        // Lấy dữ liệu từ bảng
	        txtMaHD.setText(modelHoaDon.getValueAt(row, 0).toString());
	        txtBan.setText(modelHoaDon.getValueAt(row, 1).toString());
	        txtTenKH.setText(modelHoaDon.getValueAt(row, 3).toString());
	        txtSdt.setText(modelHoaDon.getValueAt(row, 4).toString());
	        txtTenNV.setText(modelHoaDon.getValueAt(row, 5).toString());

	        // Ngày lập (cột 2)
	        try {
	            String ngayLapStr = modelHoaDon.getValueAt(row, 2).toString();
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            java.util.Date date = sdf.parse(ngayLapStr);
	            spNgayLap.setValue(date);
	            chkNgayLap.setSelected(true);
	        } catch (Exception ex) {
	            spNgayLap.setValue(new java.util.Date());
	        }
	    }

	    // Nếu click đúp → mở chi tiết hóa đơn
	    if (e.getClickCount() == 2 && row >= 0) {
	        String maHD = modelHoaDon.getValueAt(row, 0).toString();
	        new FrmChiTietHoaDon(maHD).setVisible(true);
	    }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
        new FrmHoaDon().setVisible(true);
    }
}