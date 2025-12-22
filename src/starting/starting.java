package starting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import gui.FrmDangNhap;
import gui.FrmThongKe;
import gui.ThanhTacVu;

public class starting {
    public static void main(String[] args) {
        // Thiết lập font Times New Roman cho toàn ứng dụng
        UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));
        UIManager.put("Label.font", new Font("Times New Roman", Font.PLAIN, 18));
        UIManager.put("Button.font", new Font("Times New Roman", Font.BOLD, 18));
        UIManager.put("TextField.font", new Font("Times New Roman", Font.PLAIN, 18));
        UIManager.put("ComboBox.font", new Font("Times New Roman", Font.PLAIN, 18));
        UIManager.put("Table.font", new Font("Times New Roman", Font.PLAIN, 18));
        UIManager.put("TabbedPane.font", new Font("Times New Roman", Font.BOLD, 16));

        // Áp dụng giao diện hệ thống
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Kiểm tra tham số dòng lệnh
            boolean testThongKe = false;
            if (args.length > 0 && "--test-thongke".equalsIgnoreCase(args[0])) {
                testThongKe = true;
            }

            if (testThongKe) {
                // ================== CHẾ ĐỘ TEST: MỞ THẲNG THỐNG KÊ ==================
                // Giả lập quyền Quản Lý
                ThanhTacVu.setPhanQuyen("QuanLy");
                ThanhTacVu mainFrame = ThanhTacVu.getInstance();

                // Xóa nội dung cũ
                BorderLayout layout = (BorderLayout) mainFrame.getContentPane().getLayout();
                Component oldCenter = layout.getLayoutComponent(BorderLayout.CENTER);
                if (oldCenter != null) {
                    mainFrame.getContentPane().remove(oldCenter);
                }

                // Set màu tab đúng TRƯỚC khi tạo FrmThongKe
                UIManager.put("TabbedPane.background", new Color(240, 240, 240));     // tab thường: xám
                UIManager.put("TabbedPane.foreground", Color.BLACK);                 // chữ đen
                UIManager.put("TabbedPane.selected", new Color(221, 44, 0));         // tab chọn: đỏ
                UIManager.put("TabbedPane.selectedForeground", Color.WHITE);         // chữ trắng

                // Tạo và thêm FrmThongKe
                mainFrame.getContentPane().add(new FrmThongKe(), BorderLayout.CENTER);

                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setVisible(true);

            } else {
                // ================== CHẾ ĐỘ BÌNH THƯỜNG: ĐĂNG NHẬP ==================
                new FrmDangNhap().setVisible(true);
            }
        });
    }
}