package gui;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.awt.event.*;

public class FrmSuaKhuyenMai extends JFrame {
    private JTextField txtMaKM, txtTenKM, txtGiaTri, txtDonHangTu, txtMon1, txtMon2, txtMonTang, txtGhiChu;
    private JComboBox<String> cmbLoaiKM, cmbTrangThai, cmbDoiTuongApDung;
    private JSpinner spTuNgay, spDenNgay;
    private JButton btnLuu, btnHuy;
    private KhuyenMai_DAO kmDAO;
    private JLabel lblMa;
	private JLabel lblTen;
	private JLabel lblLoai;
	private JLabel lblTuNgay;
	private JLabel lblDenNgay;
	private JLabel lblTrangThai;
	private JLabel lblDonHangTu;
	private JLabel lblMon1;
	private JLabel lblMon2;
	private JLabel lblMonTang;
	private JLabel lblGhiChu;
	private JLabel lblDoiTuong;
	private JLabel lblGiaTri;

    public FrmSuaKhuyenMai(KhuyenMai km) {
        setTitle("Sửa Khuyến Mãi");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        kmDAO = new KhuyenMai_DAO();

        JPanel pMain = new JPanel();
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // --- Dòng Mã KM ---
        Dimension size = new Dimension(150, 25);
        
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(lblMa = new JLabel("Mã KM: "));
        txtMaKM = new JTextField(kmDAO.taoMaKhuyenMaiMoi(), 15);
        txtMaKM.setEnabled(false);
        p1.add(txtMaKM);
        pMain.add(p1);

        // --- Dòng Tên KM ---
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(lblTen = new JLabel("Tên KM: "));
        txtTenKM = new JTextField(15);
        p2.add(txtTenKM);
        pMain.add(p2);

        // --- Dòng Loại KM + Giá trị ---
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(lblLoai = new JLabel("Loại KM: "));
        cmbLoaiKM = new JComboBox<>(new String[]{"Phần trăm","Giảm trực tiếp","Sinh nhật","Tặng món"});
        p3.add(cmbLoaiKM);
        p3.add(Box.createHorizontalStrut(30));

        p3.add(lblGiaTri = new JLabel("Giá trị: "));
        txtGiaTri = new JTextField(15);
        p3.add(txtGiaTri);
        pMain.add(p3);

        // --- Dòng Từ ngày + Đến ngày ---
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.add(lblTuNgay = new JLabel("Từ ngày: "));
        spTuNgay = new JSpinner(new SpinnerDateModel());
        spTuNgay.setEditor(new JSpinner.DateEditor(spTuNgay,"yyyy-MM-dd"));
        p4.add(spTuNgay);
        p4.add(Box.createHorizontalStrut(30));

        p4.add(lblDenNgay = new JLabel("Đến ngày: "));
        spDenNgay = new JSpinner(new SpinnerDateModel());
        spDenNgay.setEditor(new JSpinner.DateEditor(spDenNgay,"yyyy-MM-dd"));
        p4.add(spDenNgay);
        pMain.add(p4);

        // --- Dòng Trạng thái + Đối tượng ---
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(lblTrangThai = new JLabel("Trạng thái: "));
        cmbTrangThai = new JComboBox<>(new String[]{"Đang áp dụng","Sắp áp dụng","Hết hạn"});
        p5.add(cmbTrangThai);
        p5.add(Box.createHorizontalStrut(30));

        p5.add(lblDoiTuong = new JLabel("Đối tượng: "));
        cmbDoiTuongApDung = new JComboBox<>(new String[]{"Tất cả", "Khách thường","Thành viên"});
        p5.add(cmbDoiTuongApDung);
        pMain.add(p5);

        // --- Dòng Đơn hàng từ ---
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p6.add(lblDonHangTu = new JLabel("Đơn hàng từ: "));
        txtDonHangTu = new JTextField(15);
        p6.add(txtDonHangTu);
        pMain.add(p6);

        // --- Dòng Món1 + Món2 ---
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p7.add(lblMon1 = new JLabel("Món1: "));
        txtMon1 = new JTextField(15);
        p7.add(txtMon1);
        p7.add(Box.createHorizontalStrut(30));

        p7.add(lblMon2 = new JLabel("Món2: "));
        txtMon2 = new JTextField(15);
        p7.add(txtMon2);
        pMain.add(p7);

        // --- Dòng Món tặng + Ghi chú ---
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p8.add(lblMonTang = new JLabel("Món tặng: "));
        txtMonTang = new JTextField(15);
        p8.add(txtMonTang);
        p8.add(Box.createHorizontalStrut(30));

        p8.add(lblGhiChu = new JLabel("Ghi chú: "));
        txtGhiChu = new JTextField(15);
        p8.add(txtGhiChu);
        pMain.add(p8);
        
        lblMa.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblTen.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblLoai.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblTuNgay.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblTrangThai.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblMon1.setPreferredSize(lblDonHangTu.getPreferredSize());
		lblMonTang.setPreferredSize(lblDonHangTu.getPreferredSize());
		
		lblGhiChu.setPreferredSize(lblDoiTuong.getPreferredSize());
		lblGiaTri.setPreferredSize(lblDoiTuong.getPreferredSize());
		lblDenNgay.setPreferredSize(lblDoiTuong.getPreferredSize());
		lblMon2.setPreferredSize(lblDoiTuong.getPreferredSize());
		
        cmbLoaiKM.setPreferredSize(size);
        spTuNgay.setPreferredSize(size);
        cmbTrangThai.setPreferredSize(size);
        cmbDoiTuongApDung.setPreferredSize(size);
        spDenNgay.setPreferredSize(size);

        // --- Dòng Nút ---
        JPanel p9 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        p9.add(btnLuu);
        p9.add(btnHuy);
        pMain.add(p9);

        add(pMain);

        // --- Khởi tạo trạng thái ---
        capNhatTrangThaiField();

        // --- Thay đổi trạng thái theo Loại KM ---
        cmbLoaiKM.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                capNhatTrangThaiField();
            }
        });

        // --- Action ---
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> {
            try {
                Date bd = new Date(((java.util.Date) spTuNgay.getValue()).getTime());
                Date kt = new Date(((java.util.Date) spDenNgay.getValue()).getTime());
                if(kt.before(bd)){
                    JOptionPane.showMessageDialog(this,"Ngày kết thúc phải sau ngày bắt đầu!");
                    return;
                }

                double giaTri = 0;
                if(!cmbLoaiKM.getSelectedItem().toString().equals("Tặng món")){
                    giaTri = Double.parseDouble(txtGiaTri.getText());
                }

                String monTang = cmbLoaiKM.getSelectedItem().toString().equals("Tặng món") ? txtMonTang.getText() : null;

                KhuyenMai kmUpdate = new KhuyenMai(
                        txtMaKM.getText(),
                        txtTenKM.getText(),
                        cmbLoaiKM.getSelectedItem().toString(),
                        giaTri,
                        bd, kt,
                        cmbTrangThai.getSelectedItem().toString(),
                        cmbDoiTuongApDung.getSelectedItem().toString(),
                        txtDonHangTu.getText().isEmpty()?0:Double.parseDouble(txtDonHangTu.getText()),
                        txtMon1.getText(),
                        txtMon2.getText(),
                        monTang,
                        txtGhiChu.getText()
                );

                if(kmDAO.suaKhuyenMai(kmUpdate)){
                    JOptionPane.showMessageDialog(this,"Cập nhật thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,"Cập nhật thất bại!");
                }
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Giá trị và Đơn hàng từ phải là số!");
            }
        });

        setVisible(true);
    }

    private void capNhatTrangThaiField(){
        String loai = cmbLoaiKM.getSelectedItem().toString();
        if(loai.equals("Tặng món")){
            txtGiaTri.setEnabled(false);
            txtMonTang.setEnabled(true);
            txtMon1.setEnabled(true);
            txtMon2.setEnabled(true);
        } else {
            txtGiaTri.setEnabled(true);
            txtMonTang.setEnabled(false);
            txtMon1.setEnabled(false);
            txtMon2.setEnabled(false);
        }
    }
}