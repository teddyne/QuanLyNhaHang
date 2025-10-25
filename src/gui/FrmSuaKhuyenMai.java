package gui;

import dao.KhuyenMai_DAO;
import dao.LoaiKhuyenMai_DAO;
import entity.KhuyenMai;
import entity.LoaiKhuyenMai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;

public class FrmSuaKhuyenMai extends JFrame {
    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu, txtTrangThai;
    private JComboBox<String> cmbLoaiKM, cmbDoiTuongApDung;
    private JSpinner spTuNgay, spDenNgay;
    private JButton btnLuu, btnHuy;
    private KhuyenMai_DAO kmDAO;
    private JPanel pNorth;
    private JLabel lblTieuDe;
    private JLabel lblMa, lblTen, lblLoai, lblGiaTri, lblTuNgay, lblDenNgay, lblTrangThai, lblDonHangTu, lblDoiTuong, lblMon1, lblMon2, lblMonTang, lblGhiChu;

    public FrmSuaKhuyenMai(KhuyenMai km) {
        setTitle("Sửa Khuyến Mãi");
        setSize(770, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        kmDAO = new KhuyenMai_DAO();

        // Panel tiêu đề
        pNorth = new JPanel();
        pNorth.add(lblTieuDe = new JLabel("SỬA KHUYẾN MÃI"));
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTieuDe.setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        add(pNorth, BorderLayout.NORTH);

        // Panel chính với BoxLayout
        JPanel pMain = new JPanel();
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Font và kích thước
        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Font fieldFont = new Font("Times New Roman", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(200, 30);
        Dimension labelSize = new Dimension(110, 30);

        // Dòng 1: Mã KM
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblMa = new JLabel("Mã KM:");
        lblMa.setFont(labelFont);
        lblMa.setPreferredSize(labelSize);
        p1.add(lblMa);
        txtMaKM = new JTextField(18);
        txtMaKM.setFont(fieldFont);
        txtMaKM.setPreferredSize(fieldSize);
        txtMaKM.setEnabled(false);
        txtMaKM.setText(km.getMaKM());
        p1.add(txtMaKM);
        pMain.add(p1);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 2: Tên KM
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTen = new JLabel("Tên KM:");
        lblTen.setFont(labelFont);
        lblTen.setPreferredSize(labelSize);
        p2.add(lblTen);
        txtTenKM = new JTextField(18);
        txtTenKM.setFont(fieldFont);
        txtTenKM.setPreferredSize(fieldSize);
        txtTenKM.setText(km.getTenKM());
        p2.add(txtTenKM);
        pMain.add(p2);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 3: Loại KM + Giá trị
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblLoai = new JLabel("Loại KM:");
        lblLoai.setFont(labelFont);
        lblLoai.setPreferredSize(labelSize);
        p3.add(lblLoai);
        cmbLoaiKM = new JComboBox<>();
        loadLoaiKM();
        cmbLoaiKM.setFont(fieldFont);
        cmbLoaiKM.setPreferredSize(fieldSize);
        p3.add(cmbLoaiKM);
        p3.add(Box.createHorizontalStrut(40));
        lblGiaTri = new JLabel("Giá trị:");
        lblGiaTri.setFont(labelFont);
        lblGiaTri.setPreferredSize(labelSize);
        p3.add(lblGiaTri);
        txtGiaTri = new JTextField(18);
        txtGiaTri.setFont(fieldFont);
        txtGiaTri.setPreferredSize(fieldSize);
        txtGiaTri.setText(String.valueOf(km.getGiaTri()));
        p3.add(txtGiaTri);
        pMain.add(p3);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 4: Từ ngày + Đến ngày
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(labelFont);
        lblTuNgay.setPreferredSize(labelSize);
        p4.add(lblTuNgay);
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay, "yyyy-MM-dd"));
        spTuNgay.setFont(fieldFont);
        spTuNgay.setPreferredSize(fieldSize);
        spTuNgay.setValue(km.getNgayBatDau());
        p4.add(spTuNgay);
        p4.add(Box.createHorizontalStrut(40));
        lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setFont(labelFont);
        lblDenNgay.setPreferredSize(labelSize);
        p4.add(lblDenNgay);
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay, "yyyy-MM-dd"));
        spDenNgay.setFont(fieldFont);
        spDenNgay.setPreferredSize(fieldSize);
        spDenNgay.setValue(km.getNgayKetThuc());
        p4.add(spDenNgay);
        pMain.add(p4);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 5: Trạng thái + Đối tượng
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setPreferredSize(labelSize);
        p5.add(lblTrangThai);
        txtTrangThai = new JTextField(18);
        txtTrangThai.setFont(fieldFont);
        txtTrangThai.setPreferredSize(fieldSize);
        txtTrangThai.setText(km.getTrangThai());
        p5.add(txtTrangThai);
        p5.add(Box.createHorizontalStrut(40));
        lblDoiTuong = new JLabel("Đối tượng:");
        lblDoiTuong.setFont(labelFont);
        lblDoiTuong.setPreferredSize(labelSize);
        p5.add(lblDoiTuong);
        cmbDoiTuongApDung = new JComboBox<>(new String[]{"Tất cả", "Khách thường", "Thành viên"});
        cmbDoiTuongApDung.setFont(fieldFont);
        cmbDoiTuongApDung.setPreferredSize(fieldSize);
        cmbDoiTuongApDung.setSelectedItem(km.getDoiTuongApDung() != null ? km.getDoiTuongApDung() : "Tất cả");
        p5.add(cmbDoiTuongApDung);
        pMain.add(p5);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 6: Đơn hàng từ
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblDonHangTu = new JLabel("Đơn hàng từ:");
        lblDonHangTu.setFont(labelFont);
        lblDonHangTu.setPreferredSize(labelSize);
        p6.add(lblDonHangTu);
        txtDonHangTu = new JTextField(18);
        txtDonHangTu.setFont(fieldFont);
        txtDonHangTu.setPreferredSize(fieldSize);
        txtDonHangTu.setText(String.valueOf(km.getDonHangTu()));
        p6.add(txtDonHangTu);
        pMain.add(p6);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 7: Món 1 + Món 2
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblMon1 = new JLabel("Món 1:");
        lblMon1.setFont(labelFont);
        lblMon1.setPreferredSize(labelSize);
        p7.add(lblMon1);
        txtMon1 = new JTextField(18);
        txtMon1.setFont(fieldFont);
        txtMon1.setPreferredSize(fieldSize);
        txtMon1.setText(km.getMon1() != null ? km.getMon1() : "");
        p7.add(txtMon1);
        p7.add(Box.createHorizontalStrut(40));
        lblMon2 = new JLabel("Món 2:");
        lblMon2.setFont(labelFont);
        lblMon2.setPreferredSize(labelSize);
        p7.add(lblMon2);
        txtMon2 = new JTextField(18);
        txtMon2.setFont(fieldFont);
        txtMon2.setPreferredSize(fieldSize);
        txtMon2.setText(km.getMon2() != null ? km.getMon2() : "");
        p7.add(txtMon2);
        pMain.add(p7);
        pMain.add(Box.createVerticalStrut(10));

        // Dòng 8: Món tặng + Ghi chú
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblMonTang = new JLabel("Món tặng:");
        lblMonTang.setFont(labelFont);
        lblMonTang.setPreferredSize(labelSize);
        p8.add(lblMonTang);
        txtMonTang = new JTextField(18);
        txtMonTang.setFont(fieldFont);
        txtMonTang.setPreferredSize(fieldSize);
        txtMonTang.setText(km.getMonTang() != null ? km.getMonTang() : "");
        p8.add(txtMonTang);
        p8.add(Box.createHorizontalStrut(40));
        lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(labelFont);
        lblGhiChu.setPreferredSize(labelSize);
        p8.add(lblGhiChu);
        txtGhiChu = new JTextField(18);
        txtGhiChu.setFont(fieldFont);
        txtGhiChu.setPreferredSize(fieldSize);
        txtGhiChu.setText(km.getGhiChu() != null ? km.getGhiChu() : "");
        p8.add(txtGhiChu);
        pMain.add(p8);
        pMain.add(Box.createVerticalStrut(20));

        // Dòng 9: Nút
        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(100, 35);
        btnLuu = taoNut("Lưu", new Color(46, 204, 113), buttonSize, buttonFont);
        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
        p9.add(btnHuy);
        p9.add(Box.createHorizontalStrut(20));
        p9.add(btnLuu);
        pMain.add(p9);

        add(pMain, BorderLayout.CENTER);

        pMain.setBackground(Color.WHITE);
        p1.setBackground(Color.WHITE);
        p2.setBackground(Color.WHITE);
        p3.setBackground(Color.WHITE);
        p4.setBackground(Color.WHITE);
        p5.setBackground(Color.WHITE);
        p6.setBackground(Color.WHITE);
        p7.setBackground(Color.WHITE);
        p8.setBackground(Color.WHITE);
        p9.setBackground(Color.WHITE);
        // Tải dữ liệu ban đầu
        LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
        String tenLoai = loaiDAO.getMaLoaiByTenLoai(km.getMaLoai());
        cmbLoaiKM.setSelectedItem(tenLoai != null ? tenLoai : "");

        // Thêm ItemListener cho cmbLoaiKM để xử lý ràng buộc
        cmbLoaiKM.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                capNhatTrangThaiField();
            }
        });

        // Cập nhật trạng thái ban đầu
        capNhatTrangThaiField();

        // Sự kiện nút
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> luuKhuyenMai());

        // Tải danh sách loại khuyến mãi
        loadLoaiKM();

        setVisible(true);
    }

    private void capNhatTrangThaiField() {
        String loai = cmbLoaiKM.getSelectedItem() != null ? cmbLoaiKM.getSelectedItem().toString() : "";
        if (loai.equals("Tặng món")) {
            txtGiaTri.setEnabled(false);
            txtDonHangTu.setEnabled(false);
            txtMon1.setEnabled(true);
            txtMon2.setEnabled(true);
            txtMonTang.setEnabled(true);
            txtGiaTri.setText("");
            txtDonHangTu.setText("");
        } else {
            txtGiaTri.setEnabled(true);
            txtDonHangTu.setEnabled(true);
            txtMon1.setEnabled(false);
            txtMon2.setEnabled(false);
            txtMonTang.setEnabled(false);
            txtMon1.setText("");
            txtMon2.setText("");
            txtMonTang.setText("");
        }
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

    private void loadLoaiKM() {
        try {
            cmbLoaiKM.removeAllItems();
            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
            for (LoaiKhuyenMai loai : loaiDAO.getAllLoaiKhuyenMai()) {
                cmbLoaiKM.addItem(loai.getTenLoai());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải loại KM: " + ex.getMessage());
        }
    }

    private void luuKhuyenMai() {
        try {
            Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
            Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());
            if (kt.before(bd)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            // Lấy maLoai từ tenLoai
            String tenLoai = cmbLoaiKM.getSelectedItem().toString();
            LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
            String maLoai = loaiDAO.getMaLoaiByTenLoai(tenLoai);

            if (maLoai == null) {
                JOptionPane.showMessageDialog(this, "Loại khuyến mãi không hợp lệ!");
                return;
            }

            double giaTri = 0;
            double donHangTu = 0;
            String mon1 = txtMon1.getText().isEmpty() ? null : txtMon1.getText();
            String mon2 = txtMon2.getText().isEmpty() ? null : txtMon2.getText();
            String monTang = txtMonTang.getText().isEmpty() ? null : txtMonTang.getText();

            if (!tenLoai.equals("Tặng món")) {
                try {
                    giaTri = Double.parseDouble(txtGiaTri.getText());
                    donHangTu = txtDonHangTu.getText().isEmpty() ? 0 : Double.parseDouble(txtDonHangTu.getText());
                    mon1 = null;
                    mon2 = null;
                    monTang = null;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Giá trị và đơn hàng từ phải là số!");
                    return;
                }
            } else {
                if (monTang == null || monTang.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Món tặng không được để trống khi loại là Tặng món!");
                    return;
                }
            }

            KhuyenMai km = new KhuyenMai(
                    txtMaKM.getText(),
                    txtTenKM.getText(),
                    maLoai,
                    giaTri,
                    bd,
                    kt,
                    txtTrangThai.getText(),
                    cmbDoiTuongApDung.getSelectedItem().toString(),
                    donHangTu,
                    mon1,
                    mon2,
                    monTang,
                    txtGhiChu.getText().isEmpty() ? null : txtGhiChu.getText()
            );

            if (kmDAO.suaKhuyenMai(km)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
}