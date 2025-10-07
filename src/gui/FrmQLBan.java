package gui;

import dao.Ban_DAO;
import dao.KhuVuc_DAO;
import entity.Ban;
import entity.KhuVuc;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.function.Consumer;

public class FrmQLBan extends JDialog {
    private Ban_DAO banDAO;
    private KhuVuc_DAO khuVucDAO;
    private DefaultTableModel model;
    private JTextField txtMa, txtGhiChu, txtSoCho;
    private JComboBox<String> cbKhuVuc, cbLoai, cbTrangThai;
    private Consumer<Void> refreshCallback;

    public FrmQLBan(JFrame parent, Ban_DAO banDAO, KhuVuc_DAO khuVucDAO, Consumer<Void> refreshCallback) {
        super(parent, "Quản lý bàn", true);
        this.banDAO = banDAO;
        this.khuVucDAO = khuVucDAO;
        this.refreshCallback = refreshCallback;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(950, 900);
        setLocationRelativeTo(getParent());
        setLayout(null);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ BÀN", SwingConstants.CENTER);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(new Color(128, 0, 0)); // Đỏ rượu
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        lblTieuDe.setBounds(0, 0, 950, 60);
        add(lblTieuDe);

        // Panel thông tin bàn
        JPanel pForm = new JPanel(null);
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin bàn"));
        pForm.setBounds(10, 70, 930, 300);
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        JLabel lblMa = new JLabel("Mã bàn:");
        lblMa.setFont(labelFont);
        lblMa.setBounds(20, 40, 200, 25);
        txtMa = new JTextField();
        txtMa.setBounds(180, 30, 300, 35);

        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setFont(labelFont);
        lblKhuVuc.setBounds(20, 85, 200, 25);
        cbKhuVuc = new JComboBox<>();
        cbKhuVuc.setBounds(180, 75, 300, 35);
        try {
            List<KhuVuc> khuVucs = khuVucDAO.getAll();
            if (khuVucs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có khu vực nào trong cơ sở dữ liệu!");
            }
            for (KhuVuc k : khuVucs) {
                cbKhuVuc.addItem(k.getMaKhuVuc() + " - " + k.getTenKhuVuc());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khu vực: " + ex.getMessage());
            ex.printStackTrace();
        }

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setBounds(20, 130, 200, 25);
        cbTrangThai = new JComboBox<>(new String[]{"Trống", "Đặt", "Đang phục vụ"});
        cbTrangThai.setBounds(180, 120, 300, 35);

        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(labelFont);
        lblGhiChu.setBounds(20, 175, 200, 25);
        txtGhiChu = new JTextField();
        txtGhiChu.setBounds(180, 165, 300, 35);

        JLabel lblSoCho = new JLabel("Số chỗ ngồi:");
        lblSoCho.setFont(labelFont);
        lblSoCho.setBounds(20, 220, 200, 25);
        txtSoCho = new JTextField();
        txtSoCho.setBounds(180, 210, 300, 35);

        JLabel lblLoai = new JLabel("Loại bàn:");
        lblLoai.setFont(labelFont);
        lblLoai.setBounds(20, 265, 200, 25);
        cbLoai = new JComboBox<>(new String[]{"Thường", "VIP"});
        cbLoai.setBounds(180, 255, 300, 35);

        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(550, 30, 120, 35);
        btnThem.setBackground(new Color(46, 204, 113)); // Xanh lá
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBounds(550, 75, 120, 35);
        btnXoa.setBackground(new Color(230, 126, 34)); // Cam
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSua = new JButton("Sửa");
        btnSua.setBounds(550, 120, 120, 35);
        btnSua.setBackground(new Color(192, 57, 43)); // Đỏ
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBounds(550, 165, 120, 35);
        btnLuu.setBackground(new Color(52, 152, 219)); // Xanh dương
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 18));

        pForm.add(lblMa);
        pForm.add(txtMa);
        pForm.add(lblKhuVuc);
        pForm.add(cbKhuVuc);
        pForm.add(lblTrangThai);
        pForm.add(cbTrangThai);
        pForm.add(lblGhiChu);
        pForm.add(txtGhiChu);
        pForm.add(lblSoCho);
        pForm.add(txtSoCho);
        pForm.add(lblLoai);
        pForm.add(cbLoai);
        pForm.add(btnThem);
        pForm.add(btnXoa);
        pForm.add(btnSua);
        pForm.add(btnLuu);
        add(pForm);

