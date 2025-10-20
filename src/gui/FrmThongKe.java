package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import com.toedter.calendar.JDateChooser;

import dao.HoaDon_DAO;

public class FrmThongKe extends JPanel implements ActionListener {

    private final Color COLOR_PRIMARY = new Color(221, 44, 0); 
    private final Color COLOR_SECONDARY = new Color(245, 245, 245); 
    private final Color COLOR_TEXT = new Color(51, 51, 51); 
    private final Color COLOR_PANEL_BG = Color.WHITE; 
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_GENERAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14); 

    private HoaDon_DAO hoaDonDAO;

    // Components cho Tab 1
    private JDateChooser dateChooserBatDau1, dateChooserKetThuc1;
    private JButton btnXemTongQuan, btnXuatExcel1;
    private JLabel lblTongDoanhThu, lblTongSoHoaDon, lblDoanhThuTrungBinh;
    private JPanel pnlBieuDoTongQuan;
    private List<Object[]> duLieuThongKeTongQuan;

    // Components cho Tab 2
    private JDateChooser dateChooserBatDau2, dateChooserKetThuc2;
    private JButton btnXemMonAn, btnXuatExcel2;
    private JPanel pnlBieuDoMonAn;
    private List<Object[]> duLieuThongKeMonAn;

    public FrmThongKe() {
        hoaDonDAO = new HoaDon_DAO();
        setLayout(new BorderLayout());
        setBackground(COLOR_SECONDARY);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setForeground(COLOR_PRIMARY);
        add(tabbedPane, BorderLayout.CENTER);

        // Thêm Tab 1
        JPanel pnlThongKeTongQuan = createTabThongKeTongQuan();
        tabbedPane.addTab("Thống kê Tổng quan", pnlThongKeTongQuan);

        // Thêm Tab 2
        JPanel pnlThongKeMonAn = createTabThongKeMonAn();
        tabbedPane.addTab("Thống kê theo Món ăn", pnlThongKeMonAn);
    }

    // =================================================================================
    // ===== PHƯƠNG THỨC TẠO TAB TỔNG QUAN (TAB 1) =====
    // =================================================================================
    private JPanel createTabThongKeTongQuan() {
        JPanel pnlTab = new JPanel(new BorderLayout(20, 20));
        pnlTab.setBackground(COLOR_SECONDARY);
        pnlTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlControl.setBackground(COLOR_PANEL_BG);
        pnlControl.setBorder(createStyledTitleBorder("Tùy chọn thời gian"));

        pnlControl.add(new JLabel("Từ ngày:"));
        dateChooserBatDau1 = new JDateChooser();
        dateChooserBatDau1.setPreferredSize(new Dimension(150, 30));
        dateChooserBatDau1.setDateFormatString("dd/MM/yyyy");
        dateChooserBatDau1.setFont(FONT_GENERAL);
        pnlControl.add(dateChooserBatDau1);

        pnlControl.add(new JLabel("Đến ngày:"));
        dateChooserKetThuc1 = new JDateChooser();
        dateChooserKetThuc1.setPreferredSize(new Dimension(150, 30));
        dateChooserKetThuc1.setDateFormatString("dd/MM/yyyy");
        dateChooserKetThuc1.setFont(FONT_GENERAL);
        pnlControl.add(dateChooserKetThuc1);

        
        btnXemTongQuan = createStyledButton("Xem thống kê", "img/preview.png");
        btnXemTongQuan.addActionListener(this);
        pnlControl.add(btnXemTongQuan);

        btnXuatExcel1 = createStyledButton("Xuất Excel", "img/excel.png");
        btnXuatExcel1.addActionListener(this);
        pnlControl.add(btnXuatExcel1);
        pnlTab.add(pnlControl, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 20));
        pnlCenter.setOpaque(false);
        pnlTab.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlResults = new JPanel(new GridLayout(1, 3, 20, 20));
        pnlResults.setOpaque(false);
        lblTongDoanhThu = createResultLabel("0 VNĐ", "Tổng Doanh thu");
        lblTongSoHoaDon = createResultLabel("0", "Tổng số Hóa đơn");
        lblDoanhThuTrungBinh = createResultLabel("0 VNĐ", "Doanh thu Trung bình/Hóa đơn");
        pnlResults.add(lblTongDoanhThu);
        pnlResults.add(lblTongSoHoaDon);
        pnlResults.add(lblDoanhThuTrungBinh);
        pnlCenter.add(pnlResults, BorderLayout.NORTH);

        pnlBieuDoTongQuan = new JPanel(new BorderLayout());
        pnlBieuDoTongQuan.setBackground(COLOR_PANEL_BG);
        pnlBieuDoTongQuan.setBorder(createStyledTitleBorder("Biểu đồ doanh thu theo ngày"));
        pnlCenter.add(pnlBieuDoTongQuan, BorderLayout.CENTER);

        return pnlTab;
    }

    // =================================================================================
    // ===== PHƯƠNG THỨC TẠO TAB MÓN ĂN (TAB 2) =====
    // =================================================================================
    private JPanel createTabThongKeMonAn() {
        JPanel pnlTab = new JPanel(new BorderLayout(10, 10));
        pnlTab.setBackground(COLOR_SECONDARY);
        pnlTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlControl.setBackground(COLOR_PANEL_BG);
        pnlControl.setBorder(createStyledTitleBorder("Tùy chọn thời gian"));

        pnlControl.add(new JLabel("Từ ngày:"));
        dateChooserBatDau2 = new JDateChooser();
        dateChooserBatDau2.setPreferredSize(new Dimension(150, 30));
        dateChooserBatDau2.setDateFormatString("dd/MM/yyyy");
        dateChooserBatDau2.setFont(FONT_GENERAL);
        pnlControl.add(dateChooserBatDau2);

        pnlControl.add(new JLabel("Đến ngày:"));
        dateChooserKetThuc2 = new JDateChooser();
        dateChooserKetThuc2.setPreferredSize(new Dimension(150, 30));
        dateChooserKetThuc2.setDateFormatString("dd/MM/yyyy");
        dateChooserKetThuc2.setFont(FONT_GENERAL);
        pnlControl.add(dateChooserKetThuc2);

        btnXemMonAn = createStyledButton("Xem thống kê món ăn", "img/preview.png");
        btnXemMonAn.addActionListener(this);
        pnlControl.add(btnXemMonAn);

        btnXuatExcel2 = createStyledButton("Xuất Excel", "img/excel.png");
        btnXuatExcel2.addActionListener(this);
        pnlControl.add(btnXuatExcel2);
        pnlTab.add(pnlControl, BorderLayout.NORTH);

        pnlBieuDoMonAn = new JPanel(new BorderLayout());
        pnlBieuDoMonAn.setBackground(COLOR_PANEL_BG);
        pnlBieuDoMonAn.setBorder(createStyledTitleBorder("Top 10 món ăn bán chạy nhất"));
        pnlTab.add(pnlBieuDoMonAn, BorderLayout.CENTER);

        return pnlTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnXemTongQuan)) {
            handleThongKeTongQuan();
        } else if (o.equals(btnXemMonAn)) {
            handleThongKeMonAn();
        } else if (o.equals(btnXuatExcel1)) {
            handleXuatBaoCaoTongQuan();
        } else if (o.equals(btnXuatExcel2)) {
            handleXuatBaoCaoMonAn();
        }
    }

    // =================================================================================
    // ===== CÁC HÀM XỬ LÝ LOGIC =====
    // =================================================================================

    private void handleThongKeTongQuan() {
        java.util.Date utilNgayBatDau = dateChooserBatDau1.getDate();
        java.util.Date utilNgayKetThuc = dateChooserKetThuc1.getDate();

        if (utilNgayBatDau == null || utilNgayKetThuc == null || utilNgayBatDau.after(utilNgayKetThuc)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu và kết thúc không hợp lệ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlNgayBatDau = new java.sql.Date(utilNgayBatDau.getTime());
        java.sql.Date sqlNgayKetThuc = new java.sql.Date(utilNgayKetThuc.getTime());

        duLieuThongKeTongQuan = hoaDonDAO.getHoaDonTheoThoiGian(sqlNgayBatDau, sqlNgayKetThuc);
        
        if(duLieuThongKeTongQuan.isEmpty()) {
        	JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }

        capNhatSoLieuTongQuan(duLieuThongKeTongQuan);
        veBieuDoDoanhThuTheoNgay(duLieuThongKeTongQuan);
    }

    private void handleThongKeMonAn() {
        java.util.Date utilNgayBatDau = dateChooserBatDau2.getDate();
        java.util.Date utilNgayKetThuc = dateChooserKetThuc2.getDate();

        if (utilNgayBatDau == null || utilNgayKetThuc == null || utilNgayBatDau.after(utilNgayKetThuc)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu và kết thúc không hợp lệ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlNgayBatDau = new java.sql.Date(utilNgayBatDau.getTime());
        java.sql.Date sqlNgayKetThuc = new java.sql.Date(utilNgayKetThuc.getTime());

        duLieuThongKeMonAn = hoaDonDAO.getTopMonAnBanChay(sqlNgayBatDau, sqlNgayKetThuc);
        
        if(duLieuThongKeMonAn.isEmpty()) {
        	JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }

        veBieuDoMonAnBanChay(duLieuThongKeMonAn);
    }

    private void handleXuatBaoCaoTongQuan() {
        if (duLieuThongKeTongQuan == null || duLieuThongKeTongQuan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để xuất. Vui lòng xem thống kê trước.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file báo cáo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                fileToSave = new File(filePath + ".xlsx");
            }
            xuatBaoCaoExcelTongQuan(duLieuThongKeTongQuan, fileToSave);
        }
    }

    private void handleXuatBaoCaoMonAn() {
        if (duLieuThongKeMonAn == null || duLieuThongKeMonAn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để xuất. Vui lòng xem thống kê trước.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file báo cáo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                fileToSave = new File(filePath + ".xlsx");
            }
            xuatBaoCaoExcelMonAn(duLieuThongKeMonAn, fileToSave);
        }
    }
    
    private void xuatBaoCaoExcelTongQuan(List<Object[]> dsHD, File file) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet summarySheet = workbook.createSheet("Tóm tắt");
			CellStyle headerStyle = createHeaderStyle(workbook);

			Row row1 = summarySheet.createRow(0);
			createStyledCell(row1, 0, "Tổng Doanh thu", headerStyle);
			createStyledCell(row1, 1, lblTongDoanhThu.getText().replaceAll("<.*?>", "").replace("Tổng Doanh thu", "").trim(), null);

			Row row2 = summarySheet.createRow(1);
			createStyledCell(row2, 0, "Tổng số Hóa đơn", headerStyle);
			createStyledCell(row2, 1, lblTongSoHoaDon.getText().replaceAll("<.*?>", "").replace("Tổng số Hóa đơn", "").trim(), null);

			Row row3 = summarySheet.createRow(2);
			createStyledCell(row3, 0, "Doanh thu Trung bình", headerStyle);
			createStyledCell(row3, 1, lblDoanhThuTrungBinh.getText().replaceAll("<.*?>", "").replace("Doanh thu Trung bình/Hóa đơn", "").trim(), null);
			
			summarySheet.autoSizeColumn(0);
			summarySheet.autoSizeColumn(1);

			XSSFSheet detailSheet = workbook.createSheet("Dữ liệu chi tiết");
			String[] headers = {"Mã HĐ", "Mã Bàn", "Ngày Lập", "Tên KH", "SĐT", "Nhân Viên", "Khuyến Mãi", "Tổng Tiền"};
			Row headerRow = detailSheet.createRow(0);

			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}

			int rowNum = 1;
			for (Object[] rowData : dsHD) {
				Row row = detailSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0].toString());
				row.createCell(1).setCellValue(rowData[1] != null ? rowData[1].toString() : "");
				row.createCell(2).setCellValue(new SimpleDateFormat("dd/MM/yyyy HH:mm").format((Date)rowData[2]));
				row.createCell(3).setCellValue(rowData[3] != null ? rowData[3].toString() : "Khách vãng lai");
				row.createCell(4).setCellValue(rowData[4] != null ? rowData[4].toString() : "");
				row.createCell(5).setCellValue(rowData[5].toString());
				row.createCell(6).setCellValue(rowData[6] != null ? rowData[6].toString() : "Không có");
				row.createCell(7).setCellValue((Double)rowData[7]);
			}
			
			for (int i = 0; i < headers.length; i++) {
				detailSheet.autoSizeColumn(i);
			}
			
			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}
			JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nĐã lưu tại: " + file.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void xuatBaoCaoExcelMonAn(List<Object[]> topMonAn, File file) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Top Món ăn Bán chạy");
			CellStyle headerStyle = createHeaderStyle(workbook);
			
			String[] headers = {"Tên Món Ăn", "Tổng Số Lượng Bán"};
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}
			
			int rowNum = 1;
			for (Object[] rowData : topMonAn) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0].toString());
				row.createCell(1).setCellValue((Integer)rowData[1]);
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			
			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}
			JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nĐã lưu tại: " + file.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}
	
	private void createStyledCell(Row row, int col, String value, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
		if (style != null) {
			cell.setCellStyle(style);
		}
	}

    private void capNhatSoLieuTongQuan(List<Object[]> dsHD) {
        double tongDoanhThu = 0;
        int tongSoHoaDon = dsHD.size();
        for (Object[] row : dsHD) {
            tongDoanhThu += (Double) row[7];
        }
        double doanhThuTB = (tongSoHoaDon > 0) ? (tongDoanhThu / tongSoHoaDon) : 0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongDoanhThu.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
                + currencyFormat.format(tongDoanhThu) + "</font><br/>Tổng Doanh thu</div></html>");
        lblTongSoHoaDon.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
                + tongSoHoaDon + "</font><br/>Tổng số Hóa đơn</div></html>");
        lblDoanhThuTrungBinh.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
                + currencyFormat.format(doanhThuTB) + "</font><br/>Doanh thu TB/Hóa đơn</div></html>");
    }

    private void veBieuDoDoanhThuTheoNgay(List<Object[]> dsHD) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> doanhThuTheoNgay = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        for (Object[] row : dsHD) {
            Date ngayLap = (Date) row[2];
            double tongTien = (Double) row[7];
            String ngayKey = sdf.format(ngayLap);
            doanhThuTheoNgay.put(ngayKey, doanhThuTheoNgay.getOrDefault(ngayKey, 0.0) + tongTien);
        }
        for (Map.Entry<String, Double> entry : doanhThuTheoNgay.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "BIỂU ĐỒ DOANH THU THEO NGÀY", "Ngày", "Doanh thu (VND)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        styleChart(barChart);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(COLOR_PANEL_BG);
        pnlBieuDoTongQuan.removeAll();
        pnlBieuDoTongQuan.add(chartPanel, BorderLayout.CENTER);
        pnlBieuDoTongQuan.revalidate();
        pnlBieuDoTongQuan.repaint();
    }

    private void veBieuDoMonAnBanChay(List<Object[]> topMonAn) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] row : topMonAn) {
            String tenMon = (String) row[0];
            int soLuong = (Integer) row[1];
            dataset.addValue(soLuong, "Số lượng bán", tenMon);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "TOP MÓN ĂN BÁN CHẠY", "Món ăn", "Số lượng",
                dataset, PlotOrientation.HORIZONTAL, false, true, false);

        styleChart(barChart);
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(COLOR_PANEL_BG);
        pnlBieuDoMonAn.removeAll();
        pnlBieuDoMonAn.add(chartPanel, BorderLayout.CENTER);
        pnlBieuDoMonAn.revalidate();
        pnlBieuDoMonAn.repaint();
    }
    
    // =================================================================================
    // ===== CÁC HÀM TIỆN ÍCH TẠO COMPONENT =====
    // =================================================================================

    private void styleChart(JFreeChart chart) {
        chart.getTitle().setPaint(COLOR_PRIMARY);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        chart.setBackgroundPaint(COLOR_PANEL_BG);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_PRIMARY);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
    }

    private TitledBorder createStyledTitleBorder(String title) {
        Border blackline = BorderFactory.createLineBorder(Color.lightGray);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                blackline, title, TitledBorder.LEFT, TitledBorder.TOP);
        titledBorder.setTitleFont(FONT_TITLE);
        titledBorder.setTitleColor(COLOR_TEXT);
        return titledBorder;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon originalIcon = new ImageIcon(iconPath);
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Lỗi nạp icon: " + iconPath);
            e.printStackTrace();
        }
        
        button.setFont(FONT_BUTTON); 
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(Color.BLACK); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setIconTextGap(10); 

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_PRIMARY.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_PRIMARY);
            }
        });

        return button;
    }

    private JLabel createResultLabel(String value, String title) {
        String html = "<html><div style='text-align: center;'><font size='6' color='#dd2c00'>" + value
                + "</font><br/>" + title + "</div></html>";
        JLabel label = new JLabel(html, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setOpaque(true);
        label.setBackground(COLOR_PANEL_BG);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, COLOR_PRIMARY));
        label.setPreferredSize(new Dimension(200, 100));
        return label;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Phần mềm Quản lý Nhà hàng - Thống kê");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new FrmThongKe());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}