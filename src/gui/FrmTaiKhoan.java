package gui;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FrmTaiKhoan extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private TaiKhoan_DAO taiKhoanDao;

    public FrmTaiKhoan() {
        setTitle("Quản lý tài khoản");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        taiKhoanDao = new TaiKhoan_DAO();

        model = new DefaultTableModel(new String[]{"SĐT", "Quyền", "Lần đăng nhập gần nhất"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnReload = new JButton("Tải lại");


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(btnReload);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmTaiKhoan().setVisible(true));
    }
}
