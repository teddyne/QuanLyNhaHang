// gui/FrmLoaiMon.java
package gui;

import dao.LoaiMon_DAO;
import entity.LoaiMon;
import connectSQL.ConnectSQL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class FrmLoaiMon extends ThanhTacVu {
    private final LoaiMon_DAO loaiDAO;
    private final DefaultTableModel model;
    private final JTable tblLoaiMon;
    private final JTextField txtMaLoai, txtTenLoai;
    private final JButton btnThem, btnCapNhat, btnAn, btnTraCuu;
    private final Consumer<Void> refreshCallback;

    public FrmLoaiMon() throws SQLException { this(null, null); }
    public FrmLoaiMon(Consumer<Void> refreshCallback) throws SQLException { this(null, refreshCallback); }

    
    public FrmLoaiMon(LoaiMon_DAO loaiDAO, Consumer<Void> refreshCallback) throws SQLException {
        super(); setTitle("Quản Lý Loại Món");
        Connection conn = ConnectSQL.getConnection();
        this.loaiDAO = loaiDAO != null ? loaiDAO : new LoaiMon_DAO(conn);
        this.refreshCallback = refreshCallback;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Tiêu đề
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(169, 55, 68));
        JLabel lblTieuDe = new JLabel("QUẢN LÝ LOẠI MÓN");
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.WHITE);
        titlePanel.add(lblTieuDe);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Form nhập
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Thông Tin Loại Món", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        inputPanel.setPreferredSize(new Dimension(0, 180));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Times New Roman", Font.BOLD, 22);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 18);

        // Mã loại
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Mã loại món") {{ setFont(labelFont); }}, gbc);
        txtMaLoai = new JTextField(15); txtMaLoai.setFont(fieldFont); txtMaLoai.setEditable(false);
        gbc.gridx = 1; fieldsPanel.add(txtMaLoai, gbc);

        // Tên loại
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Tên loại món") {{ setFont(labelFont); }}, gbc);
        txtTenLoai = new JTextField(15); txtTenLoai.setFont(fieldFont);
        gbc.gridx = 1; fieldsPanel.add(txtTenLoai, gbc);

        // Nút
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension btnSize = new Dimension(150, 50);

        btnThem = taoNut("Thêm", new Color(46, 204, 113), btnSize, btnFont);
        btnCapNhat = taoNut("Cập nhật", new Color(52, 152, 219), btnSize, btnFont);
        btnAn = taoNut("Xóa", new Color(231, 76, 60), btnSize, btnFont);
        btnTraCuu = taoNut("Tra cứu", new Color(121, 89, 229), btnSize, btnFont);

        buttonPanel.add(btnThem); buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnAn); buttonPanel.add(btnTraCuu);

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Bảng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Loại Món", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));
        tablePanel.setBackground(Color.WHITE);

        String[] cols = {"Mã loại món", "Tên loại món", "Trạng thái"};
        model = new DefaultTableModel(cols, 0);
        tblLoaiMon = new JTable(model);
        tblLoaiMon.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tblLoaiMon.setRowHeight(30);
        tablePanel.add(new JScrollPane(tblLoaiMon), BorderLayout.CENTER);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        loadData();

        // Sự kiện
        btnThem.addActionListener(e -> them());
        btnCapNhat.addActionListener(e -> capNhat());
        btnAn.addActionListener(e -> an());
        btnTraCuu.addActionListener(e -> traCuu());

        tblLoaiMon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblLoaiMon.getSelectedRow();
                if (r >= 0) {
                    txtMaLoai.setText(model.getValueAt(r, 0).toString());
                    txtTenLoai.setText(model.getValueAt(r, 1).toString());
                }
            }
        });
    }

    private JButton taoNut(String text, Color c, Dimension s, Font f) {
        JButton b = new JButton(text); b.setFont(f); b.setPreferredSize(s);
        b.setForeground(Color.WHITE); b.setBackground(c); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(false); b.setOpaque(true);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(c.darker()); }
            public void mouseExited(MouseEvent e) { b.setBackground(c); }
        });
        return b;
    }

    private void loadData() {
        model.setRowCount(0);
        List<LoaiMon> list = loaiDAO.getAllLoaiMon();
        for (LoaiMon lm : list) {
            model.addRow(new Object[]{
                lm.getMaLoai(),
                lm.getTenLoai(),
                lm.getTrangThaiText()
            });
        }
    }

    private void resetForm() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtTenLoai.requestFocus(); 
        tblLoaiMon.clearSelection();
    }


    private void them() {
        String ten = txtTenLoai.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại món!");
            txtTenLoai.requestFocus();
            return;
        }

        try {
            LoaiMon lm = new LoaiMon();
            lm.setTenLoai(ten);
            lm.setTrangThai(1);

            boolean kq = loaiDAO.themLoaiMon(lm);
            if (kq) {
                loadData();
                JOptionPane.showMessageDialog(this, "Thêm thành công! Mã mới: " + lm.getMaLoai());
                resetForm(); 
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void capNhat() {
        String ten = txtTenLoai.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại món!");
            return;
        }

        try {
            if (txtMaLoai.getText().isEmpty()) {
                // THÊM MỚI
                LoaiMon lm = new LoaiMon("", ten, 1);
                if (loaiDAO.themLoaiMon(lm)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Thêm thành công! Mã: " + lm.getMaLoai());
                    resetForm(); // 
                    if (refreshCallback != null) refreshCallback.accept(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } else {
                // CẬP NHẬT
                LoaiMon lm = new LoaiMon(txtMaLoai.getText(), ten);
                if (loaiDAO.capNhatLoaiMon(lm)) {
                    int r = tblLoaiMon.getSelectedRow();
                    if (r >= 0) {
                        model.setValueAt(ten, r, 1);
                        model.setValueAt(lm.getTrangThaiText(), r, 2);
                    }
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    resetForm(); 
                    if (refreshCallback != null) refreshCallback.accept(null);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void an() {
        int r = tblLoaiMon.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại món để xóa!");
            return;
        }
        String ma = model.getValueAt(r, 0).toString();
        if (JOptionPane.showConfirmDialog(this, "Xóa loại món '" + ma + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (loaiDAO.anLoaiMon(ma)) {
                model.removeRow(r);
                JOptionPane.showMessageDialog(this, "Đã xóa loại món!");
                resetForm(); 
                if (refreshCallback != null) refreshCallback.accept(null);
            }
        }
    }


    private void traCuu() {
        String ten = txtTenLoai.getText().trim();
        model.setRowCount(0);
        List<LoaiMon> list = ten.isEmpty() ? loaiDAO.getAllLoaiMon() : loaiDAO.timLoaiMonTheoTen(ten);
        for (LoaiMon lm : list) {
            model.addRow(new Object[]{lm.getMaLoai(), lm.getTenLoai(), lm.getTrangThaiText()});
        }
        if (list.isEmpty() && !ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy!");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // SỬA: Dùng Nimbus thay vì getSystemLookAndFeel()
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

                FrmLoaiMon frm = new FrmLoaiMon();
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.setSize(1000, 650);
                frm.setLocationRelativeTo(null);
                frm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi giao diện: " + e.getMessage());
            }
        });
    }
}