package gui;

import java.awt.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import dao.Ban_DAO;
import dao.PhieuDatBan_DAO;
import entity.Ban;
import entity.PhieuDatBan;

public class BanDialog extends JDialog {
    private final Color COLOR_RED_WINE = new Color(169, 55, 68);
    protected DefaultTableModel model;
    protected JTable table;
    protected JTextField txtBanSo;
    protected JButton btnTim, btnXacNhan;
    protected String banChinh;
    protected Ban_DAO banDAO;
    protected PhieuDatBan_DAO phieuDatBanDAO;
    protected Connection connection;
    protected FrmBan parentFrame;
    private String trangThaiChinh;
    private JLabel lblBanChon;

    public BanDialog(FrmBan parent, String banChinh, Ban_DAO banDAO, PhieuDatBan_DAO phieuDatBanDAO, Connection connection) {
        super(parent, "Chuyển Bàn", true);
        this.banChinh = banChinh;
        this.banDAO = banDAO;
        this.phieuDatBanDAO = phieuDatBanDAO;
        this.connection = connection;
        this.parentFrame = parent;
        this.trangThaiChinh = parent.layTrangThaiHienTai(banChinh);

        if (trangThaiChinh == null || "Trống".equals(trangThaiChinh)) {
            JOptionPane.showMessageDialog(null,
                "Bàn " + banChinh + " không hợp lệ để chuyển (trạng thái: " +
                (trangThaiChinh != null ? trangThaiChinh : "Không xác định") + ").\n" +
                "Chỉ chuyển được bàn có trạng thái Đặt hoặc Phục vụ.");
            dispose();
            throw new IllegalStateException("Không thể khởi tạo form chuyển bàn với trạng thái không hợp lệ.");
        }

        khoiTaoGiaoDien();
        taiDuLieu();
        thietLapSuKien();
    }

    private void kieuNut(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(parentFrame);

        JPanel pnlHeader = new JPanel(new BorderLayout(10, 20));
        JLabel lblTieuDe = new JLabel("Chuyển bàn", SwingConstants.CENTER);
        lblTieuDe.setBackground(COLOR_RED_WINE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 28));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        pnlHeader.add(lblTieuDe, BorderLayout.NORTH);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblBanChon = new JLabel("Mã bàn hiện tại: " + banChinh + " >> ");
        lblBanChon.setFont(new Font("Times New Roman", Font.BOLD, 22));
        
        txtBanSo = new JTextField(10);
        txtBanSo.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Times New Roman", Font.BOLD, 22));
        kieuNut(btnTim, new Color(70, 130, 180), Color.white);
        btnTim.setForeground(Color.WHITE);
        btnTim.setFocusPainted(false);
        
        pnlSearch.add(lblBanChon);
        pnlSearch.add(Box.createHorizontalStrut(200));
        
        JLabel lblMaBan = new JLabel("Mã bàn ");
        lblMaBan.setFont(new Font("Times New Roman", Font.BOLD, 22));
        pnlSearch.add(Box.createHorizontalStrut(40));
        pnlSearch.add(lblMaBan);
        pnlSearch.add(txtBanSo);
        pnlSearch.add(btnTim);
        
        pnlHeader.add(pnlSearch, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"Mã bàn", "Trạng thái", "Loại bàn", "Số chỗ ngồi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXacNhan = new JButton("Chuyển");
        kieuNut(btnXacNhan, new Color(46, 204, 113), Color.WHITE);
        JButton btnHuy = new JButton("Hủy");
        kieuNut(btnHuy, new Color(231, 76, 60), Color.WHITE);
        pnlNut.add(btnXacNhan);
        pnlNut.add(btnHuy);
        add(pnlNut, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());
    }

