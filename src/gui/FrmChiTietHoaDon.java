package gui;

import dao.HoaDon_DAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmChiTietHoaDon extends JFrame {
    private JLabel lblTenKH, lblSDT, lblBan, lblNhanVien, lblNgayLap, lblTongTien, lblKhuyenMai;
    private JTable table;
    private DefaultTableModel model;

    public FrmChiTietHoaDon(String maHD) {
        setTitle("Chi tiết hóa đơn");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel pMain = new JPanel(new BorderLayout(10, 10));
        pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Thông tin hóa đơn =====
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
		pTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Thông tin hóa đơn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));

        lblTenKH = new JLabel();
        lblSDT = new JLabel();
        lblBan = new JLabel();
        lblNhanVien = new JLabel();
        lblNgayLap = new JLabel();
        lblKhuyenMai = new JLabel();
        lblTongTien = new JLabel();

        addRow(pTop, "Khách hàng:", lblTenKH);
        addRow(pTop, "SĐT:", lblSDT);
        addRow(pTop, "Bàn:", lblBan);
        addRow(pTop, "Nhân viên lập:", lblNhanVien);
        addRow(pTop, "Ngày lập:", lblNgayLap);
        addRow(pTop, "Khuyến mãi:", lblKhuyenMai);
        addRow(pTop, "Tổng tiền:", lblTongTien);

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
        lblTenKH.setFont(foBoLoc);
        lblSDT.setFont(foBoLoc);
        lblBan.setFont(foBoLoc);
        lblNhanVien.setFont(foBoLoc);
        lblNgayLap.setFont(foBoLoc);
        lblKhuyenMai.setFont(foBoLoc);
        lblTongTien.setFont(foBoLoc);
        
        pMain.add(pTop, BorderLayout.NORTH);

        // ===== Bảng chi tiết món =====
        String[] cols = {"Mã món", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165, 42, 42), 2),"Danh sách món ăn", 0, 0, new Font("Times New Roman", Font.BOLD, 24)));

        pMain.add(scroll, BorderLayout.CENTER);

        add(pMain);

        // ===== Nạp dữ liệu từ DAO =====
        loadData(maHD);
    }

    private void addRow(JPanel panel, String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 25));
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 18)); // 🔹 Font to cho nhãn
        valueLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // 🔹 Font to cho giá trị
        row.add(lbl);
        row.add(valueLabel);
        panel.add(row);
    }


    private void loadData(String maHD) {
        HoaDon_DAO hdDAO = new HoaDon_DAO();
        Object[] thongTin = hdDAO.layThongTinHoaDon(maHD);

        if (thongTin != null) {
            lblTenKH.setText(thongTin[0] + "");
            lblSDT.setText(thongTin[1] + "");
            lblBan.setText(thongTin[2] + "");
            lblNhanVien.setText(thongTin[3] + "");
            lblNgayLap.setText(thongTin[4] + "");
            lblKhuyenMai.setText(thongTin[5] + "");
            lblTongTien.setText(thongTin[6] + "");
        }

        List<Object[]> chiTiet = hdDAO.layChiTietHoaDon(maHD);
        model.setRowCount(0);
        for (Object[] ct : chiTiet) {
            model.addRow(ct);
        }
    }
}
