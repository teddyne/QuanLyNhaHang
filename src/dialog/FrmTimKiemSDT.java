// File: FrmTimKiemSDT.java
package dialog;

import javax.swing.*;

import connectSQL.ConnectSQL;
import dao.Ban_DAO;
import dao.LoaiBan_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.PhieuDatBan;
import gui.FrmBan;
import gui.ThanhTacVu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class FrmTimKiemSDT extends JFrame {
    private PhieuDatBan_DAO phieuDAO;
    private Ban_DAO banDAO;
    private String sdt;
    private PhieuDatBan phieu;
    private FrmBan frmBanCha;

    // Màu đỏ rượu như FrmBan
    private final Color mau_Ruou_Vang = new Color(169, 55, 68);

    public FrmTimKiemSDT(JFrame parent, String sdt, PhieuDatBan_DAO phieuDAO, Ban_DAO banDAO) {
        super();
        this.frmBanCha = (FrmBan) parent;
        this.sdt = sdt;
        this.phieuDAO = phieuDAO;
        this.banDAO = banDAO;

        setSize(600, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(mau_Ruou_Vang, 2),
            "Thông tin đặt bàn",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 24),
            mau_Ruou_Vang
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Times New Roman", Font.BOLD, 20);
        Font fontValue = new Font("Times New Roman", Font.PLAIN, 20);

        // Tìm phiếu gần nhất
        try {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            phieu = phieuDAO.getPhieuGanNhatBySDT(sdt, today);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (phieu == null) {
            JLabel lblKhongCo = new JLabel("Khách hàng không có đặt bàn nào từ hôm nay.");
            lblKhongCo.setFont(new Font("Times New Roman", Font.ITALIC, 20));
            lblKhongCo.setForeground(Color.RED);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            pnlMain.add(lblKhongCo, gbc);
        } else {
            try {
                Ban b = banDAO.getBanByMa(phieu.getMaBan());
                String tenBan = b != null ? b.getMaBan() + " (" + b.getTenKhuVuc() + ")" : phieu.getMaBan();

                // Mã phiếu
                gbc.gridwidth = 1;
                gbc.gridx = 0; gbc.gridy = 0;
                JLabel lblMaPhieu = new JLabel("Mã phiếu:");
                lblMaPhieu.setFont(fontLabel);
                pnlMain.add(lblMaPhieu, gbc);
                gbc.gridx = 1;
                JLabel valMaPhieu = new JLabel(phieu.getMaPhieu());
                valMaPhieu.setFont(fontValue);
                pnlMain.add(valMaPhieu, gbc);

                // Bàn
                gbc.gridx = 0; gbc.gridy = 1;
                JLabel lblBan = new JLabel("Bàn:");
                lblBan.setFont(fontLabel);
                pnlMain.add(lblBan, gbc);
                gbc.gridx = 1;
                JLabel valBan = new JLabel(tenBan);
                valBan.setFont(fontValue);
                pnlMain.add(valBan, gbc);

                // Tên khách
                gbc.gridx = 0; gbc.gridy = 2;
                JLabel lblTen = new JLabel("Tên khách:");
                lblTen.setFont(fontLabel);
                pnlMain.add(lblTen, gbc);
                gbc.gridx = 1;
                JLabel valTen = new JLabel(phieu.getTenKhach() != null ? phieu.getTenKhach() : "Không có");
                valTen.setFont(fontValue);
                pnlMain.add(valTen, gbc);

                // Số người
                gbc.gridx = 0; gbc.gridy = 3;
                JLabel lblSoNguoi = new JLabel("Số người:");
                lblSoNguoi.setFont(fontLabel);
                pnlMain.add(lblSoNguoi, gbc);
                gbc.gridx = 1;
                JLabel valSoNguoi = new JLabel(String.valueOf(phieu.getSoNguoi()));
                valSoNguoi.setFont(fontValue);
                pnlMain.add(valSoNguoi, gbc);

                // Ngày đến
                gbc.gridx = 0; gbc.gridy = 4;
                JLabel lblNgay = new JLabel("Ngày đến:");
                lblNgay.setFont(fontLabel);
                pnlMain.add(lblNgay, gbc);
                gbc.gridx = 1;
                String ngayStr = new SimpleDateFormat("dd/MM/yyyy").format(phieu.getNgayDen());
                JLabel valNgay = new JLabel(ngayStr);
                valNgay.setFont(fontValue);
                pnlMain.add(valNgay, gbc);

                // Giờ đến
                gbc.gridx = 0; gbc.gridy = 5;
                JLabel lblGio = new JLabel("Giờ đến:");
                lblGio.setFont(fontLabel);
                pnlMain.add(lblGio, gbc);
                gbc.gridx = 1;
                JLabel valGio = new JLabel(phieu.getGioDen().toString().substring(0, 5));
                valGio.setFont(fontValue);
                pnlMain.add(valGio, gbc);

                gbc.gridx = 0; gbc.gridy = 6;
                JLabel lblTienCoc = new JLabel("Tiền cọc:");
                lblTienCoc.setFont(fontLabel);
                pnlMain.add(lblTienCoc, gbc);
                gbc.gridx = 1;
                
                String tienCocStr = phieu.getTienCoc() > 0 
                    ? String.format("%,.0f VNĐ", phieu.getTienCoc()).replace(",", ".") 
                    : "0 VNĐ";

                JLabel valTienCoc = new JLabel(tienCocStr);
                valTienCoc.setFont(fontValue);
                pnlMain.add(valTienCoc, gbc);                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        JScrollPane scroll = new JScrollPane(pnlMain);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // === PANEL NÚT ===
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setBackground(Color.WHITE);

        if (phieu != null) {
            JButton btnChuyen = new JButton("Chuyển sang phục vụ");
            ThanhTacVu.kieuNut(btnChuyen, new Color(46, 204, 113));
            btnChuyen.setForeground(Color.WHITE);
            btnChuyen.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            btnChuyen.addActionListener(e -> chuyenSangPhucVu());
            pnlButtons.add(btnChuyen);

            JButton btnHuy = new JButton("Hủy");
            ThanhTacVu.kieuNut(btnHuy, new Color(231, 76, 60));
            btnHuy.setForeground(Color.WHITE);
            btnHuy.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            btnHuy.addActionListener(e -> {
                if (phieu != null) {
                    frmBanCha.moFormLyDoHuy(phieu, null, getDefaultCloseOperation());
                    setVisible(false);
                }
            });            
            pnlButtons.add(btnHuy);
        }

        JButton btnDong = new JButton("Đóng");
        ThanhTacVu.kieuNut(btnDong, new Color(149, 165, 166));
        btnDong.setForeground(Color.WHITE);
        btnDong.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnDong.addActionListener(e -> dispose());
        pnlButtons.add(btnDong);

        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void chuyenSangPhucVu() {
        if (phieu == null) {
            JOptionPane.showMessageDialog(this, "Không có phiếu để chuyển!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Chuyển phiếu " + phieu.getMaPhieu() + " sang phục vụ?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            // 1. CẬP NHẬT PHIẾU (GIỮ NGUYÊN THÔNG TIN KHÁCH)
            phieu.setTrangThai("Phục vụ");
            phieu.setNgayDen(new java.sql.Date(System.currentTimeMillis()));
            phieu.setGioDen(new java.sql.Time(System.currentTimeMillis()));
            phieuDAO.update(phieu);

            // 2. TẠO LOAIBAN_DAO MỚI
            LoaiBan_DAO loaiBanDAO = new LoaiBan_DAO(ConnectSQL.getConnection());

            // 3. MỞ FRMPHUCVU VỚI PHIẾU ĐÚNG
            FrmPhucVu frm = new FrmPhucVu(
            	frmBanCha,
                phieu.getMaBan(),
                phieu,           
                phieuDAO,
                banDAO,
                loaiBanDAO
            );
            frm.setVisible(true);

            // 4. CẬP NHẬT LẠI GIAO DIỆN
            frmBanCha.taiLaiBangChinh();
            setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi chuyển trạng thái: " + e.getMessage());
        }
    }


}