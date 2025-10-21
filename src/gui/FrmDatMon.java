//FrmDatMon
package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import entity.MonAn;

public class FrmDatMon extends JDialog {
    
    // ===== MÀU =====
    private static final Color COLOR_RED_WINE = new Color(169, 55, 68);
    private static final Color COLOR_PINK = new Color(241, 200, 204);
    private static final Color COLOR_GREEN = new Color(102, 210, 74);
    
    // ===== GIỎ HÀNG =====
    private List<GioHangItem> gioHang = new ArrayList<>();
    private DefaultTableModel tblModel;
    private double tongTien = 0;
    
    // UI Components
    private FrmThucDon frmThucDon;
    private JLabel lblTongTien;
    private JButton btnGoiPhucVu;
    private JTable tblGioHang; 
    
    // ===== BIẾN THÊM =====
    private String maBan = "";
    private String maPhieu = "";
    
    // ===== INNER CLASS GIỎ HÀNG =====
    class GioHangItem {
        MonAn mon;
        int soLuong;
        
        GioHangItem(MonAn mon, int soLuong) {
            this.mon = mon;
            this.soLuong = soLuong;
        }
        
        double getThanhTien() { return mon.getDonGia() * soLuong; }
    }

    // ===== CONSTRUCTOR 1: CHO FRMDATBAN =====
    public FrmDatMon(JDialog parent, String maBan) throws SQLException {
        super(parent, "ĐẶT MÓN - BÀN " + maBan, true);
        this.maBan = maBan;
        setSize(1400, 800);
        setLocationRelativeTo(parent);
        taoUI();
        setupListener();
        setVisible(true);
    }

    // ===== CONSTRUCTOR 2: CHO FRMPHUCVU =====
    public FrmDatMon(JDialog parent, String maBan, String maPhieu) throws SQLException {
        super(parent, "THÊM MÓN - BÀN " + maBan, true);
        this.maBan = maBan;
        this.maPhieu = maPhieu;
        setSize(1400, 800);
        setLocationRelativeTo(parent);
        taoUI();
        setupListener();
        setVisible(true);
    }

    // ===== CONSTRUCTOR 3: CHO TEST =====
    public FrmDatMon(FrmThucDon parent) throws SQLException {
        super(parent, "ĐẶT MÓN", true);
        this.frmThucDon = parent;
        setSize(1200, 700);
        setLocationRelativeTo(parent);
        taoUI();
        setupListener();
        setVisible(true);
    }
    
    private void taoUI() {
        // ===== HEADER =====
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_RED_WINE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        
        JLabel lblTitle = new JLabel("ĐẶT MÓN", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);
        
        // ===== SPLIT PANE CHÍNH =====
        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitMain.setDividerLocation(600);
        
        // TRÁI: GIỎ HÀNG
        splitMain.setRightComponent(taoGioHangPanel());
        if (frmThucDon == null) {
			frmThucDon = new FrmThucDon();
		}
		splitMain.setLeftComponent(frmThucDon.getContentPane());
        
        add(splitMain, BorderLayout.CENTER);
        
        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("TỔNG TIỀN: 0 đ");
        lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTongTien.setForeground(COLOR_RED_WINE);
        pnlTongTien.add(lblTongTien);
       
        pnlFooter.add(pnlTongTien, BorderLayout.CENTER);
        add(pnlFooter, BorderLayout.SOUTH);
    }
    
