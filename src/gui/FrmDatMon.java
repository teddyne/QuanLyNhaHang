package gui;

import connectSQL.ConnectSQL;
import dao.*;
import entity.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class FrmDatMon extends JFrame {
    // TONE CHỦ ĐẠO
    private static final Color MAU_DO_RUOU = new Color(169, 55, 68);
    private static final Color MAU_HONG_NHAT = new Color(255, 230, 230);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HONG_CARD = new Color(240, 182, 182);

    // FONT TIMES NEW ROMAN
    private static final Font FONT_TIEU_DE = new Font("Times New Roman", Font.BOLD, 32);
    private static final Font FONT_TIEU_DE_NHO = new Font("Times New Roman", Font.BOLD, 26);
    private static final Font FONT_NOI_DUNG = new Font("Times New Roman", Font.PLAIN, 18);
    private static final Font FONT_CARD_TEN = new Font("Times New Roman", Font.BOLD, 20);
    private static final Font FONT_CARD_GIA = new Font("Times New Roman", Font.BOLD, 22);
    private static final Font FONT_CARD_MOTA = new Font("Times New Roman", Font.PLAIN, 18);
    private static final Font FONT_NUT = new Font("Times New Roman", Font.BOLD, 18);

    private MonAn_DAO monAnDAO;
    private LoaiMon_DAO loaiMonDAO;
    private HoaDon_DAO hoaDonDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;

    private static Connection con = ConnectSQL.getConnection();
    private List<MonDat> danhSachMonDat = new ArrayList<>();
    private List<MonAn> danhSachMon = new ArrayList<>();
    private DefaultTableModel bangModel;
    private double tongTien = 0;

    private JLabel lblTongTien;
    private JTable tblMonDat;
    private JTextField txtTimKiem;
    private JComboBox<String> cmbLocLoai;
    private JPanel pnlLuoiMon;

    private String maBan;
    private String maPhieu;
    private String maHoaDon;
    private String maNhanVien = "NV000";
    private String tenKhach = "Khách vãng lai";
    private String tenNhanVien = "Nhân viên mặc định";
    private FrmPhucVu frmPhucVu;

    
    class MonDat {
        MonAn mon;
        int soLuong;
        String ghiChu = "";

        MonDat(MonAn mon, int soLuong) {
            this.mon = mon;
            this.soLuong = soLuong;
        }

        double tinhThanhTien() { return mon.getDonGia() * soLuong; }
        void datGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
        String layGhiChu() { return this.ghiChu; }
    }

    public FrmDatMon(String maBan, String maPhieu) throws SQLException {
        super("ĐẶT MÓN");
        this.maBan = maBan;
        this.maPhieu = maPhieu;

        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() throws SQLException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowStateListener(e -> quayLaiPhucVu());

        khoiTaoDAO();
        khoiTaoBang();
        khoiTaoComboBoxLoai();
        taoGiaoDien();
        taiDuLieuTuBan();
        taiDuLieuTuDB();
        themSuKien();
    }

    private void khoiTaoDAO() throws SQLException {
	    monAnDAO = new MonAn_DAO(con);
	    loaiMonDAO = new LoaiMon_DAO(con);
	    hoaDonDAO = new HoaDon_DAO(con);
	    chiTietHoaDonDAO = new ChiTietHoaDon_DAO(con);
	    phieuDatBanDAO = new PhieuDatBan_DAO(ConnectSQL.getConnection());
	}

    private void khoiTaoBang() {
        String[] cols = {"STT", "Món ăn", "Số lượng", "Giá", "Thành tiền", "Ghi chú"};
        bangModel = new DefaultTableModel(cols, 0);

        tblMonDat = new JTable(bangModel);
        tblMonDat.setRowHeight(35);
        tblMonDat.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        JTableHeader header = tblMonDat.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(MAU_DO_RUOU); // màu đỏ rượu
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Times New Roman", Font.BOLD, 18));
                label.setHorizontalAlignment(CENTER);
                label.setOpaque(true);
                return label;
            }
        });

        JScrollPane scr = new JScrollPane(tblMonDat);
        scr.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(new BorderLayout());
        add(scr, BorderLayout.CENTER);
    }

    private void taiDuLieuTuDB() {
        try {
            danhSachMon = monAnDAO.getAllMonAn();
            if (danhSachMon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có món ăn nào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

            if (maPhieu != null && !maPhieu.isEmpty()) {
                maHoaDon = hoaDonDAO.layMaHoaDonTheoPhieu(maPhieu);
                if (maHoaDon != null) {
                    List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.layChiTietTheoHoaDon(maHoaDon);
                    for (ChiTietHoaDon ct : chiTietList) {
                        MonAn mon = monAnDAO.layMonAnTheoMa(ct.getMaMon());
                        if (mon != null) {
                            MonDat item = new MonDat(mon, ct.getSoLuong());
                            item.datGhiChu(ct.getGhiChu());
                            danhSachMonDat.add(item);
                        }
                    }
                }
            }
            capNhatBang();
            capNhatLuoiMon();
            capNhatTongTien();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void taiDuLieuTuBan() {
        try {
            if (maPhieu != null && !maPhieu.isEmpty()) {
                String ten = phieuDatBanDAO.layTenKhachHang(maPhieu);
                if (ten != null && !ten.trim().isEmpty()) {
                    tenKhach = ten;
                }
            }

            TaiKhoan current = TaiKhoan_DAO.getCurrentTaiKhoan();
            if (current != null) {
                maNhanVien = current.getMaNhanVien();
                tenNhanVien = current.getHoTen();
            }
        } catch (Exception e) {
            tenKhach = "Khách vãng lai";
            tenNhanVien = "Nhân viên mặc định";
        }
    }

    private void khoiTaoComboBoxLoai() {
        List<LoaiMon> list = loaiMonDAO.getAllLoaiMon();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả");
        Set<String> set = new HashSet<>();
        for (LoaiMon lm : list) {
            if (!set.contains(lm.getTenLoai())) {
                set.add(lm.getTenLoai());
                model.addElement(lm.getTenLoai());
            }
        }
        cmbLocLoai = new JComboBox<>(model);
        cmbLocLoai.setFont(FONT_NOI_DUNG);
    }

    private void taoGiaoDien() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(MAU_TRANG);

        // HEADER
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(MAU_DO_RUOU);
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        
        pnlHeader.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblTieuDe = new JLabel("ĐẶT MÓN", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TIEU_DE);
        lblTieuDe.setForeground(MAU_TRANG);
        pnlHeader.add(lblTieuDe, BorderLayout.CENTER);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // MAIN SPLIT
        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitMain.setDividerLocation(0.7);
        splitMain.setResizeWeight(0.7);

        // LEFT: THỰC ĐƠN
        JPanel pnlThucDon = new JPanel(new BorderLayout());
        pnlThucDon.setBackground(MAU_TRANG);
        pnlThucDon.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTimKiem.setBackground(MAU_TRANG);
        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(FONT_NOI_DUNG);
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(FONT_NOI_DUNG);
        JButton btnTim = new JButton("Tìm");
        kieuNut(btnTim, MAU_DO_RUOU);
        btnTim.setPreferredSize(new Dimension(80, 35));
        btnTim.addActionListener(e -> capNhatLuoiMon());
        JLabel lblLoc = new JLabel("Loại món:");
        lblLoc.setFont(FONT_NOI_DUNG);

        pnlTimKiem.add(lblTim); pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTim); pnlTimKiem.add(lblLoc); pnlTimKiem.add(cmbLocLoai);
        pnlThucDon.add(pnlTimKiem, BorderLayout.NORTH);

        pnlLuoiMon = new JPanel(new GridLayout(0, 4, 25, 25));
        pnlLuoiMon.setBackground(MAU_TRANG);
        JScrollPane scrMon = new JScrollPane(pnlLuoiMon);
        scrMon.setBorder(BorderFactory.createEmptyBorder());
        scrMon.getViewport().setBackground(MAU_TRANG);
        pnlThucDon.add(scrMon, BorderLayout.CENTER);
        splitMain.setLeftComponent(pnlThucDon);

        // RIGHT: PHIẾU ĐẶT
        splitMain.setRightComponent(taoPanelPhieuDatMon());

        getContentPane().add(splitMain, BorderLayout.CENTER);
    }

    private JPanel taoCardMon(MonAn mon) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(MAU_TRANG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_DO_RUOU, 2, true),
                new EmptyBorder(2, 2, 2, 2)));

        Dimension cardSize = new Dimension(210, 280);
        card.setPreferredSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setMaximumSize(cardSize);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(MAU_HONG_NHAT); }
            @Override public void mouseExited(MouseEvent e) { card.setBackground(MAU_TRANG); }
            @Override public void mouseClicked(MouseEvent e) { themMonDat(mon); }
        });

        // ẢNH TỪ THƯ MỤC img/ (DỰ ÁN JAR)
        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setPreferredSize(new Dimension(240, 160));
        String anh = mon.getAnhMon();
        if (anh != null && !anh.trim().isEmpty()) {
            URL imgURL = getClass().getResource("img/" + anh);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(280, 160, Image.SCALE_SMOOTH);
                lblAnh.setIcon(new ImageIcon(img));
            } else {
                lblAnh.setText("NO IMAGE");
                lblAnh.setForeground(MAU_DO_RUOU);
                lblAnh.setFont(new Font("Times New Roman", Font.BOLD, 18));
            }
        } else {
            lblAnh.setText("NO IMAGE");
            lblAnh.setForeground(MAU_DO_RUOU);
            lblAnh.setFont(new Font("Times New Roman", Font.BOLD, 18));
        }
        card.add(lblAnh, BorderLayout.NORTH);

        // THÔNG TIN
        JPanel info = new JPanel(new GridBagLayout());
        info.setBackground(card.getBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblMa = new JLabel(mon.getMaMon());
        lblMa.setFont(FONT_CARD_TEN);
        lblMa.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(lblMa, gbc);

        gbc.gridy = 1;
        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(FONT_CARD_TEN);
        lblTen.setHorizontalAlignment(SwingConstants.CENTER);
        info.add(lblTen, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTT = new JLabel("Trạng thái: ");
        lblTT.setFont(FONT_NOI_DUNG);
        info.add(lblTT, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        String trangThai = mon.isTrangThai() ? "Còn món" : "Hết món";
        JLabel lblTTVal = new JLabel(trangThai);
        lblTTVal.setFont(FONT_NOI_DUNG);
        lblTTVal.setForeground(mon.isTrangThai() ? new Color(102, 210, 74) : Color.RED);
        info.add(lblTTVal, gbc);

//        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
//        String moTa = mon.getMoTa() != null ? mon.getMoTa() : "Không có mô tả";
//        JTextArea txtMoTa = new JTextArea("Mô tả: " + moTa);
//        txtMoTa.setFont(FONT_CARD_MOTA);
//        txtMoTa.setLineWrap(true);
//        txtMoTa.setWrapStyleWord(true);
//        txtMoTa.setEditable(false);
//        txtMoTa.setBackground(card.getBackground());
//        txtMoTa.setBorder(BorderFactory.createEmptyBorder());
//        txtMoTa.setPreferredSize(new Dimension(0, 50));
//        info.add(txtMoTa, gbc);

        gbc.gridy = 3;
        JLabel lblGia = new JLabel("Giá: " + String.format("%,.0f đ", mon.getDonGia()));
        lblGia.setFont(FONT_CARD_GIA);
        lblGia.setForeground(MAU_DO_RUOU);
        info.add(lblGia, gbc);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel taoPanelPhieuDatMon() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(MAU_TRANG);
        pnl.setPreferredSize(new Dimension(460, 0));

        // HEADER PHIẾU
        JLabel title = new JLabel("PHIẾU ĐẶT MÓN", SwingConstants.CENTER);
        title.setFont(FONT_TIEU_DE_NHO);
        title.setForeground(MAU_TRANG);
        title.setOpaque(true);
        title.setBackground(MAU_DO_RUOU);
        title.setBorder(new EmptyBorder(18, 0, 18, 0));
        pnl.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(3, 2, 15, 15));
        info.setBackground(MAU_TRANG);
        info.setBorder(new EmptyBorder(20, 30, 20, 30));

        info.add(taoLabel("Tên Khách hàng:", FONT_NOI_DUNG, Font.BOLD));
        info.add(taoTextField(tenKhach, false));
        info.add(taoLabel("Tên Nhân viên:", FONT_NOI_DUNG, Font.BOLD));
        info.add(taoTextField(tenNhanVien, false));
        info.add(taoLabel("Mã Bàn:", FONT_NOI_DUNG, Font.BOLD));
        info.add(taoTextField(maBan, false));

        pnl.add(info, BorderLayout.NORTH);

        JScrollPane scr = new JScrollPane(tblMonDat);
        scr.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        pnl.add(scr, BorderLayout.CENTER);

        // TỔNG TIỀN + 3 NÚT: XÓA - LƯU - THANH TOÁN
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(MAU_TRANG);
        bottom.setBorder(new EmptyBorder(15, 30, 20, 30));

        lblTongTien = new JLabel("TỔNG TIỀN: 0 đ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTongTien.setForeground(MAU_DO_RUOU);
        bottom.add(lblTongTien, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttons.setBackground(MAU_TRANG);

        JButton btnXoa = new JButton("XÓA");
        kieuNut(btnXoa, new Color(231, 76, 60));
        btnXoa.setPreferredSize(new Dimension(120, 40));
        btnXoa.addActionListener(e -> xoaMonDat());

        JButton btnLuu = new JButton("LƯU");
        kieuNut(btnLuu, new Color(52, 152, 219));
        btnLuu.setPreferredSize(new Dimension(120, 40));
        btnLuu.addActionListener(e -> luuDanhSachMon());

        JButton btnThanhToan = new JButton("THANH TOÁN");
        kieuNut(btnThanhToan, new Color(102, 210, 74));
        btnThanhToan.addActionListener(e -> thanhToan());
        btnThanhToan.setPreferredSize(new Dimension(180, 40));
        
        buttons.add(btnXoa); buttons.add(btnLuu); buttons.add(btnThanhToan);
        bottom.add(buttons, BorderLayout.CENTER);

        pnl.add(bottom, BorderLayout.SOUTH);
        return pnl;
    }

    private JLabel taoLabel(String text, Font font, int style) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font.deriveFont(style));
        return lbl;
    }

    private JTextField taoTextField(String text, boolean editable) {
        JTextField txt = new JTextField(text);
        txt.setFont(FONT_NOI_DUNG);
        txt.setEditable(editable);
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return txt;
    }

    private void kieuNut(JButton button, Color baseColor) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFont(FONT_NUT);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);

        Color hover = baseColor.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
        });
    }

    private void themSuKien() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void removeUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void changedUpdate(DocumentEvent e) { capNhatLuoiMon(); }
        });
        cmbLocLoai.addActionListener(e -> capNhatLuoiMon());
    }

    private void capNhatLuoiMon() {
        pnlLuoiMon.removeAll();
        String keyword = txtTimKiem.getText().toLowerCase().trim();
        String loai = (String) cmbLocLoai.getSelectedItem();

        for (MonAn m : danhSachMon) {
            if (!m.isTrangThai()) continue;
            if (!keyword.isEmpty() && !m.getTenMon().toLowerCase().contains(keyword)) continue;
            if (!"Tất cả".equals(loai) && !m.getLoaiMon().getTenLoai().equals(loai)) continue;

            pnlLuoiMon.add(taoCardMon(m));
        }
        pnlLuoiMon.revalidate();
        pnlLuoiMon.repaint();
    }

    private void themMonDat(MonAn mon) {
        for (MonDat item : danhSachMonDat) {
            if (item.mon.getMaMon().equals(mon.getMaMon())) {
                item.soLuong++;
                capNhatBang(); capNhatTongTien();
                return;
            }
        }
        danhSachMonDat.add(new MonDat(mon, 1));
        capNhatBang(); capNhatTongTien();
    }

    private void capNhatBang() {
        bangModel.setRowCount(0);
        int stt = 1;
        for (MonDat item : danhSachMonDat) {
            bangModel.addRow(new Object[]{
                stt++, item.mon.getTenMon(), item.soLuong,
                String.format("%,.0f đ", item.mon.getDonGia()),
                String.format("%,.0f đ", item.tinhThanhTien()),
                item.layGhiChu()
            });
        }
    }

    private void capNhatTongTien() {
        tongTien = danhSachMonDat.stream().mapToDouble(MonDat::tinhThanhTien).sum();
        lblTongTien.setText(String.format("TỔNG TIỀN: %,d đ", (int) tongTien));
    }

    private void xoaMonDat() {
        int row = tblMonDat.getSelectedRow();
        if (row >= 0) {
            danhSachMonDat.remove(row);
            capNhatBang(); capNhatTongTien();
        }
    }

    private void luuDanhSachMon() {
        try {
            if (danhSachMonDat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa chọn món!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (maHoaDon == null) {
                maHoaDon = hoaDonDAO.taoHoaDonMoi(maBan, maPhieu, maNhanVien);
            }

            chiTietHoaDonDAO.xoaChiTietTheoHoaDon(maHoaDon);
            for (MonDat item : danhSachMonDat) {
                ChiTietHoaDon ct = new ChiTietHoaDon(maHoaDon, item.mon.getMaMon(), item.soLuong, item.mon.getDonGia(), item.layGhiChu());
                chiTietHoaDonDAO.themChiTiet(ct);
            }

            JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void thanhToan() {
        if (danhSachMonDat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có món để thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        luuDanhSachMon(); // Lưu trước

        // Cập nhật trạng thái phiếu: đã thanh toán
		phieuDatBanDAO.capNhatTrangThaiPhieu(maPhieu, "Đã thanh toán");
		JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nTổng tiền: " + String.format("%,.0f đ", tongTien), "Thành công", JOptionPane.INFORMATION_MESSAGE);
		quayLaiPhucVu();
    }

    private void quayLaiPhucVu() {
        if (frmPhucVu != null) {
            frmPhucVu.capNhatMonDat(layDanhSachMonDat(), layTongTienMon());
            frmPhucVu.setVisible(true);
        }
        dispose();
    }

    public String layDanhSachMonDat() {
        if (danhSachMonDat.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (MonDat item : danhSachMonDat) {
            sb.append(String.format("• %dx %s (%,.0f đ)\n", item.soLuong, item.mon.getTenMon(), item.tinhThanhTien()));
        }
        return sb.toString().trim();
    }

    public double layTongTienMon() { return tongTien; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                try {
					new FrmDatMon(null, "BAN001");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } catch (Exception e) {
                e.printStackTrace();
			}
        });
    }
}