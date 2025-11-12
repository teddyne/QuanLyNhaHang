package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import connectSQL.ConnectSQL;
import entity.LoaiMon;
import entity.MonAn;
import dao.LoaiMon_DAO;
import dao.MonAn_DAO;
import dialog.FrmLoaiMon;

public class FrmThucDon extends JFrame {
    // Màu sắc (đồng bộ với FrmDatMon)
    private static final Color MAU_DO_RUOU = new Color(169, 55, 68);
    private static final Color MAU_HONG = new Color(241, 200, 204);
    private static final Color MAU_XANH_LA = new Color(46, 204, 113);
    private static final Color MAU_XANH_DUONG = new Color(52, 152, 219);
    private static final Color MAU_DO = new Color(231, 76, 60);
    private static final Color MAU_VANG = new Color(255, 200, 60);
    private static final Color MAU_XAM = Color.LIGHT_GRAY;
    private static final Color MAU_TRANG = Color.WHITE;
    private static final Font FONT_TXT = new Font("Times New Roman", Font.PLAIN, 14);
    private static final Font FONT_TIEU_DE = new Font("Times New Roman", Font.BOLD, 24);
    private static final Font FONT_BUTTON = new Font("Times New Roman", Font.BOLD, 16);
    private Connection con = ConnectSQL.getConnection();

    // DAO
    private final MonAn_DAO monAnDAO;
    private final LoaiMon_DAO loaiMonDAO;

    // Thành phần giao diện
    private List<MonAn> danhSachMon = new ArrayList<>();
    private final Map<String, JPanel> panelByLoaiMa = new HashMap<>(); // key = maLoai
    private JPanel pnlLuoiMon; // panel "Tất cả"
    private JTextField txtTimKiem;
    private JComboBox<String> cmbLocLoai;
    private JTabbedPane tabLoai;
    private MonAn monDangChon = null;
    private JPanel khungDangChon = null;

    public FrmThucDon() throws SQLException {
    	// Menu
    	setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);
        
        monAnDAO = new MonAn_DAO(con);
        loaiMonDAO = new LoaiMon_DAO(con);
        setTitle("THỰC ĐƠN");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo giao diện
        taoThanhBen();
        taoTieuDe();
        taoNoiDungChinh();

        // Load dữ liệu và build tab + combo
        refreshLoaiAndData();

