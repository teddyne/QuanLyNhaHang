package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import dao.Ban_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.PhieuDatBan;

public class FrmDatBan extends JDialog {
    private static final Color COLOR_RED_WINE = new Color(128, 0, 0);
    private PhieuDatBan_DAO phieuDatBanDAO;
    private Ban_DAO banDAO;
    private String maBan;
    private PhieuDatBan editD;
    private Font fontBig = new Font("Times New Roman", Font.BOLD, 22);
	private JButton btnDatMon;
	private JButton btnLuu;
	private JButton btnHuy;
	private JButton btnCheckKH;

    public FrmDatBan(JFrame parent, String maBan, PhieuDatBan editD, PhieuDatBan_DAO phieuDatBanDAO, Ban_DAO banDAO) throws SQLException {
        super(parent, editD == null ? "Đặt bàn" : "Sửa đặt bàn", true);
        this.maBan = maBan;
        this.editD = editD;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.banDAO = banDAO;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setSize(800, 700);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(getParent());

        // Header
        JLabel lblTieuDe = new JLabel("PHIẾU ĐẶT BÀN - Mã bàn: " + maBan, SwingConstants.CENTER);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(COLOR_RED_WINE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 25));
        lblTieuDe.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(lblTieuDe, BorderLayout.NORTH);

        // Form
        JPanel pForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaPhieu = new JTextField(editD != null ? editD.getMaPhieu() : phieuDatBanDAO.generateMaPhieu());
        txtMaPhieu.setEditable(false);
        txtMaPhieu.setFont(fontBig);

        JTextField txtTen = new JTextField(editD != null ? editD.getTenKhach() : "", 20);
        txtTen.setFont(fontBig);
        JTextField txtSDT = new JTextField(editD != null ? editD.getSoDienThoai() : "", 15);
        txtSDT.setFont(fontBig);
        
        btnCheckKH = taoNut("Kiểm tra", new Color(100, 149, 237), null, fontBig);

        SpinnerDateModel dateModel = new SpinnerDateModel(editD != null ? editD.getNgayDen() : new java.util.Date(), new java.util.Date(), null, Calendar.DAY_OF_MONTH);
        JSpinner spnNgay = new JSpinner(dateModel);
        spnNgay.setEditor(new JSpinner.DateEditor(spnNgay, "yyyy-MM-dd"));
        spnNgay.setFont(fontBig);

        SpinnerDateModel timeModel = new SpinnerDateModel(editD != null ? new java.util.Date(editD.getGioDen().getTime()) : new java.util.Date(), null, null, Calendar.MINUTE);
        JSpinner spnGio = new JSpinner(timeModel);
        spnGio.setEditor(new JSpinner.DateEditor(spnGio, "HH:mm"));
        spnGio.setFont(fontBig);

        JTextField txtSoNguoi = new JTextField(editD != null ? String.valueOf(editD.getSoNguoi()) : "", 5);
        txtSoNguoi.setFont(fontBig);
        JTextArea txtGhiChu = new JTextArea(editD != null ? editD.getGhiChu() : "", 3, 20);
        txtGhiChu.setFont(fontBig);

        JTextField txtTienCoc = new JTextField(editD != null ? String.valueOf(editD.getTienCoc()) : "", 10);
        txtTienCoc.setFont(fontBig);
        JCheckBox chkCK = new JCheckBox("Chuyển khoản");
        chkCK.setFont(fontBig);
        if (editD != null && !editD.getGhiChuCoc().isEmpty()) chkCK.setSelected(true);
        JTextArea txtGhiChuCK = new JTextArea(editD != null ? editD.getGhiChuCoc() : "", 2, 20);
        txtGhiChuCK.setFont(fontBig);
        txtGhiChuCK.setEnabled(chkCK.isSelected());

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(120, 35);
        btnDatMon = taoNut("Đặt món", new Color(50, 205, 50), buttonSize, fontBig);       // xanh lá

        btnLuu = taoNut("Đặt Bàn", new Color(100, 149, 237), buttonSize, fontBig);
        
