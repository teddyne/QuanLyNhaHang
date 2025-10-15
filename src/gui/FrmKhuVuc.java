package gui;

import dao.KhuVuc_DAO;
import entity.KhuVuc;
import connectSQL.ConnectSQL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmKhuVuc extends JFrame implements ActionListener, MouseListener {
    private KhuVuc_DAO khuDAO;
    private DefaultTableModel model;
    private JTable tblKhuVuc;
    private JTextField txtMaKhuVuc, txtTenKhuVuc, txtSoLuongBan;
    private JComboBox<String> cbTrangThai;
    private JButton btnThem, btnXoa, btnSua, btnLuu, btnTraCuu, btnLamMoi;
    private JPanel pNorth, pMain;
    private Consumer<Void> refreshCallback = null;

    public FrmKhuVuc() {
        this(null);
    }
    
    public FrmKhuVuc(Consumer<Void> refreshCallback) {
        super("Quản Lý Khu Vực");
        this.refreshCallback = refreshCallback;

        try {
            Connection conn = ConnectSQL.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Không thể kết nối cơ sở dữ liệu!");
                return;
            }
            this.khuDAO = new KhuVuc_DAO(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
            ThanhTacVu customMenu = ThanhTacVu.getInstance();
            add(customMenu.getBottomBar(), BorderLayout.SOUTH);
        } catch (Exception e) {
            // Bỏ qua nếu không có ThanhTacVu
        }

        initComponents();
        loadData();
    }

    private void initComponents() {
        // Panel tiêu đề
        pNorth = new JPanel();
        pNorth.add(new JLabel("QUẢN LÝ KHU VỰC"));
        Font fo = new Font("Times New Roman", Font.BOLD, 28);
        pNorth.getComponent(0).setFont(fo);
        pNorth.getComponent(0).setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        add(pNorth, BorderLayout.NORTH);

        // Panel chính
        pMain = new JPanel(new BorderLayout());
        JPanel pCenter = new JPanel(new BorderLayout());

        // Panel bộ lọc (Thông tin khu vực)
        JPanel pLeft = new JPanel(new GridBagLayout());
        pLeft.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
            "Thông Tin Khu Vực", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        pLeft.setBackground(new Color(240, 240, 240));

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 20);
        Font foTxt = new Font("Times New Roman", Font.PLAIN, 20);
        Dimension fieldSize = new Dimension(200, 30);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Các trường nhập liệu
        JLabel lblMaKhuVuc = new JLabel("Mã khu vực:");
        lblMaKhuVuc.setFont(foBoLoc);
        txtMaKhuVuc = new JTextField();
        txtMaKhuVuc.setFont(foTxt);
        txtMaKhuVuc.setPreferredSize(fieldSize);

        JLabel lblTenKhuVuc = new JLabel("Tên khu vực:");
        lblTenKhuVuc.setFont(foBoLoc);
        txtTenKhuVuc = new JTextField();
        txtTenKhuVuc.setFont(foTxt);
        txtTenKhuVuc.setPreferredSize(fieldSize);

        JLabel lblSoLuongBan = new JLabel("Số lượng bàn:");
        lblSoLuongBan.setFont(foBoLoc);
        txtSoLuongBan = new JTextField();
        txtSoLuongBan.setFont(foTxt);
        txtSoLuongBan.setPreferredSize(fieldSize);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(foBoLoc);
        cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng"});
        cbTrangThai.setFont(foTxt);
        cbTrangThai.setPreferredSize(fieldSize);

        // Thêm vào panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pLeft.add(lblMaKhuVuc, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(txtMaKhuVuc, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pLeft.add(lblTenKhuVuc, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(txtTenKhuVuc, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pLeft.add(lblSoLuongBan, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(txtSoLuongBan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        pLeft.add(lblTrangThai, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(cbTrangThai, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pLeft.add(new JPanel(), gbc);

        // Panel nút thao tác bên phải
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension btnSize = new Dimension(150, 40);
        
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnLuu = new JButton("Lưu");
        btnTraCuu = new JButton("Tra cứu");
        btnLamMoi = new JButton("Làm mới");

        JButton[] buttons = {btnThem, btnXoa, btnSua, btnLuu, btnTraCuu, btnLamMoi};
        for (JButton btn : buttons) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(btnFont);
            pRight.add(btn);
            pRight.add(Box.createVerticalStrut(10));
            btn.addActionListener(this);
        }

        // Styling cho các nút
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(102, 210, 74));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(230, 126, 34));
        btnSua.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(192, 57, 43));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBackground(new Color(41, 128, 185));
        btnTraCuu.setForeground(Color.WHITE);
        btnTraCuu.setBackground(new Color(62, 64, 194));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(52, 152, 219));

        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);

        // Bảng dữ liệu
        String[] columns = {"Mã khu vực", "Tên khu vực", "Số lượng bàn", "Trạng thái"};
        model = new DefaultTableModel(columns, 0);
        tblKhuVuc = new JTable(model);
        tblKhuVuc.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblKhuVuc.setRowHeight(30);
        tblKhuVuc.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblKhuVuc);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
            "Danh Sách Khu Vực", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        pMain.add(pCenter, BorderLayout.NORTH);
        pMain.add(tablePanel, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<KhuVuc> list = khuDAO.getAll();
            for (KhuVuc k : list) {
                model.addRow(new Object[]{
                    k.getMaKhuVuc(),
                    k.getTenKhuVuc(),
                    k.getSoLuongBan(),
                    k.getTrangThai()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void themKhuVuc() {
        txtMaKhuVuc.setEditable(true);
        txtMaKhuVuc.setText("KV" + System.currentTimeMillis());
        txtTenKhuVuc.setText("");
        txtSoLuongBan.setText("");
        cbTrangThai.setSelectedIndex(0);
        txtMaKhuVuc.requestFocus();
    }

    private void xoaKhuVuc() {
        int row = tblKhuVuc.getSelectedRow();
        if (row >= 0) {
            String maKhuVuc = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Xóa khu vực " + maKhuVuc + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    khuDAO.delete(maKhuVuc);
                    model.removeRow(row);
                    refreshCallback.accept(null);
                    JOptionPane.showMessageDialog(this, "Xóa khu vực thành công!");
                    lamMoiForm();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa khu vực: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để xóa!");
        }
    }

    private void suaKhuVuc() {
        int row = tblKhuVuc.getSelectedRow();
        if (row >= 0) {
            txtMaKhuVuc.setEditable(false);
            txtMaKhuVuc.setText((String) model.getValueAt(row, 0));
            txtTenKhuVuc.setText((String) model.getValueAt(row, 1));
            txtSoLuongBan.setText(model.getValueAt(row, 2).toString());
            cbTrangThai.setSelectedItem(model.getValueAt(row, 3));
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực để sửa!");
        }
    }

    private void luuKhuVuc() {
        String ma = txtMaKhuVuc.getText().trim();
        String ten = txtTenKhuVuc.getText().trim();
        String soLuongStr = txtSoLuongBan.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty() || soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số không âm!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng bàn phải là số hợp lệ!");
            return;
        }

        try {
            KhuVuc khuVuc = new KhuVuc(ma, ten, soLuong, trangThai);
            
            if (txtMaKhuVuc.isEditable()) {
                // Thêm mới
                if (khuDAO.getByMa(ma) != null) {
                    JOptionPane.showMessageDialog(this, "Mã khu vực đã tồn tại!");
                    return;
                }
                khuDAO.add(khuVuc);
                model.addRow(new Object[]{ma, ten, soLuong, trangThai});
                JOptionPane.showMessageDialog(this, "Thêm khu vực thành công!");
            } else {
                // Cập nhật
                khuDAO.update(khuVuc);
                int row = tblKhuVuc.getSelectedRow();
                if (row >= 0) {
                    model.setValueAt(ten, row, 1);
                    model.setValueAt(soLuong, row, 2);
                    model.setValueAt(trangThai, row, 3);
                }
                JOptionPane.showMessageDialog(this, "Cập nhật khu vực thành công!");
            }
            refreshCallback.accept(null);
            lamMoiForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu khu vực: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void lamMoiForm() {
        txtMaKhuVuc.setEditable(true);
        txtMaKhuVuc.setText("");
        txtTenKhuVuc.setText("");
        txtSoLuongBan.setText("");
        cbTrangThai.setSelectedIndex(0);
        loadData();
    }

    private void traCuuKhuVuc() {
        String ma = txtMaKhuVuc.getText().trim();
        String ten = txtTenKhuVuc.getText().trim();
        String trangThai = cbTrangThai.getSelectedIndex() >= 0 ? 
            (String) cbTrangThai.getSelectedItem() : null;

        try {
            // Implement logic tra cứu theo mã, tên và trạng thái
            // Tạm thời load tất cả dữ liệu
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tra cứu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            themKhuVuc();
        } else if (src == btnXoa) {
            xoaKhuVuc();
        } else if (src == btnSua) {
            suaKhuVuc();
        } else if (src == btnLuu) {
            luuKhuVuc();
        } else if (src == btnTraCuu) {
            traCuuKhuVuc();
        } else if (src == btnLamMoi) {
            lamMoiForm();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblKhuVuc.getSelectedRow();
        if (row >= 0) {
            txtMaKhuVuc.setEditable(false);
            txtMaKhuVuc.setText((String) model.getValueAt(row, 0));
            txtTenKhuVuc.setText((String) model.getValueAt(row, 1));
            txtSoLuongBan.setText(model.getValueAt(row, 2).toString());
            cbTrangThai.setSelectedItem(model.getValueAt(row, 3));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        try {
            new FrmKhuVuc(null).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}