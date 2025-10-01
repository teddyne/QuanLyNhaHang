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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.AlphaComposite;
import java.text.SimpleDateFormat;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.KhuVuc_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.KhuVuc;
import entity.PhieuDatBan;

public class FrmBan extends JFrame {

    private Container pNor;
    private JLabel lbltieude;
    private Container sidebar;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane;
    private String banDangChon = null;
    private final Color COLOR_TRONG = Color.WHITE;   // trống
    private final Color COLOR_DAT = new Color(255, 0, 0);     // đã đặt (màu đỏ)
    private final Color COLOR_PHUCVU = new Color(0, 255, 0);  // đang phục vụ (màu xanh)
    
    private Map<String, BanPanel> mapBan = new HashMap<>();
    private Map<String, JPanel> mapKhu = new HashMap<>();
    private Ban_DAO banDAO;
    private KhuVuc_DAO khuDAO;
    private Font fontBig;
    private JPanel mainPanel;
    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbLoaiBan;
    private JTextField txtTimMaBan;
    private JComboBox<Integer> cbSoNguoi;
    private PhieuDatBan_DAO phieudatBanDAO;
    
    public FrmBan() throws SQLException {
        Connection conn = ConnectSQL.getConnection();
        banDAO = new Ban_DAO(conn);
        phieudatBanDAO = new PhieuDatBan_DAO(conn);
        khuDAO = new KhuVuc_DAO(conn);
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());

        setTitle("Phần mềm quản lý nhà hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Sidebar Menu =====
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.white);
        
        // Các nút menu
        JButton btnTime = createMenuButton("Thời gian", "img/dongho.png");
        JButton btnDatBan = createMenuButton("Đặt bàn", "img/datban.png");
        JButton btnHuyBan = createMenuButton("Hủy bàn", "img/huyban.png");
        JButton btnGopBan = createMenuButton("Gộp bàn", "img/gopban.png");
        JButton btnChuyenBan = createMenuButton("Chuyển bàn", "img/chuyenban.png");
        JButton btnDatMon = createMenuButton("Đặt món", "img/thucdon.png");
        JButton btnThanhToan = createMenuButton("Thanh toán", "img/hoadon.png");
        JButton btnKhuVuc = createMenuButton("Quản lý khu vực", "img/khuvuc.png");
        JButton btnChinhSua = createMenuButton("Quản lý bàn", "img/chinhsua.png");

