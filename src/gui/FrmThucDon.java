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

public class FrmThucDon extends JFrame {
    // Màu sắc (đồng bộ với FrmDatMon)
    private static final Color MAU_DO_RUOU = new Color(169, 55, 68);
    private static final Color MAU_HONG = new Color(241, 200, 204);
    private static final Color MAU_XANH_LA = new Color(102, 210, 74);
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
    private JPanel pnlLuoiMon, pnlLuoiMonChinh, pnlLuoiMonTrangMieng, pnlLuoiMonNuocUong;
    private JTextField txtTimKiem;
    private JComboBox<String> cmbLocLoai;
    private JTabbedPane tabLoai;
    private MonAn monDangChon = null;
    private JPanel khungDangChon = null;

    public FrmThucDon() throws SQLException {
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

        // Load dữ liệu
        khoiTaoComboBoxLoai();
        loadDataFromDB();
        capNhatLuoiMon();

        setVisible(true);
    }

    // Thanh bên trái
    private void taoThanhBen() {
        JPanel pnlBenTrai = new JPanel(new BorderLayout());
        pnlBenTrai.setPreferredSize(new Dimension(220, 0));
        pnlBenTrai.setBackground(MAU_TRANG);

        // Thời gian
        JPanel pnlThoiGian = new JPanel(new GridLayout(2, 1));
        pnlThoiGian.setBackground(MAU_TRANG);
        pnlThoiGian.setBorder(new MatteBorder(0, 0, 1, 0, MAU_XAM));

        JLabel lblGio = new JLabel();
        lblGio.setFont(new Font("Consolas", Font.BOLD, 24));
        lblGio.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNgay = new JLabel();
        lblNgay.setFont(new Font("Times New Roman", Font.PLAIN, 18));
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

        // Danh mục
        JPanel pnlDanhMuc = new JPanel(new GridLayout(5, 1, 0, 0));
        pnlDanhMuc.setBackground(MAU_TRANG);
        pnlDanhMuc.setBorder(new TitledBorder(null, "DANH MỤC", TitledBorder.CENTER,
                TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 18), MAU_DO_RUOU));

        String[] mucs = {"Thực Đơn", "Hóa Đơn", "Nhân Viên", "Khách Hàng", "Khuyến Mãi"};
        for (String muc : mucs) {
            JButton btn = new JButton(muc);
            btn.setFocusPainted(false);
            btn.setFont(FONT_BUTTON);
            btn.setBackground(muc.equals("Thực Đơn") ? MAU_DO_RUOU : MAU_TRANG);
            btn.setForeground(muc.equals("Thực Đơn") ? MAU_TRANG : Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(MAU_XAM));
            pnlDanhMuc.add(btn);
        }

        pnlBenTrai.add(pnlDanhMuc, BorderLayout.CENTER);
        add(pnlBenTrai, BorderLayout.WEST);
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

        // Tạo panels
        pnlLuoiMonChinh = taoPanelLuoiMon();
        pnlLuoiMonTrangMieng = taoPanelLuoiMon();
        pnlLuoiMonNuocUong = taoPanelLuoiMon();
        pnlLuoiMon = taoPanelLuoiMon();

        // Gói vào JScrollPane
        JScrollPane scrChinh = taoScroll(pnlLuoiMonChinh);
        JScrollPane scrTrang = taoScroll(pnlLuoiMonTrangMieng);
        JScrollPane scrNuoc = taoScroll(pnlLuoiMonNuocUong);
        JScrollPane scrAll = taoScroll(pnlLuoiMon);

        tabLoai.addTab("Món chính", scrChinh);
        tabLoai.addTab("Tráng miệng", scrTrang);
        tabLoai.addTab("Nước uống", scrNuoc);
        tabLoai.addTab("Tất cả", scrAll);

        // Tab ↔ ComboBox sync
        tabLoai.addChangeListener(e -> {
            int idx = tabLoai.getSelectedIndex();
            switch (idx) {
                case 0: cmbLocLoai.setSelectedItem("Món chính"); break;
                case 1: cmbLocLoai.setSelectedItem("Tráng miệng"); break;
                case 2: cmbLocLoai.setSelectedItem("Nước uống"); break;
                default: cmbLocLoai.setSelectedItem("Tất cả"); break;
            }
            capNhatLuoiMon();
        });

        pnlChinh.add(tabLoai, BorderLayout.CENTER);
        add(pnlChinh, BorderLayout.CENTER);