    private JPanel taoGioHangPanel() {
        JPanel pnlGioHang = new JPanel(new BorderLayout());
        pnlGioHang.setBackground(Color.WHITE);
        pnlGioHang.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_RED_WINE, 2), 
            "FORM ĐẶT MÓN", 
            2, 2, 
            new Font("Times New Roman", Font.BOLD, 18), 
            COLOR_RED_WINE
        ));
        
        // Table giỏ hàng
        String[] cols = {"STT", "MÓN ĂN", "GIÁ", "SL", "THÀNH TIỀN"};
        tblModel = new DefaultTableModel(cols, 0);
        
        tblGioHang = new JTable(tblModel); 
        tblGioHang.setRowHeight(40);
        tblGioHang.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblGioHang.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblGioHang.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrGioHang = new JScrollPane(tblGioHang);
        pnlGioHang.add(scrGioHang, BorderLayout.CENTER);
        
        // Nút điều khiển
        JPanel pnlCtrl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlCtrl.setBackground(Color.WHITE);
        
        JButton btnXoa = new JButton("XÓA");
        JButton btnXoaTatCa = new JButton("XÓA TẤT CẢ");
        
        btnXoa.setPreferredSize(new Dimension(100, 35));
        btnXoaTatCa.setPreferredSize(new Dimension(130, 35));
        btnXoa.setBackground(Color.RED);
        btnXoa.setForeground(Color.WHITE);
        btnXoaTatCa.setBackground(Color.RED);
        btnXoaTatCa.setForeground(Color.WHITE);
        
        pnlCtrl.add(btnXoa);
        pnlCtrl.add(btnXoaTatCa);
        pnlGioHang.add(pnlCtrl, BorderLayout.SOUTH);
        
        // Events
        btnXoa.addActionListener(e -> xoaHang());
        btnXoaTatCa.addActionListener(e -> xoaTatCa());
        
        return pnlGioHang;
    }
    
    private void setupListener() {
       
        
    }
    
    // ===== THÊM GIỎ HÀNG =====
    private void themVaoGioHang(MonAn mon) {
        // Tìm xem đã có trong giỏ chưa
        for (GioHangItem item : gioHang) {
            if (item.mon.getMaMon().equals(mon.getMaMon())) {
                item.soLuong++;
                capNhatTable();
                capNhatTongTien();
                return;
            }
        }
        
        // Thêm mới
        gioHang.add(new GioHangItem(mon, 1));
        capNhatTable();
        capNhatTongTien();
        
        JOptionPane.showMessageDialog(this, 
            "Đã thêm: " + mon.getTenMon(), 
            "Thêm thành công", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ===== CẬP NHẬT TABLE =====
    private void capNhatTable() {
        tblModel.setRowCount(0);
        int stt = 1;
        for (GioHangItem item : gioHang) {
            Object[] row = {
                stt++,
                item.mon.getTenMon(),
                String.format("%,.0f đ", item.mon.getDonGia()),
                item.soLuong,
                String.format("%,.0f đ", item.getThanhTien())
            };
            tblModel.addRow(row);
        }
    }
    
    // ===== CẬP NHẬT TỔNG TIỀN =====
    private void capNhatTongTien() {
        tongTien = gioHang.stream().mapToDouble(GioHangItem::getThanhTien).sum();
        lblTongTien.setText(String.format("TỔNG TIỀN: %,d đ", (int)tongTien));
    }
    
    // ===== XÓA HÀNG - SỬA LỖI row =====
    private void xoaHang() {
        int row = tblGioHang.getSelectedRow(); 
        if (row >= 0) {
            gioHang.remove(row);
            capNhatTable();
            capNhatTongTien();
            JOptionPane.showMessageDialog(this, "Đã xóa món!");
        } else {
            JOptionPane.showMessageDialog(this, "Chọn món để xóa!");
        }
    }
    
    // ===== XÓA TẤT CẢ =====
    private void xoaTatCa() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xóa tất cả món trong giỏ?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gioHang.clear();
            capNhatTable();
            capNhatTongTien();
        }
    }
    // ===== GETTER CHO FRMDATBAN =====
    public String getDanhSachMonDat() {
        if (gioHang.isEmpty()) return "";
        return gioHang.stream()
            .map(item -> String.format("• %dx %s (%,.0f đ)", 
                item.soLuong, item.mon.getTenMon(), item.getThanhTien()))
            .reduce("", (a, b) -> a + "\n" + b);
    }
    
    // ===== MAIN TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FrmThucDon thucDon = new FrmThucDon();
                new FrmDatMon(thucDon);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

	public double getTongTienMon() {
		// TODO Auto-generated method stub
		return 0;
	}
}