    protected void taiDuLieu() {
        model.setRowCount(0);
        try {
            List<Ban> banTrong = banDAO.getBanTrong();
            if (banTrong != null) {
                for (Ban ban : banTrong) {
                    String trangThai = parentFrame.layTrangThaiHienTai(ban.getMaBan());
                    if ("Trống".equals(trangThai) && !ban.getMaBan().equals(banChinh)) {
                        model.addRow(new Object[]{
                            ban.getMaBan(), trangThai, ban.getTenLoai(), ban.getSoChoNgoi()
                        });
                    }
                }

                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không có bàn trống nào để chuyển!");
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không thể tải danh sách bàn trống!");
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách bàn trống!");
            dispose();
        }
    }

    protected boolean thucHienHanhDong(String banDich) {
        PreparedStatement ps = null;
        try {
            String trangThaiBanDich = parentFrame.layTrangThaiHienTai(banDich);
            if (!"Trống".equals(trangThaiBanDich)) {
                JOptionPane.showMessageDialog(this, "Bàn đích " + banDich + " không trống!");
                return false;
            }

            Date homNay = new Date(System.currentTimeMillis());
            List<PhieuDatBan> danhSachPhieu = phieuDatBanDAO.getDatBanByBanAndNgay(banChinh, homNay);

            if (danhSachPhieu == null || danhSachPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bàn " + banChinh + " không có phiếu!");
                return false;
            }

            connection.setAutoCommit(false);

            List<String> maPhieuDaCapNhat = new ArrayList<>();
            int soLuongCapNhat = 0;

            for (PhieuDatBan phieu : danhSachPhieu) {
                String trangThaiPhieu = phieu.getTrangThai();
                if (trangThaiPhieu != null &&
                    ("Đặt".equals(trangThaiPhieu.trim()) || "Phục vụ".equals(trangThaiPhieu.trim()))) {

                    String maPhieuCu = phieu.getMaPhieu();
                    String maBanCu = phieu.getMaBan();

                    try {
                        PhieuDatBan phieuUpdate = new PhieuDatBan();
                        phieuUpdate.setMaPhieu(maPhieuCu);
                        phieuUpdate.setMaBan(banDich);
                        phieuUpdate.setTenKhach(phieu.getTenKhach());
                        phieuUpdate.setSoNguoi(phieu.getSoNguoi());
                        phieuUpdate.setTrangThai(phieu.getTrangThai());
                        phieuUpdate.setNgayDen(phieu.getNgayDen());
                        phieuUpdate.setGioDen(phieu.getGioDen());
                        phieuUpdate.setGhiChu(phieu.getGhiChu());
                        phieuUpdate.setTienCoc(phieu.getTienCoc());
                        phieuUpdate.setGhiChuCoc(phieu.getGhiChuCoc());
                        phieuUpdate.setSoDienThoai(phieu.getSoDienThoai());

                        phieuDatBanDAO.update(phieuUpdate);
                        soLuongCapNhat++;
                        maPhieuDaCapNhat.add(maPhieuCu);

                    } catch (Exception ex) {
                        for (String maPhieuRollback : maPhieuDaCapNhat) {
                            try {
                                PhieuDatBan phieuRollback = phieuDatBanDAO.getByMa(maPhieuRollback);
                                if (phieuRollback != null) {
                                    phieuRollback.setMaBan(maBanCu);
                                    phieuDatBanDAO.update(phieuRollback);
                                }
                            } catch (Exception rollbackEx) {
                                System.err.println("Rollback lỗi: " + rollbackEx.getMessage());
                            }
                        }
                        connection.rollback();
                        JOptionPane.showMessageDialog(this, "Lỗi chuyển: " + ex.getMessage());
                        return false;
                    }
                }
            }

            if (soLuongCapNhat > 0) {
                Ban ban = banDAO.getBanByMa(banChinh);
                if (ban != null) {
                    ban.setTrangThai("Trống");
                    banDAO.capNhatBan(ban);
                }
                connection.commit();
                
                return true;
            } else {
                connection.rollback();
                JOptionPane.showMessageDialog(this, "Không có phiếu nào được chuyển!");
                return false;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi DB: " + e.getMessage());
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void thietLapSuKien() {
        btnTim.addActionListener(e -> {
            String banSo = txtBanSo.getText().trim();
            if (!banSo.isEmpty()) {
                model.setRowCount(0);
                try {
                    Ban ban = banDAO.getBanByMa(banSo);
                    if (ban != null && "Trống".equals(parentFrame.layTrangThaiHienTai(ban.getMaBan())) &&
                        !ban.getMaBan().equals(banChinh)) {
                        model.addRow(new Object[]{
                            ban.getMaBan(), "Trống", ban.getTenLoai(), ban.getSoChoNgoi()
                        });
                        lblBanChon.setText("Mã bàn hiện tại: " + banChinh + " >> " + ban.getMaBan());
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy bàn trống với mã " + banSo);
                        lblBanChon.setText(banChinh + " >> ");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm bàn!");
                    lblBanChon.setText("Mã bàn hiện tại: " + banChinh + " >> ");
                }
            } else {
                taiDuLieu();
                lblBanChon.setText("Mã bàn hiện tại: " +banChinh + " >> ");
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selected = table.getSelectedRow();
                if (selected >= 0) {
                    String banDich = (String) model.getValueAt(selected, 0);
                    lblBanChon.setText("Mã bàn hiện tại: " + banChinh + " >> " + banDich);
                } else {
                    lblBanChon.setText("Mã bàn hiện tại: " + banChinh + " >> ");
                }
            }
        });

        btnXacNhan.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String banDich = (String) model.getValueAt(selected, 0);
                if (thucHienHanhDong(banDich)) {
                    JOptionPane.showMessageDialog(this, "Đã chuyển bàn thành công!");
                    parentFrame.taiLaiBangChinh();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi chuyển bàn!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để chuyển!");
            }
        });
    }
}