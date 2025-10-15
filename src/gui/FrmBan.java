package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.DefaultTableModel;
import java.awt.AlphaComposite;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.KhuVuc_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.KhuVuc;
import entity.PhieuDatBan;

public class FrmBan extends JFrame {

    private Container pNor;
    private JLabel lblTieuDe;
    private Container sidebar;
    private JPanel pnlNoiDung;
    private JTabbedPane tabbedPane;
    private String banDangChon = null;
    private final Color COLOR_TRONG = Color.WHITE;
    private final Color COLOR_DAT = new Color(236, 66, 48);
    private final Color COLOR_PHUCVU = new Color(55, 212, 23);
    private final Color COLOR_RED_WINE = new Color(128, 0, 0);
    
    private Map<String, BanPanel> mapBan = new HashMap<>();
    private Map<String, JPanel> mapKhu = new HashMap<>();
    private Ban_DAO banDAO;
    private KhuVuc_DAO khuDAO;
    private JPanel pnlChinh;
    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbLoaiBan;
    private JTextField txtTimMaBan;
    private JComboBox<Integer> cbSoNguoi;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private boolean isQuanLy = true;

    public FrmBan() throws SQLException {
        Connection conn = ConnectSQL.getConnection();
        banDAO = new Ban_DAO(conn);
        phieuDatBanDAO = new PhieuDatBan_DAO(conn);
        khuDAO = new KhuVuc_DAO(conn);
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());

        setTitle("Phần mềm quản lý nhà hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar Menu
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.white);
        
        JButton btnThoiGian = createMenuButton("Thời gian", "img/dongho.png");
        btnThoiGian.setPreferredSize(new Dimension(300, 120));
        btnThoiGian.setMaximumSize(new Dimension(300, 120));
        JButton btnDatBan = createMenuButton("Đặt bàn", "img/datban.png");
        JButton btnHuyBan = createMenuButton("Hủy bàn", "img/huyban.png");
        JButton btnGopBan = createMenuButton("Gộp bàn", "img/gopban.png");
        JButton btnChuyenBan = createMenuButton("Chuyển bàn", "img/chuyenban.png");
        JButton btnDatMon = createMenuButton("Đặt món", "img/thucdon.png");
        JButton btnThanhToan = createMenuButton("Thanh toán", "img/hoadon.png");
        