     // Bảng danh sách bàn
        model = new DefaultTableModel(
            new Object[]{"Mã bàn", "Khu vực", "Trạng thái", "Ghi chú", "Số chỗ ngồi", "Loại bàn"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 380, 930, 450);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách bàn"));
        add(scroll);

        // === Thêm mouse click vào bảng ===
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMa.setEditable(false);
                    txtMa.setText((String) model.getValueAt(row, 0));

                    String khuVuc = (String) model.getValueAt(row, 1);
                    try {
                        List<KhuVuc> khuVucs = khuVucDAO.getAll();
                        for (int i = 0; i < khuVucs.size(); i++) {
                            if (khuVucs.get(i).getTenKhuVuc().equals(khuVuc)) {
                                cbKhuVuc.setSelectedIndex(i);
                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    String trangThai = (String) model.getValueAt(row, 2);
                    cbTrangThai.setSelectedItem(trangThai);
                    txtGhiChu.setText((String) model.getValueAt(row, 3));
                    txtSoCho.setText(model.getValueAt(row, 4).toString());
                    cbLoai.setSelectedItem(model.getValueAt(row, 5));
                }
            }
        });
        

        // Sự kiện nút
        btnThem.addActionListener(e -> themBan());
        btnXoa.addActionListener(e -> xoaBan(table));
        btnSua.addActionListener(e -> suaBan(table));
        btnLuu.addActionListener(e -> luuBan(table));
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Ban> list = banDAO.getAllBan(null); // Lấy tất cả bàn
            for (Ban b : list) {
                model.addRow(new Object[]{
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
        txtMa.setEditable(true);
        txtMa.setText("B" + System.currentTimeMillis());
        txtSoCho.setText("");
        txtGhiChu.setText("");
        cbKhuVuc.setSelectedIndex(0);
        cbLoai.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
    }

    private void xoaBan(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Kiểm tra xem bàn có lịch đặt không
                    Date today = new Date(System.currentTimeMillis());
                    if (banDAO.checkTrung(maBan, today, new Time(System.currentTimeMillis()))) {
                        JOptionPane.showMessageDialog(this, "Không thể xóa bàn đang có lịch đặt!");
                        return;
                    }
                    banDAO.xoaBan(maBan);
                    model.removeRow(row);
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

    private void suaBan(JTable table) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtMa.setEditable(false);
            txtMa.setText((String) model.getValueAt(row, 0));
            String khuVuc = (String) model.getValueAt(row, 1);
            try {
                List<KhuVuc> khuVucs = khuVucDAO.getAll();
                for (int i = 0; i < khuVucs.size(); i++) {
                    if (khuVucs.get(i).getTenKhuVuc().equals(khuVuc)) {
                        cbKhuVuc.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cbTrangThai.setSelectedItem(model.getValueAt(row, 2));
            txtGhiChu.setText((String) model.getValueAt(row, 3));
            txtSoCho.setText(model.getValueAt(row, 4).toString());
            cbLoai.setSelectedItem(model.getValueAt(row, 5));
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để sửa!");
        }
    }

    private void luuBan(JTable table) {
        String maBan = txtMa.getText().trim();
        String maKhuVuc = cbKhuVuc.getSelectedItem() != null ? cbKhuVuc.getSelectedItem().toString().split(" - ")[0] : "";
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String ghiChu = txtGhiChu.getText().trim();
        String soChoStr = txtSoCho.getText().trim();
        String loaiBan = (String) cbLoai.getSelectedItem();

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

            if (txtMa.isEditable()) {
                // Thêm mới
                if (banDAO.getBanByMa(maBan) != null) {
                    JOptionPane.showMessageDialog(this, "Mã bàn đã tồn tại!");
                    return;
                }
                banDAO.themBan(ban);
                model.addRow(new Object[]{maBan, ban.getTenKhuVuc(), trangThai, ghiChu, soCho, loaiBan});
                JOptionPane.showMessageDialog(this, "Thêm bàn thành công!");
            } else {
                // Cập nhật
                banDAO.capNhatBan(ban);
                int row = table.getSelectedRow();
                if (row >= 0) {
                    model.setValueAt(maBan, row, 0);
                    model.setValueAt(ban.getTenKhuVuc(), row, 1);
                    model.setValueAt(trangThai, row, 2);
                    model.setValueAt(ghiChu, row, 3);
                    model.setValueAt(soCho, row, 4);
                    model.setValueAt(loaiBan, row, 5);
                    JOptionPane.showMessageDialog(this, "Cập nhật bàn thành công!");
                }
            }
            refreshCallback.accept(null);
            txtMa.setEditable(true);
            txtMa.setText("");
            txtSoCho.setText("");
            txtGhiChu.setText("");
            cbKhuVuc.setSelectedIndex(0);
            cbLoai.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu bàn: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}