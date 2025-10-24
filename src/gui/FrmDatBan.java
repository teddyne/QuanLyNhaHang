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
import java.util.Calendar;
import java.util.regex.Pattern;
import com.toedter.calendar.JDateChooser;
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
import dao.KhachHang_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.KhachHang;
import entity.PhieuDatBan;

public class FrmDatBan extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private PhieuDatBan_DAO phieuDatBanDAO;
    private Ban_DAO banDAO;
    private KhachHang_DAO khachHangDAO;
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

    public FrmDatBan(JFrame parent, String maBan, PhieuDatBan editD, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO, KhachHang_DAO khachHangDAO, Connection conn) throws SQLException {
        super(parent, editD == null ? "Đặt bàn" : "Sửa đặt bàn", true);
        this.maBan = maBan;
        this.editD = editD;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.banDAO = banDAO;
        this.khachHangDAO = khachHangDAO;
        this.conn = conn;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setSize(800, 700);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(getParent());

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
                JDialog datMonDialog = new JDialog(this, "ĐẶT MÓN - BÀN " + maBan, true);
                datMonDialog.setSize(1400, 800);
                datMonDialog.setLocationRelativeTo(this);

                FrmDatMon frmDatMon = new FrmDatMon(datMonDialog, maBan);
                datMonDialog.add(frmDatMon.getRootPane());
                datMonDialog.setVisible(true);

                String monList = frmDatMon.getDanhSachMonDat();
                if (!monList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ĐÃ THÊM MÓN:\n" + monList, "Thành công!", JOptionPane.INFORMATION_MESSAGE);
                }

                tongTienMon = frmDatMon.getTongTienMon();
                capNhatTienCoc();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        btnLuu.addActionListener(e -> {
            try {
                String sdt = txtSDT.getText().trim();
                if (!validateSDT(sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 số hợp lệ theo Việt Nam!");
                    return;
                }

                // Lấy ngày và giờ từ JSpinner
                java.util.Date ngayUtil = (java.util.Date) spnNgay.getValue();
                java.util.Date gioUtil = (java.util.Date) spnGio.getValue();

                // Gộp ngày và giờ
                java.util.Calendar ngayDatCalendar = Calendar.getInstance();
                ngayDatCalendar.setTime(ngayUtil);
                java.util.Calendar gioDatCalendar = Calendar.getInstance();
                gioDatCalendar.setTime(gioUtil);

                java.util.Calendar thoiDiemDat = Calendar.getInstance();
                thoiDiemDat.set(
                    ngayDatCalendar.get(Calendar.YEAR),
                    ngayDatCalendar.get(Calendar.MONTH),
                    ngayDatCalendar.get(Calendar.DAY_OF_MONTH),
                    gioDatCalendar.get(Calendar.HOUR_OF_DAY),
                    gioDatCalendar.get(Calendar.MINUTE),
                    0
                );

                // Chuyển đổi sang java.sql.Date và java.sql.Time
                java.sql.Date ngay = new java.sql.Date(thoiDiemDat.getTimeInMillis());
                java.sql.Time gio = new java.sql.Time(thoiDiemDat.getTimeInMillis());

                // Kiểm tra cách 1 tiếng (giữ lại, có thể bỏ nếu không cần)
                if (editD == null && !phieuDatBanDAO.checkCachGio(maBan, ngay, gio, 1)) {
                    JOptionPane.showMessageDialog(this, "Lịch đặt phải cách ít nhất 1 tiếng với lịch khác cùng ngày!");
                    return;
                }

                // Kiểm tra số người
                int soNguoi;
                try {
                    soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());
                    if (soNguoi <= 0) {
                        JOptionPane.showMessageDialog(this, "Số người phải lớn hơn 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số người phải là số hợp lệ!");
                    return;
                }

                // Kiểm tra tiền cọc
                double tienCoc;
                try {
                    String tienCocStr = txtTienCoc.getText().trim();
                    tienCoc = tienCocStr.isEmpty() ? 200000 : Double.parseDouble(tienCocStr);
                    if (tienCoc < 0) {
                        JOptionPane.showMessageDialog(this, "Tiền cọc không được âm!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Tiền cọc phải là số hợp lệ!");
                    return;
                }

                // Tạo hoặc cập nhật phiếu đặt bàn
                PhieuDatBan dNew = editD != null ? editD : new PhieuDatBan();
                dNew.setMaPhieu(txtMaPhieu.getText());
                dNew.setMaBan(maBan);
                dNew.setTenKhach(txtTen.getText().trim());
                dNew.setSoDienThoai(sdt);
                dNew.setSoNguoi(soNguoi);
                dNew.setNgayDen(ngay);
                dNew.setGioDen(gio);
                dNew.setGhiChu(txtGhiChu.getText());
                dNew.setTienCoc(tienCoc);
                dNew.setGhiChuCoc(chkCK.isSelected() ? txtGhiChuCK.getText() : "");
                dNew.setTrangThai(editD != null ? dNew.getTrangThai() : "Đặt");

                // Tìm hoặc thêm khách hàng
                KhachHang kh = khachHangDAO.timKhachHangTheoSDT(sdt);
                if (kh == null) {
                    JOptionPane.showMessageDialog(this, "Khách hàng chưa tồn tại. Vui lòng thêm khách hàng trước!");
                    return;
                }
                if (editD != null) {
                    phieuDatBanDAO.update(dNew);
                    JOptionPane.showMessageDialog(this, "Cập nhật phiếu đặt bàn thành công!");
                } else {
                    phieuDatBanDAO.add(dNew);
                    batDauNhacNho(dNew);
                    JOptionPane.showMessageDialog(this, "Đặt bàn thành công!");
                }
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu đặt bàn: " + ex.getMessage());
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

        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(font);
        gbc.gridx = 0; gbc.gridy = 3; add(lblCCCD, gbc);
        txtCCCD = new JTextField(20); txtCCCD.setFont(fonttxt);
        gbc.gridx = 1; add(txtCCCD, gbc);

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