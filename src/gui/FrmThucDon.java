package gui;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import connectSQL.ConnectSQL;
import dao.LoaiMon_DAO;
import dao.MonAn_DAO;
import entity.LoaiMon;
import entity.MonAn;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class FrmThucDon extends JFrame {

    private java.util.List<MonAn> danhSachMon = new ArrayList<>();
    private JPanel pnlLuoiMon;                // dùng cho tab "Tất cả"
    private JPanel pnlLuoiMonChinh;
    private JPanel pnlLuoiMonTrangMieng;
    private JPanel pnlLuoiMonNuocUong;

    private JTextField txtTimKiem;
    private JComboBox<String> cmbLocLoai;
    private JTabbedPane tabLoai;

    private MonAn monDangChon = null;
    private JPanel khungDangChon = null;

    // ===== MÀU =====
    private final Color MAU_DO_RUOU = new Color(169, 55, 68);
    private final Color MAU_HONG_DAT = new Color(241, 200, 204);  // hồng đất nhạt
    private final Color MAU_XAM = new Color(217, 217, 217);
    private final Color MAU_XANH_LA = new Color(102, 210, 74);
    private final Color MAU_VANG = new Color(210, 201, 74);

    // ===== FONT =====
    private final Font FONT_BTN = new Font("Times New Roman", Font.BOLD, 22);
    private final Font FONT_TXT = new Font("Times New Roman", Font.PLAIN, 20);
	public Object monSelectedListener;

    public FrmThucDon() {
        setTitle("THỰC ĐƠN");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        setJMenuBar(ThanhTacVu.getInstance().getJMenuBar());
        ThanhTacVu customMenu = ThanhTacVu.getInstance();
        add(customMenu.getBottomBar(), BorderLayout.SOUTH);

        taoThanhBen();
        taoTieuDe();
        taoNoiDungChinh();

        napDuLieuMau();
        capNhatLuoiMon();

        setVisible(true);
    }

    // ===== THANH BÊN TRÁI =====
    private void taoThanhBen() {
        JPanel pnlBenTrai = new JPanel(new BorderLayout());
        pnlBenTrai.setPreferredSize(new Dimension(220, 0));
        pnlBenTrai.setBackground(Color.WHITE);

        // Thời gian
        JPanel pnlThoiGian = new JPanel(new GridLayout(2, 1));
        pnlThoiGian.setBackground(Color.WHITE);
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
        pnlDanhMuc.setBackground(Color.WHITE);
        pnlDanhMuc.setBorder(new TitledBorder(null, "DANH MỤC", TitledBorder.CENTER,
                TitledBorder.TOP, new Font("Times New Roman", Font.BOLD, 18), MAU_DO_RUOU));

        String[] mucs = {"Thực Đơn", "Hóa Đơn", "Nhân Viên", "Khách Hàng", "Khuyến Mãi"};
        for (String muc : mucs) {
            JButton btn = new JButton(muc);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
            btn.setBackground(muc.equals("Thực Đơn") ? MAU_DO_RUOU : Color.WHITE);
            btn.setForeground(muc.equals("Thực Đơn") ? Color.WHITE : Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(MAU_XAM));
            pnlDanhMuc.add(btn);
        }

        pnlBenTrai.add(pnlDanhMuc, BorderLayout.CENTER);
        add(pnlBenTrai, BorderLayout.WEST);
    }

    // ===== TIÊU ĐỀ =====
    private void taoTieuDe() {
        JPanel pnlTieuDe = new JPanel(new BorderLayout());
        pnlTieuDe.setBackground(MAU_DO_RUOU);
        pnlTieuDe.setPreferredSize(new Dimension(0, 50));

        JLabel lblTieuDe = new JLabel("THỰC ĐƠN", SwingConstants.CENTER);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 26));

        pnlTieuDe.add(lblTieuDe, BorderLayout.CENTER);
        add(pnlTieuDe, BorderLayout.NORTH);
    }

    // ===== NỘI DUNG CHÍNH (CENTER) =====
    private void taoNoiDungChinh() {
        JPanel pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlChinh.setBackground(Color.WHITE);

        // -------- Thanh tìm kiếm + lọc loại --------
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAU_XAM, 1),
                "Tìm món", 0, 0,
                new Font("Times New Roman", Font.BOLD, 20),
                MAU_DO_RUOU));

        JLabel lblTenMon = new JLabel("Tên món:");
        lblTenMon.setFont(FONT_TXT);
        txtTimKiem = new JTextField(24);
        txtTimKiem.setFont(FONT_TXT);

        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setFont(FONT_TXT);
        cmbLocLoai = new JComboBox<>(new String[]{"Tất cả", "Món chính", "Tráng miệng", "Nước uống"});
        cmbLocLoai.setFont(FONT_TXT);

        pnlTimKiem.add(lblTenMon);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(Box.createHorizontalStrut(10));
        pnlTimKiem.add(lblLoai);
        pnlTimKiem.add(cmbLocLoai);

        // Document listener để tìm kiếm theo tên
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void removeUpdate(DocumentEvent e) { capNhatLuoiMon(); }
            public void changedUpdate(DocumentEvent e) { capNhatLuoiMon(); }
        });

        // Khi đổi combobox => đổi tab tương ứng
        cmbLocLoai.addActionListener(e -> {
            String sel = (String) cmbLocLoai.getSelectedItem();
            if (tabLoai != null) {
                if ("Món chính".equals(sel)) tabLoai.setSelectedIndex(0);
                else if ("Tráng miệng".equals(sel)) tabLoai.setSelectedIndex(1);
                else if ("Nước uống".equals(sel)) tabLoai.setSelectedIndex(2);
                else tabLoai.setSelectedIndex(3); // Tất cả
            }
            capNhatLuoiMon();
        });

        pnlChinh.add(pnlTimKiem, BorderLayout.NORTH);

        // -------- TabbedPane (loại món) --------
        tabLoai = new JTabbedPane();
        tabLoai.setFont(new Font("Times New Roman", Font.BOLD, 16));

     // 👉 Tạo panel chứa card theo lưới cố định 4 cột, luôn bám trái
        pnlLuoiMonChinh = taoPanelLuoiMon();
        pnlLuoiMonTrangMieng = taoPanelLuoiMon();
        pnlLuoiMonNuocUong = taoPanelLuoiMon();
        pnlLuoiMon = taoPanelLuoiMon();

        // Gói mỗi panel vào JScrollPane
        JScrollPane scrChinh = taoScroll(pnlLuoiMonChinh);
        JScrollPane scrTrang = taoScroll(pnlLuoiMonTrangMieng);
        JScrollPane scrNuoc = taoScroll(pnlLuoiMonNuocUong);
        JScrollPane scrAll = taoScroll(pnlLuoiMon);



        tabLoai.addTab("Món chính", scrChinh);
        tabLoai.addTab("Tráng miệng", scrTrang);
        tabLoai.addTab("Nước uống", scrNuoc);
        tabLoai.addTab("Tất cả", scrAll);

        // Khi đổi tab => đồng bộ combobox và cập nhật nếu cần
        tabLoai.addChangeListener(ev -> {
            int idx = tabLoai.getSelectedIndex();
            switch (idx) {
                case 0: cmbLocLoai.setSelectedItem("Món chính"); break;
                case 1: cmbLocLoai.setSelectedItem("Tráng miệng"); break;
                case 2: cmbLocLoai.setSelectedItem("Nước uống"); break;
                default: cmbLocLoai.setSelectedItem("Tất cả"); break;
            }
            // capNhatLuoiMon(); // không cần gọi ở đây vì combobox listener đã gọi capNhatLuoiMon
        });

        pnlChinh.add(tabLoai, BorderLayout.CENTER);
        add(pnlChinh, BorderLayout.CENTER);

        // ======= NÚT DƯỚI cùng (SOUTH) =======
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnlBottom.setBackground(Color.WHITE);

        JButton btnThem = new JButton("Thêm");
        btnThem.setFont(FONT_BTN);
        btnThem.setBackground(MAU_XANH_LA);
        btnThem.setForeground(Color.BLACK);
        btnThem.setPreferredSize(new Dimension(140, 40));
        btnThem.setFocusPainted(false);

        JButton btnCapNhat = new JButton("Cập nhật");
        btnCapNhat.setFont(FONT_BTN);
        btnCapNhat.setBackground(MAU_VANG);
        btnCapNhat.setPreferredSize(new Dimension(140, 40));
        btnCapNhat.setFocusPainted(false);

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setFont(FONT_BTN);
        btnXoa.setBackground(MAU_DO_RUOU);
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(140, 40));
        btnXoa.setFocusPainted(false);

        // sự kiện cơ bản cho nút (bạn có thể mở dialog thêm, hoặc implement update/delete)
        btnThem.addActionListener(e -> new DialogThemMonAn(this).setVisible(true));
        btnCapNhat.addActionListener(e -> {
            if (monDangChon == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món để cập nhật!");
                return;
            }
            new DialogCapNhatMonAn(this, monDangChon).setVisible(true);
        });

        btnXoa.addActionListener(e -> {
            if (monDangChon == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xóa!");
                return;
            }
            int ch = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa món " + monDangChon.getTenMon() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION) {
                danhSachMon.remove(monDangChon);
                monDangChon = null;
                khungDangChon = null;
                capNhatLuoiMon();
            }
        });

        pnlBottom.add(btnThem);
        pnlBottom.add(btnCapNhat);
        pnlBottom.add(btnXoa);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    private JPanel taoCardMon(MonAn mon) {
        JPanel khung = new JPanel();
        khung.setLayout(new BorderLayout(5, 5));
        khung.setBackground(MAU_HONG_DAT);
        khung.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(MAU_DO_RUOU, 1, true),
                new EmptyBorder(6, 6, 6, 6)
        ));

        // 👉 Cố định kích thước hợp lý hơn (rõ 1 card, không quá to)
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(200, 260)); 
        card.setMaximumSize(new Dimension(200, 260));
        card.setMinimumSize(new Dimension(200, 260));


        // Ảnh
        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setPreferredSize(new Dimension(180, 120));

        String duongDan = (mon.getAnhMon() != null && new File(mon.getAnhMon()).exists())
                ? mon.getAnhMon() : "img/placeholder.png";
        ImageIcon icon = new ImageIcon(new ImageIcon(duongDan)
                .getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH));
        lblAnh.setIcon(icon);
        khung.add(lblAnh, BorderLayout.NORTH);

        // Thông tin
        JPanel pnlTT = new JPanel();
        pnlTT.setLayout(new BoxLayout(pnlTT, BoxLayout.Y_AXIS));
        pnlTT.setBackground(MAU_HONG_DAT);
        pnlTT.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel lblTen = new JLabel(mon.getTenMon());
        lblTen.setFont(new Font("Times New Roman", Font.BOLD, 15));

        JLabel lblTrangThai = new JLabel("Trạng thái: " + (mon.isTrangThai() ? "Còn bán" : "Hết món"));
        lblTrangThai.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        lblTrangThai.setForeground(mon.isTrangThai() ? new Color(0, 120, 0) : Color.RED);

        JLabel lblMoTa = new JLabel("<html><b>Mô tả:</b> " + mon.getMoTa() + "</html>");
        lblMoTa.setFont(new Font("Times New Roman", Font.ITALIC, 12));

        JLabel lblGia = new JLabel("Giá: " + String.format("%,.0f đ", mon.getDonGia()));
        lblGia.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblGia.setForeground(new Color(120, 40, 40));

        pnlTT.add(lblTen);
        pnlTT.add(Box.createVerticalStrut(3));
        pnlTT.add(lblTrangThai);
        pnlTT.add(Box.createVerticalStrut(3));
        pnlTT.add(lblMoTa);
        pnlTT.add(Box.createVerticalStrut(6));
        pnlTT.add(lblGia);

        khung.add(pnlTT, BorderLayout.CENTER);

        // Hiệu ứng hover & chọn
        khung.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (khungDangChon != null)
                    khungDangChon.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(MAU_DO_RUOU, 1, true),
                            new EmptyBorder(6, 6, 6, 6)
                    ));
                khungDangChon = khung;
                khungDangChon.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(Color.BLACK, 2, true),
                        new EmptyBorder(6, 6, 6, 6)
                ));
                monDangChon = mon;
            }

            public void mouseEntered(MouseEvent e) {
                khung.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(Color.BLACK, 2, true),
                        new EmptyBorder(6, 6, 6, 6)
                ));
            }

            public void mouseExited(MouseEvent e) {
                if (khung != khungDangChon)
                    khung.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(MAU_DO_RUOU, 1, true),
                            new EmptyBorder(6, 6, 6, 6)
                    ));
            }
        });

        return khung;
    }



    // ===== DỮ LIỆU GIẢ =====
    private void napDuLieuMau() {
        LoaiMon monChinh = new LoaiMon("LM001", "Món chính");
        LoaiMon trangMieng = new LoaiMon("LM002", "Tráng miệng");
        LoaiMon nuocUong = new LoaiMon("LM003", "Nước uống");

        danhSachMon.add(new MonAn("MON0001", "Soup Cua", "img/soupcua.jpg", monChinh, 150000, true, "Thanh ngọt, bổ dưỡng"));
        danhSachMon.add(new MonAn("MON0002", "Lẩu Nấm", "img/launam.jpg", monChinh, 500000, true, "Ngọt thanh, đậm đà"));
        danhSachMon.add(new MonAn("MON0003", "Vịt Quay", "img/vitquay.jpg", monChinh, 800000, false, "Da giòn rụm, thơm béo"));
        danhSachMon.add(new MonAn("MON0004", "Cơm Mai Cua", "img/commaicua.jpg", monChinh, 300000, true, "Vị cua đậm, béo nhẹ"));
        danhSachMon.add(new MonAn("MON0005", "Kem Dâu", "img/kemdau.jpg", trangMieng, 50000, true, "Kem dâu tươi mát"));
        danhSachMon.add(new MonAn("MON0006", "Bánh Flan", "img/flan.jpg", trangMieng, 40000, true, "Mềm mịn, ngọt nhẹ"));
        danhSachMon.add(new MonAn("MON0007", "Nước Cam", "img/nuoccam.jpg", nuocUong, 35000, true, "Cam tươi nguyên chất"));
        danhSachMon.add(new MonAn("MON0008", "Cà Phê Sữa", "img/caphe.jpg", nuocUong, 45000, true, "Cà phê thơm đậm đà"));
    }

 // Hàm tạo panel lưới cố định 4 cột
    private JPanel taoPanelLuoiMon() {
        JPanel pnl = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                int rowCount = (int) Math.ceil(getComponentCount() / 4.0);
                return new Dimension(900, rowCount * 280); // mỗi card cao ~260 + margin
            }
        };
        pnl.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnl.setBackground(Color.WHITE);
        return pnl;
    }

    // Hàm tạo JScrollPane cho panel lưới
    private JScrollPane taoScroll(JPanel panel) {
        JScrollPane scr = new JScrollPane(panel);
        scr.setBorder(BorderFactory.createEmptyBorder());
        scr.getVerticalScrollBar().setUnitIncrement(16);
        scr.getViewport().setBackground(Color.WHITE);
        return scr;
    }

    private void capNhatLuoiMon() {
        // clear all
        pnlLuoiMon.removeAll();
        pnlLuoiMonChinh.removeAll();
        pnlLuoiMonTrangMieng.removeAll();
        pnlLuoiMonNuocUong.removeAll();

        String tuKhoa = (txtTimKiem.getText() == null) ? "" : txtTimKiem.getText().trim().toLowerCase();

        int soCotToiDa = 4; // đúng 4 card mỗi hàng
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int colAll = 0, rowAll = 0;
        int colChinh = 0, rowChinh = 0;
        int colTrang = 0, rowTrang = 0;
        int colNuoc = 0, rowNuoc = 0;

        for (MonAn mon : danhSachMon) {
            String ten = mon.getTenMon().toLowerCase();
            if (!tuKhoa.isEmpty() && !ten.contains(tuKhoa))
                continue;

            JPanel card = taoCardMon(mon);
            card.setPreferredSize(new Dimension(200, 260));

            // "Tất cả"
            gbc.gridx = colAll;
            gbc.gridy = rowAll;
            pnlLuoiMon.add(card, gbc);
            colAll++;
            if (colAll >= soCotToiDa) { colAll = 0; rowAll++; }

            // Phân loại
            String loai = mon.getLoaiMon().getTenLoai();
            if ("Món chính".equals(loai)) {
                gbc.gridx = colChinh; gbc.gridy = rowChinh;
                pnlLuoiMonChinh.add(taoCardMon(mon), gbc);
                colChinh++; if (colChinh >= soCotToiDa) { colChinh = 0; rowChinh++; }
            } else if ("Tráng miệng".equals(loai)) {
                gbc.gridx = colTrang; gbc.gridy = rowTrang;
                pnlLuoiMonTrangMieng.add(taoCardMon(mon), gbc);
                colTrang++; if (colTrang >= soCotToiDa) { colTrang = 0; rowTrang++; }
            } else if ("Nước uống".equals(loai)) {
                gbc.gridx = colNuoc; gbc.gridy = rowNuoc;
                pnlLuoiMonNuocUong.add(taoCardMon(mon), gbc);
                colNuoc++; if (colNuoc >= soCotToiDa) { colNuoc = 0; rowNuoc++; }
            }
        }

        pnlLuoiMon.revalidate(); pnlLuoiMon.repaint();
        pnlLuoiMonChinh.revalidate(); pnlLuoiMonChinh.repaint();
        pnlLuoiMonTrangMieng.revalidate(); pnlLuoiMonTrangMieng.repaint();
        pnlLuoiMonNuocUong.revalidate(); pnlLuoiMonNuocUong.repaint();
    }
    
    private JPanel taoPanelLuoi() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        return pnl;
    }



    private void datAnhChoLabel(JLabel lbl, String path, int w, int h) {
        // Đặt ảnh cho label, scale theo kích thước
        ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        lbl.setIcon(icon);
    }

    // ===== FORM THÊM MÓN ĂN =====
    class DialogThemMonAn extends JDialog {
        private JTextField txtTen, txtGia;
        private JComboBox<String> cmbLoai, cmbTrangThai;
        private JTextArea txtMoTa;
        private JLabel lblAnh;
        private String imagePath = "img/placeholder.png";

        public DialogThemMonAn(JFrame parent) {
            super(parent, "Thêm món ăn", true);
            setSize(450, 580);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // Tiêu đề
            JLabel lblTieuDe = new JLabel("THÊM MÓN ĂN", SwingConstants.CENTER);
            lblTieuDe.setOpaque(true);
            lblTieuDe.setBackground(new Color(169,55,68));
            lblTieuDe.setForeground(Color.WHITE);
            lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 22));
            lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            add(lblTieuDe, BorderLayout.NORTH);

            // Khung form chính
            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Khung ảnh
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            lblAnh = new JLabel();
            lblAnh.setPreferredSize(new Dimension(200, 140));
            lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblAnh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            datAnhChoLabel(lblAnh, imagePath, 200, 140);
            form.add(lblAnh, gbc);
            lblAnh.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser("img/");
                    chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (*.jpg, *.png)", "jpg", "png"));
                    if (chooser.showOpenDialog(DialogThemMonAn.this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        imagePath = file.getAbsolutePath();
                        lblAnh.setIcon(new ImageIcon(new ImageIcon(imagePath)
                            .getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH)));
                    }
                }
            });

            gbc.gridwidth = 1; gbc.gridy++;
            // Tên món
            gbc.gridx = 0; form.add(new JLabel("Tên món:"), gbc);
            gbc.gridx = 1; txtTen = new JTextField(20); form.add(txtTen, gbc);

            // Loại món
            gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Loại món:"), gbc);
            gbc.gridx = 1; cmbLoai = new JComboBox<>(new String[]{"Món chính", "Tráng miệng", "Nước uống"});
            form.add(cmbLoai, gbc);

            // Giá
            gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Giá:"), gbc);
            gbc.gridx = 1; txtGia = new JTextField(); form.add(txtGia, gbc);

            // Trạng thái
            gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Trạng thái:"), gbc);
            gbc.gridx = 1; cmbTrangThai = new JComboBox<>(new String[]{"Còn bán", "Ngừng bán"});
            form.add(cmbTrangThai, gbc);

            // Mô tả
            gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Mô tả:"), gbc);
            gbc.gridx = 1; txtMoTa = new JTextArea(4, 20);
            txtMoTa.setLineWrap(true); txtMoTa.setWrapStyleWord(true);
            form.add(new JScrollPane(txtMoTa), gbc);

            add(form, BorderLayout.CENTER);

            // Panel nút
            JPanel btnPanel = new JPanel();
            JButton btnLuu = new JButton("Lưu");
            btnLuu.setFont(new Font("Tahoma", Font.BOLD, 18));
            btnLuu.setBackground(new Color(169,55,68));
            btnLuu.setForeground(Color.WHITE);
            btnLuu.setPreferredSize(new Dimension(100, 40));

            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(new Font("Tahoma", Font.BOLD, 18));
            btnHuy.setBackground(Color.GRAY);
            btnHuy.setForeground(Color.WHITE);
            btnHuy.setPreferredSize(new Dimension(100, 40));

            btnPanel.add(btnLuu); btnPanel.add(btnHuy);
            add(btnPanel, BorderLayout.SOUTH);

            btnHuy.addActionListener(e -> dispose());
            btnLuu.addActionListener(e -> {
                try {
                    String ten = txtTen.getText().trim();
                    String loaiTen = (String) cmbLoai.getSelectedItem();
                    String trangThaiStr = (String) cmbTrangThai.getSelectedItem();
                    String giaStr = txtGia.getText().trim();
                    String moTa = txtMoTa.getText().trim();
                    if (ten.isEmpty() || giaStr.isEmpty() || moTa.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
                        return;
                    }
                    double donGia;
                    try { donGia = Double.parseDouble(giaStr.replaceAll("[^0-9]", "")); }
                    catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Giá phải là số!");
                        return;
                    }
                    boolean trangThai = "Còn bán".equals(trangThaiStr);
                    String maTam = "MON" + String.format("%04d", danhSachMon.size() + 1);
                    LoaiMon loai = new LoaiMon("LM" + String.format("%03d", danhSachMon.size() + 1), loaiTen);
                    MonAn mon = new MonAn(maTam, ten, imagePath, loai, donGia, trangThai, moTa);
                    danhSachMon.add(mon);
                    capNhatLuoiMon();
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            });
        }
    }
    class DialogCapNhatMonAn extends JDialog {
        private JTextField txtMa, txtTen, txtGia;
        private JComboBox<String> cmbLoai, cmbTrangThai;
        private JTextArea txtMoTa;
        private JLabel lblAnh;
        private String imagePath;

        private MonAn_DAO monAnDao = new MonAn_DAO();
        private MonAn monHienTai;

        public DialogCapNhatMonAn(JFrame parent, MonAn mon) {
            super(parent, "Cập nhật món ăn", true);
            this.monHienTai = mon;

            setSize(450, 580);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // ===== Tiêu đề =====
            JLabel lblTieuDe = new JLabel("CẬP NHẬT MÓN ĂN", SwingConstants.CENTER);
            lblTieuDe.setOpaque(true);
            lblTieuDe.setBackground(new Color(169,55,68));
            lblTieuDe.setForeground(Color.WHITE);
            lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 22));
            lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            add(lblTieuDe, BorderLayout.NORTH);

            // ===== Khung form chính =====
            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // --- Khung ảnh ---
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            lblAnh = new JLabel();
            lblAnh.setPreferredSize(new Dimension(200, 140));
            lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblAnh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            imagePath = mon.getAnhMon();
            datAnhChoLabel(lblAnh, imagePath, 200, 140);
            form.add(lblAnh, gbc);

            lblAnh.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser("img/");
                    chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (*.jpg, *.png)", "jpg", "png"));
                    if (chooser.showOpenDialog(DialogCapNhatMonAn.this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        imagePath = file.getAbsolutePath();
                        lblAnh.setIcon(new ImageIcon(new ImageIcon(imagePath)
                                .getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH)));
                    }
                }
            });

            gbc.gridwidth = 1; gbc.gridy++;

            // --- Mã món ---
            gbc.gridx = 0;
            form.add(new JLabel("Mã món:"), gbc);
            gbc.gridx = 1;
            txtMa = new JTextField(mon.getMaMon(), 20);
            txtMa.setEditable(false);
            form.add(txtMa, gbc);

            // --- Tên món ---
            gbc.gridx = 0; gbc.gridy++;
            form.add(new JLabel("Tên món:"), gbc);
            gbc.gridx = 1;
            txtTen = new JTextField(mon.getTenMon(), 20);
            form.add(txtTen, gbc);

            // --- Loại món ---
            gbc.gridx = 0; gbc.gridy++;
            form.add(new JLabel("Loại món:"), gbc);
            gbc.gridx = 1;
            cmbLoai = new JComboBox<>(new String[]{"Món chính", "Tráng miệng", "Nước uống"});
            cmbLoai.setSelectedItem(mon.getLoaiMon().getTenLoai());
            form.add(cmbLoai, gbc);

            // --- Giá ---
            gbc.gridx = 0; gbc.gridy++;
            form.add(new JLabel("Giá:"), gbc);
            gbc.gridx = 1;
            txtGia = new JTextField(String.valueOf(mon.getDonGia()), 20);
            form.add(txtGia, gbc);

            // --- Trạng thái ---
            gbc.gridx = 0; gbc.gridy++;
            form.add(new JLabel("Trạng thái:"), gbc);
            gbc.gridx = 1;
            cmbTrangThai = new JComboBox<>(new String[]{"Còn bán", "Ngừng bán"});
            cmbTrangThai.setSelectedItem(mon.isTrangThai() ? "Còn bán" : "Ngừng bán");
            form.add(cmbTrangThai, gbc);

            // --- Mô tả ---
            gbc.gridx = 0; gbc.gridy++;
            form.add(new JLabel("Mô tả:"), gbc);
            gbc.gridx = 1;
            txtMoTa = new JTextArea(mon.getMoTa(), 4, 20);
            txtMoTa.setLineWrap(true);
            txtMoTa.setWrapStyleWord(true);
            form.add(new JScrollPane(txtMoTa), gbc);

            add(form, BorderLayout.CENTER);

            // ===== Panel nút =====
            JPanel btnPanel = new JPanel();
            JButton btnCapNhat = new JButton("Cập nhật");
            btnCapNhat.setFont(new Font("Tahoma", Font.BOLD, 18));
            btnCapNhat.setBackground(new Color(169,55,68));
            btnCapNhat.setForeground(Color.WHITE);
            btnCapNhat.setPreferredSize(new Dimension(100, 40));

            JButton btnHuy = new JButton("Hủy");
            btnHuy.setFont(new Font("Tahoma", Font.BOLD, 18));
            btnHuy.setBackground(Color.GRAY);
            btnHuy.setForeground(Color.WHITE);
            btnHuy.setPreferredSize(new Dimension(100, 40));

            btnPanel.add(btnCapNhat); btnPanel.add(btnHuy);
            add(btnPanel, BorderLayout.SOUTH);

            btnHuy.addActionListener(e -> dispose());

            btnCapNhat.addActionListener(e -> capNhatMon());
        }

        private void capNhatMon() {
            try {
                String ten = txtTen.getText().trim();
                String loaiTen = (String) cmbLoai.getSelectedItem();
                String trangThaiStr = (String) cmbTrangThai.getSelectedItem();
                String giaStr = txtGia.getText().trim();
                String moTa = txtMoTa.getText().trim();

                if (ten.isEmpty() || giaStr.isEmpty() || moTa.isEmpty() || loaiTen == null || trangThaiStr == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
                    return;
                }

                double donGia;
                try {
                    donGia = Double.parseDouble(giaStr.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Giá phải là số!");
                    return;
                }

                boolean trangThai = "Còn bán".equals(trangThaiStr);

                // Cập nhật thông tin món hiện tại
                monHienTai.setTenMon(ten);
                monHienTai.setLoaiMon(new LoaiMon(monHienTai.getLoaiMon().getMaLoai(), loaiTen));
                monHienTai.setDonGia(donGia);
                monHienTai.setTrangThai(trangThai);
                monHienTai.setMoTa(moTa);
                monHienTai.setAnhMon(imagePath);

                boolean ok = monAnDao.capNhatMonAn(monHienTai);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cập nhật món ăn thành công!");
                    capNhatLuoiMon(); 
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new FrmThucDon().setVisible(true));
    }
}
