package gui;

import connectSQL.ConnectSQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrmDangNhap extends JFrame implements ActionListener,MouseListener{
    private final JLabel lblTitle;
    private final JLabel lblTaiKhoan;
    private final JLabel lblMatKhau;
    private final JTextField txtTaiKhoan;
    private final JPasswordField txtPass;
    private final JButton btnDangNhap;
    private final JButton btnThoat;
    private final JLabel iconEye;

    private boolean isPasswordVisible = false;

    public FrmDangNhap() {
        ConnectSQL.getInstance().connect();

        setTitle("Đăng nhập");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JSplitPane jp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jp.setEnabled(false);
        add(jp, BorderLayout.CENTER);

        ImageIcon img = new ImageIcon("img/anhdangnhap.jpg");
        JPanel pLeft = new ImagePanel(img);
        pLeft.setPreferredSize(new Dimension(800, 800));

        JPanel pRight = new JPanel(null);
        pRight.setBackground(Color.gray);

        jp.setLeftComponent(pLeft);
        jp.setRightComponent(pRight);
        jp.setDividerLocation(400);

        lblTitle = new JLabel("THÔNG TIN ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblTitle.setBounds(60, 100, 400, 40);
        pRight.add(lblTitle);

        lblTaiKhoan = new JLabel("Tài khoản:");
        lblTaiKhoan.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTaiKhoan.setBounds(80, 170, 150, 25);
        pRight.add(lblTaiKhoan);

        ImageIcon imgTaiKhoan = new ImageIcon("img/taiKhoan.png");
        Image scaledTaiKhoan = imgTaiKhoan.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel iconTaiKhoan = new JLabel(new ImageIcon(scaledTaiKhoan));
        iconTaiKhoan.setBounds(40, 200, 30, 30);
        pRight.add(iconTaiKhoan);

        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setBounds(80, 200, 300, 30);
        pRight.add(txtTaiKhoan);

        lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblMatKhau.setBounds(80, 240, 150, 25);
        pRight.add(lblMatKhau);

        ImageIcon imgPass = new ImageIcon("img/password.png");
        Image scaledPass = imgPass.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JLabel iconPass = new JLabel(new ImageIcon(scaledPass));
        iconPass.setBounds(40, 270, 30, 30);
        pRight.add(iconPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(80, 270, 300, 30);
        pRight.add(txtPass);

        ImageIcon eyeClosedRaw = new ImageIcon("img/anMatKhau.png");
        ImageIcon eyeOpenRaw = new ImageIcon("img/nhinMatKhau.png");

        Image eyeClosedScaled = eyeClosedRaw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        Image eyeOpenScaled = eyeOpenRaw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);

        ImageIcon eyeClosedIcon = new ImageIcon(eyeClosedScaled);
        ImageIcon eyeOpenIcon = new ImageIcon(eyeOpenScaled);

        iconEye = new JLabel(eyeClosedIcon);
        iconEye.setBounds(380, 270, 33, 33);
        iconEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconEye.setBounds(380, 270, 33, 33);
        pRight.add(iconEye);

        iconEye.addMouseListener(this);

        JPanel tacVu = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBackground(new Color(46, 204, 113));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);

        btnThoat = new JButton("Thoát");
        btnThoat.setBackground(new Color(231, 76, 60));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFocusPainted(false);

        tacVu.add(btnDangNhap);
        tacVu.add(btnThoat);
        tacVu.setBorder(BorderFactory.createTitledBorder("Chọn tác vụ"));
        tacVu.setBounds(30, 450, 400, 80);
        pRight.add(tacVu);

        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnDangNhap) {
                dangNhap();
            } else if (e.getSource() == btnThoat) {
                exit();
            }
    }

    public void exit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void dangNhap(){
        String tk = txtTaiKhoan.getText();
        String mk = String.valueOf(txtPass.getPassword());
        if (tk.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tài khoản và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (kiemTraDangNhap(tk, mk)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            new FrmDangNhap();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }

    }
    public boolean kiemTraDangNhap(String user, String pass) {
        try {

            Connection con = ConnectSQL.getConnection();
            if (con == null) {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                return false;
            }

            String sql = "SELECT * FROM TaiKhoanNhanVien WHERE SDT = ? AND matKhau = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    class ImagePanel extends JPanel {
        private final Image img;

        public ImagePanel(ImageIcon icon) {
            this.img = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == iconEye) {
            isPasswordVisible = !isPasswordVisible;
            txtPass.setEchoChar(isPasswordVisible ? (char) 0 : '●');
            ImageIcon eyeIcon = isPasswordVisible
                    ? new ImageIcon(new ImageIcon("src/img/nhinMatKhau.png").getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
                    : new ImageIcon(new ImageIcon("src/img/anMatKhau.png").getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            iconEye.setIcon(eyeIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public static void main(String args[]) {
    	new FrmDangNhap();
    }
}
