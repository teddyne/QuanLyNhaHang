package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.ChiTietPhieuDatBan_DAO;
import dao.HoaDon_DAO;
import dao.LoaiBan_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.ChiTietPhieuDatBan;
import entity.LoaiBan;
import entity.MonAn;
import entity.PhieuDatBan;
import gui.FrmBan;
import gui.FrmDatMon;
import gui.FrmThanhToan;
import gui.ThanhTacVu;

import java.io.File;

public class FrmPhucVu extends JFrame {
    private static final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private Ban_DAO banDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private Connection con = ConnectSQL.getConnection();
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private String maBan;
    private Ban ban;
    private HoaDon_DAO hoaDonDAO;
    private LoaiBan loaiBan;
    private PhieuDatBan phieuDangPhucVu;    
    private JTable tableMon;
    private DefaultTableModel modelMon;
    private JLabel lblTotal;
    private ChiTietPhieuDatBan_DAO chiTietPDB_DAO; 

    public FrmPhucVu(JFrame parent, String maBan, PhieuDatBan phieuDatBan, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO, LoaiBan_DAO loaiBanDAO) throws SQLException {
    	if (phieuDatBan == null) {
            throw new IllegalArgumentException(
            );
        }

    	setTitle("Phục vụ - Bàn " + maBan);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            this.hoaDonDAO = HoaDon_DAO.getInstance();
            this.maBan = maBan;
            this.banDAO = banDAO;
            this.phieuDatBanDAO = phieuDatBanDAO;
            this.ban = banDAO.getBanByMa(maBan);
            this.phieuDangPhucVu = phieuDatBan;
            this.chiTietPDB_DAO = new ChiTietPhieuDatBan_DAO(con);
            this.chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
            System.out.println("phieuDatBan: " + (phieuDatBan != null ? phieuDatBan.getTenKhach() : "null"));
            this.loaiBan = loaiBanDAO.getLoaiBanByMa(ban.getMaLoai());
            if (this.loaiBan == null) {
                this.loaiBan = new LoaiBan();
                this.loaiBan.setTenLoai("Thường");
            }

            if ("Đặt".equals(phieuDangPhucVu.getTrangThai())) {
                phieuDatBanDAO.capNhatTrangThaiPhieu(
                    phieuDangPhucVu.getMaPhieu(),
                    "Phục vụ"
                );
                phieuDangPhucVu.setTrangThai("Phục vụ");
            }

            initComponents();
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khởi tạo form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() throws SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thiết lập giao diện hệ thống: " + e.getMessage());
        }
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setSize(900, 700);
        setLocationRelativeTo(getParent());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel pnlLeft = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeft.setOpaque(false);
        ImageIcon originalIcon = new ImageIcon("img/quaylai.png");
        java.awt.Image scaledImg = originalIcon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel lblBack = new JLabel(new ImageIcon(scaledImg));
        lblBack.setToolTipText("Quay lại trang bàn");
        lblBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (getParent() instanceof FrmBan) {
                    ((FrmBan) getParent()).taiLaiBangChinh();
                }
                dispose();
            }
        });
        pnlLeft.add(lblBack);
        // ---- TIÊU ĐỀ ----
        JLabel lblTitle = new JLabel("BÀN ĐANG PHỤC VỤ", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        pnlHeader.add(pnlLeft, BorderLayout.WEST);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        // Nội dung chính
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        // Panel thông tin bàn
        JPanel pnlBan = new JPanel(new GridBagLayout());
        pnlBan.setBackground(Color.WHITE);
        pnlBan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "THÔNG TIN BÀN",
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 20)));
        pnlBan.setPreferredSize(new Dimension(200, 0));
        GridBagConstraints banGbc = new GridBagConstraints();
        banGbc.insets = new Insets(5, 15, 5, 15);
        banGbc.anchor = GridBagConstraints.WEST;
        int banRow = 0;
        banGbc.gridx = 0; banGbc.gridy = banRow; banGbc.gridwidth = 2;
        banGbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblBanTitle = new JLabel("BÀN " + ban.getMaBan(), SwingConstants.CENTER);
        lblBanTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblBanTitle, banGbc);
        banRow++;
        banGbc.gridwidth = 1; banGbc.fill = GridBagConstraints.NONE;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblKhuVucBan = new JLabel("Khu vực:");
        lblKhuVucBan.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblKhuVucBan, banGbc);
        banGbc.gridx = 1;
        JLabel valKhuVucBan = new JLabel(ban.getTenKhuVuc() != null ? ban.getTenKhuVuc() : "Không xác định");
        valKhuVucBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valKhuVucBan, banGbc);
        banRow++;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblSoGheBan = new JLabel("Số ghế:");
        lblSoGheBan.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblSoGheBan, banGbc);
        banGbc.gridx = 1;
        JLabel valSoGheBan = new JLabel(String.valueOf(ban.getSoChoNgoi()));
        valSoGheBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valSoGheBan, banGbc);
        banRow++;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblLoaiBanLabel = new JLabel("Loại bàn:");
        lblLoaiBanLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblLoaiBanLabel, banGbc);
        banGbc.gridx = 1;
        JLabel lblLoai = new JLabel(loaiBan.getTenLoai() != null ? loaiBan.getTenLoai() : "Thường");
        lblLoai.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        if ("VIP".equals(loaiBan.getTenLoai())) {
            lblLoai.setForeground(new Color(238, 180, 34));
        }
        pnlBan.add(lblLoai, banGbc);
        banRow++;
        // NGÀY ĐẾN & GIỜ ĐẾN
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblNgayDen = new JLabel("Ngày đến:");
        lblNgayDen.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblNgayDen, banGbc);
        banGbc.gridx = 1;
        String ngayDenStr = (phieuDangPhucVu != null && phieuDangPhucVu.getNgayDen() != null)
                ? new SimpleDateFormat("dd/MM/yyyy").format(phieuDangPhucVu.getNgayDen()) : "Chưa có";
        JLabel valNgayDen = new JLabel(ngayDenStr);
        valNgayDen.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valNgayDen, banGbc);
        banRow++;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblGioDen = new JLabel("Giờ đến:");
        lblGioDen.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblGioDen, banGbc);
        banGbc.gridx = 1;
        String gioDenStr = (phieuDangPhucVu != null && phieuDangPhucVu.getGioDen() != null)
                ? phieuDangPhucVu.getGioDen().toString().substring(0, 5) : "Chưa có";
        JLabel valGioDen = new JLabel(gioDenStr);
        valGioDen.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valGioDen, banGbc);
        // Panel thông tin khách hàng
        JPanel pnlKhachHang = new JPanel(new GridBagLayout());
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "THÔNG TIN KHÁCH",
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 20)));
        pnlKhachHang.setPreferredSize(new Dimension(200, 0));
        GridBagConstraints khGbc = new GridBagConstraints();
        khGbc.insets = new Insets(5, 15, 5, 15);
        khGbc.anchor = GridBagConstraints.WEST;
        int khRow = 0;
        if (phieuDangPhucVu != null && phieuDangPhucVu.getTenKhach() != null && !phieuDangPhucVu.getTenKhach().trim().isEmpty()) {
            // KHÁCH ĐẶT BÀN
            khGbc.gridx = 0; khGbc.gridy = khRow; khGbc.gridwidth = 2;
            khGbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblKhTitle = new JLabel("KHÁCH HÀNG", SwingConstants.CENTER);
            lblKhTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblKhTitle, khGbc);
            khRow++;

            khGbc.gridwidth = 1; khGbc.fill = GridBagConstraints.NONE;

            // Tên khách
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTenKH = new JLabel("Tên:");
            lblTenKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblTenKH, khGbc);
            khGbc.gridx = 1;
            JLabel valTenKH = new JLabel(phieuDangPhucVu.getTenKhach().trim());
            valTenKH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            pnlKhachHang.add(valTenKH, khGbc);
            khRow++;

            // SĐT
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSDT = new JLabel("SĐT:");
            lblSDT.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblSDT, khGbc);
            khGbc.gridx = 1;
            JLabel valSDT = new JLabel(phieuDangPhucVu.getSoDienThoai() != null ? phieuDangPhucVu.getSoDienThoai() : "Không có");
            valSDT.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            pnlKhachHang.add(valSDT, khGbc);
            khRow++;

            // Số người
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSoNguoiKH = new JLabel("Số người:");
            lblSoNguoiKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblSoNguoiKH, khGbc);
            khGbc.gridx = 1;
            JLabel valSoNguoiKH = new JLabel(String.valueOf(phieuDangPhucVu.getSoNguoi()));
            valSoNguoiKH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            pnlKhachHang.add(valSoNguoiKH, khGbc);
            khRow++;

            // Tiền cọc
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTienCocLabel = new JLabel("Tiền cọc:");
            lblTienCocLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblTienCocLabel, khGbc);
            khGbc.gridx = 1;
            JLabel lblCoc = new JLabel(String.format("%,.0f VNĐ", phieuDangPhucVu.getTienCoc()));
            lblCoc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            lblCoc.setForeground(Color.BLUE);
            pnlKhachHang.add(lblCoc, khGbc);
        } else {
            // KHÁCH VÃNG LAI
            khGbc.gridx = 0; khGbc.gridy = 0; khGbc.gridwidth = 2;
            khGbc.anchor = GridBagConstraints.CENTER;
            JLabel lblNoKh = new JLabel("KHÁCH TRỰC TIẾP", SwingConstants.CENTER);
            lblNoKh.setFont(new Font("Times New Roman", Font.BOLD, 20));
            lblNoKh.setForeground(Color.GRAY);
            pnlKhachHang.add(lblNoKh, khGbc);
        }
        // Panel món ăn
        JPanel pnlMonAn = new JPanel(new BorderLayout());
        pnlMonAn.setBackground(Color.WHITE);
        pnlMonAn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "DANH SÁCH MÓN",
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 20)));
        String[] columns = {"STT", "Món ăn", "Số lượng", "Giá", "Thành tiền"};
        modelMon = new DefaultTableModel(columns, 0);
        tableMon = new JTable(modelMon);
        tableMon.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tableMon.setRowHeight(30);
        // Custom renderer for table header
        JTableHeader header = tableMon.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(COLOR_RED_WINE);
                c.setForeground(Color.WHITE);
                ((JLabel) c).setFont(new Font("Times New Roman", Font.BOLD, 20));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        JScrollPane scrollMonAn = new JScrollPane(tableMon);
        scrollMonAn.setBorder(null);
        pnlMonAn.setPreferredSize(new Dimension(850, 500));
        scrollMonAn.setPreferredSize(new Dimension(850, 450));
        pnlMonAn.add(scrollMonAn, BorderLayout.CENTER);

        this.lblTotal = new JLabel("TỔNG: 0 VNĐ", SwingConstants.RIGHT);
        this.lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 18));
        this.lblTotal.setForeground(Color.RED);
        pnlMonAn.add(this.lblTotal, BorderLayout.SOUTH);

        loadMonAnFromDB();

        // Layout nội dung
        int contentRow = 0;
        gbc.gridx = 0;
        gbc.gridy = contentRow++;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        pnlContent.add(pnlBan, gbc);
        gbc.gridx = 1;
        gbc.gridy = contentRow - 1;
        pnlContent.add(pnlKhachHang, gbc);
        gbc.gridx = 0;
        gbc.gridy = contentRow++;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 2;
        pnlContent.add(pnlMonAn, gbc);
        // Panel nút
        JPanel pnlNut = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        pnlNut.setBackground(new Color(248, 249, 250));
        pnlNut.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JButton btnDatMon = new JButton("ĐẶT MÓN");
        ThanhTacVu.kieuNut(btnDatMon, new Color(241, 196, 15)); 
        btnDatMon.setForeground(Color.WHITE);
        btnDatMon.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnDatMon);
        JButton btnThanhToan = new JButton("THANH TOÁN");
        ThanhTacVu.kieuNut(btnThanhToan, new Color(52, 152, 219));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnThanhToan);
        JButton btnDong = new JButton("ĐÓNG");
        ThanhTacVu.kieuNut(btnDong, new Color(236, 66, 48));
        btnDong.setForeground(Color.WHITE);
        btnDong.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnDong);
        btnDatMon.addActionListener(e -> {
            try {
                String maPhieu = phieuDangPhucVu.getMaPhieu();

                FrmDatMon frm = new FrmDatMon(this, con, maPhieu, maBan);
                frm.setFrmPhucVu(this);
                frm.setLocationRelativeTo(this);
                frm.setVisible(true);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Lỗi mở form đặt món",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
       
        btnThanhToan.addActionListener(e -> {
            try {
                String maHD = hoaDonDAO.getMaHoaDonTheoBan(ban.getMaBan());
                if (maHD == null) {
                    JOptionPane.showMessageDialog(this, "Bàn chưa có hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                FrmBan frmBan = (getParent() instanceof FrmBan) ? (FrmBan) getParent() : null;
                FrmThanhToan frmTT = new FrmThanhToan(maHD,ban.getMaBan(),() -> {
                        if (frmBan != null) {
                            frmBan.taiLaiBangChinh();
                        }
                        dispose();
                    }
                );
                frmTT.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở thanh toán: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        btnDong.addActionListener(e -> dispose());
        // Thêm vào dialog
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        add(pnlNut, BorderLayout.SOUTH);
    }

    private void loadMonAnFromDB() {
        try {
            modelMon.setRowCount(0);

            if (phieuDangPhucVu == null) return;

            List<ChiTietPhieuDatBan> dsMon =
                chiTietPDB_DAO.layTheoMaPhieu(
                    phieuDangPhucVu.getMaPhieu()
                );

            int stt = 1;
            for (ChiTietPhieuDatBan ct : dsMon) {
                modelMon.addRow(new Object[]{
                    stt++,
                    ct.getTenMon(),
                    ct.getSoLuong(),
                    String.format("%,.0f VNĐ", ct.getDonGia()),
                    String.format("%,.0f VNĐ",
                        ct.getSoLuong() * ct.getDonGia()
                    )
                });
            }

            capNhatTongTien();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Lỗi load danh sách món",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }



    private void capNhatNutBanTrongFrmBan() {
        if (getParent() instanceof FrmBan) {
            FrmBan frmBan = (FrmBan) getParent();
            frmBan.taiLaiBangChinh();
        }
    }

    private String getMaHDByMaPhieu(String maPhieu) throws SQLException {
        if (maPhieu == null || maPhieu.isEmpty()) {
            return null;
        }
        HoaDon_DAO dao = HoaDon_DAO.getInstance();
        return dao.layMaHoaDonTheoPhieu(maPhieu);
    }

    private Object[][] getMonAnByMaHD(String maHD) throws SQLException {
        MonAn_DAO monAnDAO = new MonAn_DAO(con);
        ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.layChiTietTheoHoaDon(maHD);
        Object[][] monData = new Object[chiTietList.size()][4];
        for (int i = 0; i < chiTietList.size(); i++) {
            ChiTietHoaDon ct = chiTietList.get(i);
            MonAn mon = monAnDAO.layMonAnTheoMa(ct.getMaMon());
            if (mon != null) {
                monData[i] = new Object[]{
                    mon.getTenMon(),
                    ct.getSoLuong(),
                    ct.getDonGia(),
                    ct.getSoLuong() * ct.getDonGia()
                };
            } else {
                monData[i] = new Object[]{"Không tìm thấy món", 0, 0.0, 0.0};
            }
        }
        return monData;
    }

    private void capNhatTongTien() {
        try {
            if (phieuDangPhucVu == null) {
                lblTotal.setText("TỔNG: 0 VNĐ");
                return;
            }

            double tongTien =
                chiTietPDB_DAO.tinhTongTien(
                    phieuDangPhucVu.getMaPhieu()
                );

            lblTotal.setText(
                "TỔNG: " + String.format("%,.0f VNĐ", tongTien)
            );

        } catch (SQLException e) {
            e.printStackTrace();
            lblTotal.setText("TỔNG: 0 VNĐ");
        }
    }

    public void capNhatMonDat() {
        loadMonAnFromDB();
        revalidate();
        repaint();
    }

//    public void chuyenSangPhucVu(String maNhanVien) {
//        if (phieuDatBanGoc == null || !"Đặt".equals(phieuDatBanGoc.getTrangThai())) {
//            JOptionPane.showMessageDialog(this, "Không thể phục vụ! Phiếu không hợp lệ.");
//            return;
//        }
//
//        int confirm = JOptionPane.showConfirmDialog(this,
//            "Bắt đầu phục vụ bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//        if (confirm != JOptionPane.YES_OPTION) return;
//
//        try (Connection conn = ConnectSQL.getConnection()) {
//            conn.setAutoCommit(false);
//
//            // Cập nhật trạng thái phiếu
//            phieuDatBanDAO.capNhatTrangThaiPhieu(phieuDatBanGoc.getMaPhieu(), "Phục vụ");
//            banDAO.capNhatTrangThai(maBan, "Đang phục vụ");
//
//            // Tạo hóa đơn
//            String maHD = hoaDonDAO.taoHoaDonMoi(
//                phieuDatBanGoc.getMaPhieu(), maNhanVien, null, null, 0, ""
//            );
//
//            // Copy chi tiết đặt món → hóa đơn
//            List<ChiTietDatMon> ds = chiTietDatMonDAO.layTheoPhieu(phieuDatBanGoc.getMaPhieu());
//            for (ChiTietDatMon ct : ds) {
//                chiTietHoaDonDAO.themChiTiet(new ChiTietHoaDon(
//                    maHD, ct.getMaMon(), ct.getSoLuong(), ct.getDonGia(), ct.getGhiChu()
//                ));
//            }
//
//            conn.commit();
//
//            // CẬP NHẬT phieuHienTai ĐỂ HIỂN THỊ
//            phieuHienTai = phieuDatBanGoc;
//            phieuHienTai.setTrangThai("Phục vụ");
//
//            JOptionPane.showMessageDialog(this, "Phục vụ thành công! HD: " + maHD);
//            loadMonAnFromDB();
//            capNhatNutBanTrongFrmBan();
//            revalidate();
//            repaint();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
//        }
//    }
}