        Timer timer = new Timer(1000, e -> {
            String time = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            btnThoiGian.setText("<html><center>" + time + "<br>" + date + "</center></html>");
        });
        timer.start();
        
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnThoiGian);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnDatBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnHuyBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnGopBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnChuyenBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnDatMon);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnThanhToan);
        
        
        btnDatBan.addActionListener(e -> {
            if (banDangChon != null) {
                try {
                    new FrmDatBan(this, banDangChon, null, phieuDatBanDAO, banDAO).setVisible(true);
                    taiTrangThaiBanTuDB();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi mở form đặt bàn!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });	
        btnHuyBan.addActionListener(e -> {
            if (banDangChon != null) {
                try {
                    hienThiThongTinBan(banDangChon);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị thông tin bàn!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });
        btnGopBan.addActionListener(e -> moFormGopBan());
        btnChuyenBan.addActionListener(e -> moFormChuyenBan());
        btnDatMon.addActionListener(e -> {
            if (banDangChon != null) {
                xuLyDatMon();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });
        btnThanhToan.addActionListener(e -> {
            if (banDangChon != null) {
                xuLyThanhToan();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });

        add(sidebar, BorderLayout.WEST);

        lblTieuDe = new JLabel("Danh Sách Bàn", SwingConstants.CENTER);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(COLOR_RED_WINE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 32));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.add(lblTieuDe, BorderLayout.CENTER);

        pnlChinh = new JPanel(new BorderLayout());

        // Filter Panel
        JPanel pnlLoc = new JPanel(new GridBagLayout());
        pnlLoc.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnLocBanPhucVu = new JButton("Bàn đang phục vụ");
        btnLocBanPhucVu.setBackground(COLOR_PHUCVU);
        btnLocBanPhucVu.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnLocBanPhucVu.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        pnlLoc.add(btnLocBanPhucVu, gbc);

        JLabel lblSoNguoi = new JLabel("Số người");
        lblSoNguoi.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        pnlLoc.add(lblSoNguoi, gbc);

        cbSoNguoi = new JComboBox<>(new Integer[]{0, 2, 4, 6, 8, 10});
        cbSoNguoi.setBackground(Color.WHITE);
        cbSoNguoi.setFont(new Font("Times New Roman", Font.BOLD, 20));   
        gbc.gridx = 2;
        gbc.gridy = 0;
        pnlLoc.add(cbSoNguoi, gbc);

        JLabel lblTim = new JLabel("                Mã bàn");
        lblTim.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 3;
        gbc.gridy = 0;
        pnlLoc.add(lblTim, gbc);
        
        txtTimMaBan = new JTextField(10);
        txtTimMaBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        gbc.gridx = 4;
        gbc.gridy = 0;
        pnlLoc.add(txtTimMaBan, gbc);

        JButton btnTim = new JButton("Tìm");
        btnTim.setBackground(new Color(102, 210, 74));
        btnTim.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnTim.setIcon(new ImageIcon(new ImageIcon("img/timkiem.png").getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));        
        gbc.gridx = 5;
        gbc.gridy = 0;
        pnlLoc.add(btnTim, gbc);


        JButton btnDatLai = new JButton("Đặt lại");
        btnDatLai.setBackground(Color.LIGHT_GRAY);
        btnDatLai.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        pnlLoc.add(btnDatLai, gbc);

        JLabel lblLoai = new JLabel("Loại bàn");
        lblLoai.setFont(new Font("Times New Roman", Font.BOLD, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlLoc.add(lblLoai, gbc);

        JRadioButton tatCa = new JRadioButton("Tất cả", true);
        JRadioButton banThuong = new JRadioButton("Bàn thường");
        JRadioButton banVIP = new JRadioButton("Bàn VIP");
        tatCa.setFont(new Font("Times New Roman", Font.BOLD, 20));
        tatCa.setBackground(Color.WHITE);
        banThuong.setFont(new Font("Times New Roman", Font.BOLD, 20));
        banThuong.setBackground(Color.WHITE);
        banVIP.setFont(new Font("Times New Roman", Font.BOLD, 20));
        banVIP.setBackground(Color.WHITE);
        ButtonGroup groupLoai = new ButtonGroup();
        groupLoai.add(tatCa);
        groupLoai.add(banThuong);
        groupLoai.add(banVIP);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 30);
        pnlLoc.add(tatCa, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        pnlLoc.add(banThuong, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        pnlLoc.add(banVIP, gbc);

        JButton btnLocBanDat = new JButton("Bàn đã đặt");
        btnLocBanDat.setBackground(COLOR_DAT);
        btnLocBanDat.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnLocBanDat.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlLoc.add(btnLocBanDat, gbc);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 20));
        tabbedPane.setBackground(Color.WHITE);

        pnlChinh.add(pnlLoc, BorderLayout.NORTH);
        pnlChinh.add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFooter.setBackground(Color.LIGHT_GRAY);

        JLabel lblTrong = new JLabel(new ImageIcon(new ImageIcon("img/bantrong.png").getImage().getScaledInstance(50, 20, Image.SCALE_SMOOTH)));
        lblTrong.setText(" Bàn trống");
        lblTrong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pnlFooter.add(lblTrong);
        pnlFooter.add(Box.createRigidArea(new Dimension(40, 0)));


        JLabel lblPhucVu = new JLabel(new ImageIcon(new ImageIcon("img/banphucvu.png").getImage().getScaledInstance(50, 20, Image.SCALE_SMOOTH)));
        lblPhucVu.setText(" Bàn đang phục vụ");
        lblPhucVu.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pnlFooter.add(lblPhucVu);
        pnlFooter.add(Box.createRigidArea(new Dimension(40, 0)));


        JLabel lblDat = new JLabel(new ImageIcon(new ImageIcon("img/bandat.png").getImage().getScaledInstance(50, 20, Image.SCALE_SMOOTH)));
        lblDat.setText(" Bàn đã đặt");
        lblDat.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pnlFooter.add(lblDat);
        pnlFooter.add(Box.createRigidArea(new Dimension(40, 0))); 


        JLabel lblVIP = new JLabel(new ImageIcon(new ImageIcon("img/vip.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        lblVIP.setText(" Bàn VIP");
        lblVIP.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pnlFooter.add(lblVIP);

        add(pnlHeader, BorderLayout.NORTH);
        add(pnlChinh, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);

        // Tải dữ liệu
        taiLaiPnlChinh();
        
        // Sự kiện lọc
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Bàn đã đặt", "Bàn đang phục vụ"});
        cbTrangThai.setBackground(Color.WHITE);
        cbLoaiBan = new JComboBox<>(new String[]{"Tất cả", "Bàn thường", "Bàn VIP"});
        cbLoaiBan.setBackground(Color.WHITE);

        btnTim.addActionListener(e -> apDungLoc());
        btnLocBanPhucVu.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đang phục vụ");
            apDungLoc();
        });
        btnLocBanDat.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đã đặt");
            apDungLoc();
        });
        tatCa.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Tất cả");
            apDungLoc();
        });
        banThuong.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn thường");
            apDungLoc();
        });
        banVIP.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn VIP");
            apDungLoc();
        });
        cbSoNguoi.addActionListener(e -> apDungLoc());
        btnDatLai.addActionListener(e -> {
            cbSoNguoi.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            cbLoaiBan.setSelectedIndex(0);
            txtTimMaBan.setText("");
            groupLoai.clearSelection();
            tatCa.setSelected(true);
            apDungLoc();
        });
    }

    private void taiLaiPnlChinh() {
        tabbedPane.removeAll();
        mapBan.clear();
        mapKhu.clear();

        List<KhuVuc> danhSachKhu;
        try {
            danhSachKhu = khuDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            danhSachKhu = new ArrayList<>();
        }

        for (KhuVuc k : danhSachKhu) {
            JPanel pnlKhu = new JPanel();
            pnlKhu.setLayout(new BoxLayout(pnlKhu, BoxLayout.Y_AXIS));
            pnlKhu.setBackground(Color.WHITE);

            List<Ban> danhSachBan;
            try {
                danhSachBan = banDAO.getAllBan(k.getMaKhuVuc());
            } catch (SQLException e) {
                e.printStackTrace();
                danhSachBan = new ArrayList<>();
            }

            JPanel rowPanel = null;
            int colCount = 0;
            for (Ban b : danhSachBan) {
                if (colCount % 5 == 0) {
                    rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
                    rowPanel.setBackground(Color.WHITE);
                    pnlKhu.add(rowPanel);
                }
                BanPanel bp = new BanPanel(b);
                bp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        banDangChon = b.getMaBan();
                        try {
                            hienThiThongTinBan(b.getMaBan());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(FrmBan.this, "Lỗi khi hiển thị thông tin bàn: " + ex.getMessage());
                        }
                    }
                });
                rowPanel.add(bp);
                mapBan.put(b.getMaBan(), bp);
                colCount++;
            }
            mapKhu.put(k.getMaKhuVuc(), pnlKhu);
            tabbedPane.addTab(k.getTenKhuVuc(), new JScrollPane(pnlKhu));
        }

        taiTrangThaiBanTuDB();
    }

    private void apDungLoc() {
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String loai = (String) cbLoaiBan.getSelectedItem();
        int soNguoi = (Integer) cbSoNguoi.getSelectedItem();
        String maSearch = txtTimMaBan.getText().trim().toLowerCase();

        for (BanPanel bp : mapBan.values()) {
            Ban b = bp.getBan();
            boolean show = true;

            if (!maSearch.isEmpty() && !b.getMaBan().toLowerCase().contains(maSearch)) show = false;

            if (soNguoi != 0 && b.getSoChoNgoi() != soNguoi) show = false;

            String tt = layTrangThaiHienTai(b.getMaBan());
            if ("Bàn đã đặt".equals(trangThai) && !"Đặt".equals(tt)) show = false;
            if ("Bàn đang phục vụ".equals(trangThai) && !"Phục vụ".equals(tt)) show = false;

            if ("Bàn thường".equals(loai) && !"Thường".equals(b.getLoaiBan())) show = false;
            if ("Bàn VIP".equals(loai) && !"VIP".equals(b.getLoaiBan())) show = false;

            bp.setVisible(show);
        }
    }

    private void taiTrangThaiBanTuDB() {
        for (BanPanel bp : mapBan.values()) {
            bp.capNhatIcon();
            bp.revalidate();
            bp.repaint();
        }
    }

    private String layTrangThaiHienTai(String maBan) {
        try {
            Date today = new Date(System.currentTimeMillis());
            List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(maBan, today);
            boolean hasPhucVu = list.stream().anyMatch(d -> "Phục vụ".equals(d.getTrangThai()));
            if (hasPhucVu) return "Phục vụ";
            boolean hasDat = list.stream().anyMatch(d -> "Đặt".equals(d.getTrangThai()));
            if (hasDat) return "Đặt";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Trống";
    }

    private void hienThiThongTinBan(String maBan) throws SQLException {
        Ban b = banDAO.getBanByMa(maBan);
        if (b == null) return;

        String tt = layTrangThaiHienTai(maBan);
        if ("Phục vụ".equals(tt)) {
            try {
                new FrmPhucVu(this, maBan, banDAO, phieuDatBanDAO).setVisible(true);
                taiTrangThaiBanTuDB();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin phục vụ: " + ex.getMessage());
            }
            return;
        }

        JDialog dialog = new JDialog(this, "Thông tin bàn", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(1200, 700);
        
        Font fontLabel = new Font("Times New Roman", Font.BOLD, 18);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 18);
        Font fontButton = new Font("Times New Roman", Font.BOLD, 18);

        // Panel thông tin bàn
        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        pnlThongTin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlThongTin.setBackground(Color.white);
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 10, 5, 10);
        gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbcInfo.gridx = 0; gbcInfo.gridy = row;
        JLabel lblMaBan = new JLabel("Mã bàn:");
        lblMaBan.setFont(fontLabel);
        pnlThongTin.add(lblMaBan, gbcInfo);
        gbcInfo.gridx = 1;
        JLabel valMaBan = new JLabel(b.getMaBan());
        valMaBan.setFont(fontValue);
        pnlThongTin.add(valMaBan, gbcInfo);
        row++;

        gbcInfo.gridx = 0; gbcInfo.gridy = row;
        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setFont(fontLabel);
        pnlThongTin.add(lblKhuVuc, gbcInfo);
        gbcInfo.gridx = 1;
        JLabel valKhuVuc = new JLabel(b.getTenKhuVuc() != null ? b.getTenKhuVuc() : "Không xác định");
        valKhuVuc.setFont(fontValue);
        pnlThongTin.add(valKhuVuc, gbcInfo);
        row++;

        gbcInfo.gridx = 0; gbcInfo.gridy = row;
        JLabel lblSoGhe = new JLabel("Số ghế:");
        lblSoGhe.setFont(fontLabel);
        pnlThongTin.add(lblSoGhe, gbcInfo);
        gbcInfo.gridx = 1;
        JLabel valSoGhe = new JLabel(String.valueOf(b.getSoChoNgoi()));
        valSoGhe.setFont(fontValue);
        pnlThongTin.add(valSoGhe, gbcInfo);
        row++;

        gbcInfo.gridx = 0; gbcInfo.gridy = row;
        JLabel lblLoaiBan = new JLabel("Loại bàn:");
        lblLoaiBan.setFont(fontLabel);
        pnlThongTin.add(lblLoaiBan, gbcInfo);
        gbcInfo.gridx = 1;
        JLabel valLoaiBan = new JLabel(b.getLoaiBan());
        valLoaiBan.setFont(fontValue);
        pnlThongTin.add(valLoaiBan, gbcInfo);
        row++;

        // Table lịch đặt
        gbcInfo.gridx = 0; gbcInfo.gridy = row; gbcInfo.gridwidth = 2; gbcInfo.fill = GridBagConstraints.BOTH;
        gbcInfo.weightx = 1; gbcInfo.weighty = 1;
        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Mã phiếu", "Tên khách", "SĐT", "Số người", "Ngày đến", "Giờ đến", "Ghi chú", "Tiền cọc", "Ghi chú cọc", "Trạng thái"
        }, 0);
        JTable table = new JTable(model);
        table.setFont(fontValue);
        table.setRowHeight(30);
        table.getTableHeader().setFont(fontLabel);
        JScrollPane scroll = new JScrollPane(table);
        pnlThongTin.add(scroll, gbcInfo);
        row++;

        // Tải dữ liệu lịch
        List<PhieuDatBan> listDatBan = phieuDatBanDAO.getDatBanByBan(maBan);
        for (PhieuDatBan d : listDatBan) {
            model.addRow(new Object[]{
                d.getMaPhieu(), d.getTenKhach(), d.getSoDienThoai(), d.getSoNguoi(), d.getNgayDen(), d.getGioDen(), d.getGhiChu(), d.getTienCoc(), d.getGhiChuCoc(), d.getTrangThai()
            });
        }

        // Panel nút
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlNut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnDatMoi = new JButton("Đặt bàn");
        btnDatMoi.setFont(fontButton);
        btnDatMoi.setPreferredSize(new Dimension(120, 35));
        btnDatMoi.setBackground(new Color(100, 149, 237));
        btnDatMoi.setForeground(Color.WHITE);
        pnlNut.add(btnDatMoi);

        JButton btnSua = new JButton("Sửa");
        btnSua.setFont(fontButton);
        btnSua.setPreferredSize(new Dimension(100, 35));
        btnSua.setBackground(Color.ORANGE);
        btnSua.setForeground(Color.WHITE);
        pnlNut.add(btnSua);

        JButton btnDangPhucVu = new JButton("Đang phục vụ");
        btnDangPhucVu.setFont(fontButton);
        btnDangPhucVu.setPreferredSize(new Dimension(150, 35));
        btnDangPhucVu.setBackground(new Color(50, 205, 50));
        btnDangPhucVu.setForeground(Color.WHITE);
        pnlNut.add(btnDangPhucVu);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(fontButton);
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setBackground(new Color(220, 20, 60));
        btnHuy.setForeground(Color.WHITE);
        pnlNut.add(btnHuy);

        JButton btnXoaLich = new JButton("Xóa lịch chọn");
        btnXoaLich.setFont(fontButton);
        btnXoaLich.setPreferredSize(new Dimension(150, 35));
        btnXoaLich.setBackground(new Color(139, 0, 0));
        btnXoaLich.setForeground(Color.WHITE);
        pnlNut.add(btnXoaLich);

        // Sự kiện
        btnDatMoi.addActionListener(e -> {
            try {
                new FrmDatBan(this, maBan, null, phieuDatBanDAO, banDAO).setVisible(true);
                dialog.dispose();
                taiTrangThaiBanTuDB();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnSua.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    PhieuDatBan d = phieuDatBanDAO.getByMa(maPhieu);
                    new FrmDatBan(this, maBan, d, phieuDatBanDAO, banDAO).setVisible(true);
                    dialog.dispose();
                    hienThiThongTinBan(maBan);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chọn lịch để sửa!");
            }
        });

        btnDangPhucVu.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    PhieuDatBan d = phieuDatBanDAO.getByMa(maPhieu);
                    if ("Đặt".equals(d.getTrangThai())) {
                        d.setTrangThai("Phục vụ");
                        phieuDatBanDAO.update(d);
                        model.setValueAt("Phục vụ", selected, 9);
                        taiTrangThaiBanTuDB();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(phieuDatBanDAO.generateMaPhieu());
                    d.setMaBan(maBan);
                    d.setNgayDen(new Date(System.currentTimeMillis()));
                    d.setGioDen(new Time(System.currentTimeMillis()));
                    d.setTrangThai("Phục vụ");
                    phieuDatBanDAO.add(d);
                    taiTrangThaiBanTuDB();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            dialog.dispose();
        });

        btnHuy.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    PhieuDatBan d = phieuDatBanDAO.getByMa(maPhieu);
                    moFormLyDoHuy(d, model, selected);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chọn lịch để hủy!");
            }
        });

        btnXoaLich.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    phieuDatBanDAO.delete(maPhieu);
                    model.removeRow(selected);
                    taiTrangThaiBanTuDB();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chọn lịch để xóa!");
            }
        });

        dialog.add(pnlThongTin, BorderLayout.CENTER);
        dialog.add(pnlNut, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void moFormLyDoHuy(PhieuDatBan d, DefaultTableModel model, int selected) {
        JDialog dlg = new JDialog(this, "Hủy Bàn", true);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.getContentPane().setBackground(Color.white);
        dlg.setSize(500, 400);
        dlg.setLocationRelativeTo(this);

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 18);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 16);
        Font fontButton = new Font("Times New Roman", Font.BOLD, 18);

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Hủy Đặt Bàn", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(fontLabel);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        // Nội dung
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(new Color(248, 249, 250));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLyDo = new JLabel("Lý do hủy:");
        lblLyDo.setFont(fontLabel);
        gbc.gridx = 0; gbc.gridy = 0;
        pnlContent.add(lblLyDo, gbc);

        JComboBox<String> cbLyDo = new JComboBox<>(new String[]{"Khách hủy", "Đặt nhầm", "Khác"});
        cbLyDo.setFont(fontValue);
        gbc.gridx = 1; gbc.gridy = 0;
        pnlContent.add(cbLyDo, gbc);

        JTextArea txtKhac = new JTextArea(3, 20);
        txtKhac.setEnabled(false);
        txtKhac.setFont(fontValue);
        txtKhac.setLineWrap(true);
        JScrollPane scrollKhac = new JScrollPane(txtKhac);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        pnlContent.add(scrollKhac, gbc);

        JLabel lblTienTra = new JLabel("Tiền cọc trả:");
        lblTienTra.setFont(fontLabel);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        pnlContent.add(lblTienTra, gbc);

        JTextField txtTienTra = new JTextField(String.valueOf(d.getTienCoc()));
        txtTienTra.setFont(fontValue);
        gbc.gridx = 1; gbc.gridy = 2;
        pnlContent.add(txtTienTra, gbc);

        // Panel nút
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlNut.setBackground(new Color(248, 249, 250));

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(fontButton);
        btnXacNhan.setPreferredSize(new Dimension(120, 35));
        btnXacNhan.setBackground(new Color(100, 149, 237));
        btnXacNhan.setForeground(Color.WHITE);
        pnlNut.add(btnXacNhan);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(fontButton);
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setBackground(Color.LIGHT_GRAY);
        btnHuy.setForeground(Color.BLACK);
        pnlNut.add(btnHuy);

        cbLyDo.addActionListener(e -> {
            txtKhac.setEnabled("Khác".equals(cbLyDo.getSelectedItem()));
        });

        btnXacNhan.addActionListener(e -> {
            String lyDo = (String) cbLyDo.getSelectedItem();
            if ("Khác".equals(lyDo)) {
                lyDo = txtKhac.getText();
            }
            if (lyDo.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập lý do nếu chọn Khác!");
                return;
            }
            double tienTra = Double.parseDouble(txtTienTra.getText());

            d.setGhiChu(d.getGhiChu() + "\nHủy: " + lyDo + " - Trả cọc: " + tienTra);
            d.setTrangThai("Hủy");
            try {
                phieuDatBanDAO.update(d);
                model.setValueAt("Hủy", selected, 9);
                model.setValueAt(d.getGhiChu(), selected, 6);
                taiTrangThaiBanTuDB();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            dlg.dispose();
        });

        btnHuy.addActionListener(e -> dlg.dispose());

        dlg.add(pnlHeader, BorderLayout.NORTH);
        dlg.add(pnlContent, BorderLayout.CENTER);
        dlg.add(pnlNut, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void moFormGopBan() {
	    if (banDangChon == null) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn chính!");
	        return;
	    }
	
	    JDialog dlg = new JDialog(this, "Gộp bàn", true);
	    dlg.setLayout(new BorderLayout(10, 10));
	    dlg.setSize(900, 600);
	    dlg.setLocationRelativeTo(this);
	
	    JPanel pnlHeader = new JPanel(new BorderLayout(10, 20));
	    JLabel lblHeader = new JLabel("Gộp bàn", SwingConstants.CENTER);
	    lblHeader.setBackground(COLOR_RED_WINE);
	    lblHeader.setForeground(Color.WHITE);
	    lblHeader.setOpaque(true);
	    lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 28));
	    pnlHeader.add(lblHeader, BorderLayout.NORTH);
	
	    JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JLabel lblBanHienTai = new JLabel("Mã bàn hiện tại: " + banDangChon + " >> ");
	    lblBanHienTai.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    JTextField txtBanSo = new JTextField(10);
	    txtBanSo.setFont(new Font("Times New Roman", Font.PLAIN, 22));
	    JButton btnTim = new JButton("Tìm");
	    btnTim.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnTim.setBackground(new Color(70, 130, 180));
	    btnTim.setForeground(Color.WHITE);
	    btnTim.setFocusPainted(false);
	    pnlSearch.add(lblBanHienTai);
	    
	    lblBanHienTai.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> ");
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> ");
	        }
	    });
	    pnlSearch.add(Box.createHorizontalStrut(200));
	    JLabel lblMaBan = new JLabel("Mã bàn ");
	    lblMaBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    pnlSearch.add(Box.createHorizontalStrut(40));
	    pnlSearch.add(lblMaBan);
	    pnlSearch.add(txtBanSo);
	    pnlSearch.add(btnTim);
	    pnlHeader.add(pnlSearch, BorderLayout.CENTER);
	    dlg.add(pnlHeader, BorderLayout.NORTH);
	
	    // Bảng dữ liệu
	    DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã bàn", "Trạng thái", "Loại bàn", "Số người"}, 0);
	    JTable table = new JTable(model) {
	        @Override
	        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
	            super.changeSelection(row, col, toggle, extend);
	            if (row >= 0) {
	                String selectedBan = (String) model.getValueAt(row, 0);
	                lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> " + selectedBan);
	            }
	        }
	    };
	    table.setFont(new Font("Times New Roman", Font.PLAIN, 20));
	    table.setRowHeight(30);
	    JScrollPane scroll = new JScrollPane(table);
	    dlg.add(scroll, BorderLayout.CENTER);
	
	    try {
	        List<Ban> banTrong = banDAO.getBanTrong();
	        if (banTrong != null) {
	            for (Ban ban : banTrong) {
	                String trangThai = layTrangThaiHienTai(ban.getMaBan());
	                if ("Trống".equals(trangThai)) { 
	                    model.addRow(new Object[]{ban.getMaBan(), trangThai, ban.getLoaiBan(), ban.getSoChoNgoi()});
	                }
	            }
	        } else {
	            JOptionPane.showMessageDialog(dlg, "Không thể tải danh sách bàn trống!");
	            dlg.dispose();
	            return;
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(dlg, "Lỗi khi tải danh sách bàn!");
	        dlg.dispose();
	        return;
	    }
	
	    // Panel nút
	    JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton btnQuayLai = new JButton("Quay lại");
	    JButton btnLamMoi = new JButton("Làm mới");
	    JButton btnGop = new JButton("Gộp");
	    btnQuayLai.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnLamMoi.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnGop.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    
	    btnQuayLai.setBackground(new Color(216, 154, 161));
	    btnLamMoi.setBackground(new Color(210, 201, 74));
	    btnGop.setBackground(new Color(102, 210, 74));
	    
	    btnQuayLai.setFocusPainted(false);
	    btnLamMoi.setFocusPainted(false);
	    btnGop.setFocusPainted(false);
	    pnlNut.add(btnQuayLai);
	    pnlNut.add(btnLamMoi);
	    pnlNut.add(btnGop);
	    dlg.add(pnlNut, BorderLayout.SOUTH);
	
	    btnGop.addActionListener(e -> {
	        int selected = table.getSelectedRow();
	        if (selected >= 0) {
	            String banGop = (String) model.getValueAt(selected, 0);
	            try {
	                banDAO.gopBan(banDangChon, banGop);
	                taiTrangThaiBanTuDB();
	                JOptionPane.showMessageDialog(dlg, "Đã gộp bàn!");
	                dlg.dispose();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        } else {
	            JOptionPane.showMessageDialog(dlg, "Vui lòng chọn bàn để gộp!");
	        }
	    });
	
	    btnLamMoi.addActionListener(e -> {
	        txtBanSo.setText("");
	        model.setRowCount(0);
	        try {
	            List<Ban> banTrong = banDAO.getBanTrong();
	            if (banTrong != null) {
	                for (Ban ban : banTrong) {
	                    model.addRow(new Object[]{ban.getMaBan(), "Trống", ban.getLoaiBan(), ban.getSoChoNgoi()});
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dlg, "Lỗi khi tải lại danh sách bàn!");
	        }
	    });
	
	    btnQuayLai.addActionListener(e -> dlg.dispose());
	    btnTim.addActionListener(e -> {
	        String banSo = txtBanSo.getText().trim();
	        if (!banSo.isEmpty()) {
	            model.setRowCount(0);
	            try {
	                Ban ban = banDAO.getBanByMa(banSo);
	                if (ban != null) {
	                    model.addRow(new Object[]{ban.getMaBan(), "Trống", ban.getLoaiBan(), ban.getSoChoNgoi()});
	                } else {
	                    JOptionPane.showMessageDialog(dlg, "Không tìm thấy bàn!");
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(dlg, "Lỗi khi tìm kiếm bàn!");
	            }
	        }
	    });

	    dlg.setVisible(true);
    }

	private void moFormChuyenBan() {
	    if (banDangChon == null) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần chuyển!");
	        return;
	    }
	
	    JDialog dlg = new JDialog(this, "Chuyển bàn", true);
	    dlg.setLayout(new BorderLayout(10, 10));
	    dlg.setSize(900, 600);
	    dlg.setLocationRelativeTo(this);
	
	    // Header với nhãn và ô nhập liệu
	    JPanel pnlHeader = new JPanel(new BorderLayout(10, 20));
	    JLabel lblHeader = new JLabel("Chuyển bàn", SwingConstants.CENTER);
	    lblHeader.setBackground(COLOR_RED_WINE);
	    lblHeader.setForeground(Color.WHITE);
	    lblHeader.setOpaque(true);
	    lblHeader.setFont(new Font("Times New Roman", Font.BOLD, 28));
	    pnlHeader.add(lblHeader, BorderLayout.NORTH);
	
	    JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JLabel lblBanHienTai = new JLabel("Mã bàn hiện tại: " + banDangChon + " >> ");
	    lblBanHienTai.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    JTextField txtBanSo = new JTextField(10);
	    txtBanSo.setFont(new Font("Times New Roman", Font.PLAIN, 22));
	    JButton btnTim = new JButton("Tìm");
	    btnTim.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnTim.setBackground(new Color(70, 130, 180));
	    btnTim.setForeground(Color.WHITE);
	    btnTim.setFocusPainted(false);
	    pnlSearch.add(lblBanHienTai);
	    
	    lblBanHienTai.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> ");
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> ");
	        }
	    });
	    pnlSearch.add(Box.createHorizontalStrut(200));
	    JLabel lblMaBan = new JLabel("Mã bàn ");
	    lblMaBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    pnlSearch.add(Box.createHorizontalStrut(40));
	    pnlSearch.add(lblMaBan);
	    pnlSearch.add(txtBanSo);
	    pnlSearch.add(btnTim);
	    pnlHeader.add(pnlSearch, BorderLayout.CENTER);
	    dlg.add(pnlHeader, BorderLayout.NORTH);
	
	    // Bảng dữ liệu
	    DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã bàn", "Trạng thái", "Loại bàn", "Số người"}, 0);
	    JTable table = new JTable(model) {
	        @Override
	        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
	            super.changeSelection(row, col, toggle, extend);
	            if (row >= 0) {
	                String selectedBan = (String) model.getValueAt(row, 0);
	                lblBanHienTai.setText("Mã bàn hiện tại: " + banDangChon + " >> " + selectedBan);
	            }
	        }
	    };
	    table.setFont(new Font("Times New Roman", Font.PLAIN, 20));
	    table.setRowHeight(30);
	    JScrollPane scroll = new JScrollPane(table);
	    dlg.add(scroll, BorderLayout.CENTER);
	
	    try {
	        List<Ban> banTrong = banDAO.getBanTrong();
	        if (banTrong != null) {
	            for (Ban ban : banTrong) {
	                String trangThai = layTrangThaiHienTai(ban.getMaBan());
	                if ("Trống".equals(trangThai)) {
	                    model.addRow(new Object[]{ban.getMaBan(), trangThai, ban.getLoaiBan(), ban.getSoChoNgoi()});
	                }
	            }
	        } else {
	            JOptionPane.showMessageDialog(dlg, "Không thể tải danh sách bàn trống!");
	            dlg.dispose();
	            return;
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(dlg, "Lỗi khi tải danh sách bàn!");
	        dlg.dispose();
	        return;
	    }
	
	    // Panel nút
	    JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton btnQuayLai = new JButton("Quay lại");
	    JButton btnLamMoi = new JButton("Làm mới");
	    JButton btnChuyen = new JButton("Chuyển");
	    btnQuayLai.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnLamMoi.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    btnChuyen.setFont(new Font("Times New Roman", Font.BOLD, 22));
	    
	    btnQuayLai.setBackground(new Color(216, 154, 161));
	    btnLamMoi.setBackground(new Color(210, 201, 74));
	    btnChuyen.setBackground(new Color(102, 210, 74));
	    
	    btnQuayLai.setFocusPainted(false);
	    btnLamMoi.setFocusPainted(false);
	    btnChuyen.setFocusPainted(false);
	    pnlNut.add(btnQuayLai);
	    pnlNut.add(btnLamMoi);
	    pnlNut.add(btnChuyen);
	    dlg.add(pnlNut, BorderLayout.SOUTH);
	
	    btnChuyen.addActionListener(e -> {
	        int selected = table.getSelectedRow();
	        if (selected >= 0) {
	            String banMoi = (String) model.getValueAt(selected, 0);
	            try {
	                banDAO.chuyenBan(banDangChon, banMoi);
	                taiTrangThaiBanTuDB();
	                JOptionPane.showMessageDialog(dlg, "Đã chuyển bàn!");
	                dlg.dispose();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        } else {
	            JOptionPane.showMessageDialog(dlg, "Vui lòng chọn bàn để chuyển!");
	        }
	    });
	
	    btnLamMoi.addActionListener(e -> {
	        txtBanSo.setText("");
	        model.setRowCount(0);
	        try {
	            List<Ban> banTrong = banDAO.getBanTrong();
	            if (banTrong != null) {
	                for (Ban ban : banTrong) {
	                    model.addRow(new Object[]{ban.getMaBan(), "Trống", ban.getLoaiBan(), ban.getSoChoNgoi()});
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(dlg, "Lỗi khi tải lại danh sách bàn!");
	        }
	    });
	
	    btnQuayLai.addActionListener(e -> dlg.dispose());
	    btnTim.addActionListener(e -> {
	        String banSo = txtBanSo.getText().trim();
	        if (!banSo.isEmpty()) {
	            model.setRowCount(0);
	            try {
	                Ban ban = banDAO.getBanByMa(banSo);
	                if (ban != null) {
	                    model.addRow(new Object[]{ban.getMaBan(), "Trống", ban.getLoaiBan(), ban.getSoChoNgoi()});
	                } else {
	                    JOptionPane.showMessageDialog(dlg, "Không tìm thấy bàn!");
	                }
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(dlg, "Lỗi khi tìm kiếm bàn!");
	            }
	        }
	    });
	
	    dlg.setVisible(true);
	}

    private void xuLyDatMon() {
        String tt = layTrangThaiHienTai(banDangChon);
        if (!"Phục vụ".equals(tt)) {
            try {
                Date today = new Date(System.currentTimeMillis());
                List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(banDangChon, today);
                PhieuDatBan existingDat = list.stream().filter(p -> "Đặt".equals(p.getTrangThai())).findFirst().orElse(null);
                if (existingDat != null) {
                    existingDat.setTrangThai("Phục vụ");
                    phieuDatBanDAO.update(existingDat);
                } else {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(phieuDatBanDAO.generateMaPhieu());
                    d.setMaBan(banDangChon);
                    d.setNgayDen(today);
                    d.setGioDen(new Time(System.currentTimeMillis()));
                    d.setTrangThai("Phục vụ");
                    phieuDatBanDAO.add(d);
                }
                taiTrangThaiBanTuDB();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn!");
                return;
            }
        }
        new FrmOrder(banDangChon).setVisible(true);
    }

    private void xuLyThanhToan() {
        String tt = layTrangThaiHienTai(banDangChon);
        if ("Phục vụ".equals(tt)) {
            new FrmHoaDon().setVisible(true);
            taiTrangThaiBanTuDB();
        } else {
            JOptionPane.showMessageDialog(this, "Bàn phải ở trạng thái phục vụ để thanh toán!");
        }
    }

    private ImageIcon toMauIcon(ImageIcon source, Color color) {
        BufferedImage image = new BufferedImage(source.getIconWidth(), source.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(source.getImage(), 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, source.getIconWidth(), source.getIconHeight());
        g2d.dispose();
        return new ImageIcon(image);
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btn.setBackground(Color.white);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);

        btn.setPreferredSize(new Dimension(300, 100));
        btn.setMaximumSize(new Dimension(300, 100));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon: " + iconPath);
        }

        Color defaultColor = Color.white;
        Color hoverColor = new Color(255, 204, 204);
        Color selectedColor = new Color(173, 216, 230);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(defaultColor)) {
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(selectedColor)) {
                    btn.setBackground(defaultColor);
                }
            }
        });

        btn.addActionListener(e -> {
            for (Component c : sidebar.getComponents()) {
                if (c instanceof JButton) {
                    c.setBackground(defaultColor);
                }
            }
            btn.setBackground(selectedColor);
        });

        return btn;
    }

    class BanPanel extends JPanel {
        private JLabel lblIcon;
        private Ban ban;

        public BanPanel(Ban b) {
            this.ban = b;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(180, 200));
            setMaximumSize(new Dimension(180, 200));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setBackground(Color.WHITE);

            lblIcon = new JLabel();
            lblIcon.setAlignmentX(CENTER_ALIGNMENT);
            lblIcon.setLayout(null);
            capNhatIcon();
            add(lblIcon);

            JLabel lblSo = new JLabel(b.getSoChoNgoi() + " người");
            lblSo.setAlignmentX(CENTER_ALIGNMENT);
            lblSo.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 20));
            add(lblSo);

            JLabel lblMa = new JLabel(b.getMaBan());
            lblMa.setAlignmentX(CENTER_ALIGNMENT);
            lblMa.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 20));
            add(lblMa);

            if ("VIP".equals(b.getLoaiBan())) {
                ImageIcon vipIcon = new ImageIcon("img/vip.png");
                Image scaledVip = vipIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                JLabel vipL = new JLabel(new ImageIcon(scaledVip));
                int iconWidth = lblIcon.getIcon().getIconWidth();
                int vipWidth = 60;
                int vipHeight = 60;
                vipL.setBounds((iconWidth - vipWidth) / 1, 0, vipWidth, vipHeight);
                lblIcon.add(vipL);
            }
        }

        public Ban getBan() {
            return ban;
        }

        public void capNhatIcon() {
            ImageIcon base = new ImageIcon("img/bantron.png");
            int newWidth = 150;
            int newHeight = 150;
            Image scaled = base.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);

            lblIcon.setIcon(scaledIcon);
            lblIcon.setPreferredSize(new Dimension(newWidth, newHeight));

            String tt = layTrangThaiHienTai(ban.getMaBan());
            Color color = null;
            if ("Đặt".equals(tt)) color = COLOR_DAT;
            else if ("Phục vụ".equals(tt)) color = COLOR_PHUCVU;

            if (color != null) {
                lblIcon.setIcon(toMauIcon(scaledIcon, color));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FrmBan().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}