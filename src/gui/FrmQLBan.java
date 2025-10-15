package gui;

import dao.Ban_DAO;
import dao.KhuVuc_DAO;
import entity.Ban;
import entity.KhuVuc;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connectSQL.ConnectSQL;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmQLBan extends JFrame implements ActionListener, MouseListener {
    private final Ban_DAO banDAO;
    private final KhuVuc_DAO khuVucDAO;
    private final DefaultTableModel modelBan;
    private final JTable tblBan;
    private JTextField txtMaBan, txtGhiChu, txtSoCho;
    private JComboBox<String> cbKhuVuc, cbTrangThai, cbLoaiBan;
    private final JButton btnThem, btnXoa, btnSua, btnLuu, btnTraCuu, btnLamMoi;
    private JPanel pNorth, pMain;
    private final Consumer<Void> refreshCallback;

    public FrmQLBan(Ban_DAO banDAO, KhuVuc_DAO khuVucDAO, Consumer<Void> refreshCallback) {
        super("Quản Lý Bàn");
        this.banDAO = banDAO;
        this.khuVucDAO = khuVucDAO;
		this.btnLamMoi = new JButton();
        this.refreshCallback = refreshCallback;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menu
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);

        // Panel tiêu đề
        pNorth = new JPanel();
        pNorth.add(new JLabel("QUẢN LÝ BÀN"));
        Font fo = new Font("Times New Roman", Font.BOLD, 28);
        pNorth.getComponent(0).setFont(fo);
        pNorth.getComponent(0).setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        add(pNorth, BorderLayout.NORTH);

        // Panel chính
        pMain = new JPanel(new BorderLayout());
        JPanel pCenter = new JPanel(new BorderLayout());

        // Panel lọc (Bộ Lọc Bàn)
        JPanel pLeft = new JPanel(new GridBagLayout());
        pLeft.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
            "Bộ Lọc Bàn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        pLeft.setBackground(new Color(240, 240, 240));

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 20);
        Font foTxt = new Font("Times New Roman", Font.PLAIN, 20);
        Dimension fieldSize = new Dimension(200, 30);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblMaBan = new JLabel("Mã bàn:");
        lblMaBan.setFont(foBoLoc);
        txtMaBan = new JTextField();
        txtMaBan.setFont(foTxt);
        txtMaBan.setPreferredSize(fieldSize);

        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setFont(foBoLoc);
        cbKhuVuc = new JComboBox<>();
        cbKhuVuc.setFont(foTxt);
        cbKhuVuc.setPreferredSize(fieldSize);
        try {
            List<KhuVuc> khuVucs = khuVucDAO.getAll();
            cbKhuVuc.addItem("Tất cả");
            for (KhuVuc k : khuVucs) {
                cbKhuVuc.addItem(k.getMaKhuVuc() + " - " + k.getTenKhuVuc());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khu vực: " + ex.getMessage());
        }

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(foBoLoc);
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đặt", "Đang phục vụ"});
        cbTrangThai.setFont(foTxt);
        cbTrangThai.setPreferredSize(fieldSize);

        JLabel lblLoaiBan = new JLabel("Loại bàn:");
        lblLoaiBan.setFont(foBoLoc);
        cbLoaiBan = new JComboBox<>(new String[]{"Tất cả", "Thường", "VIP"});
        cbLoaiBan.setFont(foTxt);
        cbLoaiBan.setPreferredSize(fieldSize);

        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(foBoLoc);
        txtGhiChu = new JTextField();
        txtGhiChu.setFont(foTxt);
        txtGhiChu.setPreferredSize(fieldSize);

        JLabel lblSoCho = new JLabel("Số chỗ ngồi:");
        lblSoCho.setFont(foBoLoc);
        txtSoCho = new JTextField();
        txtSoCho.setFont(foTxt);
        txtSoCho.setPreferredSize(fieldSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        pLeft.add(lblMaBan, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5; 
        pLeft.add(txtMaBan, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0; 
        pLeft.add(lblKhuVuc, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5; 
        pLeft.add(cbKhuVuc, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        pLeft.add(lblTrangThai, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(cbTrangThai, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pLeft.add(lblLoaiBan, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        pLeft.add(cbLoaiBan, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        pLeft.add(lblGhiChu, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        pLeft.add(txtGhiChu, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        pLeft.add(lblSoCho, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        pLeft.add(txtSoCho, gbc);

        gbc.gridx = 4;
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

        for (JButton btn : new JButton[]{btnThem, btnXoa, btnSua, btnLuu, btnTraCuu}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(btnFont);
            pRight.add(btn);
            pRight.add(Box.createVerticalStrut(10));
            btn.addActionListener(this);
        }

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
        

        pCenter.add(pLeft, BorderLayout.CENTER);
        pCenter.add(pRight, BorderLayout.EAST);

        // Bảng dữ liệu
        String[] columns = {"Mã bàn", "Khu vực", "Trạng thái", "Ghi chú", "Số chỗ ngồi", "Loại bàn"};
        modelBan = new DefaultTableModel(columns, 0);
        tblBan = new JTable(modelBan);
        tblBan.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblBan.setRowHeight(30);
        tblBan.addMouseListener(this);

        JScrollPane scroll = new JScrollPane(tblBan);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
            "Danh Sách Bàn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.add(scroll, BorderLayout.CENTER);

        pMain.add(pCenter, BorderLayout.NORTH);
        pMain.add(tablePanel, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);

        // Load dữ liệu
        loadData();
    }

    private void loadData() {
        modelBan.setRowCount(0);
        try {
            List<Ban> list = banDAO.getAll();
            for (Ban b : list) {
                modelBan.addRow(new Object[]{
                    b.getMaBan(),
                    b.getTenKhuVuc(),
                    b.getTrangThai(),
                    b.getGhiChu(),
                    b.getSoChoNgoi(),
                    b.getLoaiBan()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void themBan() {
        txtMaBan.setEditable(true);
        txtMaBan.setText("B" + System.currentTimeMillis());
        txtGhiChu.setText("");
        txtSoCho.setText("");
        cbKhuVuc.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        cbLoaiBan.setSelectedIndex(0);
    }

    private void xoaBan() {
        int row = tblBan.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) modelBan.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (banDAO.checkTrung(maBan, new java.sql.Date(System.currentTimeMillis()), new java.sql.Time(System.currentTimeMillis()))) {
                        JOptionPane.showMessageDialog(this, "Không thể xóa bàn đang có lịch đặt!");
                        return;
                    }
                    banDAO.xoaBan(maBan);
                    modelBan.removeRow(row);
                    refreshCallback.accept(null);
                    JOptionPane.showMessageDialog(this, "Xóa bàn thành công!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa bàn: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để xóa!");
        }
    }

    private void suaBan() {
        int row = tblBan.getSelectedRow();
        if (row >= 0) {
            txtMaBan.setEditable(false);
            txtMaBan.setText((String) modelBan.getValueAt(row, 0));
            String khuVuc = (String) modelBan.getValueAt(row, 1);
            try {
                List<KhuVuc> khuVucs = khuVucDAO.getAll();
                for (int i = 0; i < khuVucs.size(); i++) {
                    if (khuVucs.get(i).getTenKhuVuc().equals(khuVuc)) {
                        cbKhuVuc.setSelectedIndex(i + 1);
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cbTrangThai.setSelectedItem(modelBan.getValueAt(row, 2));
            txtGhiChu.setText((String) modelBan.getValueAt(row, 3));
            txtSoCho.setText(modelBan.getValueAt(row, 4).toString());
            cbLoaiBan.setSelectedItem(modelBan.getValueAt(row, 5));
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để sửa!");
        }
    }

    private void luuBan() {
        String maBan = txtMaBan.getText().trim();
        String maKhuVuc = cbKhuVuc.getSelectedItem() != null && cbKhuVuc.getSelectedIndex() > 0 ?
            cbKhuVuc.getSelectedItem().toString().split(" - ")[0] : "";
        String trangThai = cbTrangThai.getSelectedItem().toString();
        String ghiChu = txtGhiChu.getText().trim();
        String soChoStr = txtSoCho.getText().trim();
        String loaiBan = cbLoaiBan.getSelectedItem().toString();

        if (maBan.isEmpty() || maKhuVuc.isEmpty() || soChoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        int soCho;
        try {
            soCho = Integer.parseInt(soChoStr);
            if (soCho <= 0) {
                JOptionPane.showMessageDialog(this, "Số chỗ ngồi phải lớn hơn 0!");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số chỗ ngồi phải là số hợp lệ!");
            return;
        }

        try {
            Ban ban = new Ban();
            ban.setMaBan(maBan);
            ban.setMaKhuVuc(maKhuVuc);
            ban.setTrangThai(trangThai);
            ban.setGhiChu(ghiChu);
            ban.setSoChoNgoi(soCho);
            ban.setLoaiBan(loaiBan);

            if (txtMaBan.isEditable()) {
                if (banDAO.getBanByMa(maBan) != null) {
                    JOptionPane.showMessageDialog(this, "Mã bàn đã tồn tại!");
                    return;
                }
                banDAO.themBan(ban);
                modelBan.addRow(new Object[]{maBan, ban.getTenKhuVuc(), trangThai, ghiChu, soCho, loaiBan});
                JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
            } else {
                banDAO.capNhatBan(ban);
                int row = tblBan.getSelectedRow();
                if (row >= 0) {
                    modelBan.setValueAt(maBan, row, 0);
                    modelBan.setValueAt(ban.getTenKhuVuc(), row, 1);
                    modelBan.setValueAt(trangThai, row, 2);
                    modelBan.setValueAt(ghiChu, row, 3);
                    modelBan.setValueAt(soCho, row, 4);
                    modelBan.setValueAt(loaiBan, row, 5);
                    JOptionPane.showMessageDialog(this, "Cập nhật bàn thành công!");
                }
            }
            refreshCallback.accept(null);
            lamMoiForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu bàn: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void lamMoiForm() {
        txtMaBan.setEditable(true);
        txtMaBan.setText("");
        txtGhiChu.setText("");
        txtSoCho.setText("");
        cbKhuVuc.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        cbLoaiBan.setSelectedIndex(0);
        loadData();
    }

    private void traCuuBan() {
        String maBan = txtMaBan.getText().trim();
        String maKhuVuc = cbKhuVuc.getSelectedIndex() > 0 ? cbKhuVuc.getSelectedItem().toString().split(" - ")[0] : null;
        String trangThai = cbTrangThai.getSelectedIndex() > 0 ? cbTrangThai.getSelectedItem().toString() : null;
        String loaiBan = cbLoaiBan.getSelectedIndex() > 0 ? cbLoaiBan.getSelectedItem().toString() : null;

        try {
            List<Ban> list = banDAO.traCuuBan(maBan, maKhuVuc, trangThai, loaiBan);
            modelBan.setRowCount(0);
            for (Ban b : list) {
                modelBan.addRow(new Object[]{
                    b.getMaBan(),
                    b.getTenKhuVuc(),
                    b.getTrangThai(),
                    b.getGhiChu(),
                    b.getSoChoNgoi(),
                    b.getLoaiBan()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tra cứu bàn: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            themBan();
        } else if (src == btnXoa) {
            xoaBan();
        } else if (src == btnSua) {
            suaBan();
        } else if (src == btnLuu) {
            luuBan();
        } else if (src == btnTraCuu) {
            traCuuBan();
        } 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblBan.getSelectedRow();
        if (row >= 0) {
            txtMaBan.setEditable(false);
            txtMaBan.setText((String) modelBan.getValueAt(row, 0));
            String khuVuc = (String) modelBan.getValueAt(row, 1);
            try {
                List<KhuVuc> khuVucs = khuVucDAO.getAll();
                for (int i = 0; i < khuVucs.size(); i++) {
                    if (khuVucs.get(i).getTenKhuVuc().equals(khuVuc)) {
                        cbKhuVuc.setSelectedIndex(i + 1);
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cbTrangThai.setSelectedItem(modelBan.getValueAt(row, 2));
            txtGhiChu.setText((String) modelBan.getValueAt(row, 3));
            txtSoCho.setText(modelBan.getValueAt(row, 4).toString());
            cbLoaiBan.setSelectedItem(modelBan.getValueAt(row, 5));
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
        Connection conn = ConnectSQL.getConnection();
        Ban_DAO banDAO = new Ban_DAO(conn);
        KhuVuc_DAO khuVucDAO = new KhuVuc_DAO(conn);
        new FrmQLBan(banDAO, khuVucDAO, null).setVisible(true);
    }
}