        btnHuy= taoNut("Hủy", new Color(220, 20, 60), buttonSize, fontBig);

        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMaPhieu = new JLabel("Mã phiếu");
        lblMaPhieu.setFont(fontBig);
        pForm.add(lblMaPhieu, gbc);
        gbc.gridx = 1; pForm.add(txtMaPhieu, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTenKH = new JLabel("Tên KH");
        lblTenKH.setFont(fontBig);
        pForm.add(lblTenKH, gbc);
        gbc.gridx = 1; pForm.add(txtTen, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSDTLabel = new JLabel("SĐT");
        lblSDTLabel.setFont(fontBig);
        pForm.add(lblSDTLabel, gbc);
        gbc.gridx = 1; pForm.add(txtSDT, gbc);
        gbc.gridx = 2; pForm.add(btnCheckKH, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblNgayDen = new JLabel("Ngày đến");
        lblNgayDen.setFont(fontBig);
        pForm.add(lblNgayDen, gbc);
        gbc.gridx = 1; pForm.add(spnNgay, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGioDen = new JLabel("Giờ đến");
        lblGioDen.setFont(fontBig);
        pForm.add(lblGioDen, gbc);
        gbc.gridx = 1; pForm.add(spnGio, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblSoNguoiLabel = new JLabel("Số người");
        lblSoNguoiLabel.setFont(fontBig);
        pForm.add(lblSoNguoiLabel, gbc);
        gbc.gridx = 1; pForm.add(txtSoNguoi, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChu = new JLabel("Ghi chú");
        lblGhiChu.setFont(fontBig);
        pForm.add(lblGhiChu, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChu), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblTienCocLabel = new JLabel("Tiền cọc");
        lblTienCocLabel.setFont(fontBig);
        pForm.add(lblTienCocLabel, gbc);
        gbc.gridx = 1; pForm.add(txtTienCoc, gbc);
        gbc.gridx = 2; pForm.add(chkCK, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblGhiChuCK = new JLabel("Ghi chú CK");
        lblGhiChuCK.setFont(fontBig);
        pForm.add(lblGhiChuCK, gbc);
        gbc.gridx = 1; pForm.add(new JScrollPane(txtGhiChuCK), gbc); row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMonAn = new JLabel("Món ăn");
        lblMonAn.setFont(fontBig);
        pForm.add(lblMonAn, gbc);
        gbc.gridx = 1; pForm.add(btnDatMon, gbc);

        // Panel nút
        JPanel pSouth = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        pSouth.add(btnLuu);
        pSouth.add(btnHuy);
        
        pForm.setBackground(Color.white);
        add(pForm, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);

        // Sự kiện
        btnCheckKH.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng kiểm tra chưa triển khai.");
        });

        chkCK.addActionListener(e -> txtGhiChuCK.setEnabled(chkCK.isSelected()));

        btnDatMon.addActionListener(e -> {
            new FrmOrder(maBan).setVisible(true);
        });

        btnLuu.addActionListener(e -> {
            try {
                Date ngay = new Date(((java.util.Date) spnNgay.getValue()).getTime());
                Time gio = new Time(((java.util.Date) spnGio.getValue()).getTime());
                if (editD == null && phieuDatBanDAO.checkTrung(maBan, ngay, gio)) {
                    JOptionPane.showMessageDialog(this, "Trùng ngày và giờ với lịch khác!");
                    return;
                }
                // Kiểm tra cách 2 tiếng
                if (editD == null && !phieuDatBanDAO.checkCachGio(maBan, ngay, gio, 2)) {
                    JOptionPane.showMessageDialog(this, "Lịch đặt phải cách ít nhất 2 tiếng với lịch khác cùng ngày!");
                    return;
                }
                double tienCoc = txtTienCoc.getText().isEmpty() ? 0 : Double.parseDouble(txtTienCoc.getText());
                PhieuDatBan dNew = editD != null ? editD : new PhieuDatBan();
                dNew.setMaPhieu(txtMaPhieu.getText());
                dNew.setMaBan(maBan);
                dNew.setTenKhach(txtTen.getText());
                dNew.setSoDienThoai(txtSDT.getText());
                dNew.setSoNguoi(Integer.parseInt(txtSoNguoi.getText()));
                dNew.setNgayDen(ngay);
                dNew.setGioDen(gio);
                dNew.setGhiChu(txtGhiChu.getText());
                dNew.setTienCoc(tienCoc);
                dNew.setGhiChuCoc(chkCK.isSelected() ? txtGhiChuCK.getText() : "");
                dNew.setTrangThai(editD != null ? dNew.getTrangThai() : "Đặt");

                if (editD != null) {
                    phieuDatBanDAO.update(dNew);
                } else {
                    phieuDatBanDAO.add(dNew);
                }
                JOptionPane.showMessageDialog(this, "Đặt bàn thành công!");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }
    private JButton taoNut(String text, Color baseColor, Dimension size, Font font) {
	    JButton btn = new JButton(text);
	    btn.setFont(font);
	    btn.setPreferredSize(size);
	    btn.setForeground(Color.WHITE);
	    btn.setBackground(baseColor);
	    btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        
	    // Hiệu ứng hover
	    btn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btn.setBackground(baseColor.darker());
	        }
	
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btn.setBackground(baseColor);
	        }
	    });
	
	    return btn;
	}
}