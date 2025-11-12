package dialog;

import dao.LoaiKhachHang_DAO;
import entity.LoaiKhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;


public class FrmLoaiKhachHang extends JDialog { 
    private final LoaiKhachHang_DAO loaiKhachHangDAO;
    private final DefaultTableModel modelLoaiKhachHang;
    private final JTable tblLoaiKhachHang;
    private final JTextField txtMaLoaiKH, txtTenLoaiKH;
    private final JButton btnThem, btnSua, btnXoa;
    private final Consumer<Void> refreshCallback; // Callback 

    /**
     * Constructor đã được cập nhật để nhận một 'Frame owner'
     */
    public FrmLoaiKhachHang(Frame owner, LoaiKhachHang_DAO loaiKhachHangDAO, Consumer<Void> refreshCallback) throws SQLException {
        
        super(owner, "Quản Lý Loại Khách Hàng", false); 
        
        this.loaiKhachHangDAO = loaiKhachHangDAO;
        this.refreshCallback = refreshCallback;

        setSize(600, 400);
        setLocationRelativeTo(owner); 
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

        // Mã loại khách hàng
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaLoai = new JLabel("Mã loại KH");
        lblMaLoai.setFont(labelFont);
        inputPanel.add(lblMaLoai, gbc);
        txtMaLoaiKH = new JTextField(10);
        txtMaLoaiKH.setFont(fieldFont);
        gbc.gridx = 1;
        inputPanel.add(txtMaLoaiKH, gbc);

        // Tên loại khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenLoai = new JLabel("Tên loại KH");
        lblTenLoai.setFont(labelFont);
        inputPanel.add(lblTenLoai, gbc);
        txtTenLoaiKH = new JTextField(20);
        txtTenLoaiKH.setFont(fieldFont);
        gbc.gridx = 1;
        inputPanel.add(txtTenLoaiKH, gbc);

        // Panel nút thao tác
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(100, 35);

        // Khởi tạo các nút
        btnThem = taoNut("Thêm", new Color(46, 204, 113), buttonSize, buttonFont);
        btnSua = taoNut("Sửa", new Color(52, 152, 219), buttonSize, buttonFont);
        btnXoa = taoNut("Xóa", new Color(231, 76, 60), buttonSize, buttonFont);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // Bảng loại khách hàng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Loại Khách Hàng", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);
        String[] columns = {"Mã loại KH", "Tên loại KH"};
        modelLoaiKhachHang = new DefaultTableModel(columns, 0);
        tblLoaiKhachHang = new JTable(modelLoaiKhachHang);
        tblLoaiKhachHang.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblLoaiKhachHang.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tblLoaiKhachHang);
        tablePanel.add(scroll, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        loadDanhSachLoaiKhachHang();

        // Sự kiện nút
        btnThem.addActionListener(e -> themLoaiKhachHang());
        btnSua.addActionListener(e -> suaLoaiKhachHang());
        btnXoa.addActionListener(e -> xoaLoaiKhachHang());

        // Sự kiện bảng
        tblLoaiKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblLoaiKhachHang.getSelectedRow();
                if (row >= 0) {
                    txtMaLoaiKH.setText((String) modelLoaiKhachHang.getValueAt(row, 0));
                    txtMaLoaiKH.setEditable(false);
                    txtTenLoaiKH.setText((String) modelLoaiKhachHang.getValueAt(row, 1));
                }
            }
        });
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

    private void loadDanhSachLoaiKhachHang() {
        modelLoaiKhachHang.setRowCount(0);
        try {
            List<LoaiKhachHang> list = loaiKhachHangDAO.getAll();
            for (LoaiKhachHang lkh : list) {
                modelLoaiKhachHang.addRow(new Object[]{
                        lkh.getMaLoaiKH(),
                        lkh.getTenLoaiKH()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách loại khách hàng: " + ex.getMessage());
        }
    }

    private void themLoaiKhachHang() {
        String maLoaiKH = txtMaLoaiKH.getText().trim();
        String tenLoaiKH = txtTenLoaiKH.getText().trim();
        if (maLoaiKH.isEmpty() || tenLoaiKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên loại khách hàng!");
            return;
        }
        try {
            LoaiKhachHang lkh = new LoaiKhachHang(maLoaiKH, tenLoaiKH);
            if (loaiKhachHangDAO.getLoaiKhachHangByMa(maLoaiKH) != null) {
                JOptionPane.showMessageDialog(this, "Mã loại khách hàng đã tồn tại!");
                return;
            }
            if (loaiKhachHangDAO.themLoaiKhachHang(lkh)) {
                JOptionPane.showMessageDialog(this, "Thêm loại khách hàng thành công!");
                loadDanhSachLoaiKhachHang();
                txtMaLoaiKH.setText("");
                txtTenLoaiKH.setText("");
                txtMaLoaiKH.setEditable(true);
                
                // === GỌI CALLBACK ===
                // Thông báo cho cửa sổ cha (FrmKhachHang) biết để tải lại dữ liệu
                refreshCallback.accept(null); 
                // ====================

            } else {
                JOptionPane.showMessageDialog(this, "Thêm loại khách hàng thất bại!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm loại khách hàng: " + ex.getMessage());
        }
    }

    private void suaLoaiKhachHang() {
        String maLoaiKH = txtMaLoaiKH.getText().trim();
        String tenLoaiKH = txtTenLoaiKH.getText().trim();
        if (maLoaiKH.isEmpty() || tenLoaiKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại khách hàng cần sửa và nhập đủ thông tin!");
            return;
        }
        LoaiKhachHang lkh = new LoaiKhachHang(maLoaiKH, tenLoaiKH);
        try {
            if (loaiKhachHangDAO.capNhatLoaiKhachHang(lkh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật loại khách hàng thành công!");
                loadDanhSachLoaiKhachHang();
                txtMaLoaiKH.setText("");
                txtTenLoaiKH.setText("");
                txtMaLoaiKH.setEditable(true);
                
                // === GỌI CALLBACK ===
                refreshCallback.accept(null);
                // ====================

            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật loại khách hàng thất bại!");
            }
        } catch (HeadlessException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật loại khách hàng: " + e.getMessage());
        }
    }

    private void xoaLoaiKhachHang() {
        int row = tblLoaiKhachHang.getSelectedRow();
        if (row >= 0) {
            String maLoaiKH = (String) modelLoaiKhachHang.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa loại khách hàng " + maLoaiKH + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (loaiKhachHangDAO.xoaLoaiKhachHang(maLoaiKH)) {
                        JOptionPane.showMessageDialog(this, "Xóa loại khách hàng thành công!");
                        loadDanhSachLoaiKhachHang();
                        txtMaLoaiKH.setText("");
                        txtTenLoaiKH.setText("");
                        txtMaLoaiKH.setEditable(true);
                        
                        // === GỌI CALLBACK ===
                        refreshCallback.accept(null);
                        // ====================
                        
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa loại khách hàng thất bại!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa loại khách hàng: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại khách hàng để xóa!");
        }
    }
}