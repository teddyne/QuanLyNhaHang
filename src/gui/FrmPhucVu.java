//FrmPhucVu
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

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
import javax.swing.table.DefaultTableModel;

import dao.Ban_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.LoaiBan;
import entity.PhieuDatBan;

public class FrmPhucVu extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(128, 0, 0);
    private static final Color COLOR_PHUCVU = new Color(0, 255, 0);
    private Ban_DAO banDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private String maBan;
    private Ban ban;
    private LoaiBan loaiBan;
    private PhieuDatBan phieuDatBan;

    public FrmPhucVu(JFrame parent, String maBan, PhieuDatBan phieuDatBan, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO) throws SQLException {
        super(parent, "🍽️ Phục vụ - Bàn " + maBan, true);
        this.maBan = maBan;
        this.banDAO = banDAO;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.ban = banDAO.getBanByMa(maBan);
        this.phieuDatBan = phieuDatBan;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setSize(900, 700);
        setLocationRelativeTo(getParent());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("🍽️ BÀN ĐANG PHỤC VỤ", SwingConstants.CENTER);
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
        gbc.weightx = 1; gbc.weighty = 1;

        // Panel thông tin bàn
        JPanel pnlBan = new JPanel(new GridBagLayout());
        pnlBan.setBackground(Color.WHITE);
        pnlBan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "🪑 THÔNG TIN BÀN", 
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 18)));
        pnlBan.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints banGbc = new GridBagConstraints();
        banGbc.insets = new Insets(8, 15, 8, 15);
        banGbc.anchor = GridBagConstraints.WEST;
        int banRow = 0;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        banGbc.gridwidth = 2; banGbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblBanTitle = new JLabel("🪑 BÀN " + ban.getMaBan(), SwingConstants.CENTER);
        lblBanTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblBanTitle, banGbc); banRow++;

        banGbc.gridwidth = 1; banGbc.fill = GridBagConstraints.NONE;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblKhuVucBan = new JLabel("Khu vực:");
        lblKhuVucBan.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pnlBan.add(lblKhuVucBan, banGbc);
        banGbc.gridx = 1;
        JLabel valKhuVucBan = new JLabel(ban.getTenKhuVuc() != null ? ban.getTenKhuVuc() : "Không xác định");
        valKhuVucBan.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        pnlBan.add(valKhuVucBan, banGbc);
        banRow++;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblSoGheBan = new JLabel("Số ghế:");
        lblSoGheBan.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pnlBan.add(lblSoGheBan, banGbc);
        banGbc.gridx = 1;
        JLabel valSoGheBan = new JLabel(String.valueOf(ban.getSoChoNgoi()));
        valSoGheBan.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        pnlBan.add(valSoGheBan, banGbc);
        banRow++;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblLoaiBanLabel = new JLabel("Loại bàn:");
        lblLoaiBanLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pnlBan.add(lblLoaiBanLabel, banGbc);
        banGbc.gridx = 1;
        JLabel lblLoai = new JLabel(loaiBan.getTenLoai());
        lblLoai.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        if ("VIP".equals(loaiBan.getTenLoai())) {
            lblLoai.setForeground(Color.YELLOW);
        }
        pnlBan.add(lblLoai, banGbc);

        // Panel thông tin khách hàng
        JPanel pnlKhachHang = new JPanel(new GridBagLayout());
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "👥 THÔNG TIN KHÁCH", 
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 18)));
        pnlKhachHang.setPreferredSize(new Dimension(250, 0));
        GridBagConstraints khGbc = new GridBagConstraints();
        khGbc.insets = new Insets(8, 15, 8, 15);
        khGbc.anchor = GridBagConstraints.WEST;
        int khRow = 0;

        if (phieuDatBan != null && phieuDatBan.getTenKhach() != null) {
            khGbc.gridx = 0; khGbc.gridy = khRow;
            khGbc.gridwidth = 2; khGbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblKhTitle = new JLabel("👤 KHÁCH HÀNG", SwingConstants.CENTER);
            lblKhTitle.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblKhTitle, khGbc); khRow++;

            khGbc.gridwidth = 1; khGbc.fill = GridBagConstraints.NONE;
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTenKH = new JLabel("Tên:");
            lblTenKH.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblTenKH, khGbc);
            khGbc.gridx = 1;
            JLabel valTenKH = new JLabel(phieuDatBan.getTenKhach());
            valTenKH.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pnlKhachHang.add(valTenKH, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSDT = new JLabel("SĐT:");
            lblSDT.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblSDT, khGbc);
            khGbc.gridx = 1;
            JLabel valSDT = new JLabel(phieuDatBan.getSoDienThoai());
            valSDT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pnlKhachHang.add(valSDT, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSoNguoiKH = new JLabel("Số người:");
            lblSoNguoiKH.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblSoNguoiKH, khGbc);
            khGbc.gridx = 1;
            JLabel valSoNguoiKH = new JLabel(String.valueOf(phieuDatBan.getSoNguoi()));
            valSoNguoiKH.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            pnlKhachHang.add(valSoNguoiKH, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTienCocLabel = new JLabel("💰 Tiền cọc:");
            lblTienCocLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblTienCocLabel, khGbc);
            khGbc.gridx = 1;
            JLabel lblCoc = new JLabel(String.format("%,.0f VNĐ", phieuDatBan.getTienCoc()));
            lblCoc.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            lblCoc.setForeground(Color.BLUE);
            pnlKhachHang.add(lblCoc, khGbc);
        } else {
            khGbc.gridx = 0; khGbc.gridy = 0; khGbc.gridwidth = 2;
            khGbc.anchor = GridBagConstraints.CENTER;
            JLabel lblNoKh = new JLabel("👤 KHÁCH TRỰC TIẾP", SwingConstants.CENTER);
            lblNoKh.setFont(new Font("Times New Roman", Font.BOLD, 16));
            lblNoKh.setForeground(Color.GRAY);
            pnlKhachHang.add(lblNoKh, khGbc);
        }

        // panel món ăn - lấy dữ liệu db
        JPanel pnlMonAn = new JPanel(new BorderLayout());
        pnlMonAn.setBackground(Color.WHITE);
        pnlMonAn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "🍽️ DANH SÁCH MÓN", 
            SwingConstants.CENTER, SwingConstants.TOP, new Font("Times New Roman", Font.BOLD, 18)));

        String[] columns = {"STT", "Món ăn", "SL", "Giá", "Thành tiền"};
        DefaultTableModel modelMon = new DefaultTableModel(columns, 0);
        
        // lấy dữ liệu từ db
        loadMonAnFromDB(modelMon);
        
        JTable tableMon = new JTable(modelMon);
        tableMon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tableMon.setRowHeight(30);
        tableMon.getTableHeader().setBackground(COLOR_RED_WINE);
        tableMon.getTableHeader().setForeground(Color.WHITE);
        tableMon.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 16));
        tableMon.setGridColor(Color.LIGHT_GRAY);
        JScrollPane scrollMonAn = new JScrollPane(tableMon);
        pnlMonAn.add(scrollMonAn, BorderLayout.CENTER);

        double tongTien = calculateTongTien(modelMon);
        JLabel lblTotal = new JLabel(String.format("💰 TỔNG: %,d VNĐ", (int)tongTien), SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);
        pnlMonAn.add(lblTotal, BorderLayout.SOUTH);

        // Layout nội dung
        int contentRow = 0;
        gbc.gridx = 0; gbc.gridy = contentRow++; gbc.gridwidth = 1; gbc.weighty = 0;
        pnlContent.add(pnlBan, gbc);

        gbc.gridx = 1; gbc.gridy = contentRow - 1;
        pnlContent.add(pnlKhachHang, gbc);

        gbc.gridx = 0; gbc.gridy = contentRow++; gbc.gridwidth = 2; gbc.weighty = 1;
        pnlContent.add(pnlMonAn, gbc);

        // Panel nút
        JPanel pnlNut = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        pnlNut.setBackground(new Color(248, 249, 250));
        pnlNut.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnDatMon = new JButton("🍽️ THÊM MÓN");
        btnDatMon.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnDatMon.setPreferredSize(new Dimension(150, 40));
        btnDatMon.setBackground(new Color(46, 204, 113));
        btnDatMon.setForeground(Color.WHITE);
        pnlNut.add(btnDatMon);

        JButton btnThanhToan = new JButton("💳 THANH TOÁN");
        btnThanhToan.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnThanhToan.setPreferredSize(new Dimension(150, 40));
        btnThanhToan.setBackground(new Color(52, 152, 219));
        btnThanhToan.setForeground(Color.WHITE);
        pnlNut.add(btnThanhToan);

        JButton btnDong = new JButton("❌ ĐÓNG");
        btnDong.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnDong.setPreferredSize(new Dimension(120, 40));
        btnDong.setBackground(Color.LIGHT_GRAY);
        btnDong.setForeground(Color.BLACK);
        pnlNut.add(btnDong);

        btnDatMon.addActionListener(e -> {
            try {
                FrmDatMon frmDatMon = new FrmDatMon(this, maBan, phieuDatBan != null ? phieuDatBan.getMaPhieu() : "");
                frmDatMon.setVisible(true);
                
                loadMonAnFromDB(modelMon);
                lblTotal.setText(String.format("💰 TỔNG: %,d VNĐ", (int)calculateTongTien(modelMon)));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        btnThanhToan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                String.format("💳 CHUYỂN SANG THANH TOÁN\nBÀN %s\nTỔNG: %,d VNĐ", 
                    maBan, (int)calculateTongTien(modelMon)), 
                "Thanh toán", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        btnDong.addActionListener(e -> dispose());

        // Thêm vào dialog
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        add(pnlNut, BorderLayout.SOUTH);
    }
    
    //load món từ db
    private void loadMonAnFromDB(DefaultTableModel model) {
        model.setRowCount(0);
        if (phieuDatBan == null) return;
        
        try {
            String maHD = getMaHDByMaPhieu(phieuDatBan.getMaPhieu());
            if (maHD == null || maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "📝 Chưa có món - Nhấn THÊM MÓN!");
                return;
            }
            
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
            JOptionPane.showMessageDialog(this, "Lỗi load món: " + e.getMessage());
        }
    }

    private String getMaHDByMaPhieu(String maPhieu) {

        if ("P0001".equals(maPhieu)) return "HD0002";
        if ("P0002".equals(maPhieu)) return "HD0003";
        if ("P0003".equals(maPhieu)) return "HD0001";
        return null;
    }

    private Object[][] getMonAnByMaHD(String maHD) {
       
        if ("HD0002".equals(maHD)) { 
            return new Object[][]{
                {"Cơm chiên dương châu", 2, 40000.0, 80000.0},
                {"Bánh flan", 2, 15000.0, 30000.0}
            };
        }
        if ("HD0003".equals(maHD)) { 
            return new Object[][]{
                {"Lẩu hải sản", 1, 150000.0, 150000.0},
                {"Cơm chiên dương châu", 1, 40000.0, 40000.0}
            };
        }
        if ("HD0001".equals(maHD)) { 
            return new Object[][]{
                {"Lẩu hải sản", 2, 150000.0, 300000.0},
                {"Bánh flan", 3, 15000.0, 45000.0}
            };
        }
        return new Object[0][0];
    }

    private double calculateTongTien(DefaultTableModel model) {
        double tong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tong += Double.parseDouble(model.getValueAt(i, 4).toString().replaceAll("[^0-9]", ""));
        }
        return tong;
    }
}