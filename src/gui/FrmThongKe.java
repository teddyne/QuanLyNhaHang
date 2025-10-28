package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.function.Consumer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import org.jfree.data.general.DefaultPieDataset;
import connectSQL.ConnectSQL; 
import java.sql.Connection;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFormattedTextField;
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
	private final Color COLOR_SECONDARY = new Color(240, 240, 240);
	private final Color COLOR_TEXT = new Color(51, 51, 51);
	private final Color COLOR_PANEL_BG = Color.WHITE;
	
	// ===== BẮT ĐẦU SỬA CODE (1/7) - Thay đổi hằng số Font =====
	private final Font FONT_TITLE = new Font("Times New Roman", Font.BOLD, 18);
	private final Font FONT_GENERAL = new Font("Times New Roman", Font.PLAIN, 14);
	private final Font FONT_BUTTON = new Font("Times New Roman", Font.BOLD, 14);
	// ===== KẾT THÚC SỬA CODE =====

	private HoaDon_DAO hoaDonDAO;
	private JTabbedPane tabbedPane;

	// Components cho Tab 1
	private JDateChooser dateChooserBatDau1, dateChooserKetThuc1;
	private JButton btnXemTongQuan, btnXuatExcel1;
	private JLabel lblTongDoanhThu, lblTongSoHoaDon, lblDoanhThuTrungBinh;
	private JPanel pnlBieuDoTongQuan;
	private List<Object[]> duLieuThongKeTongQuan;

	// ===== BIẾN LƯU TRỮ GIÁ TRỊ TÓM TẮT =====
	private double tongDoanhThuTongQuan;
	private int tongSoHoaDonTongQuan;
	private double doanhThuTrungBinhTongQuan;
	// =======================================================

	// Components cho Tab 2
	private JDateChooser dateChooserBatDau2, dateChooserKetThuc2;
	private JButton btnXemMonAn, btnXuatExcel2;
	private JPanel pnlBieuDoMonAn;
	private List<Object[]> duLieuThongKeMonAn;

	// ===== BỔ SUNG BIẾN CHO TAB 3 (THÁNG) =====
	private JDateChooser dateChooserBatDau3, dateChooserKetThuc3;
	private JButton btnXemThang, btnXuatExcel3;
	private JPanel pnlBieuDoThang;
	private List<Object[]> duLieuThongKeThang; // Dữ liệu thô để xuất excel
	private JLabel lblTongDoanhThu_Thang, lblTongSoHoaDon_Thang, lblDoanhThuTrungBinh_Thang;

	// ===== BỔ SUNG BIẾN CHO TAB 4 (NĂM) =====
	private JDateChooser dateChooserBatDau4, dateChooserKetThuc4;
	private JButton btnXemNam, btnXuatExcel4;
	private JPanel pnlBieuDoNam;
	private List<Object[]> duLieuThongKeNam; // Dữ liệu thô để xuất excel
	private JLabel lblTongDoanhThu_Nam, lblTongSoHoaDon_Nam, lblDoanhThuTrungBinh_Nam;

	public FrmThongKe() {

		try {
	        hoaDonDAO = HoaDon_DAO.getInstance(); 

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi khởi tạo DAO: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	   
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, new javax.swing.plaf.FontUIResource(FONT_GENERAL));
			}
		}
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE); // Nền form chính

		// Yêu cầu: Nền XÁM, chữ ĐEN cho tab KHÔNG ĐƯỢC CHỌN
		UIManager.put("TabbedPane.background", COLOR_SECONDARY); // Màu xám
		UIManager.put("TabbedPane.foreground", Color.BLACK); // Chữ đen

		// Yêu cầu: Nền ĐỎ, chữ TRẮNG cho tab ĐANG ĐƯỢC CHỌN
		UIManager.put("TabbedPane.selected", COLOR_PRIMARY); // Màu đỏ
		UIManager.put("TabbedPane.selectedForeground", Color.WHITE); // Chữ trắng

		UIManager.put("TabbedPane.focus", COLOR_PRIMARY); // Viền focus
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
		UIManager.put("TabbedPane.tabsOverlapBorder", true);

		tabbedPane = new JTabbedPane();
		
		tabbedPane.setFont(new Font("Times New Roman", Font.BOLD, 16));
		
		add(tabbedPane, BorderLayout.CENTER);

		// Thêm Tab 1 
        JPanel pnlThongKeMonAn = createTabThongKeMonAn();
        tabbedPane.addTab("Thống kê theo Món ăn", pnlThongKeMonAn);

        // Thêm Tab 2 
        JPanel pnlThongKeTongQuan = createTabThongKeTongQuan();
        tabbedPane.addTab("Thống kê theo ngày", pnlThongKeTongQuan);
        
		// Thêm Tab 3
		JPanel pnlThongKeThang = createTabThongKeTheoThang();
		tabbedPane.addTab("Thống kê theo Tháng", pnlThongKeThang);

		// Thêm Tab 4
		JPanel pnlThongKeNam = createTabThongKeTheoNam();
		tabbedPane.addTab("Thống kê theo Năm", pnlThongKeNam);
		
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    if (i == selectedIndex) {
                        tabbedPane.setForegroundAt(i, Color.WHITE);
                    } else {
                        tabbedPane.setForegroundAt(i, Color.BLACK);
                    }
                }
            }
        });

        int initialIndex = tabbedPane.getSelectedIndex();
        if (initialIndex != -1) {
             tabbedPane.setForegroundAt(initialIndex, Color.WHITE);
        }

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (i != initialIndex) {
                tabbedPane.setForegroundAt(i, Color.BLACK);
            }
        }
	}
	
	/**
	 * Constructor để chọn tab cụ thể khi khởi tạo
	 * @param tabIndex - Chỉ số của tab cần hiển thị (0, 1, 2, 3...)
	 */
	public FrmThongKe(int tabIndex) {
	    this(); 
	    
	    if (tabbedPane != null && tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
	        tabbedPane.setSelectedIndex(tabIndex);
	    }
	}