        Timer timer = new Timer(1000, e -> {
            String time = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            btnTime.setText("<html><center>" + time + "<br>" + date + "</center></html>");
        });
        timer.start();
        
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnTime);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnDatBan);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnHuyBan);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnGopBan);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnChuyenBan);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnDatMon);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnThanhToan);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnKhuVuc);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(btnChinhSua);
        
        btnDatBan.addActionListener(e -> {
            if (banDangChon != null) {
                try {
                    moFormDatBan(banDangChon);
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
        btnKhuVuc.addActionListener(e -> moFormQuanLyKhuVuc());
        btnChinhSua.addActionListener(e -> moFormQuanLyBan());

        add(sidebar, BorderLayout.WEST);

        // ===== Header =====
        JLabel lbltieude = new JLabel("Danh Sách Bàn", SwingConstants.CENTER);
        lbltieude.setOpaque(true);
        lbltieude.setBackground(new Color(128, 0, 0)); // đỏ rượu
        lbltieude.setForeground(Color.WHITE);
        lbltieude.setFont(new Font("Times New Roman", Font.BOLD, 32));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.add(lbltieude, BorderLayout.CENTER);

        // ===== Center Panel with Filter Panel at Top =====
        mainPanel = new JPanel(new BorderLayout());

        // Filter Panel
        JPanel pnlFilter = new JPanel(new GridBagLayout());
        pnlFilter.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnlocBanPV = new JButton("Bàn đang phục vụ");
        btnlocBanPV.setBackground(COLOR_PHUCVU);
        btnlocBanPV.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnlocBanPV.setPreferredSize(new Dimension(180, 40));
        gbc.gridy = 0;
        pnlFilter.add(btnlocBanPV, gbc);

        JLabel lbltim = new JLabel("Mã bàn");
        lbltim.setFont(new Font("Times New Roman", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlFilter.add(lbltim, gbc);

        txtTimMaBan = new JTextField(20);
        gbc.gridx = 2;
        gbc.gridy = 0;
        pnlFilter.add(txtTimMaBan, gbc);

        JButton btnTim = new JButton("🔍");
        btnTim.setBackground(Color.white);
        gbc.gridx = 3;
        gbc.gridy = 0;
        pnlFilter.add(btnTim, gbc);

        JLabel lblsoNguoi = new JLabel("Số người");
        lblsoNguoi.setFont(new Font("Times New Roman", Font.BOLD, 18));
        gbc.gridx = 4;
        gbc.gridy = 0;
        pnlFilter.add(lblsoNguoi, gbc);

        cbSoNguoi = new JComboBox<>(new Integer[]{0, 2, 4, 6, 8, 10});
        gbc.gridx = 5;
        gbc.gridy = 0;
        cbSoNguoi.setFont(new Font("Times New Roman", Font.BOLD, 18));
        pnlFilter.add(cbSoNguoi, gbc);

        JButton btnlocBanDat = new JButton("Bàn đã đặt");
        btnlocBanDat.setBackground(COLOR_DAT);
        btnlocBanDat.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnlocBanDat.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlFilter.add(btnlocBanDat, gbc);

        JLabel lblLoai = new JLabel("Loại bàn");
        lblLoai.setFont(new Font("Times New Roman", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlFilter.add(lblLoai, gbc);

        JRadioButton tatCa = new JRadioButton("Tất cả", true);
        JRadioButton banThuong = new JRadioButton("Bàn thường");
        JRadioButton banVIP = new JRadioButton("Bàn VIP");
        tatCa.setFont(new Font("Times New Roman", Font.BOLD, 18));
        banThuong.setFont(new Font("Times New Roman", Font.BOLD, 18));
        banVIP.setFont(new Font("Times New Roman", Font.BOLD, 18));
        ButtonGroup groupLoai = new ButtonGroup();
        groupLoai.add(tatCa);
        groupLoai.add(banThuong);
        groupLoai.add(banVIP);

        gbc.gridx = 2;
        gbc.gridy = 1;
        pnlFilter.add(tatCa, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        pnlFilter.add(banThuong, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        pnlFilter.add(banVIP, gbc);

        // ===== Cấu hình tabbedPane cho các khu vực =====
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 20));
        tabbedPane.setBackground(Color.WHITE);

        mainPanel.add(pnlFilter, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // ===== Footer (Ghi chú) =====
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFooter.setBackground(Color.LIGHT_GRAY);
        pnlFooter.add(new JLabel("Bàn trống"));
        pnlFooter.add(new JLabel("Bàn đang phục vụ")).setForeground(COLOR_PHUCVU);
        pnlFooter.add(new JLabel("Bàn đã đặt")).setForeground(COLOR_DAT);
        pnlFooter.add(new JLabel("Bàn VIP"));

        add(pnlHeader, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);

        // Tải dữ liệu
        reloadMainPanel();
        
        // Sự kiện lọc
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Bàn đã đặt", "Bàn đang phục vụ"});
        cbLoaiBan = new JComboBox<>(new String[]{"Tất cả", "Bàn thường", "Bàn VIP"});
        pnlFilter.add(cbTrangThai);
        pnlFilter.add(cbLoaiBan);

        btnTim.addActionListener(e -> applyFilter());
        btnlocBanPV.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đang phục vụ");
            applyFilter();
        });
        btnlocBanDat.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đã đặt");
            applyFilter();
        });
        tatCa.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Tất cả");
            applyFilter();
        });
        banThuong.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn thường");
            applyFilter();
        });
        banVIP.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn VIP");
            applyFilter();
        });
        cbSoNguoi.addActionListener(e -> applyFilter());
    }

    private void reloadMainPanel() {
        tabbedPane.removeAll();
        mapBan.clear();
        mapKhu.clear();

        List<KhuVuc> khuList;
        try {
            khuList = khuDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            khuList = new ArrayList<>();
        }

        for (KhuVuc k : khuList) {
            JPanel khuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Tăng khoảng cách giữa các bàn
            khuPanel.setBackground(new Color(245, 245, 245));

            List<Ban> listBan;
            try {
                listBan = banDAO.getAllBan(k.getMaKhuVuc());
            } catch (SQLException e) {
                e.printStackTrace();
                listBan = new ArrayList<>();
            }

            for (Ban b : listBan) {
                BanPanel bp = new BanPanel(b);
                bp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        banDangChon = b.getMaBan();
                        try {
                            hienThiThongTinBan(b.getMaBan());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                khuPanel.add(bp);
                mapBan.put(b.getMaBan(), bp);
            }
            mapKhu.put(k.getMaKhuVuc(), khuPanel);
            tabbedPane.addTab(k.getTenKhuVuc(), new JScrollPane(khuPanel));
        }

        taiTrangThaiBanTuDB();
    }

    private void applyFilter() {
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String loai = (String) cbLoaiBan.getSelectedItem();
        int soNguoi = (Integer) cbSoNguoi.getSelectedItem();
        String maSearch = txtTimMaBan.getText().trim().toLowerCase();

        for (BanPanel bp : mapBan.values()) {
            Ban b = bp.getBan();
            boolean show = true;

            if (!maSearch.isEmpty() && !b.getMaBan().toLowerCase().contains(maSearch)) show = false;

            if (soNguoi != 0 && b.getSoChoNgoi() != soNguoi) show = false;

            String tt = getTrangThaiHienTai(b.getMaBan());
            if ("Bàn đã đặt".equals(trangThai) && !"Đặt".equals(tt)) show = false;
            if ("Bàn đang phục vụ".equals(trangThai) && !"Phục vu".equals(tt)) show = false;

            if ("Bàn thường".equals(loai) && !"Thường".equals(b.getLoaiBan())) show = false;
            if ("Bàn VIP".equals(loai) && !"VIP".equals(b.getLoaiBan())) show = false;

            bp.setVisible(show);
        }
    }

    private void taiTrangThaiBanTuDB() {
        for (BanPanel bp : mapBan.values()) {
            bp.updateIcon();
        }
    }

    private String getTrangThaiHienTai(String maBan) {
        try {
            Date today = new Date(new java.util.Date().getTime());
            List<PhieuDatBan> list = phieudatBanDAO.getDatBanByBanAndNgay(maBan, today);
            boolean hasPhucVu = list.stream().anyMatch(d -> "Phục vu".equals(d.getTrangThai()));
            if (hasPhucVu) return "Phục vu";
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

        JDialog dialog = new JDialog(this, "Thông tin bàn", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(this);

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 20);
        Font fontButton = new Font("Times New Roman", Font.BOLD, 20);

        // Panel chứa thông tin bàn
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMaBan = new JLabel("Mã bàn:");
        lblMaBan.setFont(fontLabel);
        infoPanel.add(lblMaBan, gbc);
        gbc.gridx = 1;
        JLabel valMaBan = new JLabel(b.getMaBan());
        valMaBan.setFont(fontValue);
        infoPanel.add(valMaBan, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setFont(fontLabel);
        infoPanel.add(lblKhuVuc, gbc);
        gbc.gridx = 1;
        JLabel valKhuVuc = new JLabel(b.getTenKhuVuc());
        valKhuVuc.setFont(fontValue);
        infoPanel.add(valKhuVuc, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSoGhe = new JLabel("Số ghế:");
        lblSoGhe.setFont(fontLabel);
        infoPanel.add(lblSoGhe, gbc);
        gbc.gridx = 1;
        JLabel valSoGhe = new JLabel(String.valueOf(b.getSoChoNgoi()));
        valSoGhe.setFont(fontValue);
        infoPanel.add(valSoGhe, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLoaiBan = new JLabel("Loại bàn:");
        lblLoaiBan.setFont(fontLabel);
        infoPanel.add(lblLoaiBan, gbc);
        gbc.gridx = 1;
        JLabel valLoaiBan = new JLabel(b.getLoaiBan());
        valLoaiBan.setFont(fontValue);
        infoPanel.add(valLoaiBan, gbc);
        row++;

        // Table lịch đặt
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Mã phiếu", "Tên khách", "SĐT", "Số người", "Ngày đến", "Giờ đến", "Ghi chú", "Tiền cọc", "Ghi chú cọc", "Trạng thái"
        }, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        infoPanel.add(scroll, gbc);
        row++;

        // Tải dữ liệu lịch
        List<PhieuDatBan> listDatBan = phieudatBanDAO.getDatBanByBan(maBan);
        for (PhieuDatBan d : listDatBan) {
            model.addRow(new Object[]{
                d.getMaPhieu(), d.getTenKhach(), d.getSoDienThoai(), d.getSoNguoi(), d.getNgayDen(), d.getGioDen(), d.getGhiChu(), d.getTienCoc(), d.getGhiChuCoc(), d.getTrangThai()
            });
        }

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnDatMoi = new JButton("Đặt bàn");
        btnDatMoi.setFont(fontButton);
        btnDatMoi.setPreferredSize(new Dimension(120, 35));
        btnDatMoi.setBackground(new Color(100, 149, 237));
        btnDatMoi.setForeground(Color.WHITE);
        buttonPanel.add(btnDatMoi);

        JButton btnSua = new JButton("Sửa");
        btnSua.setFont(fontButton);
        btnSua.setPreferredSize(new Dimension(100, 35));
        btnSua.setBackground(Color.ORANGE);
        btnSua.setForeground(Color.WHITE);
        buttonPanel.add(btnSua);

        JButton btnDangPhucVu = new JButton("Đang phục vụ");
        btnDangPhucVu.setFont(fontButton);
        btnDangPhucVu.setPreferredSize(new Dimension(120, 35));
        btnDangPhucVu.setBackground(new Color(50, 205, 50));
        btnDangPhucVu.setForeground(Color.WHITE);
        buttonPanel.add(btnDangPhucVu);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(fontButton);
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setBackground(new Color(220, 20, 60));
        btnHuy.setForeground(Color.WHITE);
        buttonPanel.add(btnHuy);

        // Sự kiện
        btnDatMoi.addActionListener(e -> {
            try {
                moFormDatBan(maBan);
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
                    PhieuDatBan d = phieudatBanDAO.getByMa(maPhieu);
                    moFormDatBan(maBan, d); // overload for edit
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
                    PhieuDatBan d = phieudatBanDAO.getByMa(maPhieu);
                    if ("Đặt".equals(d.getTrangThai())) {
                        d.setTrangThai("Phục vu");
                        phieudatBanDAO.update(d);
                        model.setValueAt("Phục vu", selected, 9);
                        taiTrangThaiBanTuDB();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chọn lịch để chuyển trạng thái!");
            }
        });

        btnHuy.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    PhieuDatBan d = phieudatBanDAO.getByMa(maPhieu);
                    d.setTrangThai("Hủy");
                    phieudatBanDAO.update(d);
                    model.setValueAt("Hủy", selected, 9);
                    taiTrangThaiBanTuDB();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chọn lịch để hủy!");
            }
        });

        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(800, 500);
        dialog.setVisible(true);
    }

    
    private void moFormDatBan(String maBan) throws SQLException {
        moFormDatBan(maBan, null);
    }

    private void moFormDatBan(String maBan, PhieuDatBan editD) throws SQLException {
        Ban ban = banDAO.getBanByMa(maBan);
        if (ban == null) return;

        String title = editD == null ? "Đặt bàn" : "Sửa đặt bàn";
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(700, 600);
        dlg.setLayout(new BorderLayout(15, 15));
        dlg.setLocationRelativeTo(this);

        Font fontBig = new Font("Arial", Font.BOLD, 20);

        // Header
        JLabel lblThongTinBan = new JLabel("Bàn: " + ban.getMaBan() + " | Loại bàn: " + ban.getLoaiBan() + " | Khu vực: " + ban.getTenKhuVuc(), SwingConstants.CENTER);
        lblThongTinBan.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblThongTinBan.setForeground(Color.BLUE);
        dlg.add(lblThongTinBan, BorderLayout.NORTH);

        // Form
        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaPhieu = new JTextField(editD != null ? editD.getMaPhieu() : phieudatBanDAO.generateMaPhieu());
        txtMaPhieu.setEditable(false);
        txtMaPhieu.setFont(fontBig);

        JTextField txtTen = new JTextField(editD != null ? editD.getTenKhach() : "", 20); txtTen.setFont(fontBig);
        JTextField txtSDT = new JTextField(editD != null ? editD.getSoDienThoai() : "", 15); txtSDT.setFont(fontBig);
        JButton btnCheckKH = new JButton("Kiểm tra"); btnCheckKH.setFont(fontBig);

        SpinnerDateModel dateModel = new SpinnerDateModel(editD != null ? editD.getNgayDen() : new java.util.Date(), new java.util.Date(), null, Calendar.DAY_OF_MONTH);
        JSpinner spnNgay = new JSpinner(dateModel);
        spnNgay.setEditor(new JSpinner.DateEditor(spnNgay, "yyyy-MM-dd")); spnNgay.setFont(fontBig);

        SpinnerDateModel timeModel = new SpinnerDateModel(editD != null ? new java.util.Date(editD.getGioDen().getTime()) : new java.util.Date(), null, null, Calendar.MINUTE);
        JSpinner spnGio = new JSpinner(timeModel);
        spnGio.setEditor(new JSpinner.DateEditor(spnGio, "HH:mm")); spnGio.setFont(fontBig);

        JTextField txtSoNguoi = new JTextField(editD != null ? String.valueOf(editD.getSoNguoi()) : "", 5); txtSoNguoi.setFont(fontBig);
        JTextArea txtGhiChu = new JTextArea(editD != null ? editD.getGhiChu() : "", 3, 20); txtGhiChu.setFont(fontBig);

        JTextField txtTienCoc = new JTextField(editD != null ? String.valueOf(editD.getTienCoc()) : "", 10); txtTienCoc.setFont(fontBig);
        JCheckBox chkCK = new JCheckBox("Chuyển khoản"); chkCK.setFont(fontBig);
        if (editD != null && !editD.getGhiChuCoc().isEmpty()) chkCK.setSelected(true);
        JTextArea txtGhiChuCK = new JTextArea(editD != null ? editD.getGhiChuCoc() : "", 2, 20); txtGhiChuCK.setFont(fontBig);
        txtGhiChuCK.setEnabled(chkCK.isSelected());

        JButton btnDatMon = new JButton("Đặt món"); btnDatMon.setFont(fontBig);
        JButton btnLuu = new JButton("Lưu"); btnLuu.setFont(fontBig);
        JButton btnHuy = new JButton("Hủy"); btnHuy.setFont(fontBig);

        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Mã phiếu:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(txtMaPhieu, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Tên KH:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(txtTen, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("SĐT:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(txtSDT, gbc);
        gbc.gridx = 2; pForm.add(btnCheckKH, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ngày đến:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(spnNgay, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Giờ đến:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(spnGio, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Số người:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(txtSoNguoi, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ghi chú:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChu), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Tiền cọc:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(txtTienCoc, gbc);
        gbc.gridx = 2; pForm.add(chkCK, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ghi chú CK:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChuCK), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Món ăn:"){{
            setFont(fontBig);
        }}, gbc);
        gbc.gridx = 1; pForm.add(btnDatMon, gbc);

        // Panel nút
        JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pSouth.add(btnLuu);
        pSouth.add(btnHuy);

        dlg.add(pForm, BorderLayout.CENTER);
        dlg.add(pSouth, BorderLayout.SOUTH);

        // Sự kiện
        btnCheckKH.addActionListener(e -> {
            JOptionPane.showMessageDialog(dlg, "Chức năng kiểm tra chưa triển khai.");
        });

        chkCK.addActionListener(e -> txtGhiChuCK.setEnabled(chkCK.isSelected()));

        btnDatMon.addActionListener(e -> {
            new FrmOrder(maBan).setVisible(true);
        });

        btnLuu.addActionListener(e -> {
            try {
                Date ngay = new Date(((java.util.Date) spnNgay.getValue()).getTime());
                Time gio = new Time(((java.util.Date) spnGio.getValue()).getTime());
                if (editD == null && phieudatBanDAO.checkTrung(maBan, ngay, gio)) {
                    JOptionPane.showMessageDialog(dlg, "Trùng ngày và giờ với lịch khác!");
                    return;
                }
                double tienCoc = txtTienCoc.getText().isEmpty() ? 0 : Double.parseDouble(txtTienCoc.getText());
                PhieuDatBan d = editD != null ? editD : new PhieuDatBan();
                d.setMaPhieu(txtMaPhieu.getText());
                d.setMaBan(maBan);
                d.setTenKhach(txtTen.getText());
                d.setSoDienThoai(txtSDT.getText());
                d.setSoNguoi(Integer.parseInt(txtSoNguoi.getText()));
                d.setNgayDen(ngay);
                d.setGioDen(gio);
                d.setGhiChu(txtGhiChu.getText());
                d.setTienCoc(tienCoc);
                d.setGhiChuCoc(chkCK.isSelected() ? txtGhiChuCK.getText() : "");
                d.setTrangThai(editD != null ? d.getTrangThai() : "Đặt");

                if (editD != null) {
                    phieudatBanDAO.update(d);
                } else {
                    phieudatBanDAO.add(d);
                }
                taiTrangThaiBanTuDB();
                JOptionPane.showMessageDialog(dlg, "Lưu thành công!");
                dlg.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dlg, "Lỗi: " + ex.getMessage());
            }
        });

        btnHuy.addActionListener(e -> dlg.dispose());

        dlg.setVisible(true);
    }

    private void moFormGopBan() {
        if (banDangChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn chính!");
            return;
        }
        String banGop = JOptionPane.showInputDialog(this, "Nhập mã bàn muốn gộp vào " + banDangChon + ":");
        if (banGop != null && mapBan.containsKey(banGop)) {
            try {
                banDAO.gopBan(banDangChon, banGop);
                taiTrangThaiBanTuDB();
                JOptionPane.showMessageDialog(this, "Đã gộp bàn!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void moFormChuyenBan() {
        if (banDangChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần chuyển!");
            return;
        }
        String banMoi = JOptionPane.showInputDialog(this, "Nhập mã bàn mới cho " + banDangChon + ":");
        if (banMoi != null && mapBan.containsKey(banMoi)) {
            try {
                banDAO.chuyenBan(banDangChon, banMoi);
                taiTrangThaiBanTuDB();
                JOptionPane.showMessageDialog(this, "Đã chuyển bàn!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void xuLyDatMon() {
        String tt = getTrangThaiHienTai(banDangChon);
        if ("Phục vu".equals(tt)) {
            new FrmOrder(banDangChon).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Bàn phải ở trạng thái phục vụ để đặt món!");
        }
    }

    private void xuLyThanhToan() {
        String tt = getTrangThaiHienTai(banDangChon);
        if ("Phục vu".equals(tt)) {
            new FrmHoaDon().setVisible(true);
            taiTrangThaiBanTuDB();
        } else {
            JOptionPane.showMessageDialog(this, "Bàn phải ở trạng thái phục vụ để thanh toán!");
        }
    }

    private void moFormQuanLyKhuVuc() {
        JDialog dlg = new JDialog(this, "Quản lý khu vực", true);
        dlg.setSize(800, 550);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);

        // ===== Tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ KHU VỰC", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(128, 0, 0)); // đỏ rượu
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBounds(0, 0, 800, 60);
        dlg.add(lblTitle);

        // ===== Panel Thông tin khu vực =====
        JPanel pForm = new JPanel(null);
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin khu vực"));
        pForm.setBounds(10, 70, 800, 210);

        Font labelFont = new Font("Arial", Font.BOLD, 16);

        JLabel lblMa = new JLabel("Mã khu vực:");
        lblMa.setFont(labelFont);
        lblMa.setBounds(20, 40, 200, 25);
        JTextField txtMa = new JTextField();
        txtMa.setBounds(180, 30, 300, 35);

        JLabel lblTen = new JLabel("Tên khu vực:");
        lblTen.setFont(labelFont);
        lblTen.setBounds(20, 85, 200, 25);
        JTextField txtTen = new JTextField();
        txtTen.setBounds(180, 75, 300, 35);

        JLabel lblSoLuong = new JLabel("Số lượng bàn:");
        lblSoLuong.setFont(labelFont);
        lblSoLuong.setBounds(20, 130, 200, 25);
        JTextField txtSoLuong = new JTextField();
        txtSoLuong.setBounds(180, 120, 300, 35);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setBounds(20, 175, 200, 25);
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng"});
        cbTrangThai.setBounds(180, 165, 300, 35);

        // ===== Nút chức năng =====
        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(550, 30, 120, 35); //ngang-dọc-rộng-cao
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBounds(550, 75, 120, 35);
        btnXoa.setBackground(new Color(230, 126, 34));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSua = new JButton("Sửa");
        btnSua.setBounds(550, 120, 120, 35);
        btnSua.setBackground(new Color(192, 57, 43));
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBounds(550, 165, 120, 35);
        btnLuu.setBackground(new Color(52, 152, 219));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 18));

        pForm.add(lblMa);
        pForm.add(txtMa);
        pForm.add(lblTen);
        pForm.add(txtTen);
        pForm.add(lblSoLuong);
        pForm.add(txtSoLuong);
        pForm.add(lblTrangThai);
        pForm.add(cbTrangThai);
        pForm.add(btnThem);
        pForm.add(btnXoa);
        pForm.add(btnSua);
        pForm.add(btnLuu);

        dlg.add(pForm);

        // ===== Bảng danh sách khu vực =====
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mã khu vực", "Tên khu vực", "Số lượng bàn", "Trạng thái"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 280, 800, 230);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khu vực"));

        dlg.add(scroll);

        // ===== Load data từ DB =====
        try {
            List<KhuVuc> list = khuDAO.getAll();
            for (KhuVuc k : list) {
                model.addRow(new Object[]{
                        k.getMaKhuVuc(),
                        k.getTenKhuVuc(),
                        k.getSoLuongBan(),
                        k.getTrangThai()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // ===== Action nút =====
        btnThem.addActionListener(e -> {
            try {
                KhuVuc k = new KhuVuc();
                k.setMaKhuVuc(txtMa.getText());
                k.setTenKhuVuc(txtTen.getText());
                k.setSoLuongBan(Integer.parseInt(txtSoLuong.getText()));
                k.setTrangThai((String) cbTrangThai.getSelectedItem());
                khuDAO.add(k);
                model.addRow(new Object[]{k.getMaKhuVuc(), k.getTenKhuVuc(), k.getSoLuongBan(), k.getTrangThai()});
                reloadMainPanel(); // Tải lại tab sau khi thêm
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String ma = (String) model.getValueAt(row, 0);
                try {
                    khuDAO.delete(ma);
                    model.removeRow(row);
                    reloadMainPanel(); // Tải lại tab sau khi xóa
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMa.setText((String) model.getValueAt(row, 0));
                txtTen.setText((String) model.getValueAt(row, 1));
                txtSoLuong.setText(model.getValueAt(row, 2).toString());
                cbTrangThai.setSelectedItem(model.getValueAt(row, 3));
            }
        });

        btnLuu.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String ma = txtMa.getText();
                try {
                    KhuVuc k = khuDAO.getByMa(ma);
                    k.setTenKhuVuc(txtTen.getText());
                    k.setSoLuongBan(Integer.parseInt(txtSoLuong.getText()));
                    k.setTrangThai((String) cbTrangThai.getSelectedItem());
                    khuDAO.update(k);
                    model.setValueAt(k.getTenKhuVuc(), row, 1);
                    model.setValueAt(k.getSoLuongBan(), row, 2);
                    model.setValueAt(k.getTrangThai(), row, 3);
                    reloadMainPanel(); // Tải lại tab sau khi lưu
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dlg.setVisible(true);
    }

    private void moFormQuanLyBan() {
        JDialog dlg = new JDialog(this, "Quản lý bàn", true);
        dlg.setSize(950, 800);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);

        // ===== Tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ BÀN", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(128, 0, 0)); // đỏ rượu
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBounds(0, 0, 950, 60);
        dlg.add(lblTitle);

        // ===== Panel Thông tin bàn =====
        JPanel pForm = new JPanel(null);
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin bàn"));
        pForm.setBounds(10, 70, 940, 300);

        Font labelFont = new Font("Arial", Font.BOLD, 16);

        // Mã bàn
        JLabel lblMa = new JLabel("Mã bàn:");
        lblMa.setFont(labelFont);
        lblMa.setBounds(20, 40, 200, 25);
        JTextField txtMa = new JTextField();
        txtMa.setBounds(180, 30, 300, 35);

        // Khu vực
        JLabel lblKhuVuc = new JLabel("Tên khu vực:");
        lblKhuVuc.setFont(labelFont);
        lblKhuVuc.setBounds(20, 85, 200, 25);
        JTextField txtKhuVuc = new JTextField();
        txtKhuVuc.setBounds(180, 75, 300, 35);

        // Trạng thái
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setBounds(20, 130, 200, 25);
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Trống", "Đặt", "Đang phục vụ"});
        cbTrangThai.setBounds(180, 120, 300, 35);

        // Ghi chú
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(labelFont);
        lblGhiChu.setBounds(20, 175, 200, 25);
        JTextField txtGhiChu = new JTextField();
        txtGhiChu.setBounds(180, 165, 300, 35);

        // Số chỗ ngồi
        JLabel lblSoCho = new JLabel("Số chỗ ngồi:");
        lblSoCho.setFont(labelFont);
        lblSoCho.setBounds(20, 220, 200, 25);
        JTextField txtSoCho = new JTextField();
        txtSoCho.setBounds(180, 210, 300, 35);

        // Loại bàn
        JLabel lblLoai = new JLabel("Loại bàn:");
        lblLoai.setFont(labelFont);
        lblLoai.setBounds(20, 265, 200, 25);
        JComboBox<String> cbLoai = new JComboBox<>(new String[]{"Thường", "VIP"});
        cbLoai.setBounds(180, 255, 300, 35);

        // ===== Nút chức năng =====
        JButton btnThem = new JButton("Thêm");
        btnThem.setBounds(550, 30, 120, 35); //ngang-dọc-rộng-cao
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBounds(550, 75, 120, 35);
        btnXoa.setBackground(new Color(230, 126, 34));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnSua = new JButton("Sửa");
        btnSua.setBounds(550, 120, 120, 35);
        btnSua.setBackground(new Color(192, 57, 43));
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBounds(550, 165, 120, 35);
        btnLuu.setBackground(new Color(52, 152, 219));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 18));

        // Add vào panel
        pForm.add(lblMa); pForm.add(txtMa);
        pForm.add(lblKhuVuc); pForm.add(txtKhuVuc);
        pForm.add(lblTrangThai); pForm.add(cbTrangThai);
        pForm.add(lblGhiChu); pForm.add(txtGhiChu);
        pForm.add(lblSoCho); pForm.add(txtSoCho);
        pForm.add(lblLoai); pForm.add(cbLoai);
        pForm.add(btnThem); pForm.add(btnXoa); pForm.add(btnSua); pForm.add(btnLuu);

        dlg.add(pForm);

        // ===== Bảng danh sách bàn =====
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mã bàn", "Khu vực", "Trạng thái", "Ghi chú", "Số chỗ ngồi", "Loại bàn"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(10, 380, 940, 350);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách bàn"));

        dlg.add(scroll);

        // ===== Load data từ DB =====
        try {
            List<Ban> list = banDAO.getAllBan(banDangChon);
            for (Ban b : list) {
                model.addRow(new Object[]{
                        b.getMaBan(),
                        b.getTenKhuVuc(),
                        b.getTrangThai(),
                        b.getGhiChu(),
                        b.getSoChoNgoi(),
                        b.getLoaiBan()
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // ===== Action nút =====
        btnThem.addActionListener(e -> {
            try {
                Ban b = new Ban();
                b.setMaBan(txtMa.getText());
                b.setTenKhuVuc(txtKhuVuc.getText()); // Giả sử txtKhuVuc là maKhuVuc hoặc ten, điều chỉnh nếu cần
                b.setTrangThai((String) cbTrangThai.getSelectedItem());
                b.setGhiChu(txtGhiChu.getText());
                b.setSoChoNgoi(Integer.parseInt(txtSoCho.getText()));
                b.setLoaiBan((String) cbLoai.getSelectedItem());
                banDAO.themBan(b);
                model.addRow(new Object[]{b.getMaBan(), b.getTenKhuVuc(), b.getTrangThai(),
                                          b.getGhiChu(), b.getSoChoNgoi(), b.getLoaiBan()});
                reloadMainPanel(); // Tải lại sau khi thêm
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String ma = (String) model.getValueAt(row, 0);
                try {
                    banDAO.xoaBan(ma);
                    model.removeRow(row);
                    reloadMainPanel(); // Tải lại sau khi xóa
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMa.setText((String) model.getValueAt(row, 0));
                txtKhuVuc.setText((String) model.getValueAt(row, 1));
                cbTrangThai.setSelectedItem(model.getValueAt(row, 2));
                txtGhiChu.setText((String) model.getValueAt(row, 3));
                txtSoCho.setText(model.getValueAt(row, 4).toString());
                cbLoai.setSelectedItem(model.getValueAt(row, 5));
            }
        });

        btnLuu.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String ma = txtMa.getText();
                try {
                    Ban b = banDAO.getBanByMa(ma);
                    b.setTenKhuVuc(txtKhuVuc.getText());
                    b.setTrangThai((String) cbTrangThai.getSelectedItem());
                    b.setGhiChu(txtGhiChu.getText());
                    b.setSoChoNgoi(Integer.parseInt(txtSoCho.getText()));
                    b.setLoaiBan((String) cbLoai.getSelectedItem());
                    banDAO.capNhatBan(b);

                    model.setValueAt(b.getTenKhuVuc(), row, 1);
                    model.setValueAt(b.getTrangThai(), row, 2);
                    model.setValueAt(b.getGhiChu(), row, 3);
                    model.setValueAt(b.getSoChoNgoi(), row, 4);
                    model.setValueAt(b.getLoaiBan(), row, 5);
                    reloadMainPanel(); // Tải lại sau khi lưu
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dlg.setVisible(true);
    }

    private ImageIcon tintIcon(ImageIcon source, Color color) {
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

        btn.setPreferredSize(new Dimension(300, 80));
        btn.setMaximumSize(new Dimension(300, 80));
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
        private JLabel iconLabel;
        private Ban ban;

        public BanPanel(Ban b) {
            this.ban = b;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(180, 250)); // Tăng kích thước bàn
            setMaximumSize(new Dimension(180, 250));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Tăng khoảng cách

            // Label số người với font lớn hơn
            JLabel lblSo = new JLabel(b.getSoChoNgoi() + " người");
            lblSo.setAlignmentX(CENTER_ALIGNMENT);
            lblSo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            add(lblSo);

            // Label icon bàn
            iconLabel = new JLabel();
            iconLabel.setAlignmentX(CENTER_ALIGNMENT);
            iconLabel.setLayout(null);
            updateIcon();
            add(iconLabel);

            // Label mã bàn với font lớn hơn
            JLabel lblMa = new JLabel(b.getMaBan());
            lblMa.setAlignmentX(CENTER_ALIGNMENT);
            lblMa.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            add(lblMa);

            // Thêm icon VIP nếu là bàn VIP, căn giữa phía trên
            if ("VIP".equals(b.getLoaiBan())) {
                ImageIcon vipIcon = new ImageIcon("img/vip.png");
                JLabel vipL = new JLabel(vipIcon);
                int iconWidth = iconLabel.getIcon().getIconWidth();
                int vipWidth = vipIcon.getIconWidth();
                int vipHeight = vipIcon.getIconHeight();
                vipL.setBounds((iconWidth - vipWidth) / 2, -10, vipWidth, vipHeight); // Căn giữa và điều chỉnh lên trên
                iconLabel.add(vipL);
            }
        }

        public Ban getBan() {
            return ban;
        }

        public void updateIcon() {
            // Load ảnh gốc
            ImageIcon base = new ImageIcon("img/bantron.png");
            int newWidth = 150; // Tăng kích thước ảnh để bàn lớn hơn
            int newHeight = 150;
            Image scaled = base.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);

            // Set icon cho label
            iconLabel.setIcon(scaledIcon);
            iconLabel.setPreferredSize(new Dimension(newWidth, newHeight));

            // Lấy trạng thái
            String tt = getTrangThaiHienTai(ban.getMaBan());
            Color color = null;
            if ("Đặt".equals(tt)) color = COLOR_DAT;
            else if ("Phục vu".equals(tt)) color = COLOR_PHUCVU;

            // Nếu có màu -> tô màu lại cho icon đã scale
            if (color != null) {
                iconLabel.setIcon(tintIcon(scaledIcon, color));
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