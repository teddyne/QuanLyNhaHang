package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.AlphaComposite;
import connectSQL.ConnectSQL;
import entity.Ban;
import entity.ChiTietPhieuDatBan;
import entity.ChiTietHoaDon;
import entity.KhuVuc;
import entity.PhieuDatBan;
import dao.*;
import dialog.FrmPhucVu;
import entity.*;
import entity.MonDat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.net.URL;
import java.util.*;


public class FrmDatMon extends JDialog {
    private static final Color MAU_DO_RUOU = new Color(169, 55, 68);
    private static final Color MAU_HONG_NHAT = new Color(255, 230, 230);
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Color MAU_HONG = new Color(255, 240, 245);
    private static final Color MAU_XANH_LA = new Color(46, 204, 113);
    private static final Color MAU_XAM = Color.LIGHT_GRAY;

    private static final Font FONT_TIEU_DE = new Font("Times New Roman", Font.BOLD, 32);
    private static final Font FONT_TIEU_DE_NHO = new Font("Times New Roman", Font.BOLD, 26);
    private static final Font FONT_NOI_DUNG = new Font("Times New Roman", Font.PLAIN, 18);
    private static final Font FONT_CARD_TEN = new Font("Times New Roman", Font.BOLD, 20);
    private static final Font FONT_CARD_GIA = new Font("Times New Roman", Font.BOLD, 22);
    private static final Font FONT_NUT = new Font("Times New Roman", Font.BOLD, 18);

    private MonAn_DAO monAnDAO;
    private LoaiMon_DAO loaiMonDAO;
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
    private String maNhanVien = "NV0001";
    private String tenKhach = "Khách thành viên";
    private String tenNhanVien = "Bùi Ngọc Hiền";
    private FrmPhucVu frmPhucVu;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private JPanel khungDangChon = null;    
    private MonAn monDangChon = null;
    
    public FrmDatMon(Window parent, Connection con, String maPhieu, String maBan) throws SQLException {
        super(parent, "ĐẶT MÓN - Bàn " + maBan, ModalityType.APPLICATION_MODAL);
        this.con = con;
        this.maPhieu = maPhieu;  
        this.maBan = maBan;

        initComponents();
        hienThiDanhSachMonDaDat();
        setSize(1500, 800);
        setLocationRelativeTo(parent);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    }

    private void initComponents() throws SQLException {
        khoiTaoDAO();
        taiDuLieuTuBan();
        khoiTaoBang();
        khoiTaoComboBoxLoai();
        taoGiaoDien(); 
        taiDuLieuTuDB();
        capNhatLuoiMon();
        hienThiDanhSachMonDaDat();
        themSuKien();
    }
    
    
    private void khoiTaoDAO() throws SQLException {
    monAnDAO = new MonAn_DAO(con);
    loaiMonDAO = new LoaiMon_DAO(con);
    phieuDatBanDAO = new PhieuDatBan_DAO(ConnectSQL.getConnection());
    }
    
