package dialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FrmTatCaLich extends JFrame {
    private PhieuDatBan_DAO phieuDAO;
    private Ban_DAO banDAO;
    private DefaultTableModel model;
    private JTable table;
	private int row;
    private final Color mau_Ruou_Vang = new Color(169, 55, 68);
    private FrmBan frmBanCha;


    public FrmTatCaLich(JFrame parent, PhieuDatBan_DAO phieuDAO, Ban_DAO banDAO) {
        this.frmBanCha = (FrmBan) parent;
        this.phieuDAO = phieuDAO;
        this.banDAO = banDAO;

        setSize(1150, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        initComponents();
        taiDuLieu();
    }

	private void initComponents() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(128, 0, 0), 2),
            "Danh sách đặt bàn",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 26),
            mau_Ruou_Vang
        ));

        // Bảng
        String[] cols = {"Mã phiếu", "Bàn", "Tên KH", "SĐT", "Số người", "Ngày đến", "Giờ đến", "Tiền cọc"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row = table.getSelectedRow();
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlMain.add(scroll, BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
        

        // === NÚT ĐÓNG ===
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnChuyen = new JButton("Chuyển sang phục vụ");
        ThanhTacVu.kieuNut(btnChuyen, new Color(55, 212, 23));
        btnChuyen.setForeground(Color.WHITE);
        btnChuyen.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnChuyen.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(FrmTatCaLich.this, "Vui lòng chọn một phiếu để chuyển!");
                return;
            }
            chuyenSangPhucVu();
        });        pnlButtons.add(btnChuyen);
        
        JButton btnHuy = new JButton("Hủy");
        ThanhTacVu.kieuNut(btnHuy, new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnHuy.addActionListener(e -> {
            if (row == -1) {
                JOptionPane.showMessageDialog(FrmTatCaLich.this, "Vui lòng chọn phiếu để hủy!");
                return;
            }

            String maPhieu = (String) model.getValueAt(row, 0);
            try {
                PhieuDatBan p = phieuDAO.getByMa(maPhieu);
                if (p == null) {
                    JOptionPane.showMessageDialog(FrmTatCaLich.this, "Không tìm thấy phiếu!");
                    return;
                }

                frmBanCha.moFormLyDoHuy(p, model, row);

                // CẬP NHẬT SAU KHI HỦY
                taiDuLieu();
                frmBanCha.taiLaiBangChinh();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(FrmTatCaLich.this, "Lỗi!");
            }
        });
        pnlButtons.add(btnHuy);
        
        JButton btnDong = new JButton("Đóng");
        ThanhTacVu.kieuNut(btnDong, new Color(149, 165, 166));
        btnDong.setForeground(Color.WHITE);
        btnDong.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnDong.addActionListener(e -> dispose());
        pnlButtons.add(btnDong);

        add(pnlButtons, BorderLayout.SOUTH);
    }

    public void taiDuLieu() {
    try {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        List<PhieuDatBan> list = phieuDAO.getAll(today);
        model.setRowCount(0);

        NumberFormat vnd = NumberFormat.getInstance(new Locale("vi", "VN"));

        for (PhieuDatBan p : list) {
            if (!"Đặt".equals(p.getTrangThai())) continue;

            Ban b = banDAO.getBanByMa(p.getMaBan());
            String tenBan = b != null ? b.getMaBan() + " (" + b.getTenKhuVuc() + ")" : p.getMaBan();

            model.addRow(new Object[]{
                p.getMaPhieu(),
                tenBan,
                p.getTenKhach() != null ? p.getTenKhach() : "Không có",
                p.getSoDienThoai() != null ? p.getSoDienThoai() : "Không có",
                p.getSoNguoi(),
                new SimpleDateFormat("dd/MM/yyyy").format(p.getNgayDen()),
                p.getGioDen().toString().substring(0, 5),
                vnd.format(p.getTienCoc()) + ".000 VNĐ"  
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu!");
    }
}

    private void chuyenSangPhucVu() {
        String maPhieu = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Chuyển phiếu " + maPhieu + " sang phục vụ?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_NO_OPTION) return;

        try {
            PhieuDatBan p = phieuDAO.getByMa(maPhieu);
            p.setTrangThai("Phục vụ");
//            p.setTenKhach(null);
//            p.setSoDienThoai(null);
//            p.setSoNguoi(0);
            p.setNgayDen(new java.sql.Date(System.currentTimeMillis()));
            p.setGioDen(new java.sql.Time(System.currentTimeMillis()));
            phieuDAO.update(p);

            JOptionPane.showMessageDialog(this, "Đã chuyển sang phục vụ!");
            String maBan = p.getMaBan();
            LoaiBan_DAO loaiBanDAO = new LoaiBan_DAO(ConnectSQL.getConnection());
            FrmPhucVu frm = new FrmPhucVu(
                frmBanCha, maBan, p, phieuDAO, banDAO, loaiBanDAO
            );
            frm.setVisible(true);

            taiDuLieu();
            frmBanCha.taiLaiBangChinh();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi chuyển trạng thái!");
        }
    }
    
}