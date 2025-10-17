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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.Ban_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.PhieuDatBan;

public class FrmPhucVu extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(128, 0, 0);
    private static final Color COLOR_PHUCVU = new Color(0, 255, 0);
    private Ban_DAO banDAO;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private String maBan;
    private Ban ban;
    private PhieuDatBan phieuDatBan;

    public FrmPhucVu(JFrame parent, String maBan, Ban_DAO banDAO, PhieuDatBan_DAO phieuDatBanDAO) throws SQLException {
        super(parent, "Thông tin bàn đang phục vụ - " + maBan, true);
        this.maBan = maBan;
        this.banDAO = banDAO;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.ban = banDAO.getBanByMa(maBan);
        this.phieuDatBan = phieuDatBanDAO.getPhucVuHienTai(maBan);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.white);
        setSize(900, 700);
        setLocationRelativeTo(getParent());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Bàn đang phục vụ", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        // Nội dung chính
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(Color.white);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 16);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 14);
        Font fontTitle = new Font("Times New Roman", Font.BOLD, 18);

        // Panel thông tin bàn
        JPanel pnlBan = new JPanel(new GridBagLayout());
        pnlBan.setBackground(Color.WHITE);
        pnlBan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Thông tin bàn", 
            SwingConstants.CENTER, SwingConstants.TOP, fontTitle));
        pnlBan.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints banGbc = new GridBagConstraints();
        banGbc.insets = new Insets(8, 15, 8, 15);
        banGbc.anchor = GridBagConstraints.WEST;
        int banRow = 0;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        banGbc.gridwidth = 2; banGbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblBanTitle = new JLabel("Bàn " + ban.getMaBan(), SwingConstants.CENTER);
        lblBanTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        pnlBan.add(lblBanTitle, banGbc); banRow++;

        banGbc.gridwidth = 1; banGbc.fill = GridBagConstraints.NONE;
        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblKhuVucBan = new JLabel("Khu vực:");
        lblKhuVucBan.setFont(fontLabel);
        pnlBan.add(lblKhuVucBan, banGbc);
        banGbc.gridx = 1;
        JLabel valKhuVucBan = new JLabel(ban.getTenKhuVuc() != null ? ban.getTenKhuVuc() : "Không xác định");
        valKhuVucBan.setFont(fontValue);
        pnlBan.add(valKhuVucBan, banGbc);
        banRow++;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblSoGheBan = new JLabel("Số ghế:");
        lblSoGheBan.setFont(fontLabel);
        pnlBan.add(lblSoGheBan, banGbc);
        banGbc.gridx = 1;
        JLabel valSoGheBan = new JLabel(String.valueOf(ban.getSoChoNgoi()));
        valSoGheBan.setFont(fontValue);
        pnlBan.add(valSoGheBan, banGbc);
        banRow++;

        banGbc.gridx = 0; banGbc.gridy = banRow;
        JLabel lblLoaiBanLabel = new JLabel("Loại bàn:");
        lblLoaiBanLabel.setFont(fontLabel);
        pnlBan.add(lblLoaiBanLabel, banGbc);
        banGbc.gridx = 1;
        JLabel lblLoai = new JLabel(ban.getMaBan());
        lblLoai.setFont(fontValue);
        if ("VIP".equals(ban.getMaBan())) {
            lblLoai.setForeground(new Color(218, 165, 32));
        }
        pnlBan.add(lblLoai, banGbc);

        // Panel thông tin khách hàng
        JPanel pnlKhachHang = new JPanel(new GridBagLayout());
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Thông tin khách hàng", 
            SwingConstants.CENTER, SwingConstants.TOP, fontTitle));
        pnlKhachHang.setPreferredSize(new Dimension(250, 0));
        GridBagConstraints khGbc = new GridBagConstraints();
        khGbc.insets = new Insets(8, 15, 8, 15);
        khGbc.anchor = GridBagConstraints.WEST;
        int khRow = 0;

        if (phieuDatBan != null && phieuDatBan.getTenKhach() != null) {
            khGbc.gridx = 0; khGbc.gridy = khRow;
            khGbc.gridwidth = 2; khGbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblKhTitle = new JLabel("Khách hàng", SwingConstants.CENTER);
            lblKhTitle.setFont(new Font("Times New Roman", Font.BOLD, 16));
            pnlKhachHang.add(lblKhTitle, khGbc); khRow++;

            khGbc.gridwidth = 1; khGbc.fill = GridBagConstraints.NONE;
            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTenKH = new JLabel("Tên KH:");
            lblTenKH.setFont(fontLabel);
            pnlKhachHang.add(lblTenKH, khGbc);
            khGbc.gridx = 1;
            JLabel valTenKH = new JLabel(phieuDatBan.getTenKhach());
            valTenKH.setFont(fontValue);
            pnlKhachHang.add(valTenKH, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSDT = new JLabel("SĐT:");
            lblSDT.setFont(fontLabel);
            pnlKhachHang.add(lblSDT, khGbc);
            khGbc.gridx = 1;
            JLabel valSDT = new JLabel(phieuDatBan.getSoDienThoai());
            valSDT.setFont(fontValue);
            pnlKhachHang.add(valSDT, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblSoNguoiKH = new JLabel("Số người:");
            lblSoNguoiKH.setFont(fontLabel);
            pnlKhachHang.add(lblSoNguoiKH, khGbc);
            khGbc.gridx = 1;
            JLabel valSoNguoiKH = new JLabel(String.valueOf(phieuDatBan.getSoNguoi()));
            valSoNguoiKH.setFont(fontValue);
            pnlKhachHang.add(valSoNguoiKH, khGbc);
            khRow++;

            khGbc.gridx = 0; khGbc.gridy = khRow;
            JLabel lblTienCocLabel = new JLabel("Tiền cọc:");
            lblTienCocLabel.setFont(fontLabel);
            pnlKhachHang.add(lblTienCocLabel, khGbc);
            khGbc.gridx = 1;
            JLabel lblCoc = new JLabel(String.valueOf(phieuDatBan.getTienCoc()) + " VNĐ");
            lblCoc.setFont(fontValue);
            lblCoc.setForeground(Color.BLUE);
            pnlKhachHang.add(lblCoc, khGbc);
        } else {
            khGbc.gridx = 0; khGbc.gridy = 0; khGbc.gridwidth = 2;
            khGbc.anchor = GridBagConstraints.CENTER;
            JLabel lblNoKh = new JLabel("Chưa có thông tin khách hàng\n(Bắt đầu phục vụ trực tiếp)", SwingConstants.CENTER);
            lblNoKh.setFont(new Font("Times New Roman", Font.ITALIC, 14));
            lblNoKh.setForeground(Color.GRAY);
            pnlKhachHang.add(lblNoKh, khGbc);
        }

        // Panel món ăn
        JPanel pnlMonAn = new JPanel(new BorderLayout());
        pnlMonAn.setBackground(Color.WHITE);
        pnlMonAn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Danh sách món ăn đang phục vụ (từ đặt món)", 
            SwingConstants.CENTER, SwingConstants.TOP, fontTitle));

        String[] columns = {"STT", "Món ăn", "Số lượng", "Giá", "Thành tiền"};
        Object[][] data = {};
        DefaultTableModel modelMon = new DefaultTableModel(data, columns);
        JTable tableMon = new JTable(modelMon);
        tableMon.setFont(fontValue);
        tableMon.setRowHeight(25);
        tableMon.getTableHeader().setBackground(COLOR_RED_WINE);
        tableMon.getTableHeader().setForeground(Color.WHITE);
        tableMon.getTableHeader().setFont(fontLabel);
        tableMon.setGridColor(Color.LIGHT_GRAY);
        JScrollPane scrollMonAn = new JScrollPane(tableMon);
        pnlMonAn.add(scrollMonAn, BorderLayout.CENTER);

        JLabel lblTotal = new JLabel("Tổng tạm tính: 0 VNĐ", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Times New Roman", Font.BOLD, 14));
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

        JButton btnDatMon = new JButton("Đặt thêm món");
        btnDatMon.setFont(fontLabel);
        btnDatMon.setPreferredSize(new Dimension(150, 40));
        btnDatMon.setBackground(new Color(46, 204, 113));
        btnDatMon.setForeground(Color.WHITE);
        pnlNut.add(btnDatMon);

        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(fontLabel);
        btnThanhToan.setPreferredSize(new Dimension(150, 40));
        btnThanhToan.setBackground(new Color(52, 152, 219));
        btnThanhToan.setForeground(Color.WHITE);
        pnlNut.add(btnThanhToan);

        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(fontLabel);
        btnDong.setPreferredSize(new Dimension(120, 40));
        btnDong.setBackground(Color.LIGHT_GRAY);
        btnDong.setForeground(Color.BLACK);
        pnlNut.add(btnDong);

        // Sự kiện nút
        btnDatMon.addActionListener(e -> {
            new FrmOrder(maBan).setVisible(true);
            dispose();
        });

        btnThanhToan.addActionListener(e -> {
            new FrmHoaDon().setVisible(true);
            dispose();
        });

        btnDong.addActionListener(e -> dispose());

        // Thêm vào dialog
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        add(pnlNut, BorderLayout.SOUTH);
    }
}