    private void khoiTaoBang() {
        String[] cols = {"STT", "Món ăn", "Số lượng", "Giá", "Thành tiền", "Ghi chú"};
        bangModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2 || c == 5; } // chỉ sửa số lượng và ghi chú
        };
        tblMonDat = new JTable(bangModel);
        tblMonDat.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        tblMonDat.setRowHeight(56);

        JTableHeader header = tblMonDat.getTableHeader();
        header.setBackground(MAU_DO_RUOU);  
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Times New Roman", Font.BOLD, 20));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                label.setBackground(MAU_DO_RUOU);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Times New Roman", Font.BOLD, 20));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0)); 
                return label;
            }
        });

        TableColumn slCol = tblMonDat.getColumnModel().getColumn(2);
        slCol.setCellRenderer(new SoLuongRenderer());
        slCol.setCellEditor(new SoLuongEditor());
        slCol.setPreferredWidth(140);
        slCol.setMaxWidth(200);
    }
    
    private void taiDuLieuTuDB() {
        try {
            danhSachMon = monAnDAO.getAllMonAn();
            if (danhSachMon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có món ăn nào!");
                return;
            }

            if (maPhieu != null && !maPhieu.isEmpty()) {

                ChiTietPhieuDatBan_DAO chiTietDAO = new ChiTietPhieuDatBan_DAO();
                List<ChiTietPhieuDatBan> chiTietList = chiTietDAO.layTheoMaPhieu(maPhieu);

                for (ChiTietPhieuDatBan ct : chiTietList) {
                    MonAn mon = monAnDAO.layMonAnTheoMa(ct.getMaMon());
                    if (mon != null) {
                        MonDat item = new MonDat(mon, ct.getSoLuong());
                        item.setGhiChu(ct.getGhiChu());
                        danhSachMonDat.add(item);
                    }
                }
            }
            capNhatBang();
            capNhatTongTien();
            capNhatLuoiMon();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu món: " + e.getMessage());
        }
    }
    
    private void taiDuLieuTuBan() {
        tenKhach = "Khách vãng lai"; 

        if (maPhieu != null && !maPhieu.trim().isEmpty()) {
            try {
                String ten = phieuDatBanDAO.layTenKhachHang(maPhieu);
                if (ten != null && !ten.trim().isEmpty()) {
                    tenKhach = ten;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        TaiKhoan current = TaiKhoan_DAO.getCurrentTaiKhoan();
        if (current != null) {
            maNhanVien = current.getMaNhanVien();
            tenNhanVien = current.getHoTen();
        } else {
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

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(MAU_DO_RUOU);
        pnlHeader.setBorder(new EmptyBorder(10, 15, 10, 15));

        ImageIcon icon = new ImageIcon(
                new ImageIcon("img/quaylai.png").getImage()
                        .getScaledInstance(35, 35, Image.SCALE_SMOOTH)
        );
        JLabel lblBack = new JLabel(icon);
        lblBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        JLabel lblTieuDe = new JLabel("ĐẶT MÓN", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TIEU_DE);
        lblTieuDe.setForeground(MAU_TRANG);

        pnlHeader.add(lblBack, BorderLayout.WEST);
        pnlHeader.add(lblTieuDe, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);


        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitMain.setResizeWeight(0.5);
        splitMain.setDividerLocation(0.5);

        // TRÁI: THỰC ĐƠN
        JPanel pnlThucDon = new JPanel(new BorderLayout());
        pnlThucDon.setBackground(MAU_TRANG);
        pnlThucDon.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTimKiem.setBackground(MAU_TRANG);
        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(FONT_NOI_DUNG);
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(FONT_NOI_DUNG);

        JLabel lblLoc = new JLabel("Loại món:");
        lblLoc.setFont(FONT_NOI_DUNG);

        pnlTimKiem.add(lblTim);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(lblLoc);
        pnlTimKiem.add(cmbLocLoai);

        pnlThucDon.add(pnlTimKiem, BorderLayout.NORTH);

        // CARD MỘT HÀNG
        pnlLuoiMon = new JPanel(new GridLayout(0, 3, 30, 30));
        pnlLuoiMon.setBackground(MAU_TRANG);
        JScrollPane scrMon = new JScrollPane(pnlLuoiMon);
        scrMon.setBorder(BorderFactory.createEmptyBorder());
        scrMon.getViewport().setBackground(MAU_TRANG);
        pnlThucDon.add(scrMon, BorderLayout.CENTER);
        splitMain.setLeftComponent(pnlThucDon);

        // PHẢI: PHIẾU
        splitMain.setRightComponent(taoPanelPhieuDatMon());

        getContentPane().add(splitMain, BorderLayout.CENTER);
    }

    private JPanel taoPanelPhieuDatMon() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(MAU_TRANG);

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

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(MAU_TRANG);
        bottom.setBorder(new EmptyBorder(15, 30, 20, 30));

        lblTongTien = new JLabel("TỔNG TIỀN: 0 đ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblTongTien.setForeground(MAU_DO_RUOU);
        bottom.add(lblTongTien, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 10));
        buttons.setBackground(MAU_TRANG);

        JButton btnXoa = new JButton("XÓA");
        kieuNut(btnXoa, new Color(231, 76, 60));
        btnXoa.setPreferredSize(new Dimension(180, 50));
        btnXoa.addActionListener(e -> xoaMonDat());

        JButton btnLuu = new JButton("LƯU");
        kieuNut(btnLuu, new Color(52, 152, 219));
        btnLuu.setPreferredSize(new Dimension(220, 50));
        btnLuu.addActionListener(e -> {
            if (maPhieu != null && !maPhieu.trim().isEmpty()) {
                if (luuVaoChiTietDatMon()) {
                    if (frmPhucVu != null) frmPhucVu.capNhatMonDat();
                    JOptionPane.showMessageDialog(this, "Đã lưu món thành công!");
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Đã lưu tạm món cho bàn " + maBan);
                dispose(); 
            }
        });

        buttons.add(btnXoa);
        buttons.add(btnLuu);

        bottom.add(buttons, BorderLayout.CENTER);
        pnl.add(bottom, BorderLayout.SOUTH);
        
        return pnl;
    }

    private boolean luuVaoChiTietDatMon() {
        try {
            String maPhieuHienTai = this.maPhieu;

            if (maPhieuHienTai == null || maPhieuHienTai.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy mã phiếu!");
                return false;
            }

            ChiTietPhieuDatBan_DAO ctDAO = new ChiTietPhieuDatBan_DAO();

            for (MonDat item : danhSachMonDat) {

                ChiTietPhieuDatBan ct = new ChiTietPhieuDatBan();
                ct.setMaPhieu(maPhieuHienTai);
                ct.setMaMon(item.mon.getMaMon());
                ct.setSoLuong(item.soLuong);
                ct.setDonGia(item.mon.getDonGia());
                ct.setGhiChu(item.getGhiChu() != null ? item.getGhiChu() : "");

                ctDAO.luuMon(ct); 
            }

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi lưu món: " + ex.getMessage());
            return false;
        }
    }

    private JPanel taoCardMon(MonAn mon) {
        JPanel khung = new JPanel(new BorderLayout(5, 5));
        khung.setBackground(MAU_TRANG);
        khung.setPreferredSize(new Dimension(220, 300));
        khung.setMaximumSize(new Dimension(220, 300));
        khung.setMinimumSize(new Dimension(220, 300));
        khung.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_DO_RUOU, 2, true),
                new EmptyBorder(8, 8, 8, 8)));

        khung.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (khung != khungDangChon) khung.setBackground(MAU_HONG);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (khung != khungDangChon) khung.setBackground(MAU_TRANG);
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (khungDangChon != null) {
                    khungDangChon.setBackground(MAU_TRANG);
                    khungDangChon.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(MAU_DO_RUOU, 2, true),
                            new EmptyBorder(8, 8, 8, 8)));
                }
                khungDangChon = khung;
                khung.setBackground(MAU_HONG);
                khung.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(MAU_XANH_LA, 3, true),
                        new EmptyBorder(8, 8, 8, 8)));
                themMonDat(mon);
            }
        });

        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setPreferredSize(new Dimension(200, 160));
        lblAnh.setBorder(BorderFactory.createLineBorder(MAU_XAM, 1));
        String duongDan = mon.getAnhMon() != null && !mon.getAnhMon().isEmpty() ? mon.getAnhMon() : "img/placeholder.png";
        ImageIcon icon = new ImageIcon(duongDan);
        if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
            icon = new ImageIcon("img/placeholder.png");
        }
        Image img = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
        lblAnh.setIcon(new ImageIcon(img));
        lblAnh.setText(""); 
        khung.add(lblAnh, BorderLayout.NORTH);

        JPanel pnlTT = new JPanel(new GridBagLayout());
        pnlTT.setBackground(MAU_TRANG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 4, 2, 4);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMa = new JLabel("<html><u>" + mon.getMaMon() + "</u></html>");
        lblMa.setFont(FONT_NOI_DUNG);
        pnlTT.add(lblMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTen.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTT.add(lblTen, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblGia = new JLabel(String.format("%,.0f đ", mon.getDonGia()));
        lblGia.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblGia.setForeground(MAU_DO_RUOU);
        pnlTT.add(lblGia, gbc);

        khung.add(pnlTT, BorderLayout.CENTER);
        return khung;
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
    public void setFrmPhucVu(FrmPhucVu frmPhucVu) {
        this.frmPhucVu = frmPhucVu;
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

    private void capNhatTongTien() {
        tongTien = danhSachMonDat.stream()
                .mapToDouble(MonDat::getThanhTien)
                .sum();
        lblTongTien.setText(String.format("TỔNG TIỀN: %,d đ", (int) tongTien));
    }
    
    private void xoaMonDat() {
    	if (tblMonDat.isEditing()) {
            tblMonDat.getCellEditor().stopCellEditing();
        }
        int row = tblMonDat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }

        MonDat item = danhSachMonDat.get(row);
        String maMon = item.mon.getMaMon();

        try {
            if (maPhieu != null && !maPhieu.trim().isEmpty()) {
                ChiTietPhieuDatBan_DAO ctDAO = new ChiTietPhieuDatBan_DAO();
                ctDAO.xoaMon(maPhieu, maMon);
            }

            danhSachMonDat.remove(row);
            capNhatBang();
            capNhatTongTien();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi xóa món: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void themSuKien() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void removeUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void changedUpdate(DocumentEvent e) { capNhatLuoiMon(); }
        });
        cmbLocLoai.addActionListener(e -> capNhatLuoiMon());
    }
    
    private void quayLaiPhucVu() {
        if (frmPhucVu != null) {
            frmPhucVu.capNhatMonDat();
            frmPhucVu.setVisible(true);
        }
        dispose();
    }
    public String layDanhSachMonDat() {
        StringBuilder sb = new StringBuilder();
        for (MonDat item : danhSachMonDat) {
            sb.append(item.getMon().getTenMon())
              .append(" x").append(item.getSoLuong())
              .append("\n");
        }
        return sb.toString();
    }

    
    public List<MonDat> getDanhSachMonDat() {
        return new ArrayList<>(danhSachMonDat); 
    }
    private void hienThiDanhSachMonDaDat() {
    bangModel.setRowCount(0);
    int stt = 1;
    for (MonDat item : danhSachMonDat) {
        bangModel.addRow(new Object[]{
            stt++,                    
            item.mon.getTenMon(),    
            item.soLuong,
            String.format("%,.0f đ", item.mon.getDonGia()),
            String.format("%,.0f đ", item.getThanhTien()),
            item.getGhiChu() == null ? "" : item.getGhiChu()
        });
    }
}
    public double layTongTienMon() { return tongTien; }

    private void capNhatBang() {
        bangModel.setRowCount(0);
        int stt = 1;
        for (MonDat item : danhSachMonDat) {
            bangModel.addRow(new Object[]{
                stt++,
                item.mon.getTenMon(),
                item.soLuong,                                           
                String.format("%,.0f đ", item.mon.getDonGia()),
                String.format("%,.0f đ", item.soLuong * item.mon.getDonGia()),
                item.getGhiChu() != null ? item.getGhiChu() : ""
            });
        }
    }
    
   
    public void refreshThucDon() throws SQLException {
        danhSachMon = monAnDAO.getAllMonAn();  
		capNhatLuoiMon();                     
    }
    
    
    class SoLuongRenderer extends JPanel implements TableCellRenderer {
        private final JLabel lbl = new JLabel("1", SwingConstants.CENTER);
        private final JButton btnMinus = new JButton("-");
        private final JButton btnPlus  = new JButton("+");

        public SoLuongRenderer() {
            setLayout(new BorderLayout(4, 0));
            setBackground(Color.WHITE);

            btnMinus.setMargin(new Insets(0, 0, 0, 0));
            btnPlus.setMargin(new Insets(0, 0, 0, 0));
            btnMinus.setPreferredSize(new Dimension(30, 30));
            btnPlus.setPreferredSize(new Dimension(30, 30));

            btnMinus.setContentAreaFilled(false);
            btnMinus.setOpaque(true);
            btnPlus.setContentAreaFilled(false);
            btnPlus.setOpaque(true);

            btnMinus.setBackground(MAU_DO_RUOU);  
            btnPlus.setBackground(MAU_XANH_LA);  
            btnMinus.setForeground(Color.WHITE);
            btnPlus.setForeground(Color.WHITE);
            btnMinus.setFont(new Font("Times New Roman", Font.BOLD, 16));
            btnPlus.setFont(new Font("Times New Roman", Font.BOLD, 16));
            btnMinus.setFocusPainted(false);
            btnPlus.setFocusPainted(false);

            lbl.setFont(new Font("Times New Roman", Font.BOLD, 17));

            add(btnMinus, BorderLayout.WEST);
            add(lbl, BorderLayout.CENTER);
            add(btnPlus, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            lbl.setText(value != null ? value.toString() : "0");
            return this;
        }
    }

    class SoLuongEditor extends DefaultCellEditor {
        private final JPanel panel = new JPanel(new BorderLayout(4, 0));
        private final JLabel lbl = new JLabel("1", SwingConstants.CENTER);
        private int currentRow;

        public SoLuongEditor() {
            super(new JCheckBox());
            panel.setBackground(Color.WHITE);
            lbl.setFont(new Font("Times New Roman", Font.BOLD, 17));

            JButton btnMinus = new JButton("-");
            JButton btnPlus  = new JButton("+");

            btnMinus.setMargin(new Insets(0, 0, 0, 0));
            btnPlus.setMargin(new Insets(0, 0, 0, 0));
            btnMinus.setPreferredSize(new Dimension(30, 30));
            btnPlus.setPreferredSize(new Dimension(30, 30));

            btnMinus.setContentAreaFilled(false);
            btnMinus.setOpaque(true);
            btnPlus.setContentAreaFilled(false);
            btnPlus.setOpaque(true);

            btnMinus.setBackground(MAU_DO_RUOU);
            btnPlus.setBackground(MAU_XANH_LA);
            btnMinus.setForeground(Color.WHITE);
            btnPlus.setForeground(Color.WHITE);
            btnMinus.setFont(new Font("Times New Roman", Font.BOLD, 16));
            btnPlus.setFont(new Font("Times New Roman", Font.BOLD, 16));

            btnPlus.addActionListener(e -> {
                MonDat md = danhSachMonDat.get(currentRow);
                md.soLuong++;
                lbl.setText(md.soLuong + "");   
                fireEditingStopped();     
                capNhatBang();
                capNhatTongTien();
            });

            btnMinus.addActionListener(e -> {
                MonDat md = danhSachMonDat.get(currentRow);
                if (md.soLuong > 1) {
                    md.soLuong--;
                    lbl.setText(md.soLuong + "");
                } else {
                    danhSachMonDat.remove(currentRow); 
                }
                fireEditingStopped(); 
                capNhatBang();
                capNhatTongTien();
            });


            panel.add(btnMinus, BorderLayout.WEST);
            panel.add(lbl, BorderLayout.CENTER);
            panel.add(btnPlus, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            lbl.setText(danhSachMonDat.get(row).soLuong + "");
            return panel;
        }

        @Override public Object getCellEditorValue() { return lbl.getText(); }
        @Override public boolean stopCellEditing() { fireEditingStopped(); return true; }
    }
    
}