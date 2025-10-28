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
import dao.Ban_DAO;
import dao.KhachHang_DAO;
import dao.KhuVuc_DAO;
import dao.LoaiBan_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.KhuVuc;
import entity.PhieuDatBan;

public class FrmBan extends JFrame {
    private JLabel lblTieuDe;
    private Container sidebar;
    private JTabbedPane tabbedPane;
    private String banDangChon = null;
    private final Color COLOR_TRONG = Color.WHITE;
    private final Color COLOR_DAT = new Color(236, 66, 48);
    private final Color COLOR_PHUCVU = new Color(55, 212, 23);
    private final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private Map<String, BanPanel> mapBan = new HashMap<>();
    private Map<String, JPanel> mapKhu = new HashMap<>();
    private Ban_DAO banDAO;
    private KhuVuc_DAO khuDAO;
    private PhieuDatBan phieuDatBan;
    private JPanel pnlChinh;
    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbLoaiBan;
    private JTextField txtTimMaBan;
    private JComboBox<Integer> cbSoNguoi;
    private PhieuDatBan_DAO phieuDatBanDAO;
    private KhachHang_DAO khachHangDAO;
    private JDateChooser dateChooser;
    private JButton btnLocBanTrong;
    private JButton btnLocBanPhucVu;
    private JButton btnLocBanDat;
    private JRadioButton tatCa;
    private JRadioButton banThuong;
    private JRadioButton banVIP;
    private JButton btnTim;
    private JButton btnDatLai;
    private ButtonGroup groupLoai;
    private Connection conn;

    public FrmBan() throws SQLException {
        conn = ConnectSQL.getConnection();
        banDAO = new Ban_DAO(conn);
        phieuDatBanDAO = new PhieuDatBan_DAO(conn);
        khachHangDAO = new KhachHang_DAO(conn);
        khuDAO = new KhuVuc_DAO(conn);
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());

        setTitle("Phần mềm quản lý nhà hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Khởi tạo cbTrangThai và cbLoaiBan
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Bàn trống", "Bàn đã đặt", "Bàn đang phục vụ"});
        cbTrangThai.setBackground(Color.WHITE);
        cbLoaiBan = new JComboBox<>(new String[]{"Tất cả", "Bàn thường", "Bàn VIP"});
        cbLoaiBan.setBackground(Color.WHITE);

        // Sidebar Menu
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(238, 238, 238));

