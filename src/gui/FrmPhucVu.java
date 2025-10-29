package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
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
import dao.ChiTietDatMon_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.LoaiBan_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.ChiTietDatMon;
import entity.ChiTietHoaDon;
import entity.LoaiBan;
import entity.MonAn;
import entity.PhieuDatBan;

public class FrmPhucVu extends JFrame {
    private static final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private Ban_DAO banDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private Connection con = ConnectSQL.getConnection();

    private ChiTietDatMon_DAO chiTietDatMonDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private String maBan;
    private Ban ban;
    private HoaDon_DAO hoaDonDAO;
    private LoaiBan loaiBan;
    private PhieuDatBan phieuDatBan;
	private JTable tableMon; 
    private DefaultTableModel modelMon;
	private JLabel lblTotal;

    public FrmPhucVu(JFrame parent, String maBan, PhieuDatBan phieuDatBan, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO, LoaiBan_DAO loaiBanDAO) throws SQLException {
    	setTitle("Phục vụ - Bàn " + maBan);
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	try {
        	this.hoaDonDAO = HoaDon_DAO.getInstance();
            this.maBan = maBan;
            this.banDAO = banDAO;
            this.phieuDatBanDAO = phieuDatBanDAO;
            this.ban = banDAO.getBanByMa(maBan);
            this.phieuDatBan = phieuDatBan;
            this.chiTietDatMonDAO = new ChiTietDatMon_DAO(con);
            this.chiTietHoaDonDAO = new ChiTietHoaDon_DAO();

            System.out.println("phieuDatBan: " + (phieuDatBan != null ? phieuDatBan.getTenKhach() : "null"));
            this.loaiBan = loaiBanDAO.getLoaiBanByMa(ban.getMaLoai());
            if (this.loaiBan == null) {
                this.loaiBan = new LoaiBan();
                this.loaiBan.setTenLoai("Thường");
            }
            initComponents();
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khởi tạo form: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kieuNut(JButton button, Color baseColor) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setBackground(baseColor);

        Color hoverColor = baseColor.darker();
        Color clickColor = baseColor.darker().darker();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(hoverColor);
            }
        });
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

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("BÀN ĐANG PHỤC VỤ", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
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

        banGbc.gridx = 0;
        banGbc.gridy = banRow;
        banGbc.gridwidth = 2;
        banGbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblBanTitle = new JLabel("BÀN " + ban.getMaBan(), SwingConstants.CENTER);
        lblBanTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblBanTitle, banGbc);
        banRow++;

        banGbc.gridwidth = 1;
        banGbc.fill = GridBagConstraints.NONE;
        banGbc.gridx = 0;
        banGbc.gridy = banRow;
        JLabel lblKhuVucBan = new JLabel("Khu vực:");
        lblKhuVucBan.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblKhuVucBan, banGbc);
        banGbc.gridx = 1;
        JLabel valKhuVucBan = new JLabel(ban.getTenKhuVuc() != null ? ban.getTenKhuVuc() : "Không xác định");
        valKhuVucBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valKhuVucBan, banGbc);
        banRow++;

        banGbc.gridx = 0;
        banGbc.gridy = banRow;
        JLabel lblSoGheBan = new JLabel("Số ghế:");
        lblSoGheBan.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblSoGheBan, banGbc);
        banGbc.gridx = 1;
        JLabel valSoGheBan = new JLabel(String.valueOf(ban.getSoChoNgoi()));
        valSoGheBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pnlBan.add(valSoGheBan, banGbc);
        banRow++;

        banGbc.gridx = 0;
        banGbc.gridy = banRow;
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

        if (phieuDatBan != null && phieuDatBan.getTenKhach() != null) {
            khGbc.gridx = 0;
            khGbc.gridy = khRow;
            khGbc.gridwidth = 2;
            khGbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblKhTitle = new JLabel("KHÁCH HÀNG", SwingConstants.CENTER);
            lblKhTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblKhTitle, khGbc);
            khRow++;

            khGbc.gridwidth = 1;
            khGbc.fill = GridBagConstraints.NONE;
            khGbc.gridx = 0;
            khGbc.gridy = khRow;
            JLabel lblTenKH = new JLabel("Tên:");
            lblTenKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblTenKH, khGbc);
            khGbc.gridx = 1;
            JLabel valTenKH = new JLabel(phieuDatBan.getTenKhach());
            valTenKH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            pnlKhachHang.add(valTenKH, khGbc);
            khRow++;

            khGbc.gridx = 0;
            khGbc.gridy = khRow;
            JLabel lblSDT = new JLabel("SĐT:");
            lblSDT.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblSDT, khGbc);
            khGbc.gridx = 1;
            JLabel valSDT = new JLabel(phieuDatBan.getSoDienThoai() != null ? phieuDatBan.getSoDienThoai() : "Không có");
            valSDT.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            pnlKhachHang.add(valSDT, khGbc);
            khRow++;

            khGbc.gridx = 0;
            khGbc.gridy = khRow;
            JLabel lblSoNguoiKH = new JLabel("Số người:");
            lblSoNguoiKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
            pnlKhachHang.add(lblSoNguoiKH, khGbc);
            khGbc.gridx = 1;
            JLabel valSoNguoiKH = new JLabel(String.valueOf(phieuDatBan.getSoNguoi()));
            valSoNguoiKH.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pnlKhachHang.add(valSoNguoiKH, khGbc);
            khRow++;

            khGbc.gridx = 0;
            khGbc.gridy = khRow;
            JLabel lblTienCocLabel = new JLabel("Tiền cọc:");
            lblTienCocLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblTienCocLabel, khGbc);
            khGbc.gridx = 1;
            JLabel lblCoc = new JLabel(String.format("%,.0f VNĐ", phieuDatBan.getTienCoc()));
            lblCoc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            lblCoc.setForeground(Color.BLUE);
            pnlKhachHang.add(lblCoc, khGbc);
        } else {
            String trangThai = layTrangThaiHienTai(maBan, phieuDatBanDAO);
            if (!trangThai.equals("Trống")) {
                try {
                    java.util.Date today = new java.util.Date();
                    List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(maBan, new java.sql.Date(today.getTime()));
                    if (!list.isEmpty()) {
                        phieuDatBan = list.get(0);
                        // Rebuild customer panel
                        khGbc.gridx = 0;
                        khGbc.gridy = 0;
                        khGbc.gridwidth = 2;
                        khGbc.fill = GridBagConstraints.HORIZONTAL;
                        JLabel lblKhTitle = new JLabel("KHÁCH HÀNG", SwingConstants.CENTER);
                        lblKhTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        pnlKhachHang.add(lblKhTitle, khGbc);
                        khRow++;

                        khGbc.gridwidth = 1;
                        khGbc.fill = GridBagConstraints.NONE;
                        khGbc.gridx = 0;
                        khGbc.gridy = khRow;
                        JLabel lblTenKH = new JLabel("Tên:");
                        lblTenKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        pnlKhachHang.add(lblTenKH, khGbc);
                        khGbc.gridx = 1;
                        JLabel valTenKH = new JLabel(phieuDatBan.getTenKhach());
                        valTenKH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                        pnlKhachHang.add(valTenKH, khGbc);
                        khRow++;

                        khGbc.gridx = 0;
                        khGbc.gridy = khRow;
                        JLabel lblSDT = new JLabel("SĐT:");
                        lblSDT.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        pnlKhachHang.add(lblSDT, khGbc);
                        khGbc.gridx = 1;
                        JLabel valSDT = new JLabel(phieuDatBan.getSoDienThoai() != null ? phieuDatBan.getSoDienThoai() : "Không có");
                        valSDT.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                        pnlKhachHang.add(valSDT, khGbc);
                        khRow++;

                        khGbc.gridx = 0;
                        khGbc.gridy = khRow;
                        JLabel lblSoNguoiKH = new JLabel("Số người:");
                        lblSoNguoiKH.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        pnlKhachHang.add(lblSoNguoiKH, khGbc);
                        khGbc.gridx = 1;
                        JLabel valSoNguoiKH = new JLabel(String.valueOf(phieuDatBan.getSoNguoi()));
                        valSoNguoiKH.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                        pnlKhachHang.add(valSoNguoiKH, khGbc);
                        khRow++;

                        khGbc.gridx = 0;
                        khGbc.gridy = khRow;
                        JLabel lblTienCocLabel = new JLabel("Tiền cọc:");
                        lblTienCocLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        pnlKhachHang.add(lblTienCocLabel, khGbc);
                        khGbc.gridx = 1;
                        JLabel lblCoc = new JLabel(String.format("%,.0f VNĐ", phieuDatBan.getTienCoc()));
                        lblCoc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                        lblCoc.setForeground(Color.BLUE);
                        pnlKhachHang.add(lblCoc, khGbc);
                    } else {
                        khGbc.gridx = 0;
                        khGbc.gridy = 0;
                        khGbc.gridwidth = 2;
                        khGbc.anchor = GridBagConstraints.CENTER;
                        JLabel lblNoKh = new JLabel("KHÁCH TRỰC TIẾP", SwingConstants.CENTER);
                        lblNoKh.setFont(new Font("Times New Roman", Font.BOLD, 20));
                        lblNoKh.setForeground(Color.GRAY);
                        pnlKhachHang.add(lblNoKh, khGbc);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin phiếu đặt bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                khGbc.gridx = 0;
                khGbc.gridy = 0;
                khGbc.gridwidth = 2;
                khGbc.anchor = GridBagConstraints.CENTER;
                JLabel lblNoKh = new JLabel("KHÁCH TRỰC TIẾP", SwingConstants.CENTER);
                lblNoKh.setFont(new Font("Times New Roman", Font.BOLD, 20));
                lblNoKh.setForeground(Color.GRAY);
                pnlKhachHang.add(lblNoKh, khGbc);
            }
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
        scrollMonAn.setPreferredSize(new Dimension(850, 450)); // bảng to hơn

        pnlMonAn.add(scrollMonAn, BorderLayout.CENTER);

        double tongTien = calculateTongTien(modelMon);
        this.lblTotal = new JLabel("TỔNG: 0 VNĐ", SwingConstants.RIGHT); // ← gán vào biến instance
        this.lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 18));
        this.lblTotal.setForeground(Color.RED);
        pnlMonAn.add(this.lblTotal, BorderLayout.SOUTH);


        loadMonAnFromDB() ;
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
        kieuNut(btnDatMon, new Color(46, 204, 113));
        btnDatMon.setForeground(Color.WHITE);
        btnDatMon.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnDatMon);

        JButton btnThanhToan = new JButton("THANH TOÁN");
        kieuNut(btnThanhToan, new Color(52, 152, 219));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnThanhToan);

        JButton btnDong = new JButton("ĐÓNG");
        kieuNut(btnDong, new Color(236, 66, 48));
        btnDong.setForeground(Color.WHITE);
        btnDong.setPreferredSize(new Dimension(200, 40));
        pnlNut.add(btnDong);

        btnDatMon.addActionListener(e -> {
            try {
                String maPhieu = phieuDatBan != null ? phieuDatBan.getMaPhieu() : null;
                System.out.println("Gọi đặt món với maBan: " + maBan + ", maPhieu: " + maPhieu);

                FrmDatMon frm = new FrmDatMon(con, maPhieu, maBan);
                frm.setFrmPhucVu(this);
                frm.setVisible(true);
                this.setVisible(false);

                frm.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi mở form đặt món: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

	private String layTrangThaiHienTai(String maBan, PhieuDatBan_DAO phieuDatBanDAO) {
        try {
            java.util.Date today = new java.util.Date();
            List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(maBan, new java.sql.Date(today.getTime()));
            if (!list.isEmpty()) {
                return "Phục vụ";
            }
            return "Trống";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Trống";
        }
    }

	void loadMonAnFromDB() {
	    modelMon.setRowCount(0);
	    try {
	        String maPhieu = phieuDatBan != null ? phieuDatBan.getMaPhieu() : null;
	        String maHD = getMaHDByMaPhieu(maPhieu);
	
	        if (maHD != null) {
	            // Load từ hóa đơn (đã phục vụ)
	            Object[][] data = getMonAnByMaHD(maHD);
	            int stt = 1;
	            for (Object[] row : data) {
	                modelMon.addRow(new Object[]{
	                    stt++,
	                    row[0],
	                    row[1],
	                    String.format("%,.0f VNĐ", (Double)row[2]),
	                    String.format("%,.0f VNĐ", (Double)row[3])
	                });
	            }
	        } 
	        // LUÔN LOAD TỪ CHI TIẾT ĐẶT MÓN NẾU CÓ maPhieu (dù trạng thái gì)
	        else if (maPhieu != null && !maPhieu.trim().isEmpty()) {
	            ChiTietDatMon_DAO chiTietDAO = new ChiTietDatMon_DAO(con);
	            List<ChiTietDatMon> ds = chiTietDAO.layTheoPhieu(maPhieu);
	            MonAn_DAO monDAO = new MonAn_DAO(con);
	            int stt = 1;
	            for (ChiTietDatMon ct : ds) {
	                MonAn mon = monDAO.layMonAnTheoMa(ct.getMaMon());
	                if (mon != null) {
	                    modelMon.addRow(new Object[]{
	                        stt++,
	                        mon.getTenMon(),
	                        ct.getSoLuong(),
	                        String.format("%,.0f VNĐ", ct.getDonGia()),
	                        String.format("%,.0f VNĐ", ct.getSoLuong() * ct.getDonGia())
	                    });
	                }
	            }
	        }
	
	        double tongTien = calculateTongTien(modelMon);
	        if (lblTotal != null) {
	            lblTotal.setText(String.format("TỔNG: %,d VNĐ", (int)tongTien));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
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

    private double calculateTongTien(DefaultTableModel model) {
        double tong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tong += Double.parseDouble(model.getValueAt(i, 4).toString().replaceAll("[^0-9]", ""));
        }
        return tong;
    }

    public void capNhatMonDat() {
	    loadMonAnFromDB();
	    revalidate();
	    repaint();
	}

	public void chuyenSangPhucVu(String maNhanVien) {
	    if (phieuDatBan == null || !"Đặt".equals(phieuDatBan.getTrangThai())) {
	        JOptionPane.showMessageDialog(this, "Không thể phục vụ!");
	        return;
	    }
	    int confirm = JOptionPane.showConfirmDialog(this,
	        "Bắt đầu phục vụ bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
	    if (confirm != JOptionPane.YES_OPTION) return;

	    try (Connection conn = ConnectSQL.getConnection()) {
	        conn.setAutoCommit(false);

	        phieuDatBanDAO.capNhatTrangThaiPhieu(phieuDatBan.getMaPhieu(), "Phục vụ");
	        banDAO.capNhatTrangThai(maBan, "Đang phục vụ");

	        String maHD = hoaDonDAO.taoHoaDonMoi(
	            phieuDatBan.getMaPhieu(), maNhanVien, null, null, 0, ""
	        );

	        List<ChiTietDatMon> ds = chiTietDatMonDAO.layTheoPhieu(phieuDatBan.getMaPhieu());
	        for (ChiTietDatMon ct : ds) {
	            chiTietHoaDonDAO.themChiTiet(new ChiTietHoaDon(maHD, ct.getMaMon(),
	                ct.getSoLuong(), ct.getDonGia(), ct.getGhiChu()));
	        }

	        conn.commit();
	        JOptionPane.showMessageDialog(this, "Phục vụ thành công! HD: " + maHD);

	        loadMonAnFromDB();
	        capNhatNutBanTrongFrmBan();

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
	    }
	}
}