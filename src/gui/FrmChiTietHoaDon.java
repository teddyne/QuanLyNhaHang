package gui;

import dao.HoaDon_DAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.itextpdf.text.pdf.BaseFont;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FrmChiTietHoaDon extends JFrame implements ActionListener{
    private JLabel lblTenKH, lblSDT, lblBan, lblNhanVien, lblNgayLap, lblTongTien, lblKhuyenMai;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnXuatPDF, btnDong;

    public FrmChiTietHoaDon(String maHD) {
        setTitle("Chi tiết hóa đơn");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== PANEL CHÍNH =====
        JPanel pMain = new JPanel(new BorderLayout(15, 15));
        pMain.setBackground(Color.WHITE);
        pMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== THÔNG TIN HÓA ĐƠN =====
        JPanel pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
        pTop.setBackground(Color.WHITE);
        pTop.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Thông tin hóa đơn", 0, 0,
                new Font("Times New Roman", Font.BOLD, 22)
        ));

        lblTenKH = new JLabel();
        lblSDT = new JLabel();
        lblBan = new JLabel();
        lblNhanVien = new JLabel();
        lblNgayLap = new JLabel();
        lblKhuyenMai = new JLabel();
        lblTongTien = new JLabel();

        addRow(pTop, "Khách hàng:", lblTenKH);
        addRow(pTop, "SĐT:", lblSDT);
        addRow(pTop, "Bàn:", lblBan);
        addRow(pTop, "Nhân viên lập:", lblNhanVien);
        addRow(pTop, "Ngày lập:", lblNgayLap);
        addRow(pTop, "Khuyến mãi:", lblKhuyenMai);
        addRow(pTop, "Tổng tiền:", lblTongTien);

        pMain.add(pTop, BorderLayout.NORTH);

        // ===== BẢNG CHI TIẾT MÓN =====
        String[] cols = {"Mã món", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setRowHeight(28);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42), 2),
                "Danh sách món ăn", 0, 0,
                new Font("Times New Roman", Font.BOLD, 22)
        ));
        scroll.setBackground(Color.WHITE);
        pMain.add(scroll, BorderLayout.CENTER);

        //CÁC NÚT
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pBottom.setBackground(Color.WHITE);
        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        Dimension btnSize = new Dimension(135, 35);

        btnXuatPDF = taoNut("Xuất PDF", new Color(46, 204, 113), btnSize, btnFont);
        btnDong = taoNut("Đóng", new Color(231, 76, 60), btnSize, btnFont);
        
        //Gán ActionListener cho tất cả nút
        for (JButton btn : new JButton[]{btnXuatPDF, btnDong}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
        }
        
        pBottom.add(btnXuatPDF);
        pBottom.add(btnDong);
        pMain.add(pBottom, BorderLayout.SOUTH);

        btnXuatPDF.addActionListener(e -> xuatHoaDonPDF(maHD));
        btnDong.addActionListener(e -> dispose());

        add(pMain);
        loadData(maHD);
    }

    private void addRow(JPanel panel, String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 25));
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 18));
        valueLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        row.add(lbl);
        row.add(valueLabel);
        panel.add(row);
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
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    private void loadData(String maHD) {
        HoaDon_DAO hdDAO = new HoaDon_DAO();
        Object[] thongTin = hdDAO.layThongTinHoaDon(maHD);

        if (thongTin != null) {
            lblTenKH.setText(thongTin[0] + "");
            lblSDT.setText(thongTin[1] + "");
            lblBan.setText(thongTin[2] + "");
            lblNhanVien.setText(thongTin[3] + "");
            lblNgayLap.setText(thongTin[4] + "");
            lblKhuyenMai.setText(thongTin[5] + "");
            lblTongTien.setText(thongTin[6] + "");
        }

        List<Object[]> chiTiet = hdDAO.layChiTietHoaDon(maHD);
        model.setRowCount(0);
        for (Object[] ct : chiTiet) {
            model.addRow(ct);
        }
    }