// =================================================================================
	// ===== PHƯƠNG THỨC TẠO TAB THỐNG KÊ THEO MÓN ĂN (TAB 2) =====
	// =================================================================================
	private JPanel createTabThongKeTongQuan() {
		JPanel pnlTab = new JPanel(new BorderLayout(20, 20));
		
		pnlTab.setBackground(COLOR_PANEL_BG);
		
		pnlTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 1. Tạo Panel Top (chứa cả 2 phần control và tóm tắt)
		JPanel pnlTop = new JPanel(new GridBagLayout());
		pnlTop.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		// 2. Tạo Panel Trái (Thời gian)
		JPanel pnlThoiGianWrapper = new JPanel(new BorderLayout());
		pnlThoiGianWrapper.setBackground(COLOR_SECONDARY);
		
		// 2b. Tạo Panel Header
		JPanel pnlHeaderThoiGian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pnlHeaderThoiGian.setBackground(COLOR_PRIMARY);

		JLabel lblTitleThoiGian = new JLabel("Thời gian");
		lblTitleThoiGian.setFont(FONT_TITLE);
		lblTitleThoiGian.setForeground(Color.WHITE);
		pnlHeaderThoiGian.add(lblTitleThoiGian);
		
		pnlThoiGianWrapper.add(pnlHeaderThoiGian, BorderLayout.NORTH);

		// 2c. Tạo Panel Content (chứa 2 cột chọn ngày và nút)
		JPanel pnlThoiGianContent = new JPanel(new GridLayout(1, 2, 30, 10));
		pnlThoiGianContent.setBackground(COLOR_SECONDARY);
		pnlThoiGianContent.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// --- Cột 1: Chứa các JDateChooser ---
		JPanel pnlChonNgay = new JPanel(new GridLayout(2, 1, 10, 15));
		pnlChonNgay.setOpaque(false);

		dateChooserBatDau1 = new JDateChooser();
		dateChooserBatDau1.setPreferredSize(new Dimension(150, 30));
		dateChooserBatDau1.setDateFormatString("dd/MM/yyyy");
		dateChooserBatDau1.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserBatDau1, "Từ");
		pnlChonNgay.add(dateChooserBatDau1);

		dateChooserKetThuc1 = new JDateChooser();
		dateChooserKetThuc1.setPreferredSize(new Dimension(150, 30));
		dateChooserKetThuc1.setDateFormatString("dd/MM/yyyy");
		dateChooserKetThuc1.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserKetThuc1, "Đến");
		pnlChonNgay.add(dateChooserKetThuc1);

		pnlThoiGianContent.add(pnlChonNgay);

		// --- Cột 2: Chứa các JButton ---
		JPanel pnlNutBam = new JPanel(new GridLayout(2, 1, 10, 10));
		pnlNutBam.setOpaque(false); // Lấy nền xám

		btnXemTongQuan = createStyledButton("Xem thống kê", "img/preview.png");
		btnXemTongQuan.addActionListener(this);
		pnlNutBam.add(btnXemTongQuan);

		btnXuatExcel1 = createStyledButton("Xuất Excel", "img/excel.png");
		btnXuatExcel1.addActionListener(this);
		pnlNutBam.add(btnXuatExcel1);

		pnlThoiGianContent.add(pnlNutBam);
		
		pnlThoiGianWrapper.add(pnlThoiGianContent, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 2.0;
		gbc.insets = new Insets(0, 0, 0, 10);
		pnlTop.add(pnlThoiGianWrapper, gbc);

		// 3. Tạo Panel Phải (Kết quả tóm tắt)
		JPanel pnlTomTat = new JPanel(new GridLayout(1, 3, 20, 20));
		pnlTomTat.setBackground(COLOR_SECONDARY);
		pnlTomTat.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		lblTongDoanhThu = createResultLabel("0 VNĐ", "Tổng Doanh thu");
		lblTongSoHoaDon = createResultLabel("0", "Tổng số Hóa đơn");
		lblDoanhThuTrungBinh = createResultLabel("0 VNĐ", "Doanh thu Trung bình/Hóa đơn");

		pnlTomTat.add(lblTongDoanhThu);
		pnlTomTat.add(lblTongSoHoaDon);
		pnlTomTat.add(lblDoanhThuTrungBinh);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 3.0;
		gbc.insets = new Insets(0, 10, 0, 0);
		pnlTop.add(pnlTomTat, gbc);
		
		// 4. Thêm pnlTop vào vị trí NORTH của pnlTab
		pnlTab.add(pnlTop, BorderLayout.NORTH);

		// 5. Thêm Biểu đồ vào vị trí CENTER
		pnlBieuDoTongQuan = new JPanel(new BorderLayout());
		pnlBieuDoTongQuan.setBackground(COLOR_PANEL_BG);
		pnlBieuDoTongQuan.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		pnlTab.add(pnlBieuDoTongQuan, BorderLayout.CENTER);

		return pnlTab;
	}

	// =================================================================================
	// ===== PHƯƠNG THỨC TẠO TAB MÓN ĂN (TAB 1) =====
	// =================================================================================
	private JPanel createTabThongKeMonAn() {
		JPanel pnlTab = new JPanel(new BorderLayout(10, 10));
		
		pnlTab.setBackground(COLOR_PANEL_BG); // Sửa: Nền trắng
		
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

		btnXemMonAn = createStyledButton("Xem thống kê", "img/preview.png");
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

	// =================================================================================
	// =====PHƯƠNG THỨC TẠO TAB THEO THÁNG (TAB 3)=====
	// =================================================================================
	private JPanel createTabThongKeTheoThang() {
		JPanel pnlTab = new JPanel(new BorderLayout(20, 20));
		pnlTab.setBackground(COLOR_PANEL_BG); // Nền trắng
		pnlTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 1. Tạo Panel Top (chứa cả 2 phần control và tóm tắt)
		JPanel pnlTop = new JPanel(new GridBagLayout());
		pnlTop.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		// 2. Tạo Panel Trái (Thời gian)
		JPanel pnlThoiGianWrapper = new JPanel(new BorderLayout());
		pnlThoiGianWrapper.setBackground(COLOR_SECONDARY);
		
		JPanel pnlHeaderThoiGian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pnlHeaderThoiGian.setBackground(COLOR_PRIMARY);
		JLabel lblTitleThoiGian = new JLabel("Thời gian");
		lblTitleThoiGian.setFont(FONT_TITLE);
		lblTitleThoiGian.setForeground(Color.WHITE);
		pnlHeaderThoiGian.add(lblTitleThoiGian);
		pnlThoiGianWrapper.add(pnlHeaderThoiGian, BorderLayout.NORTH);

		JPanel pnlThoiGianContent = new JPanel(new GridLayout(1, 2, 30, 10));
		pnlThoiGianContent.setBackground(COLOR_SECONDARY);
		pnlThoiGianContent.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// --- Cột 1: Chứa các JDateChooser (DÙNG BIẾN ...3) ---
		JPanel pnlChonNgay = new JPanel(new GridLayout(2, 1, 10, 15));
		pnlChonNgay.setOpaque(false);

		dateChooserBatDau3 = new JDateChooser();
		dateChooserBatDau3.setPreferredSize(new Dimension(150, 30));
		dateChooserBatDau3.setDateFormatString("dd/MM/yyyy");
		dateChooserBatDau3.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserBatDau3, "Từ");
		pnlChonNgay.add(dateChooserBatDau3);

		dateChooserKetThuc3 = new JDateChooser();
		dateChooserKetThuc3.setPreferredSize(new Dimension(150, 30));
		dateChooserKetThuc3.setDateFormatString("dd/MM/yyyy");
		dateChooserKetThuc3.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserKetThuc3, "Đến");
		pnlChonNgay.add(dateChooserKetThuc3);
		pnlThoiGianContent.add(pnlChonNgay);

		// --- Cột 2: Chứa các JButton (DÙNG BIẾN ...Thang và ...3) ---
		JPanel pnlNutBam = new JPanel(new GridLayout(2, 1, 10, 10));
		pnlNutBam.setOpaque(false);

		btnXemThang = createStyledButton("Xem thống kê", "img/preview.png");
		btnXemThang.addActionListener(this);
		pnlNutBam.add(btnXemThang);

		btnXuatExcel3 = createStyledButton("Xuất Excel", "img/excel.png");
		btnXuatExcel3.addActionListener(this);
		pnlNutBam.add(btnXuatExcel3);

		pnlThoiGianContent.add(pnlNutBam);
		pnlThoiGianWrapper.add(pnlThoiGianContent, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 2.0;
		gbc.insets = new Insets(0, 0, 0, 10);
		pnlTop.add(pnlThoiGianWrapper, gbc);

		// 3. Tạo Panel Phải (Kết quả tóm tắt) (DÙNG BIẾN ..._Thang)
		JPanel pnlTomTat = new JPanel(new GridLayout(1, 3, 20, 20));
		pnlTomTat.setBackground(COLOR_SECONDARY);
		pnlTomTat.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		lblTongDoanhThu_Thang = createResultLabel("0 VNĐ", "Tổng Doanh thu");
		lblTongSoHoaDon_Thang = createResultLabel("0", "Tổng số Hóa đơn");
		lblDoanhThuTrungBinh_Thang = createResultLabel("0 VNĐ", "Doanh thu Trung bình/Hóa đơn");

		pnlTomTat.add(lblTongDoanhThu_Thang);
		pnlTomTat.add(lblTongSoHoaDon_Thang);
		pnlTomTat.add(lblDoanhThuTrungBinh_Thang);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 3.0;
		gbc.insets = new Insets(0, 10, 0, 0);
		pnlTop.add(pnlTomTat, gbc);
		
		// 4. Thêm pnlTop vào vị trí NORTH
		pnlTab.add(pnlTop, BorderLayout.NORTH);

		// 5. Thêm Biểu đồ vào vị trí CENTER (DÙNG BIẾN ...Thang)
		pnlBieuDoThang = new JPanel(new BorderLayout());
		pnlBieuDoThang.setBackground(COLOR_PANEL_BG);
		pnlBieuDoThang.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		pnlTab.add(pnlBieuDoThang, BorderLayout.CENTER);

		return pnlTab;
	}

	// =================================================================================
	// =====PHƯƠNG THỨC TẠO TAB THEO NĂM (TAB 4)=====
	// =================================================================================
	private JPanel createTabThongKeTheoNam() {
		JPanel pnlTab = new JPanel(new BorderLayout(20, 20));
		pnlTab.setBackground(COLOR_PANEL_BG);
		pnlTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 1. Tạo Panel Top (chứa cả 2 phần control và tóm tắt)
		JPanel pnlTop = new JPanel(new GridBagLayout());
		pnlTop.setOpaque(false); // Trong suốt để lấy nền trắng của pnlTab
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		// 2. Tạo Panel Trái (Thời gian)
		JPanel pnlThoiGianWrapper = new JPanel(new BorderLayout());
		pnlThoiGianWrapper.setBackground(COLOR_SECONDARY); // Nền xám
		
		JPanel pnlHeaderThoiGian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pnlHeaderThoiGian.setBackground(COLOR_PRIMARY);
		JLabel lblTitleThoiGian = new JLabel("Thời gian");
		lblTitleThoiGian.setFont(FONT_TITLE);
		lblTitleThoiGian.setForeground(Color.WHITE);
		pnlHeaderThoiGian.add(lblTitleThoiGian);
		pnlThoiGianWrapper.add(pnlHeaderThoiGian, BorderLayout.NORTH);

		JPanel pnlThoiGianContent = new JPanel(new GridLayout(1, 2, 30, 10));
		pnlThoiGianContent.setBackground(COLOR_SECONDARY);
		pnlThoiGianContent.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// --- Cột 1: Chứa các JDateChooser (DÙNG BIẾN ...4) ---
		JPanel pnlChonNgay = new JPanel(new GridLayout(2, 1, 10, 15));
		pnlChonNgay.setOpaque(false);

		dateChooserBatDau4 = new JDateChooser();
		dateChooserBatDau4.setPreferredSize(new Dimension(150, 30));
		dateChooserBatDau4.setDateFormatString("dd/MM/yyyy");
		dateChooserBatDau4.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserBatDau4, "Từ");
		pnlChonNgay.add(dateChooserBatDau4);

		dateChooserKetThuc4 = new JDateChooser();
		dateChooserKetThuc4.setPreferredSize(new Dimension(150, 30));
		dateChooserKetThuc4.setDateFormatString("dd/MM/yyyy");
		dateChooserKetThuc4.setFont(FONT_GENERAL);
		setPlaceholder(dateChooserKetThuc4, "Đến");
		pnlChonNgay.add(dateChooserKetThuc4);
		pnlThoiGianContent.add(pnlChonNgay);

		// --- Cột 2: Chứa các JButton (DÙNG BIẾN ...Nam và ...4) ---
		JPanel pnlNutBam = new JPanel(new GridLayout(2, 1, 10, 10));
		pnlNutBam.setOpaque(false);

		btnXemNam = createStyledButton("Xem thống kê", "img/preview.png");
		btnXemNam.addActionListener(this);
		pnlNutBam.add(btnXemNam);

		btnXuatExcel4 = createStyledButton("Xuất Excel", "img/excel.png");
		btnXuatExcel4.addActionListener(this);
		pnlNutBam.add(btnXuatExcel4);

		pnlThoiGianContent.add(pnlNutBam);
		pnlThoiGianWrapper.add(pnlThoiGianContent, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 2.0;
		gbc.insets = new Insets(0, 0, 0, 10);
		pnlTop.add(pnlThoiGianWrapper, gbc);

		// 3. Tạo Panel Phải (Kết quả tóm tắt) (DÙNG BIẾN ..._Nam)
		JPanel pnlTomTat = new JPanel(new GridLayout(1, 3, 20, 20));
		pnlTomTat.setBackground(COLOR_SECONDARY);
		pnlTomTat.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		lblTongDoanhThu_Nam = createResultLabel("0 VNĐ", "Tổng Doanh thu");
		lblTongSoHoaDon_Nam = createResultLabel("0", "Tổng số Hóa đơn");
		lblDoanhThuTrungBinh_Nam = createResultLabel("0 VNĐ", "Doanh thu Trung bình/Hóa đơn");

		pnlTomTat.add(lblTongDoanhThu_Nam);
		pnlTomTat.add(lblTongSoHoaDon_Nam);
		pnlTomTat.add(lblDoanhThuTrungBinh_Nam);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 3.0;
		gbc.insets = new Insets(0, 10, 0, 0);
		pnlTop.add(pnlTomTat, gbc);
		
		// 4. Thêm pnlTop vào vị trí NORTH
		pnlTab.add(pnlTop, BorderLayout.NORTH);

		// 5. Thêm Biểu đồ vào vị trí CENTER (DÙNG BIẾN ...Nam)
		pnlBieuDoNam = new JPanel(new BorderLayout());
		pnlBieuDoNam.setBackground(COLOR_PANEL_BG);
		pnlBieuDoNam.setBorder(BorderFactory.createLineBorder(Color.lightGray));

		pnlTab.add(pnlBieuDoNam, BorderLayout.CENTER);

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
			handleXuatExcel(duLieuThongKeTongQuan, "ThongKe_TongQuan",
				file -> {
					try {
						xuatBaoCaoExcelTongQuan(duLieuThongKeTongQuan, file);
					} catch (IOException e1) {
						// Ném ra RuntimeException để hàm helper bắt
						throw new RuntimeException("Lỗi khi xuất Excel tổng quan", e1);
					}
				});
				
		} else if (o.equals(btnXuatExcel2)) {
			handleXuatExcel(duLieuThongKeMonAn, "ThongKe_MonAn",
				file -> {
					try {
						xuatBaoCaoExcelMonAn(duLieuThongKeMonAn, file);
					} catch (IOException e1) {
						// Ném ra RuntimeException để hàm helper bắt
						throw new RuntimeException("Lỗi khi xuất Excel món ăn", e1);
					}
				});

		} else if (o.equals(btnXemThang)) {
			handleThongKeTheoThang();
			
		} else if (o.equals(btnXemNam)) {
			handleThongKeTheoNam();
			
		} else if (o.equals(btnXuatExcel3)) {
			handleXuatExcel(duLieuThongKeThang, "ThongKe_TheoThang",
				file -> {
					try {
						xuatBaoCaoExcelTheoThang(duLieuThongKeThang, file);
					} catch (IOException e1) {
						// Ném ra RuntimeException để hàm helper bắt
						throw new RuntimeException("Lỗi khi xuất Excel theo tháng", e1);
					}
				});
				
		} else if (o.equals(btnXuatExcel4)) {
		
			handleXuatExcel(duLieuThongKeNam, "ThongKe_TheoNam",
				file -> {
					try {
						xuatBaoCaoExcelTheoNam(duLieuThongKeNam, file);
					} catch (IOException e1) {
						// Ném ra RuntimeException để hàm helper bắt
						throw new RuntimeException("Lỗi khi xuất Excel theo năm", e1);
					}
				});
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

		if (duLieuThongKeTongQuan.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			// Xóa dữ liệu cũ
			pnlBieuDoTongQuan.removeAll();
			pnlBieuDoTongQuan.revalidate();
			pnlBieuDoTongQuan.repaint();
			lblTongDoanhThu.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Tổng Doanh thu</div></html>");
			lblTongSoHoaDon.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0</font><br/>Tổng số Hóa đơn</div></html>");
			lblDoanhThuTrungBinh.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Doanh thu TB/Hóa đơn</div></html>");
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

		if (duLieuThongKeMonAn.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			// Xóa biểu đồ cũ
			pnlBieuDoMonAn.removeAll();
			pnlBieuDoMonAn.revalidate();
			pnlBieuDoMonAn.repaint();
			return;
		}

		veBieuDoMonAnBanChay(duLieuThongKeMonAn);
	}

	// =====XỬ LÝ THỐNG KÊ THEO THÁNG (TAB 3) =====
	private void handleThongKeTheoThang() {
		java.util.Date utilNgayBatDau = dateChooserBatDau3.getDate();
		java.util.Date utilNgayKetThuc = dateChooserKetThuc3.getDate();

		if (utilNgayBatDau == null || utilNgayKetThuc == null || utilNgayBatDau.after(utilNgayKetThuc)) {
			JOptionPane.showMessageDialog(this, "Ngày bắt đầu và kết thúc không hợp lệ!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.sql.Date sqlNgayBatDau = new java.sql.Date(utilNgayBatDau.getTime());
		java.sql.Date sqlNgayKetThuc = new java.sql.Date(utilNgayKetThuc.getTime());

		duLieuThongKeThang = hoaDonDAO.getHoaDonTheoThoiGian(sqlNgayBatDau, sqlNgayKetThuc);

		if (duLieuThongKeThang.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			// Xóa biểu đồ cũ
			pnlBieuDoThang.removeAll();
			pnlBieuDoThang.revalidate();
			pnlBieuDoThang.repaint();
			
			// =====Reset label tóm tắt =====
			lblTongDoanhThu_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Tổng Doanh thu</div></html>");
			lblTongSoHoaDon_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0</font><br/>Tổng số Hóa đơn</div></html>");
			lblDoanhThuTrungBinh_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Doanh thu TB/Hóa đơn</div></html>");
			// ==========================================
			return;
		}

		// =====Cập nhật số liệu tóm tắt =====
		capNhatSoLieuThang(duLieuThongKeThang);
		// =============================================

		// Vẽ biểu đồ với dữ liệu đã lấy
		veBieuDoDoanhThuTheoThang(duLieuThongKeThang);
	}

	// ===== XỬ LÝ THỐNG KÊ THEO NĂM (TAB 4) =====
	private void handleThongKeTheoNam() {
		java.util.Date utilNgayBatDau = dateChooserBatDau4.getDate();
		java.util.Date utilNgayKetThuc = dateChooserKetThuc4.getDate();

		if (utilNgayBatDau == null || utilNgayKetThuc == null || utilNgayBatDau.after(utilNgayKetThuc)) {
			JOptionPane.showMessageDialog(this, "Ngày bắt đầu và kết thúc không hợp lệ!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.sql.Date sqlNgayBatDau = new java.sql.Date(utilNgayBatDau.getTime());
		java.sql.Date sqlNgayKetThuc = new java.sql.Date(utilNgayKetThuc.getTime());

		duLieuThongKeNam = hoaDonDAO.getHoaDonTheoThoiGian(sqlNgayBatDau, sqlNgayKetThuc);

		if (duLieuThongKeNam.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			// Xóa biểu đồ cũ
			pnlBieuDoNam.removeAll();
			pnlBieuDoNam.revalidate();
			pnlBieuDoNam.repaint();

			// =====Reset label tóm tắt =====
			lblTongDoanhThu_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Tổng Doanh thu</div></html>");
			lblTongSoHoaDon_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0</font><br/>Tổng số Hóa đơn</div></html>");
			lblDoanhThuTrungBinh_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>0 VNĐ</font><br/>Doanh thu TB/Hóa đơn</div></html>");
			// ==========================================
			return;
		}

		// =====Cập nhật số liệu tóm tắt =====
		capNhatSoLieuNam(duLieuThongKeNam);
		// =============================================

		// Vẽ biểu đồ với dữ liệu đã lấy
		veBieuDoDoanhThuTheoNam(duLieuThongKeNam);
	}
	
	// =====(Refactor: Thêm hàm helper chung cho logic JFileChooser) =====
	/**
	 * Xử lý logic chung cho việc xuất file Excel.
	 */
	private void handleXuatExcel(List<Object[]> data, String defaultFileName, Consumer<File> exportFunction) {
		if (data == null || data.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để xuất. Vui lòng xem thống kê trước.", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn nơi lưu file báo cáo");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
		fileChooser.setSelectedFile(new File(defaultFileName + ".xlsx")); // Gợi ý tên file

		int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getAbsolutePath();
			if (!filePath.endsWith(".xlsx")) {
				fileToSave = new File(filePath + ".xlsx");
			}
			
			// Gọi hàm xuất file cụ thể đã được truyền vào
			try {
				exportFunction.accept(fileToSave);
				
				JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\nĐã lưu tại: " + fileToSave.getAbsolutePath(),
						"Thành công", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file: " + e.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private void xuatBaoCaoExcelTongQuan(List<Object[]> dsHD, File file) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet summarySheet = workbook.createSheet("Tóm tắt");
			CellStyle headerStyle = createHeaderStyle(workbook);

			Row row1 = summarySheet.createRow(0);
			createStyledCell(row1, 0, "Tổng Doanh thu", headerStyle);
			row1.createCell(1).setCellValue(tongDoanhThuTongQuan);

			Row row2 = summarySheet.createRow(1);
			createStyledCell(row2, 0, "Tổng số Hóa đơn", headerStyle);
			row2.createCell(1).setCellValue(tongSoHoaDonTongQuan);

			Row row3 = summarySheet.createRow(2);
			createStyledCell(row3, 0, "Doanh thu Trung bình", headerStyle);
			row3.createCell(1).setCellValue(doanhThuTrungBinhTongQuan);

			summarySheet.autoSizeColumn(0);
			summarySheet.autoSizeColumn(1);

			XSSFSheet detailSheet = workbook.createSheet("Dữ liệu chi tiết");
			String[] headers = { "Mã HĐ", "Mã Bàn", "Ngày Lập", "Tên KH", "SĐT", "Nhân Viên", "Khuyến Mãi",
					"Tổng Tiền" };
			Row headerRow = detailSheet.createRow(0);

			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}

			int rowNum = 1;
			for (Object[] rowData : dsHD) {
				Row row = detailSheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0].toString());
				row.createCell(1).setCellValue(rowData[1] != null ? rowData[1].toString() : "");
				row.createCell(2).setCellValue(new SimpleDateFormat("dd/MM/yyyy HH:mm").format((Date) rowData[2]));
				row.createCell(3).setCellValue(rowData[3] != null ? rowData[3].toString() : "Khách vãng lai");
				row.createCell(4).setCellValue(rowData[4] != null ? rowData[4].toString() : "");
				row.createCell(5).setCellValue(rowData[5].toString());
				row.createCell(6).setCellValue(rowData[6] != null ? rowData[6].toString() : "Không có");
				row.createCell(7).setCellValue(((Number) rowData[7]).doubleValue());

			}

			for (int i = 0; i < headers.length; i++) {
				detailSheet.autoSizeColumn(i);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

		} catch (Exception e) {
			// Ném lỗi ra để hàm helper 'handleXuatExcel' bắt
			throw new IOException("Lỗi khi ghi file Excel tổng quan: " + e.getMessage(), e);
		}
	}

	private void xuatBaoCaoExcelMonAn(List<Object[]> topMonAn, File file) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Top Món ăn Bán chạy");
			CellStyle headerStyle = createHeaderStyle(workbook);

			String[] headers = { "Tên Món Ăn", "Tổng Số Lượng Bán" };
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}

			int rowNum = 1;
			for (Object[] rowData : topMonAn) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(rowData[0].toString());
				row.createCell(1).setCellValue(((Number) rowData[1]).intValue());

			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

		} catch (Exception e) {
			// Ném lỗi ra để hàm helper 'handleXuatExcel' bắt
			throw new IOException("Lỗi khi ghi file Excel món ăn: " + e.getMessage(), e);
		}
	}

	// =====PHƯƠNG THỨC GHI EXCEL (THEO THÁNG) =====
	private void xuatBaoCaoExcelTheoThang(List<Object[]> dsHD, File file) throws IOException {
		// 1. Nhóm dữ liệu
		Map<String, Double> doanhThuTheoThang = aggregateDataByTime(dsHD, "MM/yyyy");

		// 2. Ghi ra Excel (Giống logic xuất của Tab 2)
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Doanh thu Theo Tháng");
			CellStyle headerStyle = createHeaderStyle(workbook);

			String[] headers = { "Tháng", "Tổng Doanh Thu" };
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}

			int rowNum = 1;
			for (Map.Entry<String, Double> entry : doanhThuTheoThang.entrySet()) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(entry.getKey());
				row.createCell(1).setCellValue(entry.getValue());
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

		} catch (Exception e) {
			// Ném lỗi ra để hàm helper 'handleXuatExcel' bắt
			throw new IOException("Lỗi khi ghi file Excel theo tháng: " + e.getMessage(), e);
		}
	}

	// =====PHƯƠNG THỨC GHI EXCEL (THEO NĂM) =====
	private void xuatBaoCaoExcelTheoNam(List<Object[]> dsHD, File file) throws IOException {
		// 1. Nhóm dữ liệu
		Map<String, Double> doanhThuTheoNam = aggregateDataByTime(dsHD, "yyyy");

		// 2. Ghi ra Excel
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Doanh thu Theo Năm");
			CellStyle headerStyle = createHeaderStyle(workbook);

			String[] headers = { "Năm", "Tổng Doanh Thu" };
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				createStyledCell(headerRow, i, headers[i], headerStyle);
			}

			int rowNum = 1;
			for (Map.Entry<String, Double> entry : doanhThuTheoNam.entrySet()) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(entry.getKey());
				row.createCell(1).setCellValue(entry.getValue());
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream fos = new FileOutputStream(file)) {
				workbook.write(fos);
			}

		} catch (Exception e) {
			// Ném lỗi ra để hàm helper 'handleXuatExcel' bắt
			throw new IOException("Lỗi khi ghi file Excel theo năm: " + e.getMessage(), e);
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
		// Reset và tính toán lại
		tongDoanhThuTongQuan = 0;
		tongSoHoaDonTongQuan = dsHD.size();

		for (Object[] row : dsHD) {
			tongDoanhThuTongQuan += ((Number) row[7]).doubleValue();
			// =======================================
		}

		doanhThuTrungBinhTongQuan = (tongSoHoaDonTongQuan > 0) ? (tongDoanhThuTongQuan / tongSoHoaDonTongQuan) : 0;

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

		// Cập nhật giao diện từ các biến thành viên
		lblTongDoanhThu.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(tongDoanhThuTongQuan) + "</font><br/>Tổng Doanh thu</div></html>");
		lblTongSoHoaDon.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ tongSoHoaDonTongQuan + "</font><br/>Tổng số Hóa đơn</div></html>");
		lblDoanhThuTrungBinh.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(doanhThuTrungBinhTongQuan) + "</font><br/>Doanh thu TB/Hóa đơn</div></html>");
	}

	// =====CẬP NHẬT SỐ LIỆU TÓM TẮT CHO TAB 3 =====
	private void capNhatSoLieuThang(List<Object[]> dsHD) {
		// Tính toán cục bộ
		double tongDoanhThu = 0;
		int tongSoHoaDon = dsHD.size();

		for (Object[] row : dsHD) {
			tongDoanhThu += ((Number) row[7]).doubleValue();
		}

		double doanhThuTrungBinh = (tongSoHoaDon > 0) ? (tongDoanhThu / tongSoHoaDon) : 0;

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

		// Cập nhật giao diện của Tab 3 (dùng biến ..._Thang)
		lblTongDoanhThu_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(tongDoanhThu) + "</font><br/>Tổng Doanh thu</div></html>");
		lblTongSoHoaDon_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ tongSoHoaDon + "</font><br/>Tổng số Hóa đơn</div></html>");
		lblDoanhThuTrungBinh_Thang.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(doanhThuTrungBinh) + "</font><br/>Doanh thu TB/Hóa đơn</div></html>");
	}

	// =====CẬP NHẬT SỐ LIỆU TÓM TẮT CHO TAB 4 =====
	private void capNhatSoLieuNam(List<Object[]> dsHD) {
		// Tính toán cục bộ
		double tongDoanhThu = 0;
		int tongSoHoaDon = dsHD.size();

		for (Object[] row : dsHD) {
			tongDoanhThu += ((Number) row[7]).doubleValue();
		}

		double doanhThuTrungBinh = (tongSoHoaDon > 0) ? (tongDoanhThu / tongSoHoaDon) : 0;

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

		// Cập nhật giao diện của Tab 4 (dùng biến ..._Nam)
		lblTongDoanhThu_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(tongDoanhThu) + "</font><br/>Tổng Doanh thu</div></html>");
		lblTongSoHoaDon_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ tongSoHoaDon + "</font><br/>Tổng số Hóa đơn</div></html>");
		lblDoanhThuTrungBinh_Nam.setText("<html><div style='text-align: center;'><font size='6' color='#dd2c00'>"
				+ currencyFormat.format(doanhThuTrungBinh) + "</font><br/>Doanh thu TB/Hóa đơn</div></html>");
	}

	
	private Map<String, Double> aggregateDataByTime(List<Object[]> dsHD, String format) {
		Map<String, Double> aggregatedData = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		for (Object[] row : dsHD) {
			Date ngayLap = (Date) row[2];
			double tongTien = ((Number) row[7]).doubleValue();
			String key = sdf.format(ngayLap);
			aggregatedData.put(key, aggregatedData.getOrDefault(key, 0.0) + tongTien);
		}
		return aggregatedData;
	}

	private void veBieuDoDoanhThuTheoNgay(List<Object[]> dsHD) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Map<String, Double> doanhThuTheoNgay = aggregateDataByTime(dsHD, "dd/MM");
		
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
			
			// ----------------------------------------------------
			// PHẦN 1: TẠO BIỂU ĐỒ TRÒN (BÊN TRÁI)
			// ----------------------------------------------------
			DefaultPieDataset dataset = new DefaultPieDataset();
			for (Object[] row : topMonAn) {
				String tenMon = (String) row[0];
				Number soLuong = (Number) row[1];
				dataset.setValue(tenMon, soLuong);
			}

			JFreeChart pieChart = ChartFactory.createPieChart(
					null,
					dataset,
					true, // Hiển thị legend (chú thích)
					true, // Hiển thị tooltips
					false // Không dùng URLs
			);

			// Gọi style chung
			stylePieChart(pieChart);

			ChartPanel chartPanel = new ChartPanel(pieChart);
			chartPanel.setBackground(COLOR_PANEL_BG);
			
			// ----------------------------------------------------
			// PHẦN 2: TẠO BẢNG (BÊN PHẢI) - THIẾT KẾ MỚI
			// ----------------------------------------------------
			
			String[] columnNames = {"Tên Món Ăn", "Số Lượng Bán"};
			DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			
			for (Object[] rowData : topMonAn) {
				tableModel.addRow(rowData);
			}

			// Tạo JTable với custom renderer
			JTable table = new JTable(tableModel) {

				@Override
				public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
					Component c = super.prepareRenderer(renderer, row, column);

					c.setBackground(COLOR_PANEL_BG);
					c.setForeground(COLOR_TEXT);
					
					// ===== BẮT ĐẦU SỬA CODE (4/7) - Font cho JTable Cell =====
					c.setFont(FONT_BUTTON); // Dùng hằng số (Times New Roman, Bold, 14)
					// ===== KẾT THÚC SỬA CODE =====
					
					// Tùy chỉnh màu khi được chọn (hover/click)
					if (isRowSelected(row)) {
						c.setBackground(COLOR_SECONDARY); // Nền xám nhạt khi chọn
						c.setForeground(COLOR_PRIMARY); // Chữ đỏ khi chọn
					}
					
					return c;
				}
			};

			// --- Tùy chỉnh giao diện cho bảng ---
			table.setFont(FONT_GENERAL);
			table.setRowHeight(35); // Tăng chiều cao hàng cho thoáng
			table.setFillsViewportHeight(true); // Cho phép màu nền lấp đầy viewport

			// --- Tùy chỉnh đường kẻ (Grid) ---
			table.setShowHorizontalLines(true); // Chỉ bật gạch ngang
			table.setShowVerticalLines(false); // Tắt gạch dọc
			table.setGridColor(COLOR_SECONDARY); // Màu gạch ngang là màu xám nhạt
			table.setIntercellSpacing(new Dimension(0, 1)); // Khoảng cách cho gạch ngang

			// --- Tùy chỉnh cột ---
			table.getColumnModel().getColumn(0).setPreferredWidth(200); // Cột Tên Món rộng hơn
			table.getColumnModel().getColumn(1).setPreferredWidth(50);

	// --- Tùy chỉnh Cell Renderer (Căn lề và Padding) ---

			javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			centerRenderer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // 15px padding

			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			
			// --- Tùy chỉnh Header (Tiêu đề cột) ---
			javax.swing.table.JTableHeader header = table.getTableHeader();
			header.setOpaque(false);
			header.setPreferredSize(new Dimension(100, 40)); // Tăng chiều cao header
			header.setFont(FONT_BUTTON);
			header.setReorderingAllowed(false);

			// Tạo Header Renderer tùy chỉnh
			javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					// Lấy component gốc
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					
					c.setBackground(COLOR_PRIMARY);
					c.setForeground(Color.WHITE);
					setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
					setHorizontalAlignment(SwingConstants.CENTER);
					
					return c;
				}
			};

			table.getTableHeader().setDefaultRenderer(headerRenderer);

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getViewport().setBackground(COLOR_PANEL_BG);
			scrollPane.setBackground(COLOR_PANEL_BG);
			
			scrollPane.setBorder(BorderFactory.createEmptyBorder());

			// ----------------------------------------------------
			// PHẦN 3: TẠO JSplitPane VÀ HIỂN THỊ
			// ----------------------------------------------------
			
			// Tạo một panel chia đôi theo chiều ngang
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chartPanel, scrollPane);
			
			// Thiết lập tỷ lệ ban đầu: 60% cho biểu đồ (bên trái), 40% cho bảng (bên phải)
			splitPane.setResizeWeight(0.6);
			splitPane.setBorder(null);
			splitPane.setBackground(COLOR_PANEL_BG);
			splitPane.setDividerSize(5);

			pnlBieuDoMonAn.removeAll();
			pnlBieuDoMonAn.add(splitPane, BorderLayout.CENTER);
			pnlBieuDoMonAn.revalidate();
			pnlBieuDoMonAn.repaint();
		}

	
	// =====PHƯƠNG THỨC VẼ BIỂU ĐỒ (THEO THÁNG) =====
	private void veBieuDoDoanhThuTheoThang(List<Object[]> dsHD) {
		// 1. Nhóm dữ liệu
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// =====(Sử dụng hàm helper) =====
		Map<String, Double> doanhThuTheoThang = aggregateDataByTime(dsHD, "MM/yyyy");

		for (Map.Entry<String, Double> entry : doanhThuTheoThang.entrySet()) {
			dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
		}

		// 2. Tạo biểu đồ
		JFreeChart barChart = ChartFactory.createBarChart(
				"BIỂU ĐỒ DOANH THU THEO THÁNG",
				"Tháng", // Label trục X
				"Doanh thu (VND)", // Label trục Y
				dataset, PlotOrientation.VERTICAL, false, true, false);

		styleChart(barChart);

		// 3. Hiển thị lên panel
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setBackground(COLOR_PANEL_BG);
		pnlBieuDoThang.removeAll(); // Dùng panel của Tab 3
		pnlBieuDoThang.add(chartPanel, BorderLayout.CENTER);
		pnlBieuDoThang.revalidate();
		pnlBieuDoThang.repaint();
	}

	// =====PHƯƠNG THỨC VẼ BIỂU ĐỒ (THEO NĂM) =====
	private void veBieuDoDoanhThuTheoNam(List<Object[]> dsHD) {
		// 1. Nhóm dữ liệu
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// =====(Sử dụng hàm helper) =====
		Map<String, Double> doanhThuTheoNam = aggregateDataByTime(dsHD, "yyyy");

		for (Map.Entry<String, Double> entry : doanhThuTheoNam.entrySet()) {
			dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
		}

		// 2. Tạo biểu đồ
		JFreeChart barChart = ChartFactory.createBarChart(
				"BIỂU ĐỒ DOANH THU THEO NĂM",
				"Năm", // Label trục X
				"Doanh thu (VND)", // Label trục Y
				dataset, PlotOrientation.VERTICAL, false, true, false);

		styleChart(barChart); // Áp dụng style cũ

		// 3. Hiển thị lên panel
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setBackground(COLOR_PANEL_BG);
		pnlBieuDoNam.removeAll(); // Dùng panel của Tab 4
		pnlBieuDoNam.add(chartPanel, BorderLayout.CENTER);
		pnlBieuDoNam.revalidate();
		pnlBieuDoNam.repaint();
	}

	private void styleChart(JFreeChart chart) {
		chart.getTitle().setPaint(COLOR_PRIMARY);
		// ===== BẮT ĐẦU SỬA CODE (5/7) - Font cho JFreeChart Title =====
		chart.getTitle().setFont(FONT_TITLE); // Dùng hằng số
		// ===== KẾT THÚC SỬA CODE =====
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
	
	// =====(Thêm hàm style cho Pie Chart) =====
	private void stylePieChart(JFreeChart chart) {

		chart.setBackgroundPaint(COLOR_PANEL_BG);

		// Style Legend (chú thích)
		org.jfree.chart.title.LegendTitle legend = chart.getLegend();
		if (legend != null) {
			legend.setPosition(org.jfree.chart.ui.RectangleEdge.LEFT);
			legend.setBackgroundPaint(Color.WHITE);
			
			legend.setPadding(new org.jfree.chart.ui.RectangleInsets(10.0, 10.0, 10.0, 10.0)); // top, left, bottom, right
	
			// ===== BẮT ĐẦU SỬA CODE (6/7) - Font cho JFreeChart Legend =====
			legend.setItemFont(FONT_GENERAL); // Dùng hằng số
			// ===== KẾT THÚC SỬA CODE =====

		}

		org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		plot.setSectionOutlinesVisible(false);
		plot.setSimpleLabels(true);

	// ===== (Chỉ hiển thị % khi hover) =====

		plot.setLabelGenerator(null);

		// 2. Định dạng nội dung cho Tooltip (chỉ hiển thị phần trăm)
		// 2a. Tạo định dạng phần trăm (với 1 số lẻ)
		NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("vi", "VN"));
		percentFormat.setMaximumFractionDigits(1);

		plot.setToolTipGenerator(new org.jfree.chart.labels.StandardPieToolTipGenerator(
			"{2}",
			NumberFormat.getInstance(),
			percentFormat
		));

		plot.setNoDataMessage("Không có dữ liệu để hiển thị");
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

		button.setBackground(COLOR_PANEL_BG);
		button.setForeground(COLOR_TEXT);
		
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY), // Viền ngoài
				BorderFactory.createEmptyBorder(10, 20, 10, 20) // Viền trong (padding)
		));

		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setFocusPainted(false);
		button.setIconTextGap(10);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// Khi hover, chuyển sang nền xám (COLOR_SECONDARY)
				button.setBackground(COLOR_SECONDARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Khi rời chuột, trở lại nền trắng (COLOR_PANEL_BG)
				button.setBackground(COLOR_PANEL_BG);
			}
		});

		return button;
	}
	
