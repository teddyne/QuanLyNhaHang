package gui;

import dao.NhanVien_DAO;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FrmNhanVien extends JFrame {
    private JTextField txtMaNV, txtHoTen, txtCCCD, txtSDT, txtEmail, txtChucVu;
    private JDateChooser dcNgaySinh;
    private JComboBox<String> cmbGioiTinh, cmbTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem;
    private JLabel lblAnhNV;
    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;
    private NhanVien_DAO dao = new NhanVien_DAO();
    private String duongDanAnh = "default_avatar.png";

    public FrmNhanVien() {
        setTitle("Quản lý nhân viên");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5,5));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Color.decode("#A93744"));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTitle.setPreferredSize(new Dimension(0, 40));



        // PANEL THÔNG TIN VÀ ẢNH
        JPanel pnlThongTin = new JPanel(new BorderLayout(10,10));
        pnlThongTin.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin nhân viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 16)
        ));

        // --- PANEL ẢNH ---
        JPanel pnlAnh = new JPanel();
        pnlAnh.setBorder(BorderFactory.createTitledBorder("Ảnh nhân viên"));
        pnlAnh.setPreferredSize(new Dimension(120, 100));
        lblAnhNV = new JLabel();
        lblAnhNV.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnhNV.setPreferredSize(new Dimension(114,152)); // 3*4 cm
        setAnhChoLabel(lblAnhNV, duongDanAnh);
        pnlAnh.add(lblAnhNV);

        // Chọn ảnh khi click
        lblAnhNV.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn ảnh nhân viên");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "Image files", "jpg","jpeg","png","gif"));
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    duongDanAnh = fileChooser.getSelectedFile().getAbsolutePath();
                    setAnhChoLabel(lblAnhNV, duongDanAnh);
                }
            }
        });

        // --- PANEL NHẬP LIỆU ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Mã NV
        gbc.gridx=0; gbc.gridy=row;
        pnlForm.add(new JLabel("Mã NV:"), gbc);
        txtMaNV = new JTextField(10); txtMaNV.setEditable(false);
        gbc.gridx=1; pnlForm.add(txtMaNV, gbc);

        // Tên NV
        gbc.gridx=2; pnlForm.add(new JLabel("Tên NV:"), gbc);
        txtHoTen = new JTextField(15);
        gbc.gridx=3; gbc.weightx=2.0; pnlForm.add(txtHoTen, gbc);
        gbc.weightx=1.0;

        row++;
        // Ngày sinh
        gbc.gridx=0; gbc.gridy=row;
        pnlForm.add(new JLabel("Ngày sinh:"), gbc);
        dcNgaySinh = new JDateChooser(); dcNgaySinh.setDateFormatString("dd/MM/yyyy");
        gbc.gridx=1; pnlForm.add(dcNgaySinh, gbc);

        // Giới tính
        gbc.gridx=2; pnlForm.add(new JLabel("Giới tính:"), gbc);
        cmbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        gbc.gridx=3; pnlForm.add(cmbGioiTinh, gbc);

        row++;
        // CCCD
        gbc.gridx=0; gbc.gridy=row;
        pnlForm.add(new JLabel("CCCD:"), gbc);
        txtCCCD = new JTextField(12);
        gbc.gridx=1; pnlForm.add(txtCCCD, gbc);

        // SDT
        gbc.gridx=2;
        pnlForm.add(new JLabel("SDT:"), gbc);
        txtSDT = new JTextField(10);
        gbc.gridx=3; pnlForm.add(txtSDT, gbc);

        row++;
        // Email
        gbc.gridx=0; gbc.gridy=row;
        pnlForm.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx=1; gbc.gridwidth=3; pnlForm.add(txtEmail, gbc);
        gbc.gridwidth=1;

        row++;
        // Chức vụ
        gbc.gridx=0; gbc.gridy=row;
        pnlForm.add(new JLabel("Chức vụ:"), gbc);
        txtChucVu = new JTextField(10);
        gbc.gridx=1; pnlForm.add(txtChucVu, gbc);

        // Trạng thái
        gbc.gridx=2;
        pnlForm.add(new JLabel("Trạng thái:"), gbc);
        cmbTrangThai = new JComboBox<>(new String[]{"Đang làm việc","Ngừng làm việc"});
        gbc.gridx=3; pnlForm.add(cmbTrangThai, gbc);

        row++;
        // Nút 
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        btnThem = new JButton("Thêm"); btnSua = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa"); btnTimKiem = new JButton("Tìm kiếm");
        pnlButton.add(btnThem); pnlButton.add(btnSua); pnlButton.add(btnXoa); pnlButton.add(btnTimKiem);
        gbc.gridx=0; gbc.gridy=row; gbc.gridwidth=4;
        pnlForm.add(pnlButton, gbc);
        gbc.gridwidth=1;
        
        // Panel nút
        btnThem.setBackground(new Color(76, 175, 80));   // xanh lá
        btnSua.setBackground(new Color(255, 193, 7));    // vàng
        btnXoa.setBackground(new Color(244, 67, 54));    // đỏ
        btnTimKiem.setBackground(new Color(33, 150, 243)); // xanh dương
        btnThem.setForeground(Color.WHITE);
        btnSua.setForeground(Color.WHITE);
        btnXoa.setForeground(Color.WHITE);
        btnTimKiem.setForeground(Color.WHITE);

        // --- Kết hợp ảnh + form ---
        pnlThongTin.add(pnlAnh, BorderLayout.WEST);
        pnlThongTin.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlNorth = new JPanel(new BorderLayout(5,5));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlThongTin, BorderLayout.CENTER);

        add(pnlNorth, BorderLayout.NORTH);

        // --- Bảng nhân viên ---
        modelNhanVien = new DefaultTableModel(
                new String[]{"Mã NV","Tên NV","Ngày sinh","Giới tính","CCCD","SDT","Email","Chức vụ","Trạng thái"},0);
        tblNhanVien = new JTable(modelNhanVien);

        //tiêu đề bảng
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setBackground(Color.decode("#A93744"));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Times New Roman", Font.BOLD, 13));

        JPanel pnlBang = new JPanel(new BorderLayout(5,5));
        JLabel lblTieuDe = new JLabel("DANH SÁCH NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblTieuDe.setOpaque(true);
        lblTieuDe.setForeground(Color.decode("#A93744"));
        lblTieuDe.setPreferredSize(new Dimension(0, 35));
        pnlBang.add(lblTieuDe, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblNhanVien);
        pnlBang.add(scroll, BorderLayout.CENTER);

        add(pnlBang, BorderLayout.CENTER);

        loadNhanVien();
        
        // Border panel thông tin
        pnlThongTin.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#A93744")),
                "Thông tin nhân viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("T", Font.BOLD, 16),
                Color.decode("#A93744")
        ));
        
        // Click hiển thị dữ liệu lên form
        tblNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                int row = tblNhanVien.getSelectedRow();
                if(row >=0){
                    txtMaNV.setText(modelNhanVien.getValueAt(row,0).toString());
                    txtHoTen.setText(modelNhanVien.getValueAt(row,1).toString());
                    try{
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate ld = LocalDate.parse(modelNhanVien.getValueAt(row,2).toString(), dtf);
                        dcNgaySinh.setDate(Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    } catch(Exception ex){}
                    cmbGioiTinh.setSelectedItem(modelNhanVien.getValueAt(row,3).toString());
                    txtCCCD.setText(modelNhanVien.getValueAt(row,4).toString());
                    txtSDT.setText(modelNhanVien.getValueAt(row,5).toString());
                    txtEmail.setText(modelNhanVien.getValueAt(row,6).toString());
                    txtChucVu.setText(modelNhanVien.getValueAt(row,7).toString());
                    cmbTrangThai.setSelectedItem(modelNhanVien.getValueAt(row,8).toString());

                    // Lấy ảnh từ database
                    NhanVien nv = dao.timNhanVienTheoMa(txtMaNV.getText());
                    if(nv!=null && nv.getAnhNV()!=null && !nv.getAnhNV().isEmpty()){
                        duongDanAnh = nv.getAnhNV();
                    } else {
                        duongDanAnh = "default_avatar.png";
                    }
                    setAnhChoLabel(lblAnhNV, duongDanAnh);
                }
            }
        });

        // Nút sự kiện
        btnThem.addActionListener(e->themNhanVien());
        btnSua.addActionListener(e->capNhatNhanVien());
        btnXoa.addActionListener(e->xoaNhanVien());
        btnTimKiem.addActionListener(e->timKiemNhanVien());
    }

    private void setAnhChoLabel(JLabel lbl, String duongDan){
        ImageIcon icon = new ImageIcon(duongDan);
        Image img = icon.getImage();
        int maxW = 114, maxH = 152; //3*4
        float ratio = (float) img.getWidth(null)/img.getHeight(null);
        int newW = maxW;
        int newH = (int)(maxW/ratio);
        if(newH > maxH){
            newH = maxH;
            newW = (int)(maxH*ratio);
        }
        Image scaled = img.getScaledInstance(newW,newH,Image.SCALE_SMOOTH);
        lbl.setIcon(new ImageIcon(scaled));
    }

    private void loadNhanVien(){
        modelNhanVien.setRowCount(0);
        List<NhanVien> list = dao.getAllNhanVien();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(NhanVien nv: list){
            if(!nv.isTrangThai()) continue; // chỉ hiển thị nhân viên đang làm việc
            modelNhanVien.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    nv.getNgaySinh()!=null ? nv.getNgaySinh().format(dtf):"",
                    nv.isGioiTinh()? "Nam":"Nữ",
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getEmail(),
                    nv.getChucVu(),
                    nv.isTrangThai()? "Đang làm việc":"Ngừng làm việc"
            });
        }
    }

    private boolean kiemTraDuLieu(){
        if(txtHoTen.getText().isEmpty() || dcNgaySinh.getDate()==null ||
           txtCCCD.getText().isEmpty() || txtSDT.getText().isEmpty() ||
           txtEmail.getText().isEmpty() || txtChucVu.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if(!txtCCCD.getText().matches("\\d{12}")){
            JOptionPane.showMessageDialog(this,"CCCD phải 12 chữ số!");
            return false;
        }
        if(!txtSDT.getText().matches("^(03|05|07|08)\\d{8}$")){
            JOptionPane.showMessageDialog(this,"SĐT phải 10 chữ số và bắt đầu bằng 03, 05, 07 hoặc 08!");
            return false;
        }
        if(!txtEmail.getText().matches("^\\w+@\\w+\\.\\w+$")){
            JOptionPane.showMessageDialog(this,"Email không hợp lệ!");
            return false;
        }
        return true;
    }

    private NhanVien layNhanVienTuForm(){
        Date date = dcNgaySinh.getDate();
        LocalDate ngaySinh = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return new NhanVien(
                txtMaNV.getText(),
                txtHoTen.getText(),
                duongDanAnh,
                ngaySinh,
                cmbGioiTinh.getSelectedItem().equals("Nam"),
                txtCCCD.getText(),
                txtEmail.getText(),
                txtSDT.getText(),
                cmbTrangThai.getSelectedItem().equals("Đang làm việc"),
                txtChucVu.getText()
        );
    }

    private void lamMoiForm(){
        txtMaNV.setText("");
        txtHoTen.setText("");
        dcNgaySinh.setDate(null);
        txtCCCD.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtChucVu.setText("");
        cmbGioiTinh.setSelectedIndex(0);
        cmbTrangThai.setSelectedIndex(0);
        tblNhanVien.clearSelection();
        duongDanAnh = "default_avatar.png";
        setAnhChoLabel(lblAnhNV, duongDanAnh);
    }

    private String taoMaNhanVien(){
        List<NhanVien> list = dao.getAllNhanVien();
        int max=0;
        for(NhanVien nv:list){
            try{
                int num = Integer.parseInt(nv.getMaNhanVien().replaceAll("\\D",""));
                if(num>max) max=num;
            }catch(Exception e){}
        }
        return String.format("NV%04d", max+1);
    }

    private void themNhanVien(){
        if(!kiemTraDuLieu()) return;
        NhanVien nv = layNhanVienTuForm();

        // Kiểm tra trùng CCCD, SDT, Email
        List<NhanVien> list = dao.getAllNhanVien();
        for(NhanVien n:list){
            if(n.getCccd().equals(nv.getCccd())){
                JOptionPane.showMessageDialog(this,"CCCD đã tồn tại!");
                return;
            }
            if(n.getSdt().equals(nv.getSdt())){
                JOptionPane.showMessageDialog(this,"SĐT đã tồn tại!");
                return;
            }
            if(n.getEmail().equalsIgnoreCase(nv.getEmail())){
                JOptionPane.showMessageDialog(this,"Email đã tồn tại!");
                return;
            }
        }

        nv.setMaNhanVien(taoMaNhanVien());
        if(dao.themNhanVien(nv)){
            JOptionPane.showMessageDialog(this,"Thêm nhân viên thành công!");
            loadNhanVien();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this,"Thêm thất bại!");
        }
    }

    private void capNhatNhanVien(){
        if(!kiemTraDuLieu()) return;
        NhanVien nv = layNhanVienTuForm();
        if(dao.capNhatNhanVien(nv)){
            JOptionPane.showMessageDialog(this,"Cập nhật thành công!");
            loadNhanVien();
        } else {
            JOptionPane.showMessageDialog(this,"Cập nhật thất bại!");
        }
    }

    private void xoaNhanVien(){
        String ma = txtMaNV.getText();
        if(ma.isEmpty()){
            JOptionPane.showMessageDialog(this,"Chọn nhân viên cần xóa!");
            return;
        }
        if(dao.anNhanVien(ma)){
            JOptionPane.showMessageDialog(this,"Nhân viên đã ẩn!");
            loadNhanVien();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this,"Xóa thất bại!");
        }
    }

    private void timKiemNhanVien(){
        String tuKhoaTen = txtHoTen.getText().trim().toLowerCase();
        String tuKhoaChucVu = txtChucVu.getText().trim().toLowerCase();
        String gioiTinh = (String)cmbGioiTinh.getSelectedItem();

        List<NhanVien> list = dao.getAllNhanVien();
        modelNhanVien.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(NhanVien nv : list){
            if(!nv.isTrangThai()) continue;
            boolean match = true;
            if(!tuKhoaTen.isEmpty() && !nv.getHoTen().toLowerCase().contains(tuKhoaTen)){
                match = false;
            }
            if(!tuKhoaChucVu.isEmpty() && !nv.getChucVu().toLowerCase().contains(tuKhoaChucVu)){
                match = false;
            }
            if(gioiTinh.equals("Nam") && !nv.isGioiTinh()) match = false;
            if(gioiTinh.equals("Nữ") && nv.isGioiTinh()) match = false;

            if(match){
                modelNhanVien.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getHoTen(),
                    nv.getNgaySinh() != null ? nv.getNgaySinh().format(dtf) : "",
                    nv.isGioiTinh()? "Nam":"Nữ",
                    nv.getCccd(),
                    nv.getSdt(),               
                    nv.getEmail(),
                    nv.getChucVu(),
                    nv.isTrangThai()? "Đang làm việc":"Ngừng làm việc"
                });
            }
        }

        if(modelNhanVien.getRowCount() == 0){
            JOptionPane.showMessageDialog(this,"Không tìm thấy nhân viên!");
        }

        txtHoTen.setText(""); 
        txtChucVu.setText(""); 
        dcNgaySinh.setDate(null); 
        txtCCCD.setText(""); 
        txtSDT.setText(""); 
        txtEmail.setText(""); 
        cmbGioiTinh.setSelectedIndex(0); 
        cmbTrangThai.setSelectedIndex(0); 
        lblAnhNV.setIcon(new ImageIcon("default_avatar.png")); 
        duongDanAnh = "default_avatar.png"; 
        tblNhanVien.clearSelection();   
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new FrmNhanVien().setVisible(true));
    }
}