        JButton btnThoiGian = taoNutMenu("Thời gian", "img/dongho.png");
        btnThoiGian.setPreferredSize(new Dimension(300, 120));
        btnThoiGian.setMaximumSize(new Dimension(300, 120));
        JButton btnDatBan = taoNutMenu("Đặt bàn", "img/datban.png");
        JButton btnHuyBan = taoNutMenu("Hủy bàn", "img/huyban.png");
        JButton btnChuyenBan = taoNutMenu("Chuyển bàn", "img/chuyenban.png");
        JButton btnDatMon = taoNutMenu("Đặt món", "img/thucdon.png");
        JButton btnThanhToan = taoNutMenu("Thanh toán", "img/hoadon.png");

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
        sidebar.add(btnChuyenBan);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnDatMon);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnThanhToan);

        btnDatBan.addActionListener(e -> {
            if (banDangChon != null) {
                try {
                    new FrmDatBan(this, banDangChon, null, phieuDatBanDAO, banDAO, khachHangDAO, conn).setVisible(true);
                    taiLaiBangChinh();
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
        btnChuyenBan.addActionListener(e -> moFormChuyenBan());
        btnDatMon.addActionListener(e -> {
            if (banDangChon != null) {
                try {
					xuLyDatMon();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

        JPanel pnlLoc = taoPanelBoLoc();
        pnlChinh.add(pnlLoc, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 20));
        tabbedPane.setBackground(Color.WHITE);
        pnlChinh.add(tabbedPane, BorderLayout.CENTER);

        JPanel pnlFooter = taoPanelChanTrang();
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlChinh, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);

        taiLaiBangChinh();
        xuLySuKienBoLoc();
    }

    private JButton taoNutMenu(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btn.setBackground(Color.white);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(300, 100));
        btn.setMaximumSize(new Dimension(300, 100));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

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

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (btn.getBackground().equals(defaultColor)) {
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
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

    private void kieuNut(JButton button, Color baseColor) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setBackground(baseColor);

        Color hoverColor = baseColor.darker();
        Color clickColor = baseColor.darker();

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

    private JPanel taoPanelBoLoc() {
        JPanel pnlLoc = new JPanel(new GridBagLayout());
        pnlLoc.setBackground(Color.WHITE);
        pnlLoc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
        Font fontInput = new Font("Times New Roman", Font.PLAIN, 20);

        JLabel lblTim = new JLabel("Mã bàn");
        lblTim.setFont(fontLabel);
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlLoc.add(lblTim, gbc);

        txtTimMaBan = new JTextField(8);
        txtTimMaBan.setFont(fontInput);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        pnlLoc.add(txtTimMaBan, gbc);
        gbc.gridwidth = 1;

        btnTim = new JButton("Tìm");
        kieuNut(btnTim, Color.white);
        btnTim.setIcon(new ImageIcon(new ImageIcon("img/timkiem.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        gbc.gridx = 4;
        gbc.gridy = 0;
        pnlLoc.add(btnTim, gbc);

        JLabel lblSoNguoi = new JLabel("Số người");
        lblSoNguoi.setFont(fontLabel);
        gbc.gridx = 5;
        gbc.gridy = 0;
        pnlLoc.add(lblSoNguoi, gbc);

        cbSoNguoi = new JComboBox<>(new Integer[]{0, 2, 4, 6, 8, 10});
        cbSoNguoi.setBackground(Color.WHITE);
        cbSoNguoi.setFont(fontInput);
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        pnlLoc.add(cbSoNguoi, gbc);
        gbc.gridwidth = 1;

        btnLocBanTrong = new JButton("Bàn trống");
        kieuNut(btnLocBanTrong, COLOR_TRONG);
        btnLocBanTrong.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 8;
        gbc.gridy = 0;
        pnlLoc.add(btnLocBanTrong, gbc);

        btnLocBanPhucVu = new JButton("Bàn đang phục vụ");
        kieuNut(btnLocBanPhucVu, COLOR_PHUCVU);
        btnLocBanPhucVu.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 8;
        gbc.gridy = 1;
        pnlLoc.add(btnLocBanPhucVu, gbc);

        btnLocBanDat = new JButton("Bàn đã đặt");
        kieuNut(btnLocBanDat, COLOR_DAT);
        btnLocBanDat.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 9;
        gbc.gridy = 0;
        pnlLoc.add(btnLocBanDat, gbc);

        btnDatLai = new JButton("Đặt lại");
        kieuNut(btnDatLai, Color.LIGHT_GRAY);
        btnDatLai.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 9;
        gbc.gridy = 1;
        pnlLoc.add(btnDatLai, gbc);

        JLabel lblLoai = new JLabel("Loại bàn");
        lblLoai.setFont(fontLabel);
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlLoc.add(lblLoai, gbc);

        tatCa = new JRadioButton("Tất cả", true);
        banThuong = new JRadioButton("Thường");
        banVIP = new JRadioButton("VIP");

        tatCa.setFont(fontLabel);
        tatCa.setBackground(Color.WHITE);
        banThuong.setFont(fontLabel);
        banThuong.setBackground(Color.WHITE);
        banVIP.setFont(fontLabel);
        banVIP.setBackground(Color.WHITE);

        groupLoai = new ButtonGroup();
        groupLoai.add(tatCa);
        groupLoai.add(banThuong);
        groupLoai.add(banVIP);

        gbc.gridx = 2;
        gbc.gridy = 1;
        pnlLoc.add(tatCa, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        pnlLoc.add(banThuong, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        pnlLoc.add(banVIP, gbc);

        JLabel lblNgay = new JLabel("Ngày đặt");
        lblNgay.setFont(fontLabel);
        gbc.gridx = 5;
        gbc.gridy = 1;
        pnlLoc.add(lblNgay, gbc);

        dateChooser = new JDateChooser();
        dateChooser.setFont(fontInput);
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setBackground(Color.WHITE);
        dateChooser.setPreferredSize(new Dimension(150, 35));
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        pnlLoc.add(dateChooser, gbc);
        gbc.gridwidth = 1;

        return pnlLoc;
    }

    private JPanel taoPanelChanTrang() {
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
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

        return pnlFooter;
    }

    private void xuLySuKienBoLoc() {
        btnLocBanTrong.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn trống");
            apDungBoLoc();
        });

        btnLocBanPhucVu.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đang phục vụ");
            apDungBoLoc();
        });

        btnLocBanDat.addActionListener(e -> {
            cbTrangThai.setSelectedItem("Bàn đã đặt");
            apDungBoLoc();
        });

        tatCa.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Tất cả");
            apDungBoLoc();
        });

        banThuong.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn thường");
            apDungBoLoc();
        });

        banVIP.addActionListener(e -> {
            cbLoaiBan.setSelectedItem("Bàn VIP");
            apDungBoLoc();
        });

        cbSoNguoi.addActionListener(e -> apDungBoLoc());

        if (dateChooser != null && dateChooser.getDateEditor() != null) {
            dateChooser.getDateEditor().addPropertyChangeListener("date", e -> apDungBoLoc());
        }

        btnTim.addActionListener(e -> apDungBoLoc());

        btnDatLai.addActionListener(e -> {
            cbSoNguoi.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            cbLoaiBan.setSelectedIndex(0);
            txtTimMaBan.setText("");
            dateChooser.setDate(null);
            groupLoai.clearSelection();
            tatCa.setSelected(true);
            apDungBoLoc();
        });
    }

    private void apDungBoLoc() {
        String trangThaiLoc = (String) cbTrangThai.getSelectedItem();
        String loaiBanLoc = (String) cbLoaiBan.getSelectedItem();
        Integer soNguoiLoc = (Integer) cbSoNguoi.getSelectedItem();
        String maTimKiem = txtTimMaBan.getText().trim().toLowerCase();
        java.util.Date ngayLoc = dateChooser.getDate();

        for (BanPanel bp : mapBan.values()) {
            Ban b = bp.getBan();
            boolean hienThi = true;

            if (!maTimKiem.isEmpty() && !b.getMaBan().toLowerCase().contains(maTimKiem)) {
                hienThi = false;
            }

            if (soNguoiLoc != null && soNguoiLoc != 0 && b.getSoChoNgoi() != soNguoiLoc) {
                hienThi = false;
            }

            if (!"Tất cả".equals(loaiBanLoc)) {
                String loaiMongMuon = loaiBanLoc.equals("Bàn thường") ? "Thường" : "VIP";
                if (!b.getTenLoai().equals(loaiMongMuon)) {
                    hienThi = false;
                }
            }

            if (!"Tất cả".equals(trangThaiLoc) || ngayLoc != null) {
                String trangThaiThucTe;
                if (ngayLoc != null) {
                    try {
                        Date sqlNgay = new Date(ngayLoc.getTime());
                        List<PhieuDatBan> phieuList = phieuDatBanDAO.getDatBanByBanAndNgay(
                            b.getMaBan(), sqlNgay);

                        boolean coPhucVu = phieuList.stream().anyMatch(p -> "Phục vụ".equals(p.getTrangThai()));
                        boolean coDat = phieuList.stream().anyMatch(p -> "Đặt".equals(p.getTrangThai()));

                        if (coPhucVu) trangThaiThucTe = "Phục vụ";
                        else if (coDat) trangThaiThucTe = "Đặt";
                        else trangThaiThucTe = "Trống";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        trangThaiThucTe = "Trống";
                    }
                } else {
                    trangThaiThucTe = layTrangThaiHienTai(b.getMaBan());
                }

                if ("Bàn trống".equals(trangThaiLoc) && !"Trống".equals(trangThaiThucTe)) {
                    hienThi = false;
                }
                if ("Bàn đã đặt".equals(trangThaiLoc) && !"Đặt".equals(trangThaiThucTe)) {
                    hienThi = false;
                }
                if ("Bàn đang phục vụ".equals(trangThaiLoc) && !"Phục vụ".equals(trangThaiThucTe)) {
                    hienThi = false;
                }
            }

            bp.setVisible(hienThi);
            bp.capNhatBieuTuong();
        }

        for (JPanel pnlKhu : mapKhu.values()) {
            pnlKhu.revalidate();
            pnlKhu.repaint();
        }
    }

    void taiLaiBangChinh() {
        SwingUtilities.invokeLater(() -> {
            tabbedPane.removeAll();
            mapBan.clear();
            mapKhu.clear();

            try {
                List<KhuVuc> danhSachKhu = khuDAO.getAll();
                if (danhSachKhu == null || danhSachKhu.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có khu vực nào!");
                    return;
                }

                for (KhuVuc k : danhSachKhu) {
                    JPanel pnlKhu = taoPanelKhuVuc(k);
                    if (pnlKhu != null) {
                        mapKhu.put(k.getMaKhuVuc(), pnlKhu);
                        tabbedPane.addTab(k.getTenKhuVuc(), new JScrollPane(pnlKhu));
                    }
                }

                tabbedPane.revalidate();
                tabbedPane.repaint();
                taiTrangThaiBanTuCSDL();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
            }
        });
    }

	private JPanel taoPanelKhuVuc(KhuVuc k) throws SQLException {
	    JPanel pnlKhu = new JPanel();
	    pnlKhu.setLayout(new BoxLayout(pnlKhu, BoxLayout.Y_AXIS));
	    pnlKhu.setBackground(Color.WHITE);
	
	    List<Ban> danhSachBan = banDAO.getAll(k.getMaKhuVuc());
	    if (danhSachBan == null || danhSachBan.isEmpty()) {
	        return null;
	    }
	
	    JPanel rowPanel = null;
	    int colCount = 0;
	    for (Ban b : danhSachBan) {
	        if (!b.getTrangThai().equals("Ẩn")) {
	            if (colCount % 5 == 0) {
	                rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
	                rowPanel.setBackground(Color.WHITE);
	                pnlKhu.add(rowPanel);
	            }
	
	            BanPanel bp = new BanPanel(b);
	            bp.addMouseListener(taoSuKienNhapChuot(b));
	            rowPanel.add(bp);
	            mapBan.put(b.getMaBan(), bp);
	            colCount++;
	        }
	    }
	
	    return colCount > 0 ? pnlKhu : null;
	}

    private MouseAdapter taoSuKienNhapChuot(Ban b) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                banDangChon = b.getMaBan();
                try {
                    hienThiThongTinBan(b.getMaBan());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FrmBan.this, "Lỗi: " + ex.getMessage());
                }
            }
        };
    }

    private void taiTrangThaiBanTuCSDL() {
        for (BanPanel bp : mapBan.values()) {
            try {
                bp.capNhatBieuTuong();
                bp.revalidate();
                bp.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn " + bp.getBan().getMaBan());
            }
        }
    }

    public String layTrangThaiHienTai(String maBan) {
	    try {
	        Date today = new Date(System.currentTimeMillis());
	        List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(maBan, today);
	
	        if (list != null && !list.isEmpty()) {
	            for (PhieuDatBan phieu : list) {
	                String trangThai = phieu.getTrangThai();
	                if (trangThai != null && !trangThai.trim().isEmpty()) {
	                    String cleanStatus = trangThai.trim();
	                    if ("Phục vụ".equals(cleanStatus)) {
	                        return "Phục vụ";
	                    } else if ("Đặt".equals(cleanStatus)) {
	                        return "Đặt";
	                    }
	                }
	            }
	        }
	        return "Trống"; 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.err.println("Lỗi khi lấy trạng thái bàn " + maBan + ": " + e.getMessage());
	        return "Trống"; 
	    }
	}

    private void hienThiThongTinBan(String maBan) throws SQLException {
        Ban b = banDAO.getBanByMa(maBan);
        if (b == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bàn với mã " + maBan);
            return;
        }

        String tt = layTrangThaiHienTai(maBan);
        if ("Phục vụ".equals(tt)) {
            try {
            	PhieuDatBan phieu = phieuDatBanDAO.getByMaBanAndTrangThai(maBan, "Đang phục vụ");
            	if (phieu == null) {
            	    phieu = phieuDatBanDAO.getByMaBanAndTrangThai(maBan, "Đặt");
            	}
            	LoaiBan_DAO loaiBanDAO = new LoaiBan_DAO(conn);
            	FrmPhucVu frm = new FrmPhucVu(this, maBan, phieuDatBan, phieuDatBanDAO, banDAO, loaiBanDAO);
            	taiTrangThaiBanTuCSDL();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin phục vụ: " + ex.getMessage());
            }
            return;
        }

        JDialog dialog = new JDialog(this, "Thông tin bàn", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(1100, 500);

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 18);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 18);

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
        JLabel valLoaiBan = new JLabel(b.getTenLoai());
        valLoaiBan.setFont(fontValue);
        pnlThongTin.add(valLoaiBan, gbcInfo);
        row++;

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

        List<PhieuDatBan> listDatBan = phieuDatBanDAO.getDatBanByBan(maBan);
        for (PhieuDatBan d : listDatBan) {
            model.addRow(new Object[]{
                d.getMaPhieu(), d.getTenKhach(), d.getSoDienThoai(), d.getSoNguoi(), d.getNgayDen(), d.getGioDen(), d.getGhiChu(), d.getTienCoc(), d.getGhiChuCoc(), d.getTrangThai()
            });
        }

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlNut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnDatMoi = new JButton("Đặt bàn");
        kieuNut(btnDatMoi, new Color(52, 152, 219));
        btnDatMoi.setPreferredSize(new Dimension(120, 35));
        btnDatMoi.setForeground(Color.white);
        pnlNut.add(btnDatMoi);

        JButton btnSua = new JButton("Sửa");
        kieuNut(btnSua, new Color(255, 193, 7));
        btnSua.setForeground(Color.white);
        btnSua.setPreferredSize(new Dimension(100, 35));
        pnlNut.add(btnSua);

        JButton btnDangPhucVu = new JButton("Đang phục vụ");
        btnDangPhucVu.setPreferredSize(new Dimension(180, 35));
        btnDangPhucVu.setForeground(Color.white);
        kieuNut(btnDangPhucVu, new Color(50, 205, 50));
        pnlNut.add(btnDangPhucVu);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setForeground(Color.white);
        kieuNut(btnHuy, new Color(231, 76, 60));
        pnlNut.add(btnHuy);

        JButton btnXoaLich = new JButton("Xóa lịch chọn");
        btnXoaLich.setPreferredSize(new Dimension(150, 35));
        btnXoaLich.setForeground(Color.white);
        kieuNut(btnXoaLich, new Color(149, 165, 166));
        pnlNut.add(btnXoaLich);

        btnDatMoi.addActionListener(e -> {
            try {
                new FrmDatBan(this, maBan, null, phieuDatBanDAO, banDAO, khachHangDAO, conn).setVisible(true);
                dialog.dispose();
                taiLaiBangChinh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form đặt bàn!");
            }
        });

        btnSua.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String maPhieu = (String) model.getValueAt(selected, 0);
                try {
                    PhieuDatBan d = phieuDatBanDAO.getByMa(maPhieu);
                    new FrmDatBan(this, maBan, d, phieuDatBanDAO, banDAO, khachHangDAO, conn).setVisible(true);
                    dialog.dispose();
                    hienThiThongTinBan(maBan);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi sửa lịch đặt bàn!");
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
                        taiLaiBangChinh();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái phục vụ!");
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
                    taiLaiBangChinh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm lịch phục vụ!");
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
                    JOptionPane.showMessageDialog(this, "Lỗi khi mở form hủy bàn!");
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
                    taiLaiBangChinh();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa lịch đặt bàn!");
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

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Hủy Đặt Bàn", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(fontLabel);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

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

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlNut.setBackground(new Color(248, 249, 250));

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(fontButton);
        btnXacNhan.setPreferredSize(new Dimension(120, 35));
        kieuNut(btnXacNhan, new Color(46, 204, 113));
        btnXacNhan.setForeground(Color.WHITE);
        pnlNut.add(btnXacNhan);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(fontButton);
        btnHuy.setPreferredSize(new Dimension(100, 35));
        kieuNut(btnHuy, new Color(231, 76, 60));
        btnHuy.setForeground(Color.white);
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
            try {
                double tienTra = Double.parseDouble(txtTienTra.getText());
                d.setGhiChu(d.getGhiChu() + "\nHủy: " + lyDo + " - Trả cọc: " + tienTra);
                d.setTrangThai("Hủy");
                phieuDatBanDAO.update(d);
                model.setValueAt("Hủy", selected, 9);
                model.setValueAt(d.getGhiChu(), selected, 6);
                taiLaiBangChinh();
                dlg.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Tiền trả cọc phải là số!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dlg, "Lỗi khi hủy đặt bàn!");
            }
        });

        btnHuy.addActionListener(e -> dlg.dispose());

        dlg.add(pnlHeader, BorderLayout.NORTH);
        dlg.add(pnlContent, BorderLayout.CENTER);
        dlg.add(pnlNut, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }
    private void moFormChuyenBan() {
        if (banDangChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần chuyển!");
            return;
        }
        new BanDialog(this, banDangChon, banDAO, phieuDatBanDAO, conn).setVisible(true);
        taiLaiBangChinh();
    }

    private void xuLyDatMon() throws SQLException {
        String tt = layTrangThaiHienTai(banDangChon);
        String maPhieuHienTai = null;

        if (!"Phục vụ".equals(tt)) {
            try {
                Date today = new Date(System.currentTimeMillis());
                List<PhieuDatBan> list = phieuDatBanDAO.getDatBanByBanAndNgay(banDangChon, today);

                // Tìm phiếu đã đặt
                PhieuDatBan existingDat = list.stream()
                        .filter(p -> "Đặt".equals(p.getTrangThai()))
                        .findFirst()
                        .orElse(null);

                if (existingDat != null) {
                    existingDat.setTrangThai("Phục vụ");
                    phieuDatBanDAO.update(existingDat);
                    maPhieuHienTai = existingDat.getMaPhieu(); // dùng phiếu đã tồn tại
                } else {
                    PhieuDatBan d = new PhieuDatBan();
                    d.setMaPhieu(phieuDatBanDAO.generateMaPhieu());
                    d.setMaBan(banDangChon);
                    d.setNgayDen(today);
                    d.setGioDen(new Time(System.currentTimeMillis()));
                    d.setTrangThai("Phục vụ");
                    phieuDatBanDAO.add(d);
                    maPhieuHienTai = d.getMaPhieu(); // phiếu mới tạo
                }
                taiLaiBangChinh();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái bàn!");
                return;
            }
        }

        // Mở FrmDatMon với maPhieu đúng
        new FrmDatMon(banDangChon, maPhieuHienTai).setVisible(true);
    }


    private void xuLyThanhToan() {
        String tt = layTrangThaiHienTai(banDangChon);
        if ("Phục vụ".equals(tt)) {
            new FrmThanhToan().setVisible(true);
            taiLaiBangChinh();
        } else {
            JOptionPane.showMessageDialog(this, "Bàn phải ở trạng thái phục vụ để thanh toán!");
        }
    }

    private ImageIcon toMauBieuTuong(ImageIcon nguon, Color mau) {
        Image imgNguon = nguon.getImage();
        BufferedImage imgTinted = new BufferedImage(imgNguon.getWidth(null),
            imgNguon.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = imgTinted.createGraphics();
        g2d.drawImage(imgNguon, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop.derive(1.0f));
        g2d.setColor(mau);
        g2d.fillRect(0, 0, imgNguon.getWidth(null), imgNguon.getHeight(null));
        g2d.dispose();

        return new ImageIcon(imgTinted);
    }

    class BanPanel extends JPanel {
        private JLabel lblIcon;
        private Ban ban;

        public BanPanel(Ban b) {
            this.ban = b;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(180, 200));
            setMaximumSize(new Dimension(180, 200));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setBackground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            lblIcon = new JLabel();
            lblIcon.setAlignmentX(CENTER_ALIGNMENT);
            lblIcon.setPreferredSize(new Dimension(150, 150));
            capNhatBieuTuong();
            add(lblIcon);

            JLabel lblSoNguoi = new JLabel(b.getSoChoNgoi() + " người");
            lblSoNguoi.setAlignmentX(CENTER_ALIGNMENT);
            lblSoNguoi.setFont(new Font("Times New Roman", Font.BOLD, 20));
            add(lblSoNguoi);

            JLabel lblMaBan = new JLabel(b.getMaBan());
            lblMaBan.setAlignmentX(CENTER_ALIGNMENT);
            lblMaBan.setFont(new Font("Times New Roman", Font.BOLD, 20));
            add(lblMaBan);

            if ("VIP".equals(b.getTenLoai())) {
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

        public void capNhatBieuTuong() {
            try {
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
                    lblIcon.setIcon(toMauBieuTuong(scaledIcon, color));
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(FrmBan.this, "Lỗi khi cập nhật icon bàn " + ban.getMaBan());
            }
        }
    }
    
    public void capNhatTrangThai(String maBan, String trangThai) throws SQLException {
        String sql = "UPDATE Ban SET trangThai = ? WHERE maBan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trangThai);
            stmt.setString(2, maBan);
            stmt.executeUpdate();
        }
    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new FrmBan().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi khởi động ứng dụng!");
            }
        });
    }
}