//    private void xuatHoaDonPDF(String maHD) {
//        try {
//            String fileName = "HoaDon_" + maHD + ".pdf";
//            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileName));
//            document.open();
//
//            String fontPath = "C:/Windows/Fonts/arial.ttf";
//            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(
//                    com.itextpdf.text.pdf.BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
//                    20, com.itextpdf.text.Font.BOLD);
//
//            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(
//                    com.itextpdf.text.pdf.BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
//                    12, com.itextpdf.text.Font.NORMAL);
//
//            document.add(new com.itextpdf.text.Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle));
//            document.add(new com.itextpdf.text.Paragraph(" ", fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Mã hóa đơn: " + maHD, fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Khách hàng: " + lblTenKH.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("SĐT: " + lblSDT.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Bàn: " + lblBan.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Nhân viên: " + lblNhanVien.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Ngày lập: " + lblNgayLap.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Khuyến mãi: " + lblKhuyenMai.getText(), fontNormal));
//            document.add(new com.itextpdf.text.Paragraph("Tổng tiền: " + lblTongTien.getText(), fontNormal));
//
//            document.add(new com.itextpdf.text.Paragraph(" ", fontNormal));
//            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(5);
//            pdfTable.setWidthPercentage(100);
//
//            String[] headers = {"Mã món", "Tên món", "Số lượng", "Đơn giá", "Thành tiền"};
//            for (String header : headers) {
//                com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(
//                        new com.itextpdf.text.Phrase(header, fontNormal));
//                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(230, 230, 230));
//                pdfTable.addCell(cell);
//            }
//
//            for (int i = 0; i < model.getRowCount(); i++) {
//                for (int j = 0; j < model.getColumnCount(); j++) {
//                    pdfTable.addCell(new com.itextpdf.text.Phrase(model.getValueAt(i, j).toString(), fontNormal));
//                }
//            }
//
//            document.add(pdfTable);
//            document.close();
//
//            JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn ra file PDF: " + fileName);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
//        }
//    }

    private void xuatHoaDonPDF(String maHD) {
        try {
            String fileName = "HoaDon_" + maHD + ".pdf";
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileName));
            document.open();

            // ===== Font tiếng Việt =====
            String fontPath = "C:/Windows/Fonts/times.ttf";
            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont(fontPath,
                    com.itextpdf.text.pdf.BaseFont.IDENTITY_H,
                    com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);

            // ===== Header nhà hàng =====
            com.itextpdf.text.Paragraph header = new com.itextpdf.text.Paragraph("NHÀ HÀNG VANG\n", fontTitle);
            header.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(header);

            com.itextpdf.text.Paragraph info = new com.itextpdf.text.Paragraph(
                    "Địa chỉ: 12 Nguyễn Văn Bảo, quận Gò Vấp, TP.HCM\n"
                            + "Điện thoại: 0987654321\n\n",
                    fontNormal);
            info.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(info);

            // ===== Tiêu đề hóa đơn =====
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("HÓA ĐƠN THANH TOÁN\n\n", fontTitle);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);

            // ===== Thông tin hóa đơn =====
            document.add(new com.itextpdf.text.Paragraph("Số HĐ: " + maHD, fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Ngày: " + lblNgayLap.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Bàn số: " + lblBan.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Khách hàng: " + lblTenKH.getText(), fontNormal));
            document.add(new com.itextpdf.text.Paragraph("Nhân viên phục vụ: " + lblNhanVien.getText() + "\n\n", fontNormal));

            // ===== Bảng chi tiết món ăn =====
            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(new float[]{1, 4, 2, 2, 3});
            pdfTable.setWidthPercentage(100);
            String[] headers = {"STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền"};

            for (String h : headers) {
                com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(
                        new com.itextpdf.text.Phrase(h, fontBold));
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(220, 220, 220));
                pdfTable.addCell(cell);
            }

            double tongTien = 0;
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
            for (int i = 0; i < model.getRowCount(); i++) {
                pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(i + 1), fontNormal));
                pdfTable.addCell(new com.itextpdf.text.Phrase(model.getValueAt(i, 1).toString(), fontNormal)); // Tên món
                pdfTable.addCell(new com.itextpdf.text.Phrase(model.getValueAt(i, 3).toString(), fontNormal)); // Đơn giá
                pdfTable.addCell(new com.itextpdf.text.Phrase(model.getValueAt(i, 2).toString(), fontNormal)); // SL
                pdfTable.addCell(new com.itextpdf.text.Phrase(model.getValueAt(i, 4).toString(), fontNormal)); // Thành tiền

                try {
                    tongTien += Double.parseDouble(model.getValueAt(i, 4).toString().replace(",", ""));
                } catch (Exception ignore) {}
            }
            document.add(pdfTable);

            // ===== Tính toán tổng =====
            double vat = tongTien * 0.08;
            double giamGia = 0;
            try {
                String textKM = lblKhuyenMai.getText().replace("%", "").trim();
                if (!textKM.isEmpty()) {
                    giamGia = tongTien * Double.parseDouble(textKM) / 100.0;
                }
            } catch (Exception ignore) {}
            double thanhTien = tongTien + vat - giamGia;

            document.add(new com.itextpdf.text.Paragraph("\nTổng tiền: " + df.format(tongTien), fontBold));
            document.add(new com.itextpdf.text.Paragraph("Thuế VAT (8%): " + df.format(vat), fontBold));
            document.add(new com.itextpdf.text.Paragraph("Giảm giá/Khuyến mãi: " + df.format(giamGia), fontBold));
            document.add(new com.itextpdf.text.Paragraph("Thành tiền: " + df.format(thanhTien) + "\n\n", fontBold));

            // ===== Footer cảm ơn =====
            com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph(
                    "Trân trọng cảm ơn quý khách. Hẹn gặp lại!", fontNormal);
            footer.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            // ===== Thông báo và mở file =====
            JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn ra file: " + fileName);
            java.awt.Desktop.getDesktop().open(new java.io.File(fileName));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}



