package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.BoxLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import entity.Ban;

public class FrmBan extends JFrame {

    private Container pNor;
    private JLabel lbltieude;
    private Container sidebar;
    private JPanel contentPanel;
    private JTabbedPane tabbedPane;
    private String banDangChon = null;
    private final Color COLOR_TRONG = Color.white;   // trống
    private final Color COLOR_DAT = new Color(255, 102, 102);     // đã đặt
    private final Color COLOR_PHUCVU = Color.green;  // đang phục vụ
    
    private Map<String, JButton> mapBan = new HashMap<>();
    private Ban_DAO banDAO;
	private Font fontBig;
    public FrmBan() throws SQLException {
        Connection conn = ConnectSQL.getConnection();
        banDAO = new Ban_DAO(conn);

        setTitle("Phần mềm quản lý nhà hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pNor = new JPanel(new BorderLayout());
        pNor.add(lbltieude = new JLabel("QUẢN LÝ NHÀ HÀNG VANG"));
        lbltieude.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lbltieude.setHorizontalAlignment(SwingConstants.LEFT);
        lbltieude.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pNor.setBackground(new Color(245, 245, 245));
        add(pNor, BorderLayout.NORTH);

        // ===== Sidebar Menu =====
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245,245,245));

        // Các nút menu
        JButton btnTim = createMenuButton("Tìm kiếm bàn", "img/timkiem.png");
        JButton btnAll = createMenuButton("Tất cả các lịch", "img/All.png");
        JButton btnDatBan = createMenuButton("Đặt bàn", "img/datban.png");
        JButton btnHuyBan = createMenuButton("Hủy bàn", "img/huyban.png");
        JButton btnGopBan = createMenuButton("Gộp bàn", "img/gopban.png");
        JButton btnChuyenBan = createMenuButton("Chuyển bàn", "img/chuyenban.png");
        JButton btnDatMon = createMenuButton("Đặt món", "img/thucdon.png");
        JButton btnChinhSua = createMenuButton("Chỉnh sửa", "img/chinhsua.png");

        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(btnTim);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnAll);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnDatBan);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnHuyBan);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnGopBan);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnChuyenBan);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnDatMon);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnChinhSua);
        
        btnTim.addActionListener(e -> {
            String maBan = JOptionPane.showInputDialog(this, "Nhập mã bàn để tìm kiếm:");
            if (maBan != null && !maBan.trim().isEmpty()) {
                try {
                    Ban b = banDAO.getBanByMa(maBan.trim());
                    if (b != null) {
                        hienThiThongTinBan(b);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy bàn!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm bàn!");
                }
            }
        });
        btnAll.addActionListener(e -> {
			try {
				hienThiTatCaLich();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
        btnDatBan.addActionListener(e -> {
            if (banDangChon != null) {
                try {
                    moFormDatBan();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi mở form đặt bàn!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });	
        btnHuyBan.addActionListener(e -> {
			try {
				moFormHuyBan();
			} catch (SQLException e1) {
				e1.printStackTrace();
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
        btnChinhSua.addActionListener(e -> {
			try {
				moFormChinhSua();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

        add(sidebar, BorderLayout.WEST);

        //vùng Center
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 20));
        tabbedPane.setBackground(new Color(255, 255, 255));

        // Thêm các khu vực
        tabbedPane.addTab("Khu A", createKhuPanel("A"));
        tabbedPane.addTab("Khu B", createKhuPanel("B"));
        tabbedPane.addTab("Khu C", createKhuPanel("C"));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);

        taiTrangThaiBanTuDB();

        // ===== Bottom Bar =====
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(245, 245, 245));
        bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 180, 0, 0));

        // Icon địa chỉ
        ImageIcon iconAddress = new ImageIcon("img/diachi.png");
        Image imgAddr = iconAddress.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblAddress = new JLabel("Đại học Công Nghiệp Tp HCM", new ImageIcon(imgAddr), JLabel.LEFT);
        lblAddress.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblAddress.setForeground(Color.DARK_GRAY);

        // Icon liên hệ
        ImageIcon iconPhone = new ImageIcon("img/lienhe.png");
        Image imgPhone = iconPhone.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel lblPhone = new JLabel("Nhóm 9_PTUD", new ImageIcon(imgPhone), JLabel.LEFT);
        lblPhone.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblPhone.setForeground(Color.DARK_GRAY);

        infoPanel.add(lblAddress);
        infoPanel.add(lblPhone);
        bottomBar.add(infoPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 190));

        BottomButton btnHome = new BottomButton("Trang chủ", "img/home.png");
        BottomButton btnQuanLy = new BottomButton("Thoát", "img/quanly.png");

        btnHome.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnQuanLy.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttonPanel.add(btnHome);
        buttonPanel.add(btnQuanLy);

        bottomBar.add(buttonPanel, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);
        
        btnHome.addActionListener(e -> {
            new FrmTrangChu().setVisible(true);
            dispose();
        });
        btnQuanLy.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

    }

    private void taiTrangThaiBanTuDB() {
        for (String tenBan : mapBan.keySet()) {
            try {
                Ban b = banDAO.getBanByMa(tenBan);
                if (b != null && b.getTrangThai() != null) {
                    String trangThai = b.getTrangThai();
                    JButton btn = mapBan.get(tenBan);
                    if ("Đặt".equals(trangThai)) {
                        btn.setBackground(COLOR_DAT);
                    } else if ("Phục vụ".equals(trangThai)) {
                        btn.setBackground(COLOR_PHUCVU);
                    } else {
                        btn.setBackground(COLOR_TRONG);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tải trạng thái bàn từ cơ sở dữ liệu!");
            }
        }
    }

    private JPanel createKhuPanel(String khuVuc) {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(1200, 800));

        // Background (add trước để nằm dưới cùng)
        JLabel background = new JLabel();
        ImageIcon originalIcon = new ImageIcon("img/background_" + khuVuc + ".png");
        background.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(1200, 800, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 1200, 800);
        panel.add(background);

        // Thêm listener để resize background full panel
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = panel.getWidth();
                int h = panel.getHeight();
                background.setBounds(0, 0, w, h);
                background.setIcon(new ImageIcon(originalIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
            }
        });

        // Lấy danh sách bàn trong khu vực
        List<Ban> listBan;
        try {
            listBan = banDAO.getAllBan(khuVuc);
        } catch (SQLException e) {
            e.printStackTrace();
            listBan = new ArrayList<>();
        }

        for (Ban b : listBan) {
            JButton btn = new JButton(b.getMaBan());
            btn.setBounds(b.getX(), b.getY(), 70, 70);

            // Màu trạng thái
            if ("Trống".equalsIgnoreCase(b.getTrangThai())) {
                btn.setBackground(COLOR_TRONG);
            } else if ("Đặt".equalsIgnoreCase(b.getTrangThai())) {
                btn.setBackground(COLOR_DAT);
            } else if ("Phục vụ".equalsIgnoreCase(b.getTrangThai())) {
                btn.setBackground(COLOR_PHUCVU);
            }

            // Sự kiện click
            btn.addActionListener(e -> {
                xuLyBanClick(b.getMaBan(), btn);
                hienThiThongTinBan(b);
            });

            panel.add(btn);
            mapBan.put(b.getMaBan(), btn);
        }

        // Đưa background xuống dưới cùng (để không che nút)
        panel.setComponentZOrder(background, panel.getComponentCount() - 1);

        return panel;
    }

    private void hienThiThongTinBan(Ban b) {
    // Tạo JDialog để hiển thị thông tin
    JDialog dialog = new JDialog(this, "Thông tin bàn", true);
    dialog.setLayout(new BorderLayout(10, 10));
    dialog.setLocationRelativeTo(this);

    // Font chữ
    Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
    Font fontValue = new Font("Times New Roman", Font.PLAIN, 20);
    Font fontButton = new Font("Times New Roman", Font.BOLD, 20);

    // Panel chứa thông tin
    JPanel infoPanel = new JPanel(new GridBagLayout());
    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Thêm padding
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(3, 5, 3, 5);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Thêm thông tin bàn
    int row = 0;

    // Mã bàn
    gbc.gridx = 0; gbc.gridy = row;
    JLabel lblMaBan = new JLabel("Mã bàn:");
    lblMaBan.setFont(fontLabel);
    infoPanel.add(lblMaBan, gbc);
    gbc.gridx = 1;
    JLabel valMaBan = new JLabel(b.getMaBan());
    valMaBan.setFont(fontValue);
    infoPanel.add(valMaBan, gbc);
    row++;

    // Khu vực
    gbc.gridx = 0; gbc.gridy = row;
    JLabel lblKhuVuc = new JLabel("Khu vực:");
    lblKhuVuc.setFont(fontLabel);
    infoPanel.add(lblKhuVuc, gbc);
    gbc.gridx = 1;
    JLabel valKhuVuc = new JLabel(b.getKhuVuc());
    valKhuVuc.setFont(fontValue);
    infoPanel.add(valKhuVuc, gbc);
    row++;

    // Số ghế
    gbc.gridx = 0; gbc.gridy = row;
    JLabel lblSoGhe = new JLabel("Số ghế:");
    lblSoGhe.setFont(fontLabel);
    infoPanel.add(lblSoGhe, gbc);
    gbc.gridx = 1;
    JLabel valSoGhe = new JLabel(String.valueOf(b.getSoGhe()));
    valSoGhe.setFont(fontValue);
    infoPanel.add(valSoGhe, gbc);
    row++;

    // Trạng thái
    gbc.gridx = 0; gbc.gridy = row;
    JLabel lblTrangThai = new JLabel("Trạng thái:");
    lblTrangThai.setFont(fontLabel);
    infoPanel.add(lblTrangThai, gbc);
    gbc.gridx = 1;
    JLabel valTrangThai = new JLabel(b.getTrangThai());
    valTrangThai.setFont(fontValue);
    infoPanel.add(valTrangThai, gbc);
    row++;

    // Nếu bàn không trống, hiển thị thêm thông tin đặt bàn
    boolean isNotTrong = !"Trống".equalsIgnoreCase(b.getTrangThai());
    if (isNotTrong) {
        // Tên khách
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTenKhach = new JLabel("Tên khách:");
        lblTenKhach.setFont(fontLabel);
        infoPanel.add(lblTenKhach, gbc);
        gbc.gridx = 1;
        JLabel valTenKhach = new JLabel(b.getTenKhach() != null ? b.getTenKhach() : "");
        valTenKhach.setFont(fontValue);
        infoPanel.add(valTenKhach, gbc);
        row++;

        // SĐT
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(fontLabel);
        infoPanel.add(lblSDT, gbc);
        gbc.gridx = 1;
        JLabel valSDT = new JLabel(b.getSoDienThoai() != null ? b.getSoDienThoai() : "");
        valSDT.setFont(fontValue);
        infoPanel.add(valSDT, gbc);
        row++;

        // Số người
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSoNguoi = new JLabel("Số người:");
        lblSoNguoi.setFont(fontLabel);
        infoPanel.add(lblSoNguoi, gbc);
        gbc.gridx = 1;
        JLabel valSoNguoi = new JLabel(String.valueOf(b.getSoNguoi()));
        valSoNguoi.setFont(fontValue);
        infoPanel.add(valSoNguoi, gbc);
        row++;

        // Ngày đặt
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblNgayDat = new JLabel("Ngày đặt:");
        lblNgayDat.setFont(fontLabel);
        infoPanel.add(lblNgayDat, gbc);
        gbc.gridx = 1;
        JLabel valNgayDat = new JLabel(b.getNgayDat() != null ? b.getNgayDat().toString() : "");
        valNgayDat.setFont(fontValue);
        infoPanel.add(valNgayDat, gbc);
        row++;

        // Giờ đặt
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGioDat = new JLabel("Giờ đặt:");
        lblGioDat.setFont(fontLabel);
        infoPanel.add(lblGioDat, gbc);
        gbc.gridx = 1;
        JLabel valGioDat = new JLabel(b.getGioDat() != null ? b.getGioDat().toString() : "");
        valGioDat.setFont(fontValue);
        infoPanel.add(valGioDat, gbc);
        row++;

        // Ghi chú
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(fontLabel);
        infoPanel.add(lblGhiChu, gbc);
        gbc.gridx = 1;
        JLabel valGhiChu = new JLabel(b.getGhiChu() != null ? b.getGhiChu() : "");
        valGhiChu.setFont(fontValue);
        infoPanel.add(valGhiChu, gbc);
        row++;

        // Đặt cọc
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblDatCoc = new JLabel("Đặt cọc:");
        lblDatCoc.setFont(fontLabel);
        infoPanel.add(lblDatCoc, gbc);
        gbc.gridx = 1;
        JLabel valDatCoc = new JLabel(String.valueOf(b.getTienCoc()));
        valDatCoc.setFont(fontValue);
        infoPanel.add(valDatCoc, gbc);
        row++;

        // Ghi chú cọc
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChuCoc = new JLabel("Ghi chú cọc:");
        lblGhiChuCoc.setFont(fontLabel);
        infoPanel.add(lblGhiChuCoc, gbc);
        gbc.gridx = 1;
        JLabel valGhiChuCoc = new JLabel(b.getGhiChuCoc() != null ? b.getGhiChuCoc() : "");
        valGhiChuCoc.setFont(fontValue);
        infoPanel.add(valGhiChuCoc, gbc);
        row++;

    }

    // Đặt kích thước dialog dựa trên trạng thái bàn
    dialog.setSize(isNotTrong ? 550 : 450, isNotTrong ? 500 : 300); // Nhỏ hơn khi bàn trống

    // Panel chứa các nút
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    JButton btnDatBan = new JButton("Đặt bàn");
    btnDatBan.setFont(fontButton);
    btnDatBan.setPreferredSize(new Dimension(100, 35));
    btnDatBan.setBackground(new Color(100, 149, 237)); // Màu xanh dương nhạt
    btnDatBan.setForeground(Color.WHITE);
    btnDatBan.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));

    JButton btnHuyBan = new JButton("Hủy bàn");
    btnHuyBan.setFont(fontButton);
    btnHuyBan.setPreferredSize(new Dimension(100, 35));
    btnHuyBan.setBackground(new Color(220, 20, 60)); // Màu đỏ
    btnHuyBan.setForeground(Color.WHITE);
    btnHuyBan.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2, true));

    JButton btnDangPhucVu = new JButton("Đang phục vụ");
    btnDangPhucVu.setFont(fontButton);
    btnDangPhucVu.setPreferredSize(new Dimension(120, 35));
    btnDangPhucVu.setBackground(new Color(50, 205, 50)); // Màu xanh lá
    btnDangPhucVu.setForeground(Color.WHITE);
    btnDangPhucVu.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true));

    if ("Trống".equalsIgnoreCase(b.getTrangThai())) {
        buttonPanel.add(btnDatBan);
    } else if ("Đặt".equalsIgnoreCase(b.getTrangThai())) {
        buttonPanel.add(btnHuyBan);
        buttonPanel.add(btnDangPhucVu);
    } else if ("Phục vụ".equalsIgnoreCase(b.getTrangThai())) {
        buttonPanel.add(btnHuyBan);
    }
    
    btnDatBan.addActionListener(e -> {
        try {
            moFormDatBan();
            taiTrangThaiBanTuDB(); // Cập nhật trạng thái bàn ngay sau khi đặt
            dialog.dispose();
            hienThiThongTinBan(banDAO.getBanByMa(b.getMaBan()));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở form đặt bàn!");
        }
    });

    // Sự kiện cho nút "Hủy bàn"
    btnHuyBan.addActionListener(e -> {
        try {
            moFormHuyBan(b.getMaBan());
            taiTrangThaiBanTuDB(); // Cập nhật trạng thái bàn ngay sau khi hủy
            dialog.dispose();
            hienThiThongTinBan(banDAO.getBanByMa(b.getMaBan())); // Mở lại dialog với thông tin mới
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hủy bàn!");
        }
    });

    // Sự kiện cho nút "Đang phục vụ"
    btnDangPhucVu.addActionListener(e -> {
        try {
            Ban ban = banDAO.getBanByMa(b.getMaBan());
            ban.setTrangThai("Phục vụ");
            banDAO.capNhatTrangThai(ban);
            mapBan.get(b.getMaBan()).setBackground(COLOR_PHUCVU);
            taiTrangThaiBanTuDB(); // Cập nhật trạng thái bàn
            dialog.dispose();
            hienThiThongTinBan(banDAO.getBanByMa(b.getMaBan())); // Mở lại dialog với thông tin mới
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi chuyển trạng thái phục vụ!");
        }
    });

    // Thêm các panel vào dialog
    dialog.add(infoPanel, BorderLayout.CENTER); // Không dùng JScrollPane
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Hiển thị dialog
    dialog.setVisible(true);
}

    private void xuLyBanClick(String name, JButton btn) {
        if (banDangChon != null) {
            JButton btnCu = mapBan.get(banDangChon);
            if (btnCu.getBackground().equals(Color.YELLOW)) {
                btnCu.setBackground(COLOR_TRONG);
            }
        }
        banDangChon = name;

        if (btn.getBackground().equals(COLOR_TRONG)) {
            btn.setBackground(Color.YELLOW);
        }
    }
    
    private void hienThiTatCaLich() throws SQLException {
    JDialog dlg = new JDialog(this, "Danh sách lịch đặt bàn", true);
    dlg.setSize(1000, 600);
    dlg.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new BorderLayout());

    // Bảng danh sách với đầy đủ thông tin
    DefaultTableModel model = new DefaultTableModel(new Object[]{
        "Khu vực", "Bàn", "Tên khách", "SĐT", "Số người", "Ngày đặt", "Giờ đặt", 
        "Ghi chú", "Đặt cọc", "Ghi chú cọc", "Món ăn", "Số lượng", "Trạng thái"
    }, 0);
    JTable table = new JTable(model);
    JScrollPane scroll = new JScrollPane(table);
    panel.add(scroll, BorderLayout.CENTER);

    // Tải dữ liệu từ DB
    List<Ban> listDatBan = banDAO.getAllBanDat();
    for (Ban b : listDatBan) {
        model.addRow(new Object[]{
            b.getKhuVuc() != null ? b.getKhuVuc() : "",
            b.getMaBan() != null ? b.getMaBan() : "",
            b.getTenKhach() != null ? b.getTenKhach() : "",
            b.getSoDienThoai() != null ? b.getSoDienThoai() : "",
            b.getSoNguoi(),
            b.getNgayDat() != null ? b.getNgayDat() : "",
            b.getGioDat() != null ? b.getGioDat() : "",
            b.getGhiChu() != null ? b.getGhiChu() : "",
            b.getTienCoc(),
            b.getGhiChuCoc() != null ? b.getGhiChuCoc() : "",
            "(danh sách món)", 
            "(số lượng)", 
            b.getTrangThai() != null ? b.getTrangThai() : ""
        });
    }

    // Thanh nút chức năng
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton btnDichCho = new JButton("Dịch chỗ");
    JButton btnSuaThongTin = new JButton("Sửa thông tin");
    JButton btnRutGon = new JButton("Rút gọn");
    JButton btnDatMon = new JButton("Đặt món");
    JButton btnIn = new JButton("In");
    JButton btnPhucVu = new JButton("Phục vụ");
    JButton btnNhapHang = new JButton("Nhập hàng");
    JButton btnXoa = new JButton("Xóa");
    JButton btnDong = new JButton("Đóng");

    btnPanel.add(btnDichCho);
    btnPanel.add(btnSuaThongTin);
    btnPanel.add(btnRutGon);
    btnPanel.add(btnDatMon);
    btnPanel.add(btnIn);
    btnPanel.add(btnPhucVu);
    btnPanel.add(btnNhapHang);
    btnPanel.add(btnXoa);
    btnPanel.add(btnDong);

    // Action cho Đóng
    btnDong.addActionListener(e -> dlg.dispose());

    // Xóa (hủy bàn)
    btnXoa.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 1);
            try {
                moFormHuyBan(maBan);
                model.removeRow(row);
                taiTrangThaiBanTuDB(); // Cập nhật lại giao diện
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    });

    // Sửa thông tin
    btnSuaThongTin.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 1);
            try {
                Ban b = banDAO.getBanByMa(maBan);
                moFormChinhSuaBan(b, model, row);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });

    // Phục vụ (chuyển trạng thái)
    btnPhucVu.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 1);
            try {
                Ban b = banDAO.getBanByMa(maBan);
                b.setTrangThai("Phục vụ");
                banDAO.capNhatTrangThai(b);
                model.setValueAt("Phục vụ", row, 12);
                mapBan.get(maBan).setBackground(COLOR_PHUCVU);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });

    // Đặt món từ lịch
    btnDatMon.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 1);
            new FrmOrder(maBan).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(dlg, "Vui lòng chọn lịch!");
        }
    });

    panel.add(btnPanel, BorderLayout.NORTH);
    dlg.add(panel);
    dlg.setVisible(true);
}
    private void moFormChinhSuaBan(Ban b, DefaultTableModel model, int row) {
        JDialog dlg = new JDialog(this, "Sửa thông tin đặt bàn", true);
        dlg.setSize(600, 500);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Arial", Font.PLAIN, 16);

        JTextField txtTen = new JTextField(b.getTenKhach()); txtTen.setFont(font);
        JTextField txtSDT = new JTextField(b.getSoDienThoai()); txtSDT.setFont(font);
        JTextField txtSoNguoi = new JTextField(String.valueOf(b.getSoNguoi())); txtSoNguoi.setFont(font);
        JSpinner spnNgay = new JSpinner(new SpinnerDateModel(b.getNgayDat(), null, null, Calendar.DAY_OF_MONTH));
        spnNgay.setEditor(new JSpinner.DateEditor(spnNgay, "yyyy-MM-dd")); spnNgay.setFont(font);
        JSpinner spnGio = new JSpinner(new SpinnerDateModel(new java.util.Date(b.getGioDat().getTime()), null, null, Calendar.HOUR_OF_DAY));
        spnGio.setEditor(new JSpinner.DateEditor(spnGio, "HH:mm")); spnGio.setFont(font);
        JTextArea txtGhiChu = new JTextArea(b.getGhiChu()); txtGhiChu.setFont(font);
        JTextField txtDatCoc = new JTextField(String.valueOf(b.getTienCoc())); txtDatCoc.setFont(font);
        JTextArea txtGhiChuCoc = new JTextArea(b.getGhiChuCoc()); txtGhiChuCoc.setFont(font);

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Tên KH:"), gbc);
        gbc.gridx = 1; dlg.add(txtTen, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1; dlg.add(txtSDT, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Số người:"), gbc);
        gbc.gridx = 1; dlg.add(txtSoNguoi, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Ngày đặt:"), gbc);
        gbc.gridx = 1; dlg.add(spnNgay, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Giờ đặt:"), gbc);
        gbc.gridx = 1; dlg.add(spnGio, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; dlg.add(new JScrollPane(txtGhiChu), gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Đặt cọc:"), gbc);
        gbc.gridx = 1; dlg.add(txtDatCoc, gbc); r++;
        gbc.gridx = 0; gbc.gridy = r; dlg.add(new JLabel("Ghi chú cọc:"), gbc);
        gbc.gridx = 1; dlg.add(new JScrollPane(txtGhiChuCoc), gbc); r++;

        JButton btnLuu = new JButton("Lưu");
        gbc.gridx = 1; gbc.gridy = r; dlg.add(btnLuu, gbc);

        btnLuu.addActionListener(e -> {
            try {
                b.setTenKhach(txtTen.getText());
                b.setSoDienThoai(txtSDT.getText());
                b.setSoNguoi(Integer.parseInt(txtSoNguoi.getText()));
                b.setNgayDat(new Date(((java.util.Date) spnNgay.getValue()).getTime()));
                b.setGioDat(new Time(((java.util.Date) spnGio.getValue()).getTime()));
                b.setGhiChu(txtGhiChu.getText());
                b.setTienCoc(Double.parseDouble(txtDatCoc.getText()));
                b.setGhiChuCoc(txtGhiChuCoc.getText());
                banDAO.capNhatBan(b);
                // Cập nhật bảng
                model.setValueAt(b.getTenKhach(), row, 2);
                model.setValueAt(b.getSoDienThoai(), row, 3);
                model.setValueAt(b.getSoNguoi(), row, 4);
                model.setValueAt(b.getNgayDat(), row, 5);
                model.setValueAt(b.getGioDat(), row, 6);
                model.setValueAt(b.getGhiChu(), row, 7);
                model.setValueAt(b.getTienCoc(), row, 8);
                model.setValueAt(b.getGhiChuCoc(), row, 9);
                dlg.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        dlg.setVisible(true);
    }

    private void moFormDatBan() throws SQLException {
    if (banDangChon == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
        return;
    }

    Ban ban = banDAO.getBanByMa(banDangChon);
    if (ban == null || !"Trống".equalsIgnoreCase(ban.getTrangThai())) {
        JOptionPane.showMessageDialog(this, "Bàn không trống!");
        return;
    }
    String thongTinBan = "Bàn: " + ban.getMaBan() + " | Khu vực: " + ban.getKhuVuc();

    JDialog dlg = new JDialog(this, "Đặt bàn", true);
    dlg.setSize(700, 600);
    dlg.setLayout(new BorderLayout(15, 15));
    dlg.setLocationRelativeTo(this);

    Font fontBig = new Font("Arial", Font.BOLD, 20);

    // Header
    JLabel lblThongTinBan = new JLabel(thongTinBan, SwingConstants.CENTER);
    lblThongTinBan.setFont(new Font("Times New Roman", Font.BOLD, 24));
    lblThongTinBan.setForeground(Color.BLUE);
    dlg.add(lblThongTinBan, BorderLayout.NORTH);

    // Form
    JPanel pForm = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JTextField txtTen = new JTextField(20); txtTen.setFont(fontBig);
    JTextField txtSDT = new JTextField(15); txtSDT.setFont(fontBig);
    JButton btnCheckKH = new JButton("Kiểm tra"); btnCheckKH.setFont(fontBig);

    // Ngày từ hiện tại trở đi
    Calendar cal = Calendar.getInstance();
    java.util.Date today = cal.getTime();
    SpinnerDateModel dateModel = new SpinnerDateModel(today, today, null, Calendar.DAY_OF_MONTH);
    JSpinner spnNgay = new JSpinner(dateModel);
    spnNgay.setEditor(new JSpinner.DateEditor(spnNgay, "yyyy-MM-dd")); spnNgay.setFont(fontBig);

    // Giờ
    SpinnerDateModel timeModel = new SpinnerDateModel(today, null, null, Calendar.MINUTE);
    JSpinner spnGio = new JSpinner(timeModel);
    spnGio.setEditor(new JSpinner.DateEditor(spnGio, "HH:mm")); spnGio.setFont(fontBig);

    JTextField txtSoNguoi = new JTextField(5); txtSoNguoi.setFont(fontBig);
    JTextArea txtGhiChu = new JTextArea(3, 20); txtGhiChu.setFont(fontBig);

    JTextField txtDatCoc = new JTextField(10); txtDatCoc.setFont(fontBig);
    JCheckBox chkCK = new JCheckBox("Chuyển khoản"); chkCK.setFont(fontBig);
    JTextArea txtGhiChuCK = new JTextArea(2, 20); txtGhiChuCK.setFont(fontBig);

    JButton btnDatMon = new JButton("Đặt món"); btnDatMon.setFont(fontBig);
    JButton btnOK = new JButton("OK"); btnOK.setFont(fontBig);
    JButton btnCancel = new JButton("Hủy"); btnCancel.setFont(fontBig);

    // Layout
    int row = 0;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Tên KH:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(txtTen, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("SĐT:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(txtSDT, gbc);
    gbc.gridx = 2; gbc.gridy = row; pForm.add(btnCheckKH, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ngày đặt:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(spnNgay, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Giờ đặt:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(spnGio, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Số người:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(txtSoNguoi, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ghi chú:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(new JScrollPane(txtGhiChu), gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Đặt cọc:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(txtDatCoc, gbc);
    gbc.gridx = 2; gbc.gridy = row; pForm.add(chkCK, gbc);
    row++;
    gbc.gridx = 0; gbc.gridy = row; pForm.add(new JLabel("Ghi chú CK:"){{
        setFont(fontBig);
    }}, gbc);
    gbc.gridx = 1; gbc.gridy = row; pForm.add(new JScrollPane(txtGhiChuCK), gbc);

    // Panel nút
    JPanel pSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pSouth.add(btnDatMon);
    pSouth.add(btnOK);
    pSouth.add(btnCancel);

    dlg.add(pForm, BorderLayout.CENTER);
    dlg.add(pSouth, BorderLayout.SOUTH);

    // Sự kiện
    btnCheckKH.addActionListener(e -> {
        // Giữ nguyên logic kiểm tra khách hàng nếu có
    });

    chkCK.addActionListener(e -> {
        txtGhiChuCK.setEnabled(chkCK.isSelected());
    });

    btnDatMon.addActionListener(e -> {
        new FrmOrder(banDangChon).setVisible(true);
    });

    btnOK.addActionListener(e -> {
        try {
            double tienCoc = txtDatCoc.getText().isEmpty() ? 0 : Double.parseDouble(txtDatCoc.getText());
            Ban b = new Ban();
            b.setMaBan(banDangChon);
            b.setTenKhach(txtTen.getText());
            b.setSoDienThoai(txtSDT.getText());
            b.setSoNguoi(Integer.parseInt(txtSoNguoi.getText()));
            b.setNgayDat(new Date(((java.util.Date) spnNgay.getValue()).getTime()));
            b.setGioDat(new Time(((java.util.Date) spnGio.getValue()).getTime()));
            b.setGhiChu(txtGhiChu.getText());
            b.setTienCoc(tienCoc);
            b.setGhiChuCoc(chkCK.isSelected() ? txtGhiChuCK.getText() : "");
            b.setTrangThai("Đặt");

            banDAO.datBan(b);
            mapBan.get(banDangChon).setBackground(COLOR_DAT);
            taiTrangThaiBanTuDB(); // Cập nhật trạng thái bàn ngay sau khi đặt
            JOptionPane.showMessageDialog(dlg, "Đặt bàn thành công!");
            dlg.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dlg, "Lỗi khi đặt bàn: " + ex.getMessage());
        }
    });

    btnCancel.addActionListener(e -> dlg.dispose());

    dlg.setVisible(true);
}


    private void moFormHuyBan() throws SQLException {
        moFormHuyBan(banDangChon);
    }

   private void moFormHuyBan(String maBan) throws SQLException {
    if (maBan == null) {
        JOptionPane.showMessageDialog(this, "Chưa chọn bàn!");
        return;
    }

    JPanel panelLyDo = new JPanel(new GridLayout(2, 1));
    JLabel lblLyDo = new JLabel("Chọn lý do hủy:");
    JComboBox<String> cbLyDo = new JComboBox<>(new String[]{"Khách không đến", "Đặt nhầm", "Khác"});
    panelLyDo.add(lblLyDo);
    panelLyDo.add(cbLyDo);

    int opt = JOptionPane.showConfirmDialog(this, panelLyDo, "Hủy bàn", JOptionPane.OK_CANCEL_OPTION);
    if (opt == JOptionPane.OK_OPTION) {
        String lyDo = (String) cbLyDo.getSelectedItem();
        if ("Khác".equals(lyDo)) {
            lyDo = JOptionPane.showInputDialog(this, "Nhập lý do khác:");
        }
        // Cập nhật trạng thái bàn về trống và xóa thông tin đặt bàn
        Ban b = banDAO.getBanByMa(maBan);
        b.setTrangThai("Trống");
        b.setTenKhach(null);
        b.setSoDienThoai(null);
        b.setSoNguoi(0);
        b.setNgayDat(null);
        b.setGioDat(null);
        b.setGhiChu(null);
        b.setTienCoc(0);
        b.setGhiChuCoc(null);
        banDAO.capNhatBan(b); // Cập nhật vào DB
        mapBan.get(maBan).setBackground(COLOR_TRONG); // Cập nhật màu bàn
        JOptionPane.showMessageDialog(this, "Đã hủy bàn!");
    }
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
                Color mauChung = COLOR_DAT;
                mapBan.get(banDangChon).setBackground(mauChung);
                mapBan.get(banGop).setBackground(mauChung);
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
                mapBan.get(banMoi).setBackground(mapBan.get(banDangChon).getBackground());
                mapBan.get(banDangChon).setBackground(COLOR_TRONG);
                JOptionPane.showMessageDialog(this, "Đã chuyển bàn!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void xuLyDatMon() {
        try {
            Ban b = banDAO.getBanByMa(banDangChon);
            b.setTrangThai("Phục vụ");
            banDAO.capNhatTrangThai(b);
            mapBan.get(banDangChon).setBackground(COLOR_PHUCVU);
            JOptionPane.showMessageDialog(this, "Đã chuyển sang trạng thái phục vụ!");
            new FrmOrder(banDangChon).setVisible(true);
            dispose(); // Tùy chọn: đóng FrmBan nếu muốn chuyển hoàn toàn
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void moFormChinhSua() throws SQLException {
    JDialog dlg = new JDialog(this, "Chỉnh sửa bàn", true);
    dlg.setSize(800, 600);
    dlg.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new BorderLayout());

    // Bảng danh sách tất cả bàn
    DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã bàn", "Khu vực", "Số ghế", "Trạng thái", "Độ dài", "Độ rộng"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1 || column == 2 || column == 4 || column == 5;
        }
    };
    JTable table = new JTable(model);
    JScrollPane scroll = new JScrollPane(table);
    panel.add(scroll, BorderLayout.CENTER);

    // Tải dữ liệu
    List<Ban> listBan = banDAO.getAllBan();
    for (Ban b : listBan) {
        model.addRow(new Object[]{b.getMaBan(), b.getKhuVuc(), b.getSoGhe(), b.getTrangThai(), b.getX(), b.getY()});
    }

    // Nút chức năng: Thêm, Sửa, Xóa, Lưu
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton btnThem = new JButton("Thêm bàn");
    JButton btnSua = new JButton("Sửa bàn");
    JButton btnXoa = new JButton("Xóa bàn");
    JButton btnLuu = new JButton("Lưu thay đổi");
    JButton btnDong = new JButton("Đóng");

    btnPanel.add(btnThem);
    btnPanel.add(btnSua);
    btnPanel.add(btnXoa);
    btnPanel.add(btnLuu);
    btnPanel.add(btnDong);

    panel.add(btnPanel, BorderLayout.NORTH);
    dlg.add(panel);

    // Action Thêm
    btnThem.addActionListener(e -> {
        try {
            String maBan = JOptionPane.showInputDialog("Nhập mã bàn mới:");
            String khuVuc = JOptionPane.showInputDialog("Nhập khu vực (A/B/C):");
            int soGhe = Integer.parseInt(JOptionPane.showInputDialog("Nhập số ghế:"));
            int x = Integer.parseInt(JOptionPane.showInputDialog("Nhập vị trí X:"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Nhập vị trí Y:"));
            Ban b = new Ban(maBan, khuVuc, soGhe, "Trống", x, y);
            banDAO.themBan(b);
            model.addRow(new Object[]{maBan, khuVuc, soGhe, "Trống", x, y});
            // Cập nhật giao diện
            int tabIndex = "A".equals(khuVuc) ? 0 : "B".equals(khuVuc) ? 1 : 2;
            JPanel khuPanel = (JPanel) tabbedPane.getComponentAt(tabIndex);
            JButton btnNew = new JButton(maBan);
            btnNew.setBounds(x, y, 70, 70);
            btnNew.setBackground(COLOR_TRONG);
            btnNew.addActionListener(ev -> {
                xuLyBanClick(maBan, btnNew);
                try {
                    hienThiThongTinBan(banDAO.getBanByMa(maBan));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            khuPanel.add(btnNew);
            mapBan.put(maBan, btnNew);
            khuPanel.revalidate();
            khuPanel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });

    // Action Sửa
    btnSua.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 0);
            try {
                Ban b = banDAO.getBanByMa(maBan);
                String khuVucMoi = JOptionPane.showInputDialog("Khu vực mới:", b.getKhuVuc());
                int soGheMoi = Integer.parseInt(JOptionPane.showInputDialog("Số ghế mới:", b.getSoGhe()));
                int xMoi = Integer.parseInt(JOptionPane.showInputDialog("Vị trí X mới:", b.getX()));
                int yMoi = Integer.parseInt(JOptionPane.showInputDialog("Vị trí Y mới:", b.getY()));
                b.setKhuVuc(khuVucMoi);
                b.setSoGhe(soGheMoi);
                b.setX(xMoi);
                b.setY(yMoi);
                banDAO.capNhatBan(b);
                model.setValueAt(khuVucMoi, row, 1);
                model.setValueAt(soGheMoi, row, 2);
                model.setValueAt(xMoi, row, 4);
                model.setValueAt(yMoi, row, 5);
                // Cập nhật button
                JButton btn = mapBan.get(maBan);
                btn.setBounds(xMoi, yMoi, 70, 70);
                if (!khuVucMoi.equals(b.getKhuVuc())) {
                    int oldTab = "A".equals(b.getKhuVuc()) ? 0 : "B".equals(b.getKhuVuc()) ? 1 : 2;
                    JPanel oldPanel = (JPanel) tabbedPane.getComponentAt(oldTab);
                    oldPanel.remove(btn);
                    oldPanel.revalidate();
                    oldPanel.repaint();
                    int newTab = "A".equals(khuVucMoi) ? 0 : "B".equals(khuVucMoi) ? 1 : 2;
                    JPanel newPanel = (JPanel) tabbedPane.getComponentAt(newTab);
                    newPanel.add(btn);
                    newPanel.revalidate();
                    newPanel.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    });

    // Action Xóa
    btnXoa.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String maBan = (String) model.getValueAt(row, 0);
            try {
                banDAO.xoaBan(maBan);
                model.removeRow(row);
                JButton btn = mapBan.get(maBan);
                btn.getParent().remove(btn);
                btn.getParent().revalidate();
                btn.getParent().repaint();
                mapBan.remove(maBan);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    });

    // Action Lưu
    btnLuu.addActionListener(e -> {
        for (int i = 0; i < model.getRowCount(); i++) {
            String maBan = (String) model.getValueAt(i, 0);
            try {
                Ban b = banDAO.getBanByMa(maBan);
                String khuVucMoi = (String) model.getValueAt(i, 1);
                int soGheMoi = Integer.parseInt(model.getValueAt(i, 2).toString());
                int xMoi = Integer.parseInt(model.getValueAt(i, 4).toString());
                int yMoi = Integer.parseInt(model.getValueAt(i, 5).toString());
                b.setKhuVuc(khuVucMoi);
                b.setSoGhe(soGheMoi);
                b.setX(xMoi);
                b.setY(yMoi);
                banDAO.capNhatBan(b);
                // Cập nhật button
                JButton btn = mapBan.get(maBan);
                btn.setBounds(xMoi, yMoi, 70, 70);
                if (!khuVucMoi.equals(b.getKhuVuc())) {
                    int oldTab = "A".equals(b.getKhuVuc()) ? 0 : "B".equals(b.getKhuVuc()) ? 1 : 2;
                    JPanel oldPanel = (JPanel) tabbedPane.getComponentAt(oldTab);
                    oldPanel.remove(btn);
                    oldPanel.revalidate();
                    oldPanel.repaint();
                    int newTab = "A".equals(khuVucMoi) ? 0 : "B".equals(khuVucMoi) ? 1 : 2;
                    JPanel newPanel = (JPanel) tabbedPane.getComponentAt(newTab);
                    newPanel.add(btn);
                    newPanel.revalidate();
                    newPanel.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(dlg, "Đã lưu thay đổi!");
    });

    btnDong.addActionListener(e -> dlg.dispose());

    dlg.setVisible(true);
}

    // ===== Hàm tạo nút menu bên trái =====
    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 22));
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

    // ===== Class nút dưới cùng =====
    class BottomButton extends JButton {
        public BottomButton(String text, String iconPath) {
            super(text);

            setFont(new Font("Times New Roman", Font.BOLD, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(false);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setVerticalTextPosition(SwingConstants.CENTER);
            try {
                ImageIcon icon = new ImageIcon(iconPath);
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Không tìm thấy icon: " + iconPath);
            }

            setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setForeground(new Color(200, 0, 0));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setForeground(Color.BLACK);
                }
            });
        }
    }

    // ===== Main =====
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