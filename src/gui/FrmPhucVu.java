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
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.LoaiBan_DAO;
import dao.MonAn_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.LoaiBan;
import entity.MonAn;
import entity.PhieuDatBan;

public class FrmPhucVu extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(128, 0, 0);
    private Ban_DAO banDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private LoaiBan_DAO loaiBanDAO;
    private Connection con = ConnectSQL.getConnection();

    private String maBan;
    private Ban ban;
    private LoaiBan loaiBan;
    private PhieuDatBan phieuDatBan;

    public FrmPhucVu(JFrame parent, String maBan, PhieuDatBan phieuDatBan, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO, LoaiBan_DAO loaiBanDAO) throws SQLException {
        super(parent, "Phục vụ - Bàn " + maBan, true);
        try {
            this.maBan = maBan;
            this.banDAO = banDAO;
            this.phieuDatBanDAO = phieuDatBanDAO;
            this.loaiBanDAO = loaiBanDAO;
            this.ban = banDAO.getBanByMa(maBan);
            this.phieuDatBan = phieuDatBan;

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
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        pnlBan.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints banGbc = new GridBagConstraints();
        banGbc.insets = new Insets(8, 15, 8, 15);
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
        pnlKhachHang.setPreferredSize(new Dimension(250, 0));
        GridBagConstraints khGbc = new GridBagConstraints();
        khGbc.insets = new Insets(8, 15, 8, 15);
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
        DefaultTableModel modelMon = new DefaultTableModel(columns, 0);

        loadMonAnFromDB(modelMon);

        JTable tableMon = new JTable(modelMon);
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
        pnlMonAn.add(scrollMonAn, BorderLayout.CENTER);

        double tongTien = calculateTongTien(modelMon);
        JLabel lblTotal = new JLabel(String.format("TỔNG: %,d VNĐ", (int)tongTien), SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);
        pnlMonAn.add(lblTotal, BorderLayout.SOUTH);

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
        gbc.weighty = 1;
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
                FrmDatMon frmDatMon = new FrmDatMon(maBan, maPhieu);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi mở form đặt món: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnThanhToan.addActionListener(e -> {
            if (ban != null) {
                xuLyThanhToan(modelMon);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDong.addActionListener(e -> dispose());

        // Thêm vào dialog
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        add(pnlNut, BorderLayout.SOUTH);
    }
    private void hienThiDanhSachMonDat(FrmDatMon datMon) {
        String danhSach = datMon.layDanhSachMonDat();
        double tongTien = datMon.layTongTienMon();
    }

    private void xuLyThanhToan(DefaultTableModel modelMon) {
        String trangThai = layTrangThaiHienTai(maBan, phieuDatBanDAO);
        if ("Phục vụ".equals(trangThai)) {
            double tongTien = calculateTongTien(modelMon);
			new FrmThanhToan(maBan, phieuDatBan, tongTien).setVisible(true); // Adjust constructor as needed
			dispose();
			if (getParent() instanceof FrmBan) {
			    ((FrmBan) getParent()).taiLaiBangChinh();
			}
        } else {
            JOptionPane.showMessageDialog(this, "Bàn phải ở trạng thái phục vụ để thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
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

    void loadMonAnFromDB(DefaultTableModel model) {
	    model.setRowCount(0);
	    if (phieuDatBan == null) {
	        JOptionPane.showMessageDialog(this, "Chưa có phiếu đặt bàn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }
	    try {
	        String maHD = getMaHDByMaPhieu(phieuDatBan.getMaPhieu());
//	        if (maHD == null || maHD.isEmpty()) {
//	            JOptionPane.showMessageDialog(this, "Chưa có món - Nhấn THÊM MÓN!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//	            return;
	        Object[][] monData = getMonAnByMaHD(maHD);
	        int stt = 1;
	        for (Object[] row : monData) {
	            model.addRow(new Object[]{
	                stt++,
	                row[0],
	                row[1],
	                String.format("%,.0f VNĐ", (Double)row[2]),
	                String.format("%,.0f VNĐ", (Double)row[3])
	            });
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi load món: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}

    private String getMaHDByMaPhieu(String maPhieu) throws SQLException {
        if (maPhieu == null || maPhieu.isEmpty()) {
            return null;
        }
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO(ConnectSQL.getConnection());
        return hoaDonDAO.layMaHoaDonTheoPhieu(maPhieu);
    }

    private Object[][] getMonAnByMaHD(String maHD) throws SQLException {
	    MonAn_DAO monAnDAO = new MonAn_DAO(con);
	    ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO(ConnectSQL.getConnection());
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

   
    public void capNhatMonDat(String danhSachMonDat, double tongTienMon) {
        // Tìm JLabel hiển thị danh sách món (ví dụ: lblMonDat)
        JLabel lblMonDat = timJLabelTheoTen("lblMonDat");
        if (lblMonDat != null) {
            if (danhSachMonDat == null || danhSachMonDat.trim().isEmpty()) {
                lblMonDat.setText("<html><i>Chưa có món nào</i></html>");
            } else {
                lblMonDat.setText("<html>" + danhSachMonDat.replace("\n", "<br>") + "</html>");
            }
        }

        // Tìm JLabel hiển thị tổng tiền (ví dụ: lblTongTienBan)
        JLabel lblTongTien = timJLabelTheoTen("lblTongTienBan");
        if (lblTongTien != null) {
            lblTongTien.setText(String.format("%,.0f đ", tongTienMon));
            lblTongTien.setForeground(new Color(169, 55, 68)); // Đỏ rượu vang
            lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 20));
        }

        JButton btnBan = timNutBanHienTai();
        if (btnBan != null) {
            if (tongTienMon > 0) {
                btnBan.setBackground(new Color(231, 76, 60)); // Đỏ: đã đặt
                btnBan.setText(maBan + " (Đã đặt)");
            } else {
                btnBan.setBackground(new Color(102, 210, 74)); // Xanh: trống
                btnBan.setText(maBan);
            }
        }

        // Cập nhật lại giao diện
        revalidate();
        repaint();
    }

	private JLabel timJLabelTheoTen(String ten) {
    for (Component c : getContentPane().getComponents()) {
        if (c instanceof JPanel) {
            for (Component sub : ((JPanel) c).getComponents()) {
                if (sub instanceof JLabel && ten.equals(((JLabel) sub).getName())) {
                    return (JLabel) sub;
                }
            }
        }
    }
    return null;
}

	private JButton timNutBanHienTai() {
	    // Giả sử bạn có biến maBan hiện tại
	    String ma = this.maBan; // hoặc biến toàn cục
	    for (Component c : getContentPane().getComponents()) {
	        if (c instanceof JButton && ((JButton) c).getText().startsWith(ma)) {
	            return (JButton) c;
	        }
	    }
	    return null;
	}
}