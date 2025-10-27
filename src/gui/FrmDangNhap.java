package gui;

import connectSQL.ConnectSQL;
import entity.TaiKhoan;
import dao.TaiKhoan_DAO;

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
    private final JButton btnQuenMatKhau;
    private final JLabel iconEye;
    private Connection con = ConnectSQL.getConnection();

    private boolean isPasswordVisible = false;
	private JButton btnTiepTuc;
	private JButton btnHuy;
	private JButton btnDoiMK;
    
    public FrmDangNhap(){
    	Connection conn = ConnectSQL.getConnection();
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
        txtTaiKhoan.setFont(new Font("Times New Roman", Font.PLAIN, 25));
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
        txtPass.setFont(new Font("Times New Roman", Font.PLAIN, 25));
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

        // Thêm nút Quên mật khẩu
        btnQuenMatKhau = new JButton("Quên mật khẩu?");
        btnQuenMatKhau.setBackground(Color.white);
        btnQuenMatKhau.setForeground(Color.BLUE);
        btnQuenMatKhau.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        btnQuenMatKhau.setBorder(null);
        btnQuenMatKhau.setBounds(340, 390, 150, 30);
        pRight.add(btnQuenMatKhau);
        btnQuenMatKhau.addActionListener(this);

        JPanel tacVu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 25);
        Dimension buttonSize = new Dimension(200, 60);
        
        btnDangNhap = taoNut("Đăng Nhập", new Color(46, 204, 113), buttonSize, buttonFont);
        btnThoat = taoNut("Thoát", new Color(231, 76, 60), buttonSize, buttonFont);

        txtTaiKhoan.addActionListener(e -> txtPass.requestFocus());
        txtPass.addActionListener(e -> btnDangNhap.doClick());

        tacVu.add(btnDangNhap);
        tacVu.add(btnThoat);
        tacVu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Chọn tác vụ", 0, 0, new Font("Times New Roman", Font.BOLD, 30)));
        tacVu.setBounds(140, 450, 500, 150);
        tacVu.setBackground(Color.white);
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
        } else if (e.getSource() == btnQuenMatKhau) {
            showQuenMatKhauDialog();
        }
    }

    private void showQuenMatKhauDialog() {
        JDialog dialog = new JDialog(this, "Quên mật khẩu", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        // Tiêu đề
        JLabel lblTitle = new JLabel("Đặt lại mật khẩu");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(30, 10, 340, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(lblTitle);

        // Nhập số điện thoại
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblSDT.setBounds(30, 50, 120, 30);
        dialog.add(lblSDT);

        JTextField txtSoDienThoai = new JTextField();
        txtSoDienThoai.setBounds(30, 80, 340, 30);
        txtSoDienThoai.setFont(new Font("Times New Roman",Font.PLAIN, 18));
        dialog.add(txtSoDienThoai);

        // Nhập mã xác nhận cố định
        JLabel lblMa = new JLabel("Mã xác nhận:");
        lblMa.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblMa.setBounds(30, 120, 120, 30);
        dialog.add(lblMa);

        JTextField txtMaXacNhan = new JTextField();
        txtMaXacNhan.setBounds(30, 150, 340, 30);
        txtMaXacNhan.setFont(new Font("Times New Roman",Font.PLAIN, 18));
        dialog.add(txtMaXacNhan);

        // Nút tiếp tục
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 18);
        Dimension buttonSize = new Dimension(100, 30);
        
        btnTiepTuc = taoNut("Tiếp tục", new Color(46, 204, 113), buttonSize, buttonFont);
        btnTiepTuc.setForeground(Color.WHITE);
        btnTiepTuc.setBounds(30, 190, 120, 30);
        dialog.add(btnTiepTuc);

        // Nút hủy
        btnHuy = taoNut("Hủy", new Color(231, 76, 60), buttonSize, buttonFont);
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setBounds(250, 190, 120, 30);
        dialog.add(btnHuy);

        String[] soDienThoai = {null};
        final String MA_XAC_NHAN_CO_DINH = "nhaHangVang"; // Mã cố định cho quán

        btnTiepTuc.addActionListener(e -> {
            soDienThoai[0] = txtSoDienThoai.getText().trim();
            String maNhap = txtMaXacNhan.getText().trim();

            TaiKhoan_DAO dao = new TaiKhoan_DAO(con);
            if (soDienThoai[0].isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!dao.kiemTraSoDienThoaiTonTai(soDienThoai[0])) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!maNhap.equals(MA_XAC_NHAN_CO_DINH)) {
                JOptionPane.showMessageDialog(dialog, "Mã xác nhận không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dialog.dispose();
            showDoiMatKhauDialog(soDienThoai[0]);
        });

        btnHuy.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void showDoiMatKhauDialog(String soDienThoai) {
        JDialog dialog = new JDialog(this, "Đổi mật khẩu", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(new Color(245, 245, 245));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Đổi mật khẩu mới");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblTitle.setBounds(30, 10, 340, 30);
        
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(lblTitle);

        // Nhập mật khẩu mới
        JLabel lblMKMoi = new JLabel("Mật khẩu mới:");
        lblMKMoi.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblMKMoi.setBounds(30, 50, 340, 30);
        dialog.add(lblMKMoi);
        
        JPasswordField txtMatKhauMoi = new JPasswordField();
        txtMatKhauMoi.setBounds(30, 80, 340, 30); //ngang-dọc-rộng-cao
        txtMatKhauMoi.setFont(new Font("Times New Roman",Font.BOLD, 18));
        txtMatKhauMoi.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
        dialog.add(txtMatKhauMoi);

        // Xác nhận mật khẩu
        JLabel lblXacNhanMK = new JLabel("Xác nhận mật khẩu:");
        lblXacNhanMK.setFont(new Font("Times New Roman",Font.BOLD, 18));
        lblXacNhanMK.setBounds(30, 120, 220, 30);
        dialog.add(lblXacNhanMK);

        JPasswordField txtXacNhanMK = new JPasswordField();
        txtXacNhanMK.setBounds(30, 150, 340, 30);
        txtXacNhanMK.setFont(new Font("Times New Roman",Font.BOLD, 18));
        dialog.add(txtXacNhanMK);

        // Nút đổi mật khẩu
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 18);
        Dimension buttonSize = new Dimension(100, 30);
        
        btnDoiMK = taoNut("Xác nhận", new Color(46, 204, 113), buttonSize, buttonFont);
        btnDoiMK.setForeground(Color.WHITE);
        btnDoiMK.setBounds(30, 190, 120, 30);
        dialog.add(btnDoiMK);

        // Nút hủy
        btnHuy = taoNut("Hủy", new Color(231,76,60), buttonSize, buttonFont);
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setBounds(250, 190, 120, 30);
        dialog.add(btnHuy);

        btnDoiMK.addActionListener(e -> {
            String mkMoi = String.valueOf(txtMatKhauMoi.getPassword());
            String xacNhanMK = String.valueOf(txtXacNhanMK.getPassword());

            if (mkMoi.isEmpty() || xacNhanMK.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!mkMoi.equals(xacNhanMK)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoan_DAO dao = new TaiKhoan_DAO(con);
            if (dao.capNhatMatKhauTheoSDT(soDienThoai, mkMoi)) {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công! Bạn có thể đăng nhập với mật khẩu mới.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi đổi mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnHuy.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
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
                TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO(con);
                Thread.sleep(1000);
                return taiKhoanDAO.kiemTraDangNhap(tk, mk);
            }

            protected void done() {
            loadingDialog.dispose();
            try {
                boolean ketQua = get();
                if (ketQua) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công! Quyền: " + TaiKhoan_DAO.currentTaiKhoan.getPhanQuyen(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    FrmTrangChu.resetPhanQuyen();
                    FrmTrangChu.setPhanQuyen(TaiKhoan_DAO.currentTaiKhoan.getPhanQuyen());
                    ThanhTacVu.resetInstance();
                    ThanhTacVu.setPhanQuyen(TaiKhoan_DAO.currentTaiKhoan.getPhanQuyen());

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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
    }
}