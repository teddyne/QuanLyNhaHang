package dialog;

import dao.LoaiKhuyenMai_DAO;
import entity.LoaiKhuyenMai;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FrmLoaiKhuyenMai extends JFrame {
    private JTextField txtMaLoai, txtTenLoai;
    private JButton btnThem, btnXoa, btnSua;
    private JTable table;
    private DefaultTableModel model;
    private LoaiKhuyenMai_DAO dao;
	private Component lblMa;
	private JLabel lblTen;
    private Connection con = ConnectSQL.getConnection();

	public FrmLoaiKhuyenMai() {
	    setTitle("Quản lý Loại Khuyến Mãi");
	    setSize(600, 400);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	    dao = new LoaiKhuyenMai_DAO();

	    //Nền trắng toàn JFrame
	    getContentPane().setBackground(Color.WHITE);

	    // --- Form nhập liệu ---
	    JPanel pInput = new JPanel();
	    pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
	    pInput.add(Box.createVerticalStrut(10));
	    pInput.setBackground(Color.WHITE);

	    JPanel p1 = new JPanel();
	    p1.setBackground(Color.WHITE);
	    p1.add(lblMa = new JLabel("Mã loại:"));
	    p1.add(txtMaLoai = new JTextField(20));
	    
	    JPanel p2 = new JPanel();
	    p2.setBackground(Color.WHITE);
	    p2.add(lblTen = new JLabel("Tên loại:"));
	    p2.add(txtTenLoai = new JTextField(20));


	    // Font
	    Font labelFont = new Font("Times New Roman", Font.BOLD, 20);
	    Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);
	    lblMa.setFont(labelFont);
	    lblTen.setFont(labelFont);
	    txtMaLoai.setFont(fieldFont);
	    txtTenLoai.setFont(fieldFont);
	    
	    lblMa.setPreferredSize(lblTen.getPreferredSize());
	    pInput.add(p1);
	    pInput.add(p2);
	    pInput.add(Box.createVerticalStrut(10));

	    add(pInput, BorderLayout.NORTH);

	    // --- Bảng hiển thị loại khuyến mãi ---
	    model = new DefaultTableModel(new String[]{"Mã loại", "Tên loại"}, 0);
	    table = new JTable(model);
	    table.setRowHeight(30);
	    table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
	    table.setBackground(Color.WHITE);
	    table.setGridColor(Color.LIGHT_GRAY);

	    JScrollPane scroll = new JScrollPane(table);
	    scroll.setBackground(Color.WHITE);
	    scroll.getViewport().setBackground(Color.WHITE); // nền bên trong bảng
	    scroll.setBorder(BorderFactory.createTitledBorder(
	            BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
	            "Danh sách loại khuyến mãi", 0, 0, new Font("Times New Roman", Font.BOLD, 22)));
	    add(scroll, BorderLayout.CENTER);

	    //Nút
	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    buttonPanel.setBackground(Color.WHITE);

	    Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
	    Dimension buttonSize = new Dimension(100, 35);

	    btnThem = taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont); 
	    btnSua = taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);   
	    btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);    

	    buttonPanel.add(btnThem);
	    buttonPanel.add(btnSua);
	    buttonPanel.add(btnXoa);
	    add(buttonPanel, BorderLayout.SOUTH);

	    loadDanhSachLoaiKhuyenMai();

	    btnThem.addActionListener(e -> themLoai());
	    btnSua.addActionListener(e -> suaLoai());
	    btnXoa.addActionListener(e -> xoaLoai());

	    table.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            int row = table.getSelectedRow();
	            if (row >= 0) {
	                txtMaLoai.setText(model.getValueAt(row, 0).toString());
	                txtTenLoai.setText(model.getValueAt(row, 1).toString());
	            }
	        }
	    });

	    setVisible(true);
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

    private void loadDanhSachLoaiKhuyenMai() {
        model.setRowCount(0); // Xóa dữ liệu cũ
        try {
            List<LoaiKhuyenMai> list = dao.getAllLoaiKhuyenMai();
            for (LoaiKhuyenMai loai : list) {
                model.addRow(new Object[]{loai.getMaLoai(), loai.getTenLoai()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách loại khuyến mãi: " + ex.getMessage());
        }
    }

    private void themLoai() {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        LoaiKhuyenMai loai = new LoaiKhuyenMai(ma, ten);
        if (dao.themLoaiKhuyenMai(loai)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadDanhSachLoaiKhuyenMai();
            txtMaLoai.setText("");
            txtTenLoai.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã loại có thể đã tồn tại.");
        }
    }

    private void suaLoai() {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập thông tin cần sửa!");
            return;
        }

        LoaiKhuyenMai loai = new LoaiKhuyenMai(ma, ten);
        if (dao.capNhatLoaiKhuyenMai(loai)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDanhSachLoaiKhuyenMai();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xoaLoai() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại cần xóa!");
            return;
        }

        String ma = model.getValueAt(row, 0).toString();
        if (JOptionPane.showConfirmDialog(this, "Xóa loại " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.xoaLoaiKhuyenMai(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDanhSachLoaiKhuyenMai();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    public static void main(String[] args) {
    	UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 18));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        new FrmLoaiKhuyenMai();
    }
}