        setVisible(true);
    }


 // Thanh bên trái
    private void taoThanhBen() {
        JPanel pnlBenTrai = new JPanel(new BorderLayout());
        pnlBenTrai.setPreferredSize(new Dimension(220, 0));
        pnlBenTrai.setBackground(MAU_TRANG);

        // --- Thời gian ---
        JPanel pnlThoiGian = new JPanel(new GridLayout(2, 1));
        pnlThoiGian.setBackground(MAU_TRANG);
        pnlThoiGian.setBorder(new MatteBorder(0, 0, 1, 0, MAU_XAM));

        JLabel lblGio = new JLabel();
        lblGio.setFont(new Font("Consolas", Font.BOLD, 22));
        lblGio.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNgay = new JLabel();
        lblNgay.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblNgay.setHorizontalAlignment(SwingConstants.CENTER);

        Timer dongHo = new Timer(1000, e -> {
            Date now = new Date();
            lblGio.setText(new SimpleDateFormat("HH:mm:ss").format(now));
            lblNgay.setText(new SimpleDateFormat("dd/MM/yyyy").format(now));
        });
        dongHo.start();

        pnlThoiGian.add(lblGio);
        pnlThoiGian.add(lblNgay);
        pnlBenTrai.add(pnlThoiGian, BorderLayout.NORTH);

        // --- Các nút chức năng ---
        JPanel pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new BoxLayout(pnlChucNang, BoxLayout.Y_AXIS));
        pnlChucNang.setBackground(MAU_TRANG);
        pnlChucNang.setBorder(new TitledBorder(null, "CHỨC NĂNG", TitledBorder.CENTER,
                TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 17), MAU_DO_RUOU));

        Dimension btnSize = new Dimension(160, 50);

        JButton btnThem = taoNhoButton("Thêm món", MAU_XANH_LA, MAU_TRANG, btnSize);
        btnThem.addActionListener(e -> new DialogThemMonAn(this).setVisible(true));

        JButton btnCapNhat = taoNhoButton("Cập nhật", MAU_XANH_DUONG, MAU_TRANG, btnSize);
        btnCapNhat.addActionListener(e -> {
            if (monDangChon == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món để cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new DialogCapNhatMonAn(this, monDangChon).setVisible(true);
        });

        JButton btnXoa = taoNhoButton("Xóa món", MAU_DO, MAU_TRANG, btnSize);
        btnXoa.addActionListener(e -> xoaMonAn());

        JButton btnLoaiMon = taoNhoButton("Thêm loại món", MAU_VANG, Color.BLACK, btnSize);
        btnLoaiMon.addActionListener(e -> {
            FrmLoaiMon frm = null;
			try {
				frm = new FrmLoaiMon();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            frm.setVisible(true);
        });

        JButton btnLamMoi = taoNhoButton("Làm mới", MAU_XAM, Color.WHITE, btnSize);
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            if (cmbLocLoai.getItemCount() > 0)
                cmbLocLoai.setSelectedIndex(0);
            refreshLoaiAndData();
        });

        // Thêm các nút cách nhau một khoảng
        pnlChucNang.add(Box.createVerticalStrut(30));
        pnlChucNang.add(center(btnThem));
        pnlChucNang.add(Box.createVerticalStrut(30));
        pnlChucNang.add(center(btnCapNhat));
        pnlChucNang.add(Box.createVerticalStrut(30));
        pnlChucNang.add(center(btnXoa));
        pnlChucNang.add(Box.createVerticalStrut(30));
        pnlChucNang.add(center(btnLoaiMon));
        pnlChucNang.add(Box.createVerticalStrut(30));
        pnlChucNang.add(center(btnLamMoi));
        pnlChucNang.add(Box.createVerticalGlue());

        pnlBenTrai.add(pnlChucNang, BorderLayout.CENTER);
        add(pnlBenTrai, BorderLayout.WEST);
    }

    private JButton taoNhoButton(String text, Color bg, Color fg, Dimension size) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);

        // Thiết lập màu nền và chữ
        btn.setBackground(bg);
        btn.setForeground(fg);

        // Bắt buộc hiển thị màu nền đúng
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false); // tắt viền mặc định, nhìn gọn hơn

        // Hover effect
        Color hover = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    


    // Căn giữa component trong BoxLayout
    private Component center(JComponent comp) {
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        return comp;
    }



    // Tiêu đề
    private void taoTieuDe() {
        JPanel pnlTieuDe = new JPanel(new BorderLayout());
        pnlTieuDe.setBackground(MAU_DO_RUOU);
        pnlTieuDe.setPreferredSize(new Dimension(0, 60));

        JLabel lblTieuDe = new JLabel("THỰC ĐƠN", SwingConstants.CENTER);
        lblTieuDe.setForeground(MAU_TRANG);
        lblTieuDe.setFont(FONT_TIEU_DE);

        pnlTieuDe.add(lblTieuDe, BorderLayout.CENTER);
        add(pnlTieuDe, BorderLayout.NORTH);
    }

    // Nội dung chính

    private void taoNoiDungChinh() {
        JPanel pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlChinh.setBackground(MAU_TRANG);

        // Thanh tìm kiếm và lọc
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        pnlTimKiem.setBackground(MAU_TRANG);
        pnlTimKiem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAU_XAM, 1),
                "Tìm kiếm & Lọc", 0, 0,
                new Font("Times New Roman", Font.BOLD, 20), MAU_DO_RUOU));

        JLabel lblTenMon = new JLabel("Tên món:");
        lblTenMon.setFont(FONT_TXT);
        txtTimKiem = new JTextField(24);
        txtTimKiem.setFont(FONT_TXT);

        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setFont(FONT_TXT);
        cmbLocLoai = new JComboBox<>();
        cmbLocLoai.setFont(FONT_TXT);
        cmbLocLoai.setPreferredSize(new Dimension(150, 30));

        pnlTimKiem.add(lblTenMon);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(Box.createHorizontalStrut(10));
        pnlTimKiem.add(lblLoai);
        pnlTimKiem.add(cmbLocLoai);


        // Live search
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void removeUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void changedUpdate(DocumentEvent e) { capNhatLuoiMon(); }
        });

        // ComboBox filter
        cmbLocLoai.addActionListener(e -> capNhatLuoiMon());

        pnlChinh.add(pnlTimKiem, BorderLayout.NORTH);

        // Tạo tabbed pane
        tabLoai = new JTabbedPane();
        tabLoai.setFont(new Font("Times New Roman", Font.BOLD, 16));
        UIManager.put("TabbedPane.selectedForeground", MAU_TRANG);
        tabLoai.setOpaque(true);
        tabLoai.setBackground(MAU_HONG);
        tabLoai.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? MAU_DO_RUOU : MAU_XAM);
                g.fillRect(x, y, w, h);
            }
        });

        pnlChinh.add(tabLoai, BorderLayout.CENTER);
        add(pnlChinh, BorderLayout.CENTER);

        refreshLoaiAndData();
    }

    private void refreshLoaiAndData() {
        // Xóa dữ liệu cũ
        cmbLocLoai.removeAllItems();
        tabLoai.removeAll();

        // Lấy danh sách loại món từ DB
        List<LoaiMon> dsLoai = loaiMonDAO.getAllLoaiMon();

        for (LoaiMon lm : dsLoai) {
            cmbLocLoai.addItem(lm.getTenLoai());
        }

        // Build tabs theo loại món
        buildTabs();

        // Load dữ liệu món từ DB
        loadDataFromDB();

        // Chọn mặc định loại đầu tiên
        if (cmbLocLoai.getItemCount() > 0)
            cmbLocLoai.setSelectedIndex(0);

        // Cập nhật món hiển thị
        capNhatLuoiMon();
    }



    private void dongBoLoaiMon() {
        // Khi chọn trong combobox → đổi tab
        cmbLocLoai.addActionListener(e -> {
            String selected = (String) cmbLocLoai.getSelectedItem();
            for (int i = 0; i < tabLoai.getTabCount(); i++) {
                if (tabLoai.getTitleAt(i).equals(selected)) {
                    tabLoai.setSelectedIndex(i);
                    break;
                } else if ("Tất cả".equals(selected)) {
                    tabLoai.setSelectedIndex(-1); // Không chọn tab cụ thể
                }
            }
            capNhatLuoiMon();
        });

        // Khi đổi tab → đổi combobox
        tabLoai.addChangeListener(e -> {
            int index = tabLoai.getSelectedIndex();
            if (index >= 0) {
                String tenLoai = tabLoai.getTitleAt(index);
                cmbLocLoai.setSelectedItem(tenLoai);
            }
        });
    }


    // Build dynamic tabs from LoaiMon table (only trangThai=1)
    private void buildTabs() {
        tabLoai.removeAll();
        panelByLoaiMa.clear();

        List<LoaiMon> listLoai = loaiMonDAO.getAllLoaiMon();
        for (LoaiMon lm : listLoai) {
            JPanel pnl = taoPanelLuoiMon();
            panelByLoaiMa.put(lm.getMaLoai(), pnl);
            JScrollPane scr = taoScroll(pnl);
            tabLoai.addTab(lm.getTenLoai(), scr);
        }

        // Khi đổi tab -> cập nhật combobox
        tabLoai.addChangeListener(e -> {
            int idx = tabLoai.getSelectedIndex();
            if (idx >= 0) {
                String title = tabLoai.getTitleAt(idx);
                cmbLocLoai.setSelectedItem(title);
                capNhatLuoiMon();
            }
        });
    }

    // Load dữ liệu món ăn từ DB
    private void loadDataFromDB() {
        danhSachMon = monAnDAO.getAllMonAn();
        if (danhSachMon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cơ sở dữ liệu rỗng! Vui lòng chạy script SQL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Lọc ra những món có loại món hợp lệ
        Set<String> activeLoai = new HashSet<>();
        for (LoaiMon lm : loaiMonDAO.getAllLoaiMon()) activeLoai.add(lm.getMaLoai());
        List<MonAn> filtered = new ArrayList<>();
        for (MonAn m : danhSachMon) {
            if (m.getLoaiMon() != null && activeLoai.contains(m.getLoaiMon().getMaLoai()))
                filtered.add(m);
        }
        danhSachMon = filtered;
    }


    // Khởi tạo combobox loại món (dynamic)
    private void khoiTaoComboBoxLoai() {
        List<LoaiMon> listLoai = loaiMonDAO.getAllLoaiMon();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả");
        for (LoaiMon lm : listLoai) {
            model.addElement(lm.getTenLoai());
        }
        cmbLocLoai.setModel(model);
        cmbLocLoai.setSelectedItem("Tất cả");
    }

    // Cập nhật lưới món: đặt card vào panel tương ứng (dynamic)
    private void capNhatLuoiMon() {
        for (JPanel p : panelByLoaiMa.values()) p.removeAll();

        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        String loaiChon = (String) cmbLocLoai.getSelectedItem();

        for (MonAn mon : danhSachMon) {
            if (!mon.isTrangThai()) continue;

            boolean matchKeyword = tuKhoa.isEmpty() || mon.getTenMon().toLowerCase().contains(tuKhoa);
            boolean matchLoai = mon.getLoaiMon() != null &&
                    mon.getLoaiMon().getTenLoai().equalsIgnoreCase(loaiChon);

            if (matchKeyword && matchLoai) {
                JPanel card = taoCardMon(mon);
                String maLoai = mon.getLoaiMon().getMaLoai();
                if (panelByLoaiMa.containsKey(maLoai)) {
                    panelByLoaiMa.get(maLoai).add(card);
                }
            }
        }

        for (JPanel p : panelByLoaiMa.values()) {
            p.revalidate();
            p.repaint();
        }

        // Đồng bộ Tab ↔ ComboBox
        int idx = tabLoai.getSelectedIndex();
        if (idx >= 0 && idx < tabLoai.getTabCount()) {
            String tabName = tabLoai.getTitleAt(idx);
            cmbLocLoai.setSelectedItem(tabName);
        }
    }



 // Tạo panel lưới món
    private JPanel taoPanelLuoiMon() {
        JPanel pnl = new JPanel(new GridLayout(0, 4, 15, 15));
        pnl.setBackground(MAU_TRANG);
        return pnl;
    }

    // Tạo JScrollPane
    private JScrollPane taoScroll(JPanel panel) {
        JScrollPane scr = new JScrollPane(panel);
        scr.setBorder(BorderFactory.createEmptyBorder());
        scr.getViewport().setBackground(MAU_TRANG);
        return scr;
    }
    
    // Tạo card món
    private JPanel taoCardMon(MonAn mon) {
        JPanel khung = new JPanel(new BorderLayout(5, 5));
        khung.setBackground(MAU_TRANG);
        khung.setPreferredSize(new Dimension(220, 300));
        khung.setMaximumSize(new Dimension(220, 300));
        khung.setMinimumSize(new Dimension(220, 300));
        khung.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_DO_RUOU, 2, true),
                new EmptyBorder(8, 8, 8, 8)));

        // Hover effect + select
        khung.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (khung != khungDangChon) {
                    khung.setBackground(MAU_HONG);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (khung != khungDangChon) {
                    khung.setBackground(MAU_TRANG);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (khungDangChon != null) {
                    khungDangChon.setBackground(MAU_TRANG);
                    khungDangChon.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(MAU_DO_RUOU, 2, true),
                            new EmptyBorder(8, 8, 8, 8)));
                }
                khungDangChon = khung;
                monDangChon = mon;
                khung.setBackground(MAU_HONG);
                khung.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(MAU_XANH_LA, 3, true),
                        new EmptyBorder(8, 8, 8, 8)));
            }
        });

        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setPreferredSize(new Dimension(200, 160));
        lblAnh.setBorder(BorderFactory.createLineBorder(MAU_XAM, 1));

        String duongDan = mon.getAnhMon() != null && !mon.getAnhMon().isEmpty() ? mon.getAnhMon() : "img/placeholder.png";
        datAnhChoLabel(lblAnh, duongDan, 200, 160);

        khung.add(lblAnh, BorderLayout.NORTH);

        JPanel pnlTT = new JPanel(new GridBagLayout());
        pnlTT.setBackground(MAU_TRANG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 4, 2, 4);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblMa = new JLabel("<html><u>" + mon.getMaMon() + "</u></html>");
        lblMa.setFont(FONT_TXT);
        lblMa.setForeground(Color.BLACK);
        pnlTT.add(lblMa, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblTen.setForeground(Color.BLACK);
        lblTen.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTT.add(lblTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTrangThaiLabel = new JLabel("Trạng thái:");
        lblTrangThaiLabel.setFont(FONT_TXT);
        lblTrangThaiLabel.setForeground(Color.BLACK);
        pnlTT.add(lblTrangThaiLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JLabel lblTrangThai = new JLabel(mon.isTrangThai() ? "Còn món" : "Hết món");
        lblTrangThai.setFont(FONT_TXT);
        lblTrangThai.setForeground(mon.isTrangThai() ? MAU_XANH_LA : Color.RED);
        pnlTT.add(lblTrangThai, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblMoTaLabel = new JLabel("Mô tả:");
        lblMoTaLabel.setFont(FONT_TXT);
        lblMoTaLabel.setForeground(Color.BLACK);
        pnlTT.add(lblMoTaLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JLabel lblMoTa = new JLabel("<html><i>" + (mon.getMoTa() != null ? mon.getMoTa() : "") + "</i></html>");
        lblMoTa.setFont(FONT_TXT);
        lblMoTa.setForeground(Color.BLACK);
        pnlTT.add(lblMoTa, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblGia = new JLabel(String.format("%,.0f đ", mon.getDonGia() > 0.0 ? mon.getDonGia() : 0.0));
        lblGia.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblGia.setForeground(MAU_DO_RUOU);
        pnlTT.add(lblGia, gbc);

        khung.add(pnlTT, BorderLayout.CENTER);
        return khung;
    }

    // Xóa món ăn (thực chất ẩn)
    private void xoaMonAn() {
        if (monDangChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ch = JOptionPane.showConfirmDialog(this,
                String.format("Xóa '%s' khỏi thực đơn?", monDangChon.getTenMon()),
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (ch == JOptionPane.YES_OPTION) {
            try {
                if (monAnDAO.anMonAn(monDangChon.getMaMon())) {
                    JOptionPane.showMessageDialog(this, "Ẩn món thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDataFromDB();
                    capNhatLuoiMon();
                    monDangChon = null;
                    khungDangChon = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa món thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Dialog thêm món ăn
    class DialogThemMonAn extends JDialog {
        private JTextField txtTen, txtGia;
        private JComboBox<String> cmbLoai, cmbTrangThai;
        private JTextArea txtMoTa;
        private JLabel lblAnh;
        private String imagePath = "img/placeholder.png";

        public DialogThemMonAn(JFrame parent) {
            super(parent, "Thêm món ăn", true);
            initDialog();
        }

        private void initDialog() {
            setSize(450, 580);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout(10, 10));

            // Tiêu đề
            JLabel lblTieuDe = new JLabel("THÊM MÓN ĂN MỚI", SwingConstants.CENTER);
            lblTieuDe.setOpaque(true);
            lblTieuDe.setBackground(MAU_DO_RUOU);
            lblTieuDe.setForeground(MAU_TRANG);
            lblTieuDe.setFont(FONT_TIEU_DE);
            lblTieuDe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            add(lblTieuDe, BorderLayout.NORTH);

            // Form
            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            form.setBackground(MAU_TRANG);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            // Ảnh
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            lblAnh = new JLabel();
            lblAnh.setPreferredSize(new Dimension(240, 160));
            lblAnh.setBorder(BorderFactory.createLineBorder(MAU_XAM, 2));
            lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblAnh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            datAnhChoLabel(lblAnh, imagePath, 240, 160);
            form.add(lblAnh, gbc);

            lblAnh.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser("img/");
                    chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (*.jpg, *.png)", "jpg", "png"));
                    if (chooser.showOpenDialog(DialogThemMonAn.this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        imagePath = file.getAbsolutePath();
                        datAnhChoLabel(lblAnh, imagePath, 240, 160);
                    }
                }
            });

            gbc.gridwidth = 1; gbc.gridy++;

            // Tên món
            gbc.gridx = 0;
            JLabel lblTen = new JLabel("Tên món:*");
            lblTen.setFont(FONT_TXT);
            form.add(lblTen, gbc);
            gbc.gridx = 1;
            txtTen = new JTextField(28);
            txtTen.setFont(FONT_TXT);
            form.add(txtTen, gbc);

            // Loại món
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblLoai = new JLabel("Loại món:*");
            lblLoai.setFont(FONT_TXT);
            form.add(lblLoai, gbc);
            gbc.gridx = 1;
            cmbLoai = new JComboBox<>();
            khoiTaoComboLoaiDialog(cmbLoai);
            cmbLoai.setFont(FONT_TXT);
            form.add(cmbLoai, gbc);

            // Giá
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblGia = new JLabel("Giá:*");
            lblGia.setFont(FONT_TXT);
            form.add(lblGia, gbc);
            gbc.gridx = 1;
            txtGia = new JTextField(28);
            txtGia.setFont(FONT_TXT);
            form.add(txtGia, gbc);

            // Trạng thái
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblTrangThai = new JLabel("Trạng thái:");
            lblTrangThai.setFont(FONT_TXT);
            form.add(lblTrangThai, gbc);
            gbc.gridx = 1;
            cmbTrangThai = new JComboBox<>(new String[]{"Còn bán", "Ngừng bán"});
            cmbTrangThai.setSelectedIndex(0);
            cmbTrangThai.setFont(FONT_TXT);
            form.add(cmbTrangThai, gbc);

            // Mô tả
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblMoTa = new JLabel("Mô tả:*");
            lblMoTa.setFont(FONT_TXT);
            form.add(lblMoTa, gbc);
            gbc.gridx = 1;
            txtMoTa = new JTextArea(6, 28);
            txtMoTa.setLineWrap(true);
            txtMoTa.setWrapStyleWord(true);
            txtMoTa.setFont(FONT_TXT);
            form.add(new JScrollPane(txtMoTa), gbc);

            add(form, BorderLayout.CENTER);

         // Nút
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
            btnPanel.setBackground(MAU_TRANG);

            // Nút Lưu (xanh lá, chữ trắng)
            JButton btnThem = new JButton("Lưu");
            btnThem.setFont(FONT_BUTTON);
            btnThem.setBackground(MAU_XANH_LA); 
            btnThem.setForeground(MAU_TRANG);
            btnThem.setPreferredSize(new Dimension(100, 35));
            btnThem.setFocusPainted(false);
            btnThem.setOpaque(true);          
            btnThem.setBorderPainted(false);  // Không để viền nút che màu

            // Nút Hủy (xám, chữ trắng)
            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(FONT_BUTTON);
            btnHuy.setBackground(MAU_XAM); 
            btnHuy.setForeground(Color.BLACK);
            btnHuy.setPreferredSize(new Dimension(100, 35));
            btnHuy.setFocusPainted(false);
            btnHuy.setOpaque(true);
            btnHuy.setBorderPainted(false);

            btnPanel.add(btnHuy);
            btnPanel.add(btnThem);
            add(btnPanel, BorderLayout.SOUTH);

            btnHuy.addActionListener(e -> dispose());
            btnThem.addActionListener(e -> luuMonAnVaoDB());

        }

        private void khoiTaoComboLoaiDialog(JComboBox<String> combo) {
            List<LoaiMon> listLoai = loaiMonDAO.getAllLoaiMon();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (LoaiMon lm : listLoai) {
                model.addElement(lm.getTenLoai());
            }
            combo.setModel(model);
        }

        private void luuMonAnVaoDB() {
            try {
                String ten = txtTen.getText().trim();
                String giaStr = txtGia.getText().trim();
                String moTa = txtMoTa.getText().trim();
                String tenLoai = (String) cmbLoai.getSelectedItem();

                // Validate
                if (ten.isEmpty() || giaStr.isEmpty() || moTa.isEmpty() || tenLoai == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường có dấu *!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double donGia;
                try {
                    donGia = Double.parseDouble(giaStr.replaceAll("[^0-9.]", ""));
                    if (donGia <= 0) {
                        JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LoaiMon loaiMon = null;
                for (LoaiMon lm : loaiMonDAO.getAllLoaiMon()) {
                    if (lm.getTenLoai().equals(tenLoai)) {
                        loaiMon = lm;
                        break;
                    }
                }
                if (loaiMon == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy loại món: " + tenLoai, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MonAn mon = new MonAn(null, ten, imagePath, loaiMon, donGia, "Còn bán".equals(cmbTrangThai.getSelectedItem()), moTa);
                if (monAnDAO.themMonAn(mon)) {
                    JOptionPane.showMessageDialog(this, "Thêm món thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDataFromDB();
                    capNhatLuoiMon();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm món thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Dialog cập nhật món ăn
    class DialogCapNhatMonAn extends JDialog {
        private MonAn monHienTai;
        private JTextField txtMa, txtTen, txtGia;
        private JComboBox<String> cmbLoai, cmbTrangThai;
        private JTextArea txtMoTa;
        private JLabel lblAnh;
        private String imagePath;

        public DialogCapNhatMonAn(JFrame parent, MonAn mon) {
            super(parent, "Cập nhật món ăn", true);
            this.monHienTai = mon;
            this.imagePath = mon.getAnhMon();
            initDialog();
        }

        private void initDialog() {
            setSize(450, 580);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout(10, 10));

            // Tiêu đề
            JLabel lblTieuDe = new JLabel("CẬP NHẬT MÓN: " + monHienTai.getTenMon(), SwingConstants.CENTER);
            lblTieuDe.setOpaque(true);
            lblTieuDe.setBackground(MAU_DO_RUOU);
            lblTieuDe.setForeground(MAU_TRANG);
            lblTieuDe.setFont(FONT_TIEU_DE);
            lblTieuDe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            add(lblTieuDe, BorderLayout.NORTH);

            // Form
            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            form.setBackground(MAU_TRANG);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            // Ảnh
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            lblAnh = new JLabel();
            lblAnh.setPreferredSize(new Dimension(240, 160));
            lblAnh.setBorder(BorderFactory.createLineBorder(MAU_XAM, 2));
            lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblAnh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            datAnhChoLabel(lblAnh, imagePath, 240, 160);
            form.add(lblAnh, gbc);

            lblAnh.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser("img/");
                    chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (*.jpg, *.png)", "jpg", "png"));
                    if (chooser.showOpenDialog(DialogCapNhatMonAn.this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        imagePath = file.getAbsolutePath();
                        datAnhChoLabel(lblAnh, imagePath, 240, 160);
                    }
                }
            });

            gbc.gridwidth = 1; gbc.gridy++;

            // Mã món
            gbc.gridx = 0;
            JLabel lblMa = new JLabel("Mã món:");
            lblMa.setFont(FONT_TXT);
            form.add(lblMa, gbc);
            gbc.gridx = 1;
            txtMa = new JTextField(monHienTai.getMaMon(), 28);
            txtMa.setEditable(false);
            txtMa.setBackground(new Color(240, 240, 240));
            txtMa.setFont(FONT_TXT);
            form.add(txtMa, gbc);

            // Tên món
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblTen = new JLabel("Tên món:*");
            lblTen.setFont(FONT_TXT);
            form.add(lblTen, gbc);
            gbc.gridx = 1;
            txtTen = new JTextField(monHienTai.getTenMon(), 28);
            txtTen.setFont(FONT_TXT);
            form.add(txtTen, gbc);

            // Loại món
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblLoai = new JLabel("Loại món:*");
            lblLoai.setFont(FONT_TXT);
            form.add(lblLoai, gbc);
            gbc.gridx = 1;
            cmbLoai = new JComboBox<>();
            khoiTaoComboLoaiDialog(cmbLoai);
            cmbLoai.setSelectedItem(monHienTai.getLoaiMon().getTenLoai());
            cmbLoai.setFont(FONT_TXT);
            form.add(cmbLoai, gbc);

            // Giá
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblGia = new JLabel("Giá:*");
            lblGia.setFont(FONT_TXT);
            form.add(lblGia, gbc);
            gbc.gridx = 1;
            txtGia = new JTextField(String.format("%.0f", monHienTai.getDonGia()), 28);
            txtGia.setFont(FONT_TXT);
            form.add(txtGia, gbc);

            // Trạng thái
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblTrangThai = new JLabel("Trạng thái:");
            lblTrangThai.setFont(FONT_TXT);
            form.add(lblTrangThai, gbc);
            gbc.gridx = 1;
            cmbTrangThai = new JComboBox<>(new String[]{"Còn bán", "Ngừng bán"});
            cmbTrangThai.setSelectedItem(monHienTai.isTrangThai() ? "Còn bán" : "Ngừng bán");
            cmbTrangThai.setFont(FONT_TXT);
            form.add(cmbTrangThai, gbc);

            // Mô tả
            gbc.gridx = 0; gbc.gridy++;
            JLabel lblMoTa = new JLabel("Mô tả:*");
            lblMoTa.setFont(FONT_TXT);
            form.add(lblMoTa, gbc);
            gbc.gridx = 1;
            txtMoTa = new JTextArea(monHienTai.getMoTa(), 6, 28);
            txtMoTa.setLineWrap(true);
            txtMoTa.setWrapStyleWord(true);
            txtMoTa.setFont(FONT_TXT);
            form.add(new JScrollPane(txtMoTa), gbc);

            add(form, BorderLayout.CENTER);

         // --- Nút ---
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
            btnPanel.setBackground(MAU_TRANG);

            // Nút cập nhật: xanh dương, chữ trắng
            JButton btnCapNhat = new JButton("Cập nhật");
            btnCapNhat.setFont(FONT_BUTTON);
            btnCapNhat.setBackground(MAU_XANH_DUONG); 
            btnCapNhat.setForeground(Color.WHITE);             
            btnCapNhat.setPreferredSize(new Dimension(100, 35));
            btnCapNhat.setFocusPainted(false);
            btnCapNhat.setOpaque(true);          // bắt buộc để hiển thị màu
            btnCapNhat.setBorderPainted(false);

            // Nút hủy: xám nhạt, chữ đen
            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(FONT_BUTTON);
            btnHuy.setBackground(MAU_XAM); 
            btnHuy.setForeground(Color.BLACK);      
            btnHuy.setPreferredSize(new Dimension(100, 35));
            btnHuy.setFocusPainted(false);
            btnHuy.setOpaque(true);              // bắt buộc để hiển thị màu
            btnHuy.setBorderPainted(false);

            btnPanel.add(btnHuy);
            btnPanel.add(btnCapNhat);
            add(btnPanel, BorderLayout.SOUTH);

            btnHuy.addActionListener(e -> dispose());
            btnCapNhat.addActionListener(e -> capNhatMonVaoDB());


        }

        private void khoiTaoComboLoaiDialog(JComboBox<String> combo) {
            List<LoaiMon> listLoai = loaiMonDAO.getAllLoaiMon();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (LoaiMon lm : listLoai) {
                model.addElement(lm.getTenLoai());
            }
            combo.setModel(model);
        }

        private void capNhatMonVaoDB() {
            try {
                String ten = txtTen.getText().trim();
                String giaStr = txtGia.getText().trim();
                String moTa = txtMoTa.getText().trim();
                String loaiTen = (String) cmbLoai.getSelectedItem();
                String trangThaiStr = (String) cmbTrangThai.getSelectedItem();

                if (ten.isEmpty() || giaStr.isEmpty() || moTa.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường có dấu *!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double donGia;
                try {
                    donGia = Double.parseDouble(giaStr.replaceAll("[^0-9.]", ""));
                    if (donGia <= 0) {
                        JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LoaiMon loai = null;
                for (LoaiMon lm : loaiMonDAO.getAllLoaiMon()) {
                    if (lm.getTenLoai().equals(loaiTen)) {
                        loai = lm;
                        break;
                    }
                }
                if (loai == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy loại món!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                monHienTai.setTenMon(ten);
                monHienTai.setAnhMon(imagePath);
                monHienTai.setDonGia(donGia);
                monHienTai.setTrangThai("Còn bán".equals(trangThaiStr));
                monHienTai.setMoTa(moTa);
                monHienTai.setLoaiMon(loai);

                if (monAnDAO.capNhatMonAn(monHienTai)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDataFromDB();
                    capNhatLuoiMon();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Đặt ảnh an toàn
    private void datAnhChoLabel(JLabel lbl, String path, int w, int h) {
        try {
            if (path != null && !path.isEmpty()) {
                File file = new File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(path)
                            .getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
                    lbl.setIcon(icon);
                    return;
                }
            }
            lbl.setIcon(null);
            lbl.setText("🍽️");
            lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        } catch (Exception e) {
            lbl.setText("🍽️");
            lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        }
    }

    public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        SwingUtilities.invokeLater(() -> {
            try {
                FrmThucDon frame = new FrmThucDon();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khởi tạo: " + e.getMessage());
            }
        });
    }
}