        // Nút chức năng
        taoNutChucNang();
    }

    // Nút chức năng
    private void taoNutChucNang() {
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlBottom.setBackground(MAU_TRANG);

        JButton btnThem = new JButton("Thêm");
        btnThem.setFont(FONT_BUTTON);
        btnThem.setBackground(MAU_XANH_LA);
        btnThem.setForeground(MAU_TRANG);
        btnThem.setPreferredSize(new Dimension(100, 35));
        btnThem.setFocusPainted(false);

        JButton btnCapNhat = new JButton("Cập nhật");
        btnCapNhat.setFont(FONT_BUTTON);
        btnCapNhat.setBackground(MAU_XANH_LA);
        btnCapNhat.setForeground(MAU_TRANG);
        btnCapNhat.setPreferredSize(new Dimension(100, 35));
        btnCapNhat.setFocusPainted(false);

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setFont(FONT_BUTTON);
        btnXoa.setBackground(MAU_DO_RUOU);
        btnXoa.setForeground(MAU_TRANG);
        btnXoa.setPreferredSize(new Dimension(100, 35));
        btnXoa.setFocusPainted(false);

        btnThem.addActionListener(e -> new DialogThemMonAn(this).setVisible(true));
        btnCapNhat.addActionListener(e -> {
            if (monDangChon == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món để cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new DialogCapNhatMonAn(this, monDangChon).setVisible(true);
        });
        btnXoa.addActionListener(e -> xoaMonAn());

        pnlBottom.add(btnThem);
        pnlBottom.add(btnCapNhat);
        pnlBottom.add(btnXoa);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // Load dữ liệu từ DB
    private void loadDataFromDB() {
        danhSachMon = monAnDAO.getAllMonAn();
		if (danhSachMon.isEmpty()) {
		    JOptionPane.showMessageDialog(this, "Cơ sở dữ liệu rỗng! Vui lòng chạy script SQL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
    }

    // Khởi tạo combobox loại món
    private void khoiTaoComboBoxLoai() {
        List<LoaiMon> listLoai = loaiMonDAO.getAllLoaiMon();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		model.addElement("Tất cả");
		for (LoaiMon lm : listLoai) {
		    model.addElement(lm.getTenLoai());
		}
		cmbLocLoai.setModel(model);
    }

    // Cập nhật lưới món
    private void capNhatLuoiMon() {
        pnlLuoiMon.removeAll();
        pnlLuoiMonChinh.removeAll();
        pnlLuoiMonTrangMieng.removeAll();
        pnlLuoiMonNuocUong.removeAll();

        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        String loaiChon = (String) cmbLocLoai.getSelectedItem();

        for (MonAn mon : danhSachMon) {
            if (!mon.isTrangThai()) continue;

            boolean matchKeyword = tuKhoa.isEmpty() || mon.getTenMon().toLowerCase().contains(tuKhoa);
            boolean matchLoai = "Tất cả".equals(loaiChon) || mon.getLoaiMon().getTenLoai().equals(loaiChon);

            if (matchKeyword && matchLoai) {
                JPanel card = taoCardMon(mon);
                pnlLuoiMon.add(card);
                String tenLoai = mon.getLoaiMon().getTenLoai();
                switch (tenLoai) {
                    case "Món chính": pnlLuoiMonChinh.add(card); break;
                    case "Tráng miệng": pnlLuoiMonTrangMieng.add(card); break;
                    case "Nước uống": pnlLuoiMonNuocUong.add(card); break;
                }
            }
        }

        pnlLuoiMon.revalidate();
        pnlLuoiMon.repaint();
        pnlLuoiMonChinh.revalidate();
        pnlLuoiMonChinh.repaint();
        pnlLuoiMonTrangMieng.revalidate();
        pnlLuoiMonTrangMieng.repaint();
        pnlLuoiMonNuocUong.revalidate();
        pnlLuoiMonNuocUong.repaint();
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

        // Hover effect
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

    // Xóa món ăn
    private void xoaMonAn() {
        if (monDangChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ch = JOptionPane.showConfirmDialog(this,
                String.format("Ẩn '%s' khỏi thực đơn?", monDangChon.getTenMon()),
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
                    JOptionPane.showMessageDialog(this, "Ẩn món thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

            JButton btnThem = new JButton("Lưu");
            btnThem.setFont(FONT_BUTTON);
            btnThem.setBackground(MAU_XANH_LA);
            btnThem.setForeground(MAU_TRANG);
            btnThem.setPreferredSize(new Dimension(100, 35));
            btnThem.setFocusPainted(false);

            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(FONT_BUTTON);
            btnHuy.setBackground(MAU_XAM);
            btnHuy.setForeground(MAU_TRANG);
            btnHuy.setPreferredSize(new Dimension(100, 35));
            btnHuy.setFocusPainted(false);

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

            // Nút
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
            btnPanel.setBackground(MAU_TRANG);

            JButton btnCapNhat = new JButton("Cập nhật");
            btnCapNhat.setFont(FONT_BUTTON);
            btnCapNhat.setBackground(MAU_XANH_LA);
            btnCapNhat.setForeground(MAU_TRANG);
            btnCapNhat.setPreferredSize(new Dimension(100, 35));
            btnCapNhat.setFocusPainted(false);

            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(FONT_BUTTON);
            btnHuy.setBackground(MAU_XAM);
            btnHuy.setForeground(MAU_TRANG);
            btnHuy.setPreferredSize(new Dimension(100, 35));
            btnHuy.setFocusPainted(false);

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
        SwingUtilities.invokeLater(() -> {
            try {
                new FrmThucDon().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}