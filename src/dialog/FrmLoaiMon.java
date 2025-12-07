package dialog;

import dao.LoaiMon_DAO;
import entity.LoaiMon;
import gui.FrmThucDon;
import connectSQL.ConnectSQL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class FrmLoaiMon extends JFrame {

    private JTextField txtMaLoai, txtTenLoai;
    private JButton btnThem, btnSua, btnXoa;
    private JTable table;
    private DefaultTableModel model;
    private LoaiMon_DAO dao;

    public FrmLoaiMon() {
        setTitle("Quản Lý Loại Món");
        setSize(650, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        dao = new LoaiMon_DAO(ConnectSQL.getConnection());
        getContentPane().setBackground(Color.WHITE);

        taoGiaoDien();
        loadDanhSach();

        setVisible(true);
    }

    private void taoGiaoDien() {
        // === FORM NHẬP LIỆU ===
        JPanel pInput = new JPanel();
        pInput.setLayout(new BoxLayout(pInput, BoxLayout.Y_AXIS));
        pInput.setBackground(Color.WHITE);
        pInput.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p1.setBackground(Color.WHITE);
        p1.add(new JLabel("Mã loại:") {{
            setFont(new Font("Times New Roman", Font.BOLD, 20));
        }});
        txtMaLoai = new JTextField(20);
        txtMaLoai.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        txtMaLoai.setEditable(false);
        txtMaLoai.setBackground(new Color(245, 245, 245));
        p1.add(txtMaLoai);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p2.setBackground(Color.WHITE);
        p2.add(new JLabel("Tên loại:") {{
            setFont(new Font("Times New Roman", Font.BOLD, 20));
        }});
        txtTenLoai = new JTextField(20);
        txtTenLoai.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        p2.add(txtTenLoai);

        pInput.add(p1);
        pInput.add(Box.createVerticalStrut(10));
        pInput.add(p2);
        pInput.add(Box.createVerticalStrut(10));

        add(pInput, BorderLayout.NORTH);

        // === BẢNG ===
        model = new DefaultTableModel(new String[]{"Mã loại", "Tên loại", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh Sách Loại Món", 0, 0,
                new Font("Times New Roman", Font.BOLD, 22),
                new Color(165, 42, 42)));

        add(scroll, BorderLayout.CENTER);

        // === NÚT ===
        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pNut.setBackground(Color.WHITE);

        btnThem = taoNut("Thêm", new Color(46, 204, 113));
        btnSua  = taoNut("Sửa",  new Color(52, 152, 219));
        btnXoa  = taoNut("Xóa",   new Color(231, 76, 60));

        pNut.add(btnThem);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        add(pNut, BorderLayout.SOUTH);

        // === SỰ KIỆN ===
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> an());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaLoai.setText(model.getValueAt(row, 0).toString());
                    txtTenLoai.setText(model.getValueAt(row, 1).toString());
                }
            }
        });
    }

    private JButton taoNut(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(110, 38));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    private void loadDanhSach() {
        model.setRowCount(0);
        try {
            List<LoaiMon> list = dao.getAllLoaiMon();
            for (LoaiMon lm : list) {
                model.addRow(new Object[]{
                    lm.getMaLoai(),
                    lm.getTenLoai(),
                    lm.getTrangThaiText()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu loại món!");
        }
    }

    private void them() {
        String ten = txtTenLoai.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại món!");
            return;
        }
        LoaiMon lm = new LoaiMon(null, ten, 1);
        if (dao.themLoaiMon(lm)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadDanhSach();
            txtTenLoai.setText("");
            refreshThucDon();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void sua() {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn và nhập tên loại!");
            return;
        }
        LoaiMon lm = new LoaiMon(ma, ten);
        if (dao.capNhatLoaiMon(lm)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDanhSach();
            refreshThucDon();
        }
    }

    private void an() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại món để xóa!");
            return;
        }
        String ma = model.getValueAt(row, 0).toString();
        String ten = model.getValueAt(row, 1).toString();

        if (JOptionPane.showConfirmDialog(this,
                "Xóa loại món \"" + ten + "\"?\nCác món thuộc loại này sẽ không hiển thị!",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.anLoaiMon(ma)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadDanhSach();
                refreshThucDon();
            }
        }
    }

    private void refreshThucDon() {
        Component parent = getOwner();
        while (parent != null) {
            if (parent instanceof FrmThucDon) {
                ((FrmThucDon) parent).refreshLoaiAndData();
                break;
            }
            parent = parent.getParent();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLoaiMon());
    }
}