package gui;

import dao.*;
import entity.KhuyenMai;
import entity.PhieuDatBan;
import connectSQL.ConnectSQL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class FrmThanhToan extends JFrame implements ActionListener {
    private String maHD;
    private String banDangChon; // THÊM: nhận từ FrmBan
    private Runnable onThanhToanThanhCong; // THÊM: callback

    private JLabel lblTieuDe;
    private JLabel lblKH, lblSDT, lblNgayLap, lblBan, lblNV;
    private JLabel valKH, valSDT, valNgayLap, valBan, valNV;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongCong, lblTongHD, lblKM, lblCoc, lblVAT, lblTienThanhToan, lblTienNhan, lblThua, lblPhuThu;
    private JLabel valTongCong, valTongHD, valCoc, valVAT, valTienThanhToan, valThua;
    private JTextField txtTienNhan, txtPhuThu;
    private JCheckBox chkPhuThu;
    private JComboBox<KhuyenMai> cmbKhuyenMai;
    private JButton btnQuayLai, btnHuy, btnXacNhan;
    private DecimalFormat df = new DecimalFormat("#,##0 VND");
    private HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
    private LoaiKhuyenMai_DAO loaiDAO = new LoaiKhuyenMai_DAO();
    private double tongCong;
    private double giamGia;
    private double phuThu;
    private JLabel lblDonViTien;
    private JLabel lblDonVi;
    private final KhuyenMai NO_PROMO = createNoPromo();

    private FrmHoaDon parent;
    private FrmPhucVu parentPhucVu;

    // CONSTRUCTOR MỚI: DÙNG KHI GỌI TỪ FrmBan
    public FrmThanhToan(String maHD, String banDangChon, Runnable onThanhToanThanhCong) {
        this.maHD = maHD;
        this.banDangChon = banDangChon;
        this.onThanhToanThanhCong = onThanhToanThanhCong;
        initComponents();
        loadDuLieuHoaDon(maHD);
        loadChiTietHoaDon(maHD);
        loadKhuyenMaiTotNhat();
    }

    // Các constructor cũ (giữ lại nếu cần)
    public FrmThanhToan(String maHD) {
        this(maHD, null, null);
    }
    public FrmThanhToan(String maHD, FrmHoaDon parent) {
        this(maHD, null, null);
        this.parent = parent;
    }
    public FrmThanhToan(String maHD, FrmPhucVu parentPhucVu) {
        this(maHD, null, null);
        this.parentPhucVu = parentPhucVu;
    }

		private void initComponents() {
        setTitle("Thanh toán");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Tiêu đề ===
        JPanel pNorth = new JPanel();
        pNorth.add(lblTieuDe = new JLabel("THANH TOÁN"));
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 26));
        lblTieuDe.setForeground(Color.WHITE);
        pNorth.setBackground(new Color(169, 55, 68));
        add(pNorth, BorderLayout.NORTH);

        // === Panel chính ===
        JPanel pMain = new JPanel(new BorderLayout());
        pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colNames = {"STT", "Món", "Số lượng", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(colNames, 0);
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        table.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        pMain.add(scroll, BorderLayout.CENTER);

        // === Hàng dưới: 2 cột (Thông tin và Tổng hóa đơn) ===
        JPanel pBottom = new JPanel(new GridLayout(1, 2, 10, 10));
        lblDonViTien = new JLabel("VND");
        lblDonVi = new JLabel("VND");

        // === Cột trái: Thông tin hóa đơn ===
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Thông tin hóa đơn", 0, 0, new Font("Times New Roman", Font.BOLD, 22)));

        Font foBoLoc = new Font("Times New Roman", Font.BOLD, 18);
        Font foTxt = new Font("Times New Roman", Font.PLAIN, 18);
        lblDonViTien.setFont(foTxt);
        lblDonVi.setFont(foTxt);

        // Khách hàng
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblKH = new JLabel("Khách hàng:");
        lblKH.setFont(foBoLoc);
        lblKH.setPreferredSize(new Dimension(110, 30));
        valKH = new JLabel("");
        valKH.setFont(foTxt);
        p1.add(lblKH);
        p1.add(valKH);
        pLeft.add(p1);

        // SĐT khách
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblSDT = new JLabel("SĐT khách:");
        lblSDT.setFont(foBoLoc);
        lblSDT.setPreferredSize(new Dimension(110, 30));
        valSDT = new JLabel("");
        valSDT.setFont(foTxt);
        p2.add(lblSDT);
        p2.add(valSDT);
        pLeft.add(p2);

        // Ngày lập
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblNgayLap = new JLabel("Ngày lập:");
        lblNgayLap.setFont(foBoLoc);
        lblNgayLap.setPreferredSize(new Dimension(110, 30));
        valNgayLap = new JLabel("");
        valNgayLap.setFont(foTxt);
        p3.add(lblNgayLap);
        p3.add(valNgayLap);
        pLeft.add(p3);

        // Bàn
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p4.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblBan = new JLabel("Bàn:");
        lblBan.setFont(foBoLoc);
        lblBan.setPreferredSize(new Dimension(110, 30));
        valBan = new JLabel("");
        valBan.setFont(foTxt);
        p4.add(lblBan);
        p4.add(valBan);
        pLeft.add(p4);

        // Nhân viên
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p5.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblNV = new JLabel("Nhân viên:");
        lblNV.setFont(foBoLoc);
        lblNV.setPreferredSize(new Dimension(110, 30));
        valNV = new JLabel("");
        valNV.setFont(foTxt);
        p5.add(lblNV);
        p5.add(valNV);
        pLeft.add(p5);
        pLeft.add(Box.createVerticalGlue());

        // === Cột phải: Tổng hóa đơn ===
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Tổng hóa đơn", 0, 0, new Font("Times New Roman", Font.BOLD, 22)));

        // Tổng cộng
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p6.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTongCong = new JLabel("Tổng cộng:");
        lblTongCong.setFont(foBoLoc);
        lblTongCong.setPreferredSize(new Dimension(150, 30));
        valTongCong = new JLabel("");
        valTongCong.setFont(foTxt);
        p6.add(lblTongCong);
        p6.add(valTongCong);
        pRight.add(p6);

        // Khuyến mãi
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p7.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblKM = new JLabel("Khuyến mãi:");
        lblKM.setFont(foBoLoc);
        lblKM.setPreferredSize(new Dimension(150, 30));
        cmbKhuyenMai = new JComboBox<>();
        cmbKhuyenMai.setFont(foTxt);
        cmbKhuyenMai.setPreferredSize(new Dimension(215, 30));
        cmbKhuyenMai.addActionListener(this);
        p7.add(lblKM);
        p7.add(cmbKhuyenMai);
        pRight.add(p7);

        // Phụ thu
        JPanel pPhuThu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        pPhuThu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblPhuThu = new JLabel("Phụ thu:");
        lblPhuThu.setFont(foBoLoc);
        lblPhuThu.setPreferredSize(new Dimension(150, 30));
        chkPhuThu = new JCheckBox();
        chkPhuThu.setFont(foTxt);
        txtPhuThu = new JTextField(15);
        txtPhuThu.setFont(foTxt);
        txtPhuThu.setPreferredSize(new Dimension(200, 30));
        txtPhuThu.setEnabled(false); // Ban đầu vô hiệu hóa

        pPhuThu.add(lblPhuThu);
        pPhuThu.add(txtPhuThu);
        pPhuThu.add(lblDonVi);
        pPhuThu.add(chkPhuThu);
        pRight.add(pPhuThu);

        // Sự kiện cho checkbox Phụ thu
        chkPhuThu.addItemListener(e -> {
            txtPhuThu.setEnabled(chkPhuThu.isSelected());
            if (!chkPhuThu.isSelected()) {
                txtPhuThu.setText("");
                phuThu = 0;
            } else {
                try {
                    phuThu = txtPhuThu.getText().isEmpty() ? 0 : Double.parseDouble(txtPhuThu.getText().trim());
                } catch (NumberFormatException ex) {
                    phuThu = 0;
                }
            }
            tinhTienThua(); // Cập nhật tiền thừa khi thay đổi trạng thái phụ thu
        });

        // Sự kiện nhập liệu cho txtPhuThu
        txtPhuThu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    phuThu = chkPhuThu.isSelected() && !txtPhuThu.getText().isEmpty() ? Double.parseDouble(txtPhuThu.getText().trim()) : 0;
                } catch (NumberFormatException ex) {
                    phuThu = 0;
                }
                tinhTienThua(); // Cập nhật tiền thừa khi nhập phụ thu
            }
        });

        // Đã cọc
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p8.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblCoc = new JLabel("Đã cọc:");
        lblCoc.setFont(foBoLoc);
        lblCoc.setPreferredSize(new Dimension(150, 30));
        valCoc = new JLabel("");
        valCoc.setFont(foTxt);
        p8.add(lblCoc);
        p8.add(valCoc);
        pRight.add(p8);

        // Thuế VAT
        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p9.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblVAT = new JLabel("Thuế VAT:");
        lblVAT.setFont(foBoLoc);
        lblVAT.setPreferredSize(new Dimension(150, 30));
        valVAT = new JLabel("8%");
        valVAT.setFont(foTxt);
        p9.add(lblVAT);
        p9.add(valVAT);
        pRight.add(p9);

        // Tổng hóa đơn
        JPanel p10 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p10.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTongHD = new JLabel("Tổng hóa đơn:");
        lblTongHD.setFont(foBoLoc);
        lblTongHD.setPreferredSize(new Dimension(150, 30));
        valTongHD = new JLabel("");
        valTongHD.setFont(foTxt);
        p10.add(lblTongHD);
        p10.add(valTongHD);
        pRight.add(p10);

        // Tiền thanh toán
        JPanel p11 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p11.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTienThanhToan = new JLabel("Tiền thanh toán:");
        lblTienThanhToan.setFont(foBoLoc);
        lblTienThanhToan.setPreferredSize(new Dimension(150, 30));
        valTienThanhToan = new JLabel("");
        valTienThanhToan.setFont(foTxt);
        p11.add(lblTienThanhToan);
        p11.add(valTienThanhToan);
        pRight.add(p11);

        // Tiền khách đưa
        JPanel p12 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p12.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblTienNhan = new JLabel("Tiền khách đưa:");
        lblTienNhan.setFont(foBoLoc);
        lblTienNhan.setPreferredSize(new Dimension(150, 30));
        txtTienNhan = new JTextField(15);
        txtTienNhan.setFont(foTxt);
        txtTienNhan.setPreferredSize(new Dimension(200, 30));
        txtTienNhan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTienThua();
            }
        });
        p12.add(lblTienNhan);
        p12.add(txtTienNhan);
        p12.add(lblDonViTien);
        pRight.add(p12);

        // Tiền thừa
        JPanel p13 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        p13.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lblThua = new JLabel("Tiền thừa:");
        lblThua.setFont(foBoLoc);
        lblThua.setPreferredSize(new Dimension(150, 30));
        valThua = new JLabel("");
        valThua.setFont(foTxt);
        p13.add(lblThua);
        p13.add(valThua);
        pRight.add(p13);
        pRight.add(Box.createVerticalGlue());

        pBottom.add(pLeft);
        pBottom.add(pRight);
        pMain.add(pBottom, BorderLayout.SOUTH);
        add(pMain, BorderLayout.CENTER);

        // === Nút chức năng ===
        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 16);
        Dimension btnSize = new Dimension(150, 50);
        Dimension buttonSize = new Dimension(100, 35);

        btnQuayLai = taoNut("Quay lại", new Color(255, 193, 7), buttonSize, buttonFont);
        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
        btnXacNhan = taoNut("Xác nhận", new Color(46, 204, 113), buttonSize, buttonFont);

        for (JButton btn : new JButton[]{btnQuayLai, btnHuy, btnXacNhan}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
        }
        pSouth.add(btnQuayLai);
        pSouth.add(Box.createHorizontalStrut(350));
        pSouth.add(btnHuy);
        pSouth.add(Box.createHorizontalStrut(20));
        pSouth.add(btnXacNhan);
        add(pSouth, BorderLayout.SOUTH);

        // background
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
        p10.setBackground(Color.WHITE);
        p11.setBackground(Color.WHITE);
        p12.setBackground(Color.WHITE);
        p13.setBackground(Color.WHITE);
        pPhuThu.setBackground(Color.WHITE);
        pBottom.setBackground(Color.WHITE);
        pLeft.setBackground(Color.WHITE);
        pRight.setBackground(Color.WHITE);
        pSouth.setBackground(Color.WHITE);

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
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    private KhuyenMai createNoPromo() {
        KhuyenMai k = new KhuyenMai();
        k.setMaKM(null);
        k.setTenKM("Không có khuyến mãi phù hợp");
        return k;
    }

    private void loadKhuyenMai() {
        try {
            List<KhuyenMai> dsKM = kmDAO.layDanhSachKhuyenMai();
            Collections.sort(dsKM, Comparator.comparing(KhuyenMai::getTenKM));
            cmbKhuyenMai.removeAllItems();
            cmbKhuyenMai.addItem(NO_PROMO); // mặc định: không chọn KM
            for (KhuyenMai km : dsKM) {
                cmbKhuyenMai.addItem(km);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khuyến mãi: " + e.getMessage());
        }
    }

    private void loadKhuyenMaiTotNhat() {
        try {
            // Lấy danh sách khuyến mãi hợp lệ từ DAO (chưa lọc chi tiết món)
            List<KhuyenMai> dsKM = kmDAO.getDanhSachKhuyenMaiHopLe(maHD, tongCong);

            cmbKhuyenMai.removeAllItems();
            giamGia = 0;

            if (dsKM == null || dsKM.isEmpty()) {
                cmbKhuyenMai.addItem(NO_PROMO);
                tinhTienThua();
                return;
            }

            List<KhuyenMai> dsKMThucSu = new ArrayList<>();
            List<Object[]> chiTietHD = hoaDonDAO.layChiTietHoaDon(maHD);

            for (KhuyenMai km : dsKM) {
                if ("L03".equalsIgnoreCase(km.getMaLoai())) { // Tặng món
                    int slMon1 = 0, slMon2 = 0;
                    for (Object[] row : chiTietHD) {
                        String maMon = (String) row[0];
                        int soLuong = ((Number) row[2]).intValue();
                        if (maMon.equals(km.getMon1())) slMon1 += soLuong;
                        if (maMon.equals(km.getMon2())) slMon2 += soLuong;
                    }

                    boolean duDieuKien = false;
                    if (km.getMon1().equals(km.getMon2())) {
                        // Nếu 2 món giống nhau thì cần số lượng >= 2
                        duDieuKien = slMon1 >= 2;
                    } else {
                        // 2 món khác nhau thì mỗi món ≥ 1
                        duDieuKien = slMon1 > 0 && slMon2 > 0;
                    }

                    if (duDieuKien) {
                        dsKMThucSu.add(km);
                    }

                } else {
                    // Các loại khác (giảm %, giảm tiền) không cần kiểm tra món
                    dsKMThucSu.add(km);
                }
            }

            if (dsKMThucSu.isEmpty()) {
                cmbKhuyenMai.addItem(NO_PROMO);
            } else {
                // Thêm các KM đủ điều kiện vào combobox
                for (KhuyenMai km : dsKMThucSu) {
                    cmbKhuyenMai.addItem(km);
                }

                // Chọn khuyến mãi tốt nhất
                KhuyenMai kmTotNhat = kmDAO.getKhuyenMaiTotNhat(tongCong, maHD);
                if (kmTotNhat != null && dsKMThucSu.contains(kmTotNhat)) {
                    cmbKhuyenMai.setSelectedItem(kmTotNhat);
                    giamGia = tinhGiamGia(kmTotNhat);
                } else {
                    cmbKhuyenMai.setSelectedIndex(0);
                    giamGia = tinhGiamGia((KhuyenMai) cmbKhuyenMai.getSelectedItem());
                }
            }

            tinhTienThua();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải khuyến mãi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private double tinhGiamGia(KhuyenMai km) {
        if (km == null || km.getMaKM() == null) return 0;
        // Lấy tên loại theo mã loại; DAO phải có phương thức tương ứng
        String tenLoai;
        try {
            tenLoai = loaiDAO.getTenLoaiByMaLoai(km.getMaLoai()); // hãy đảm bảo DAO có hàm này
        } catch (Exception ex) {
            // fallback: nếu không lấy được tên, dùng mã loại
            tenLoai = km.getMaLoai();
        }

        try {
            if ("Phần trăm".equalsIgnoreCase(tenLoai) || "L01".equalsIgnoreCase(km.getMaLoai())) {
                return tongCong * (km.getGiaTri() / 100.0);
            } else if ("Giảm trực tiếp".equalsIgnoreCase(tenLoai) || "L02".equalsIgnoreCase(km.getMaLoai())) {
                return km.getGiaTri();
            } else if ("Tặng món".equalsIgnoreCase(tenLoai) || "L03".equalsIgnoreCase(km.getMaLoai())) {
                if (km.getMonTang() != null) {
                    String sql = "SELECT donGia FROM MonAn WHERE maMon = ?";
                    try (Connection con = ConnectSQL.getInstance().getConnection();
                         PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, km.getMonTang());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                return rs.getDouble("donGia");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tính giảm giá: " + e.getMessage());
        }
        return 0;
    }

    private void tinhTienThua() {
        try {
            double tienKhachDua = txtTienNhan.getText().isEmpty() ? 0 : Double.parseDouble(txtTienNhan.getText().trim());
            double vat = tongCong * 0.08;
            double tienSauGiam = tongCong + vat - giamGia + phuThu;
            double tienThua = tienKhachDua - tienSauGiam;
            valTongHD.setText(df.format(tienSauGiam));
            valTienThanhToan.setText(df.format(tienSauGiam));
            valThua.setText(df.format(tienThua < 0 ? 0 : tienThua));
            // IMPORTANT: không ghi đè txtTienNhan ở đây — người dùng nhập tiền
        } catch (NumberFormatException e) {
            double vat = tongCong * 0.08;
            double tienSauGiam = tongCong + vat - giamGia + phuThu;
            valTongHD.setText(df.format(tienSauGiam));
            valTienThanhToan.setText(df.format(tienSauGiam));
            valThua.setText(df.format(0));
        }
    }
    
    private void loadDuLieuHoaDon(String maHD) {
        Object[] thongTin = hoaDonDAO.layThongTinHoaDon(maHD);
        if (thongTin == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn " + maHD);
            return;
        }

        Object[] khach = hoaDonDAO.layThongTinKhachTuPhieuDatBan(maHD);
        if (khach != null) {
            valKH.setText((String) khach[0]);
            valSDT.setText((String) khach[1]);
        } else {
            valKH.setText("Không xác định");
            valSDT.setText("Không xác định");
        }
        valBan.setText((String) thongTin[2]);
        valNV.setText((String) thongTin[3]);
        valNgayLap.setText(thongTin[4].toString());
        valCoc.setText(df.format(0));

        Object tongObj = thongTin[6];
        if (tongObj instanceof Number) {
            tongCong = ((Number) tongObj).doubleValue();
        } else {
            try {
                tongCong = Double.parseDouble(String.valueOf(tongObj));
            } catch (Exception ex) {
                tongCong = 0;
            }
        }
        valTongCong.setText(df.format(tongCong));

        double vat = tongCong * 0.08;
        giamGia = 0;
        phuThu = 0;
        double tongHD = tongCong + vat - giamGia + phuThu;
        valTongHD.setText(df.format(tongHD));
        valTienThanhToan.setText(df.format(tongHD));

        txtTienNhan.setText("");          // để người dùng nhập
        valThua.setText(df.format(0));    // tiền thừa mặc định 0
    }


    private void loadChiTietHoaDon(String maHD) {
        List<Object[]> dsCT = hoaDonDAO.layChiTietHoaDon(maHD);
        model.setRowCount(0);
        int stt = 1;
        for (Object[] row : dsCT) {
            model.addRow(new Object[]{
                    stt++,
                    row[1],
                    row[2],
                    df.format(row[3]),
                    df.format(row[4])
            });
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnQuayLai || source == btnHuy) {
            dispose();
        } else if (source == btnXacNhan) {
            try {
                double tienKhachDua = txtTienNhan.getText().isEmpty() ? 0 : Double.parseDouble(txtTienNhan.getText().trim());
                double vat = tongCong * 0.08;
                double tienSauGiam = tongCong + vat - giamGia + phuThu;
                if (tienKhachDua < tienSauGiam) {
                    JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
                String maKM = (selectedKM == null || selectedKM.getMaKM() == null) ? null : selectedKM.getMaKM();

                String tenKH = valKH.getText().trim();
                String sdt = valSDT.getText().trim();
                String maKH = hoaDonDAO.layMaKhachTuTen(tenKH, sdt);
                if (maKH == null || maKH.isEmpty()) maKH = null;

                double phuThuValue = chkPhuThu.isSelected() && !txtPhuThu.getText().trim().isEmpty()
                        ? Double.parseDouble(txtPhuThu.getText().trim()) : 0;

                String ghiChu = (maKM != null) ? "Áp dụng khuyến mãi: " + maKM : null;

                boolean capNhatHD = hoaDonDAO.thanhToanHoaDon(
                        maHD, tienKhachDua, tienSauGiam, maKM, maKH, phuThuValue, ghiChu
                );

                boolean capNhatBan = capNhatPhieuDatBanSauThanhToan();

                xuatHoaDonPDF(maHD);

                if (capNhatHD && capNhatBan) {
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // GỌI CALLBACK ĐỂ CẬP NHẬT GIAO DIỆN FrmBan
                    if (onThanhToanThanhCong != null) {
                        onThanhToanThanhCong.run(); // ← Sẽ gọi taiLaiBangChinh()
                    }

                    if (parent != null) parent.capNhatDuLieu();
                    if (parentPhucVu != null) parentPhucVu.dispose();

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == cmbKhuyenMai) {
            KhuyenMai selectedKM = (KhuyenMai) cmbKhuyenMai.getSelectedItem();
            giamGia = tinhGiamGia(selectedKM);
            tinhTienThua();
        }
    }

    // CẬP NHẬT PHIẾU ĐẶT BÀN SAU THANH TOÁN
    private boolean capNhatPhieuDatBanSauThanhToan() {
        if (banDangChon == null || banDangChon.isEmpty()) return false;

        try {
            Connection conn = ConnectSQL.getInstance().getConnection();
            PhieuDatBan_DAO phieuDAO = new PhieuDatBan_DAO(conn);

            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            List<PhieuDatBan> listPhieu = phieuDAO.getDatBanByBanAndNgay(banDangChon, today);

            boolean updated = false;
            for (PhieuDatBan p : listPhieu) {
                if ("Phục vụ".equals(p.getTrangThai())) {
                    p.setTrangThai("Hoàn thành");
                    phieuDAO.update(p);
                    updated = true;
                }
            }
            return updated || listPhieu.stream().noneMatch(p -> "Phục vụ".equals(p.getTrangThai()));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái bàn!");
            return false;
        }
    }

	private void xuatHoaDonPDF(String maHD) {
        try {
            String fileName = "HoaDon_" + maHD + ".pdf";
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileName));
            document.open();

            // ===== Font tiếng Việt =====
            String fontPath = "C:/Windows/Fonts/times.ttf";
            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(
                    fontPath, com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            // ===== Header =====
            Paragraph header = new Paragraph("NHÀ HÀNG VANG\n", fontTitle);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph info = new Paragraph("Địa chỉ: 12 Nguyễn Văn Bảo, quận Gò Vấp, TP.HCM\n"
                    + "Điện thoại: 0987654321\n\n", fontNormal);
            info.setAlignment(Element.ALIGN_CENTER);
            document.add(info);

            // ===== Hóa đơn =====
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN\n\n", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Số HĐ: " + maHD, fontNormal));
            document.add(new Paragraph("Ngày: " + valNgayLap.getText(), fontNormal));
            document.add(new Paragraph("Bàn số: " + valBan.getText(), fontNormal));
            document.add(new Paragraph("Khách hàng: " + valKH.getText(), fontNormal));
            document.add(new Paragraph("Nhân viên phục vụ: " + valNV.getText(), fontNormal));
            document.add(new Paragraph("Khuyến mãi: " +
                    (cmbKhuyenMai.getSelectedItem() != null ? cmbKhuyenMai.getSelectedItem().toString() : "Không"), fontNormal));
            document.add(new Paragraph("\n"));

            // ===== Bảng món =====
            PdfPTable pdfTable = new PdfPTable(new float[]{1, 4, 2, 2, 3});
            pdfTable.setWidthPercentage(100);
            String[] headers = {"STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền"};

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new BaseColor(220, 220, 220));
                pdfTable.addCell(cell);
            }

            double tongTien = 0;
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
            for (int i = 0; i < model.getRowCount(); i++) {
                pdfTable.addCell(new Phrase(String.valueOf(i + 1), fontNormal));
                pdfTable.addCell(new Phrase(model.getValueAt(i, 1).toString(), fontNormal));
                pdfTable.addCell(new Phrase(model.getValueAt(i, 3).toString(), fontNormal));
                pdfTable.addCell(new Phrase(model.getValueAt(i, 2).toString(), fontNormal));
                pdfTable.addCell(new Phrase(model.getValueAt(i, 4).toString(), fontNormal));

                try {
                    tongTien += Double.parseDouble(model.getValueAt(i, 4).toString().replace(",", ""));
                } catch (Exception ignore) {}
            }
            document.add(pdfTable);

            // ===== Tổng tiền =====
            double vat = tongCong * 0.08;
            double thanhTien = tongCong + vat - giamGia + phuThu;

            document.add(new Paragraph("\nTổng tiền: " + df.format(tongCong), fontBold));
            document.add(new Paragraph("Thuế VAT (8%): " + df.format(vat), fontBold));
            document.add(new Paragraph("Giảm giá: " + df.format(giamGia), fontBold));
            if (phuThu > 0) document.add(new Paragraph("Phụ thu: " + df.format(phuThu), fontBold));
            document.add(new Paragraph("Thành tiền: " + df.format(thanhTien) + "\n\n", fontBold));

            // ===== Footer =====
            Paragraph footer = new Paragraph("Trân trọng cảm ơn quý khách. Hẹn gặp lại!", fontNormal);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn ra file: " + fileName);
            java.awt.Desktop.getDesktop().open(new java.io.File(fileName));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
        }
    }}