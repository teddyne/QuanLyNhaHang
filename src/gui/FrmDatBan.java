package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import com.toedter.calendar.JDateChooser;
import connectSQL.ConnectSQL;
import java.time.ZoneId;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import dao.Ban_DAO;
import dao.ChiTietDatMon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.ChiTietDatMon;
import entity.KhachHang;
import entity.MonAn;
import entity.PhieuDatBan;
import entity.MonDat;

public class FrmDatBan extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private PhieuDatBan_DAO phieuDatBanDAO;
    private Ban_DAO banDAO;
    private KhachHang_DAO khachHangDAO;
    private List<MonDat> danhSachMonDat = new ArrayList<>();
    private String maBan;
    private PhieuDatBan editD;
    private Font fontBig = new Font("Times New Roman", Font.BOLD, 22);
    private JButton btnDatMon;
    private JButton btnLuu;
    private JButton btnHuy;
    private JButton btnCheckKH;
    private JTextField txtTen;
    private JTextField txtSDT;
    private JTextField txtTienCoc;
    private JSpinner spnNgay;
    private JSpinner spnGio;
    private double tongTienMon = 0.0;
    private Connection conn;
	private Object maPhieu;
	private FrmDatMon frmDatMon;
	
    public FrmDatBan(JFrame parent, String maBan, PhieuDatBan phieu, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO, KhachHang_DAO khachHangDAO, Connection conn) throws SQLException {
        super(parent, phieu == null ? "Đặt bàn" : "Sửa đặt bàn", true);
        
        this.maBan = maBan;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.banDAO = banDAO;
        this.khachHangDAO = khachHangDAO;
        this.conn = conn;
        this.maPhieu = (phieu != null) ? phieu.getMaPhieu() : "";
        this.danhSachMonDat = new ArrayList<>();
        initComponents();
        // Nếu đang sửa phiếu => load dữ liệu chi tiết món đã đặt
        if (phieu != null) {
            ChiTietDatMon_DAO chiTietDAO = new ChiTietDatMon_DAO(conn);
            List<ChiTietDatMon> dsChiTiet = chiTietDAO.layTheoPhieu(phieu.getMaPhieu());
            MonAn_DAO monDAO = new MonAn_DAO(conn);
            for (ChiTietDatMon ct : dsChiTiet) {
                MonAn mon = monDAO.layMonAnTheoMa(ct.getMaMon());
                MonDat monDat = new MonDat(mon, ct.getSoLuong(), ct.getDonGia(), ct.getGhiChu());
                danhSachMonDat.add(monDat);
            }
        }
    }
    private void initComponents() throws SQLException {
        setSize(800, 700);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(getParent());
        new ChiTietDatMon_DAO(conn);
        // Header
        JLabel lblTieuDe = new JLabel("PHIẾU ĐẶT BÀN - Mã bàn: " + maBan, SwingConstants.CENTER);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(COLOR_RED_WINE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblTieuDe.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(lblTieuDe, BorderLayout.NORTH);
        // Form
        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtMaPhieu = new JTextField(editD != null ? editD.getMaPhieu() : phieuDatBanDAO.generateMaPhieu());
        txtMaPhieu.setEditable(false);
        txtMaPhieu.setFont(fontBig);
        txtTen = new JTextField(editD != null ? editD.getTenKhach() : "", 20);
        txtTen.setFont(fontBig);
        txtSDT = new JTextField(editD != null ? editD.getSoDienThoai() : "", 15);
        txtSDT.setFont(fontBig);
        btnCheckKH = taoNut("Kiểm tra", new Color(100, 149, 237), null, fontBig);

        SpinnerDateModel dateModel = new SpinnerDateModel(editD != null ? editD.getNgayDen() : new java.util.Date(), null, null, Calendar.DAY_OF_MONTH);
        spnNgay = new JSpinner(dateModel);
        spnNgay.setEditor(new JSpinner.DateEditor(spnNgay, "yyyy-MM-dd"));
        spnNgay.setFont(fontBig);

        SpinnerDateModel timeModel = new SpinnerDateModel(editD != null ? new java.util.Date(editD.getGioDen().getTime()) : new java.util.Date(), null, null, Calendar.MINUTE);
        spnGio = new JSpinner(timeModel);
        spnGio.setEditor(new JSpinner.DateEditor(spnGio, "HH:mm"));
        spnGio.setFont(fontBig);

        JTextField txtSoNguoi = new JTextField(editD != null ? String.valueOf(editD.getSoNguoi()) : "", 5);
        txtSoNguoi.setFont(fontBig);
        JTextArea txtGhiChu = new JTextArea(editD != null ? editD.getGhiChu() : "", 3, 20);
        txtGhiChu.setFont(fontBig);

        txtTienCoc = new JTextField(editD != null ? String.valueOf(editD.getTienCoc()) : "200000", 10); // Mặc định 200.000
        txtTienCoc.setFont(fontBig);
        JCheckBox chkCK = new JCheckBox("Chuyển khoản");
        chkCK.setFont(fontBig);
        if (editD != null && !editD.getGhiChuCoc().isEmpty()) chkCK.setSelected(true);
        JTextArea txtGhiChuCK = new JTextArea(editD != null ? editD.getGhiChuCoc() : "", 2, 20);
        txtGhiChuCK.setFont(fontBig);
        txtGhiChuCK.setEnabled(chkCK.isSelected());
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(120, 35);
        btnDatMon = taoNut("Đặt món", new Color(50, 205, 50), buttonSize, fontBig);
        btnLuu = taoNut("Đặt Bàn", new Color(100, 149, 237), buttonSize, fontBig);
        btnHuy = taoNut("Hủy", new Color(220, 20, 60), buttonSize, fontBig);

        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMaPhieu = new JLabel("Mã phiếu");
        lblMaPhieu.setFont(fontBig);
        pForm.add(lblMaPhieu, gbc);
        gbc.gridx = 1; pForm.add(txtMaPhieu, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTenKH = new JLabel("Tên KH");
        lblTenKH.setFont(fontBig);
        pForm.add(lblTenKH, gbc);
        gbc.gridx = 1; pForm.add(txtTen, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSDTLabel = new JLabel("SĐT");
        lblSDTLabel.setFont(fontBig);
        pForm.add(lblSDTLabel, gbc);
        gbc.gridx = 1; pForm.add(txtSDT, gbc);
        gbc.gridx = 2; pForm.add(btnCheckKH, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblNgayDen = new JLabel("Ngày đến");
        lblNgayDen.setFont(fontBig);
        pForm.add(lblNgayDen, gbc);
        gbc.gridx = 1; pForm.add(spnNgay, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGioDen = new JLabel("Giờ đến");
        lblGioDen.setFont(fontBig);
        pForm.add(lblGioDen, gbc);
        gbc.gridx = 1; pForm.add(spnGio, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSoNguoiLabel = new JLabel("Số người");
        lblSoNguoiLabel.setFont(fontBig);
        pForm.add(lblSoNguoiLabel, gbc);
        gbc.gridx = 1; pForm.add(txtSoNguoi, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChu = new JLabel("Ghi chú");
        lblGhiChu.setFont(fontBig);
        pForm.add(lblGhiChu, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChu), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTienCocLabel = new JLabel("Tiền cọc");
        lblTienCocLabel.setFont(fontBig);
        pForm.add(lblTienCocLabel, gbc);
        gbc.gridx = 1; pForm.add(txtTienCoc, gbc);
        gbc.gridx = 2; pForm.add(chkCK, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChuCK = new JLabel("Ghi chú CK");
        lblGhiChuCK.setFont(fontBig);
        pForm.add(lblGhiChuCK, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChuCK), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMonAn = new JLabel("Món ăn");
        lblMonAn.setFont(fontBig);
        pForm.add(lblMonAn, gbc);
        gbc.gridx = 1; pForm.add(btnDatMon, gbc);

        // Panel nút
        JPanel pSouth = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        pSouth.add(btnLuu);
        pSouth.add(btnHuy);

        pForm.setBackground(Color.white);
        add(pForm, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);

        // Sự kiện
        btnCheckKH.addActionListener(e -> {
            String sdt = txtSDT.getText().trim();
            if (!validateSDT(sdt)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 số hợp lệ theo Việt Nam!");
                return;
            }
            try {
                if (khachHangDAO == null) {
                    JOptionPane.showMessageDialog(this, "Lỗi: khachHangDAO chưa được khởi tạo!");
                    return;
                }
                KhachHang kh = khachHangDAO.timKhachHangTheoSDT(sdt);
                if (kh != null) {
                    txtTen.setText(kh.getTenKH());
                    JOptionPane.showMessageDialog(this, "Tìm thấy khách hàng: " + kh.getTenKH());
                } else {
                    Object[] options = {"OK", "Thêm khách hàng"};
                    int choice = JOptionPane.showOptionDialog(this, "Không tìm thấy khách hàng!", "Thông báo",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    if (choice == 0) {
                        if (txtTen.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                            return;
                        }
                        KhachHang khNew = new KhachHang();
                        khNew.setMaKH(khachHangDAO.generateMaKH());
                        khNew.setTenKH(txtTen.getText().trim());
                        khNew.setSdt(sdt);
                        khNew.setNgaySinh(null);
                        khNew.setEmail(null);
                        khNew.setLoaiKH("Thành viên");
                        khachHangDAO.themKhachHang(khNew);
                        JOptionPane.showMessageDialog(this, "Đã tạo khách hàng mới theo số điện thoại và tên!");
                    } else if (choice == 1) {
                        FrmThemKhachHang frmThemKH = new FrmThemKhachHang(this, sdt, txtTen.getText().trim(), khachHangDAO);
                        frmThemKH.setVisible(true);
                        KhachHang khNew = khachHangDAO.timKhachHangTheoSDT(sdt);
                        if (khNew != null) {
                            txtTen.setText(khNew.getTenKH());
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        chkCK.addActionListener(e -> txtGhiChuCK.setEnabled(chkCK.isSelected()));

        btnDatMon.addActionListener(e -> {
            try {
                String maPhieu = txtMaPhieu.getText().trim();
                if (maPhieu.isEmpty()) {
                    // Chỉ tạo mã tạm, KHÔNG lưu DB
                    maPhieu = "P" + System.currentTimeMillis();
                    txtMaPhieu.setText(maPhieu);
                }

               // Mở form đặt món (chưa lưu DB)
                JDialog datMonDialog = new JDialog(this, "ĐẶT MÓN - BÀN " + maBan, true);
                datMonDialog.setSize(1500, 800);
                datMonDialog.setLocationRelativeTo(this);
                frmDatMon = new FrmDatMon(conn, maPhieu, maBan);
                datMonDialog.add(frmDatMon.getRootPane());
                datMonDialog.setVisible(true);

                // Sau khi đóng dialog
                if (frmDatMon != null) {
                    danhSachMonDat = frmDatMon.getDanhSachMonDat();
                    String monList = frmDatMon.layDanhSachMonDat();

                   if (!monList.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "ĐÃ CHỌN MÓN:\n" + monList);
                    }

                    tongTienMon = frmDatMon.layTongTienMon();
                    capNhatTienCoc();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form đặt món: " + ex.getMessage());
            }
        });


        btnLuu.addActionListener(e -> {
            Connection conn = null;
            try {
                conn = ConnectSQL.getConnection();
                conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

               String sdt = txtSDT.getText().trim();
                if (!validateSDT(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 số hợp lệ theo Việt Nam!");
                    return;
                }

                int soNguoi;
                try {
                    soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());
                    if (soNguoi <= 0) throw new Exception();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Số người phải là số dương!");
                    return;
                }

                double tienCoc;
                try {
                    String str = txtTienCoc.getText().trim();
                    tienCoc = str.isEmpty() ? 200000 : Double.parseDouble(str);
                    if (tienCoc < 0) throw new Exception();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Tiền cọc không hợp lệ!");
                    return;
                }
                java.util.Date ngayUtil = (java.util.Date) spnNgay.getValue();
                java.util.Date gioUtil = (java.util.Date) spnGio.getValue();

                Calendar cal = Calendar.getInstance();
                cal.setTime(ngayUtil);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

               cal.setTime(gioUtil);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                cal.set(year, month, day, hour, minute, 0);
                java.sql.Date ngay = new java.sql.Date(cal.getTimeInMillis());
                java.sql.Time gio = new java.sql.Time(cal.getTimeInMillis());

                                if (editD == null && !phieuDatBanDAO.checkCachGio(maBan, ngay, gio, 1)) {
                    JOptionPane.showMessageDialog(this, "Lịch đặt phải cách ít nhất 1 tiếng với lịch khác cùng ngày!");
                    return;
                }

                PhieuDatBan dNew = editD != null ? editD : new PhieuDatBan();
                dNew.setMaPhieu(txtMaPhieu.getText().trim());
                dNew.setMaBan(maBan);
                dNew.setTenKhach(txtTen.getText().trim());
                dNew.setSoDienThoai(sdt);
                dNew.setSoNguoi(soNguoi);
                dNew.setNgayDen(ngay);
                dNew.setGioDen(gio);
                dNew.setGhiChu(txtGhiChu.getText());
                dNew.setTienCoc(tienCoc);
                dNew.setGhiChuCoc(chkCK.isSelected() ? txtGhiChuCK.getText() : "");
                dNew.setTrangThai(editD != null ? editD.getTrangThai() : "Đặt");
                PhieuDatBan_DAO phieuDAO = new PhieuDatBan_DAO(conn);
                if (editD != null) {
                    phieuDAO.update(dNew);
                } else {
                    phieuDAO.add(dNew);
                }
                if (!danhSachMonDat.isEmpty()) {
                    ChiTietDatMon_DAO chiTietDAO = new ChiTietDatMon_DAO(conn); // CÙNG conn
                    chiTietDAO.xoaTheoPhieu(dNew.getMaPhieu());

                    for (MonDat item : danhSachMonDat) {
                        ChiTietDatMon ct = new ChiTietDatMon(
                            dNew.getMaPhieu(),
                            item.getMon().getMaMon(),
                            item.getSoLuong(),
                            item.getMon().getDonGia(),
                            item.getGhiChu()
                        );
                        chiTietDAO.themChiTiet(ct);
                    }
                }
                conn.commit();
               JOptionPane.showMessageDialog(this, "Đặt bàn và món thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException r) {
                        r.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }
        });
       btnHuy.addActionListener(e -> dispose());
    }
    private void capNhatTienCoc() {
        double tienCoc = (tongTienMon > 0) ? tongTienMon : 200000;
        txtTienCoc.setText(String.valueOf(tienCoc));
    }
    private boolean validateSDT(String sdt) {
        return Pattern.matches("0\\d{9}", sdt);
    }

    private void batDauNhacNho(PhieuDatBan pdb) {
        long thoiGianDenGioHen = pdb.getNgayDen().getTime() + pdb.getGioDen().getTime() - System.currentTimeMillis();
        if (thoiGianDenGioHen > 0) {
            Timer timerDenGio = new Timer((int) thoiGianDenGioHen, e -> {
                JOptionPane.showMessageDialog(null, "Khách hàng chưa đến cho phiếu " + pdb.getMaPhieu() + " tại bàn " + maBan);
                Timer timerSau1Tieng = new Timer(3600000, ev -> {
                    try {
                        Ban ban = banDAO.getBanByMa(maBan);
                        if (!"Phục vụ".equals(ban.getTrangThai())) {
                            FrmNhacNho frmNhacNho = new FrmNhacNho(this, pdb, conn);
                            frmNhacNho.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra trạng thái bàn: " + ex.getMessage());
                    }
                });
                timerSau1Tieng.setRepeats(false);
                timerSau1Tieng.start();
            });
            timerDenGio.setRepeats(false);
            timerDenGio.start();
        }
    }

    private JButton taoNut(String text, Color baseColor, Dimension size, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        if (size != null) btn.setPreferredSize(size);
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
}

// Class form thêm khách hàng
class FrmThemKhachHang extends JDialog {
    private KhachHang_DAO khachHangDAO;
    private JTextField txtTen;
    private JTextField txtSDT;
    private JTextField txtCCCD;
    private JTextField txtEmail;
    private JDateChooser dateChooserNgaySinh;

    public FrmThemKhachHang(JDialog parent, String sdt, String ten, KhachHang_DAO khachHangDAO) {
        super(parent, "Thêm khách hàng thành viên", true);
        this.khachHangDAO = khachHangDAO;
        initComponents(sdt, ten);
    }

   private void initComponents(String sdt, String ten) {
        setSize(450, 350);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(getParent());

        getContentPane().setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Times New Roman", Font.BOLD, 20);
        Font fonttxt = new Font("Times New Roman", Font.PLAIN, 20);

        JLabel lblMa;
        try {
            lblMa = new JLabel("Mã KH: " + khachHangDAO.generateMaKH());
        } catch (SQLException ex) {
            lblMa = new JLabel("Mã KH: Lỗi tạo mã");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo mã khách hàng: " + ex.getMessage());
        }
        lblMa.setFont(font);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(lblMa, gbc);

        JLabel lblTen = new JLabel("Tên KH:");
        lblTen.setFont(font);
        gbc.gridy = 1; gbc.gridwidth = 1; add(lblTen, gbc);
        txtTen = new JTextField(ten, 20); txtTen.setFont(fonttxt);
        gbc.gridx = 1; add(txtTen, gbc);

        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(font);
        gbc.gridx = 0; gbc.gridy = 2; add(lblSDT, gbc);
        txtSDT = new JTextField(sdt, 20); txtSDT.setFont(fonttxt); txtSDT.setEditable(false);
        gbc.gridx = 1; add(txtSDT, gbc);

        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblNgaySinh, gbc);

        JDateChooser dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setFont(fonttxt);
        dateChooserNgaySinh.setDateFormatString("dd/MM/yyyy"); // định dạng ngày
        gbc.gridx = 1;
        add(dateChooserNgaySinh, gbc);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(font);
        gbc.gridx = 0; gbc.gridy = 4; add(lblEmail, gbc);
        txtEmail = new JTextField(20); txtEmail.setFont(fonttxt);
        gbc.gridx = 1; add(txtEmail, gbc);

        JLabel lblLoai = new JLabel("Loại: Thành viên");
        lblLoai.setFont(font);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(lblLoai, gbc);

        JButton btnThem = new JButton("Thêm");
        btnThem.setForeground(Color.white);
        btnThem.setFocusPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.setContentAreaFilled(false);
        btnThem.setOpaque(true);
        btnThem.setBackground(new Color(231, 76, 60));
        btnThem.setFont(font);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2; add(btnThem, gbc);

        btnThem.addActionListener(e -> {
            try {
                KhachHang kh = new KhachHang();
                kh.setMaKH(khachHangDAO.generateMaKH());
                kh.setTenKH(txtTen.getText().trim());
                kh.setSdt(txtSDT.getText().trim());
                kh.setNgaySinh( (dateChooserNgaySinh.getDate() == null) ? null : dateChooserNgaySinh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() );
                kh.setEmail(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());
                kh.setLoaiKH("Thành viên");
                khachHangDAO.themKhachHang(kh);
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
    }
}

// Class form nhắc nhở
class FrmNhacNho extends JDialog {
    private PhieuDatBan pdb;
    private PhieuDatBan_DAO phieuDatBanDAO;

    public FrmNhacNho(JDialog parent, PhieuDatBan pdb, Connection conn) {
        super(parent, "Nhắc nhở hủy bàn", true);
        this.pdb = pdb;
        this.phieuDatBanDAO = new PhieuDatBan_DAO(conn);
        initComponents();
    }
    private void initComponents() {
        setSize(300, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getParent());
        JLabel lblMessage = new JLabel("Đã quá 1 tiếng, khách chưa đến. Hủy bàn?", SwingConstants.CENTER);
        add(lblMessage, BorderLayout.CENTER);
        JPanel pButtons = new JPanel();
        JButton btnHuyBan = new JButton("Hủy bàn");
        JButton btnDong = new JButton("Đóng");
        pButtons.add(btnHuyBan);
        pButtons.add(btnDong);
        add(pButtons, BorderLayout.SOUTH);
        btnHuyBan.addActionListener(e -> {
            try {
                pdb.setTrangThai("Hủy");
                phieuDatBanDAO.update(pdb);
                JOptionPane.showMessageDialog(this, "Đã hủy bàn!");
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        btnDong.addActionListener(e -> dispose());
    }
}
