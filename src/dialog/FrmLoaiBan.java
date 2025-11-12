package dialog;
import dao.LoaiBan_DAO;
import entity.LoaiBan;
import gui.FrmDangNhap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmLoaiBan extends JFrame {
    private final LoaiBan_DAO loaiBanDAO;
    private final DefaultTableModel modelLoaiBan;
    private final JTable tblLoaiBan;
    private final JTextField txtMaLoai, txtTenLoai;
    private final JButton btnThem, btnSua, btnXoa;
    private final Consumer<Void> refreshCallback;

    public FrmLoaiBan(LoaiBan_DAO loaiBanDAO, Consumer<Void> refreshCallback) throws SQLException {
        this.loaiBanDAO = loaiBanDAO;
        this.refreshCallback = refreshCallback;

        setTitle("Quản Lý Loại Bàn");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        // Mã loại bàn
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaLoai = new JLabel("Mã loại bàn");
        lblMaLoai.setFont(labelFont);
        inputPanel.add(lblMaLoai, gbc);
        txtMaLoai = new JTextField(10);
        txtMaLoai.setFont(fieldFont);
        gbc.gridx = 1;
        inputPanel.add(txtMaLoai, gbc);

        // Tên loại bàn
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenLoai = new JLabel("Tên loại bàn");
        lblTenLoai.setFont(labelFont);
        inputPanel.add(lblTenLoai, gbc);
        txtTenLoai = new JTextField(20);
        txtTenLoai.setFont(fieldFont);
        gbc.gridx = 1;
        inputPanel.add(txtTenLoai, gbc);

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(100, 35);

        // Khởi tạo các nút
        btnThem = FrmDangNhap.taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont); 
        btnSua = FrmDangNhap.taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);         // xanh dương
        btnXoa = FrmDangNhap.taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);          // đỏ      

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // Bảng loại bàn
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Loại Bàn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);
        String[] columns = {"Mã loại", "Tên loại"};
        modelLoaiBan = new DefaultTableModel(columns, 0);
        tblLoaiBan = new JTable(modelLoaiBan);
        tblLoaiBan.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblLoaiBan.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tblLoaiBan);
        tablePanel.add(scroll, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        loadDanhSachLoaiBan();

        // Sự kiện nút
        btnThem.addActionListener(e -> themLoaiBan());
        btnSua.addActionListener(e -> suaLoaiBan());
        btnXoa.addActionListener(e -> xoaLoaiBan());

        // Sự kiện bảng
        tblLoaiBan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblLoaiBan.getSelectedRow();
                if (row >= 0) {
                    txtMaLoai.setText((String) modelLoaiBan.getValueAt(row, 0));
                    txtMaLoai.setEditable(false);
                    txtTenLoai.setText((String) modelLoaiBan.getValueAt(row, 1));
                }
            }
        });
    }
    
	private void loadDanhSachLoaiBan() {
        modelLoaiBan.setRowCount(0);
        try {
            List<LoaiBan> list = loaiBanDAO.getAll();
            for (LoaiBan lb : list) {
                modelLoaiBan.addRow(new Object[]{
                        lb.getMaLoai(),
                        lb.getTenLoai()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách loại bàn: " + ex.getMessage());
        }
    }

    private void themLoaiBan() {
        String maLoai = txtMaLoai.getText().trim();
        String tenLoai = txtTenLoai.getText().trim();
        if (maLoai.isEmpty() || tenLoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên loại bàn!");
            return;
        }
        try {
            LoaiBan lb = new LoaiBan();
            lb.setMaLoai(maLoai);
            lb.setTenLoai(tenLoai);
            if (loaiBanDAO.getLoaiBanByMa(maLoai) != null) {
                JOptionPane.showMessageDialog(this, "Mã loại bàn đã tồn tại!");
                return;
            }
            if (loaiBanDAO.themLoaiBan(lb)) {
                JOptionPane.showMessageDialog(this, "Thêm loại bàn thành công!");
                loadDanhSachLoaiBan();
                txtMaLoai.setText("");
                txtTenLoai.setText("");
                txtMaLoai.setEditable(true);
                refreshCallback.accept(null);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm loại bàn thất bại!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm loại bàn: " + ex.getMessage());
        }
    }

    private void suaLoaiBan() {
        String maLoai = txtMaLoai.getText().trim();
        String tenLoai = txtTenLoai.getText().trim();
        if (maLoai.isEmpty() || tenLoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên loại bàn!");
            return;
        }
        LoaiBan lb = new LoaiBan();
		lb.setMaLoai(maLoai);
		lb.setTenLoai(tenLoai);
		try {
			if (loaiBanDAO.capNhatLoaiBan(lb)) {
			    JOptionPane.showMessageDialog(this, "Cập nhật loại bàn thành công!");
			    loadDanhSachLoaiBan();
			    txtMaLoai.setText("");
			    txtTenLoai.setText("");
			    txtMaLoai.setEditable(true);
			    refreshCallback.accept(null);
			} else {
			    JOptionPane.showMessageDialog(this, "Cập nhật loại bàn thất bại!");
			}
		} catch (HeadlessException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void xoaLoaiBan() {
        int row = tblLoaiBan.getSelectedRow();
        if (row >= 0) {
            String maLoai = (String) modelLoaiBan.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa loại bàn " + maLoai + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (loaiBanDAO.xoaLoaiBan(maLoai)) {
                        JOptionPane.showMessageDialog(this, "Xóa loại bàn thành công!");
                        loadDanhSachLoaiBan();
                        txtMaLoai.setText("");
                        txtTenLoai.setText("");
                        txtMaLoai.setEditable(true);
                        refreshCallback.accept(null);
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa loại bàn thất bại!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa loại bàn: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại bàn để xóa!");
        }
    }
}