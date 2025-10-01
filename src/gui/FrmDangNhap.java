package gui;

import connectSQL.ConnectSQL;
import entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FrmDangNhap extends JFrame implements ActionListener, MouseListener {
    private final JLabel lblTitle;
    private final JLabel lblTaiKhoan;
    private final JLabel lblMatKhau;
    private final JTextField txtTaiKhoan;
    private final JPasswordField txtPass;
    private final JButton btnDangNhap;
    private final JButton btnThoat;
    private final JLabel iconEye;

    private boolean isPasswordVisible = false;
    private static TaiKhoan currentTaiKhoan;

    public static TaiKhoan getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    public static void resetCurrentTaiKhoan() {
        currentTaiKhoan = null; // Đặt lại currentTaiKhoan khi đăng xuất
    }

    public FrmDangNhap() {
        ConnectSQL.getInstance().connect();

        setTitle("Đăng nhập");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JSplitPane jp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jp.setEnabled(false);
        add(jp, BorderLayout.CENTER);
        ImageIcon img = new ImageIcon("img/anhbia.png");
        JPanel pLeft = new ImagePanel(img);

        JPanel pRight = new JPanel(null);
        jp.setLeftComponent(pLeft);
        jp.setRightComponent(pRight);
        jp.setResizeWeight(0.5);

        pRight.setBackground(Color.white);
        lblTitle = new JLabel("THÔNG TIN ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 40));
        lblTitle.setBounds(140, 100, 600, 60);
        pRight.add(lblTitle);

        lblTaiKhoan = new JLabel("Tài khoản:");
        lblTaiKhoan.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblTaiKhoan.setBounds(140, 180, 150, 25);
        pRight.add(lblTaiKhoan);

        ImageIcon imgTaiKhoan = new ImageIcon("img/taiKhoan.png");
        Image scaledTaiKhoan = imgTaiKhoan.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconTaiKhoan = new JLabel(new ImageIcon(scaledTaiKhoan));
        iconTaiKhoan.setBounds(140, 210, 30, 55);
        pRight.add(iconTaiKhoan);

        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setBounds(180, 220, 290, 35);
        pRight.add(txtTaiKhoan);

        lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblMatKhau.setBounds(140, 300, 200, 35);
        pRight.add(lblMatKhau);

        ImageIcon imgPass = new ImageIcon("img/password.png");
        Image scaledPass = imgPass.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel iconPass = new JLabel(new ImageIcon(scaledPass));
        iconPass.setBounds(140, 345, 30, 32);
        pRight.add(iconPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(180, 350, 300, 30);
        txtTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 20));
        txtPass.setFont(new Font("Arial", Font.PLAIN, 20));
        pRight.add(txtPass);

        ImageIcon eyeClosedRaw = new ImageIcon("img/anMatKhau.png");
        ImageIcon eyeOpenRaw = new ImageIcon("img/nhinMatKhau.png");

        Image eyeClosedScaled = eyeClosedRaw.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        Image eyeOpenScaled = eyeOpenRaw.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);

        ImageIcon eyeClosedIcon = new ImageIcon(eyeClosedScaled);
        ImageIcon eyeOpenIcon = new ImageIcon(eyeOpenScaled);

        iconEye = new JLabel(eyeClosedIcon);
        iconEye.setBounds(490, 350, 33, 33);
        iconEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pRight.add(iconEye);

        iconEye.addMouseListener(this);

        JPanel tacVu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        tacVu.setBackground(Color.white);
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBackground(new Color(46, 204, 113));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btnDangNhap.setPreferredSize(new Dimension(160, 50));

        btnThoat = new JButton("Thoát");
        btnThoat.setBackground(new Color(231, 76, 60));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFocusPainted(false);
        btnThoat.setFont(new Font("Times New Roman", Font.BOLD, 25));
        btnThoat.setPreferredSize(new Dimension(160, 50));

        tacVu.add(btnDangNhap);
        tacVu.add(btnThoat);
        tacVu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Chọn tác vụ", 0, 0, new Font("Times New Roman", Font.BOLD, 30)));
        tacVu.setBounds(140, 450, 500, 150);
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
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void dangNhap() {
        String tk = txtTaiKhoan.getText();
        String mk = String.valueOf(txtPass.getPassword());

        if (tk.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tài khoản và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog loadingDialog = new JDialog(this, "Đang xử lý...", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(BorderLayout.CENTER, progressBar);
        loadingDialog.setSize(500, 95);
        loadingDialog.setLocationRelativeTo(this);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Thread.sleep(1000);
                return kiemTraDangNhap(tk, mk);
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    boolean ketQua = get();
                    if (ketQua) {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công! Quyền: " + currentTaiKhoan.getPhanQuyen(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        FrmTrangChu.resetPhanQuyen();
                        FrmTrangChu.setPhanQuyen(currentTaiKhoan.getPhanQuyen());
                        ThanhTacVu.resetInstance();
                        ThanhTacVu.setPhanQuyen(currentTaiKhoan.getPhanQuyen());
                        FrmTrangChu trangChu = new FrmTrangChu();
                        trangChu.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không đúng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    public boolean kiemTraDangNhap(String user, String pass) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement logPs = null;
        try {
            con = ConnectSQL.getConnection();
            if (con == null) {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
                return false;
            }

            String sql = "SELECT t.maTaiKhoan, t.soDienThoai, t.matKhau, t.maNhanVien, t.phanQuyen, n.hoTen " +
                         "FROM TaiKhoan t INNER JOIN NhanVien n ON t.maNhanVien = n.maNhanVien " +
                         "WHERE t.soDienThoai = ? OR t.maNhanVien = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, user);
            rs = ps.executeQuery();
            boolean loginSuccess = false;
            String maTaiKhoan = null;

            if (rs.next() && rs.getString("matKhau").equals(pass)) {
                currentTaiKhoan = new TaiKhoan();
                currentTaiKhoan.setMaTaiKhoan(rs.getString("maTaiKhoan"));
                currentTaiKhoan.setSoDienThoai(rs.getString("soDienThoai"));
                currentTaiKhoan.setMatKhau(rs.getString("matKhau"));
                currentTaiKhoan.setMaNhanVien(rs.getString("maNhanVien"));
                currentTaiKhoan.setPhanQuyen(rs.getString("phanQuyen"));
                currentTaiKhoan.setHoTen(rs.getString("hoTen"));
                maTaiKhoan = rs.getString("maTaiKhoan");
                try {
                    currentTaiKhoan.setPhanQuyen(currentTaiKhoan.getPhanQuyen());
                    loginSuccess = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Phân quyền không hợp lệ: " + e.getMessage());
                }
            }

            if (maTaiKhoan == null) {
                String findSql = "SELECT maTaiKhoan FROM TaiKhoan WHERE soDienThoai = ? OR maNhanVien = ?";
                PreparedStatement findPs = con.prepareStatement(findSql);
                findPs.setString(1, user);
                findPs.setString(2, user);
                ResultSet findRs = findPs.executeQuery();
                if (findRs.next()) {
                    maTaiKhoan = findRs.getString("maTaiKhoan");
                }
                findRs.close();
                findPs.close();
            }

            if (maTaiKhoan != null) {
                String logSql = "INSERT INTO LichSuDangNhap (maTaiKhoan, trangThai) VALUES (?, ?)";
                logPs = con.prepareStatement(logSql);
                logPs.setString(1, maTaiKhoan);
                logPs.setBoolean(2, loginSuccess);
                logPs.executeUpdate();
            }

            return loginSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (logPs != null) logPs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                    ? new ImageIcon(new ImageIcon("img/nhinMatKhau.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH))
                    : new ImageIcon(new ImageIcon("img/anMatKhau.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
            iconEye.setIcon(eyeIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
    }
}
