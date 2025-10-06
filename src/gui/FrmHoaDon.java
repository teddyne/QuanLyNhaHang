package gui;

import dao.HoaDon_DAO;
import entity.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class FrmHoaDon extends JFrame implements ActionListener{
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
    

    public FrmHoaDon() {
        setTitle("Quản Lý Hóa Đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        hdDAO = new HoaDon_DAO();
        
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
        txtMaHD = new JTextField(28);
        txtTenNV = new JTextField(28);
        p1.add(Box.createHorizontalStrut(25));

        p1.add(lblMa = new JLabel("Mã hóa đơn: "));
        p1.add(txtMaHD);
        p1.add(Box.createHorizontalStrut(100));

        p1.add(lblTenNV = new JLabel("Tên nhân viên: "));
        p1.add(txtTenNV);

        
        // Loại tên khách + ngày lập
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTenKH = new JTextField(28);
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
        txtSdt = new JTextField(28);
        txtBan = new JTextField(28);
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
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));//trên trái dưới phải
        
        Font btnFont = new Font("Times New Roman", Font.BOLD, 22);
        btnChiTiet = new JButton("Chi tiết");
        btnLamMoi = new JButton("Làm mới");
        btnTraCuu = new JButton("Tra cứu");
        btnTatCa = new JButton("Tất cả");
        Dimension btnSize = new Dimension(180,50);
        for (JButton b : new JButton[]{btnChiTiet,btnLamMoi,btnTraCuu,btnTatCa}) {
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setFont(btnFont);
            pRight.add(b); 
            pRight.add(Box.createVerticalStrut(10));
            b.addActionListener(this);
        }
        
        btnChiTiet.setForeground(Color.WHITE);
        btnChiTiet.setBackground(new Color(102, 210, 74));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(210, 201, 74));
        btnTraCuu.setForeground(Color.WHITE);
        btnTraCuu.setBackground(new Color(62, 64, 194));
        btnTatCa.setForeground(Color.WHITE);
        btnTatCa.setBackground(new Color(169, 55, 68));

        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);

        // ==== Table ====
        String[] cols = {"Mã hóa đơn","Bàn","Ngày lập","Khách hàng","SĐT khách","Nhân viên","Khuyến mãi","Tổng tiền"};
        modelHoaDon = new DefaultTableModel(cols,0);
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(28);
        tblHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        //tblHoaDon.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165,42,42),2),"Danh Sách Hóa Đơn",0,0,new Font("Times New Roman", Font.BOLD, 24)));

        pMain.add(pCenter, BorderLayout.NORTH);
        pMain.add(scroll, BorderLayout.CENTER);

        add(pMain, BorderLayout.CENTER);

        
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


    public static void main(String[] args) {
        new FrmHoaDon().setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();

		if (o == btnChiTiet) {
		    int row = tblHoaDon.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hóa đơn!");
		        return;
		    }
		    String maHD = modelHoaDon.getValueAt(row, 0).toString();
		    new FrmChiTietHoaDon(maHD).setVisible(true);
		}

	}
}
