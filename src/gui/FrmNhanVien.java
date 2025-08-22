package gui;

import javax.swing.*;
import java.awt.*;

public class FrmNhanVien extends JFrame {
    public FrmNhanVien() {
        setTitle("Quản lý Nhân viên");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Đây là trang Quản lý Nhân viên", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        add(lbl, BorderLayout.CENTER);
    }
}