// =====PHƯƠNG THỨC TẠO PLACEHOLDER CHO JDATECHOOSER =====
	private void setPlaceholder(JDateChooser dateChooser, String placeholder) {

		JFormattedTextField textField = (JFormattedTextField) dateChooser.getDateEditor().getUiComponent();

		// 1. Đặt trạng thái ban đầu
		if (dateChooser.getDate() == null) {
			textField.setText(placeholder);
			textField.setForeground(Color.GRAY);
		} else {
			textField.setForeground(Color.BLACK);
		}

		// 2. Xử lý khi người dùng click vào
		textField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				// Khi click vào, nếu là placeholder thì xóa đi
				if (dateChooser.getDate() == null && textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				// Khi click ra ngoài, nếu trống và chưa có ngày thì đặt lại placeholder
				if (dateChooser.getDate() == null && textField.getText().isEmpty()) {
					textField.setText(placeholder);
					textField.setForeground(Color.GRAY);
				}
			}
		});

		// 3. Xử lý khi một ngày được CHỌN (từ popup)
		dateChooser.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
			@Override
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				if (evt.getNewValue() == null) {
					
					if(textField.getText().isEmpty()) {
						textField.setText(placeholder);
						textField.setForeground(Color.GRAY);
					}
				} else {
					textField.setForeground(Color.BLACK);
				}
			}
		});
	}

	private JLabel createResultLabel(String value, String title) {
		String html = "<html><div style='text-align: center;'><font size='6' color='#dd2c00'>" + value
				+ "</font><br/>" + title + "</div></html>";
		JLabel label = new JLabel(html, SwingConstants.CENTER);
		// ===== BẮT ĐẦU SỬA CODE (7/7) - Font cho ResultLabel =====
		label.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		// ===== KẾT THÚC SỬA CODE =====
		label.setOpaque(true);
		
		label.setBackground(COLOR_SECONDARY);

		label.setPreferredSize(new Dimension(200, 100));
		return label;
	}

	public static void main(String[] args) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {

			ThanhTacVu.setPhanQuyen("QuanLy");
			ThanhTacVu mainFrame = ThanhTacVu.getInstance();
			BorderLayout layout = (BorderLayout) mainFrame.getContentPane().getLayout();
			Component oldCenter = layout.getLayoutComponent(BorderLayout.CENTER);
			if (oldCenter != null) {
				mainFrame.getContentPane().remove(oldCenter);
			}

			mainFrame.getContentPane().add(new FrmThongKe(), BorderLayout.CENTER);
			mainFrame.revalidate();
			mainFrame.repaint();
			mainFrame.setVisible(true);

		});
	}
}