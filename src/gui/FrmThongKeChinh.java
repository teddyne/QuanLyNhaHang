package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Đây là Frame chính chứa panel thống kê (FrmThongKe)
 * Kế thừa ThanhTacVu để có sẵn Menu Bar và Bottom Bar
 */
public class FrmThongKeChinh extends ThanhTacVu { 

    private FrmThongKe pnlThongKe;

    public FrmThongKeChinh(int tabIndex) {
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        pnlThongKe = new FrmThongKe(tabIndex);
        
        getContentPane().add(pnlThongKe, BorderLayout.CENTER);
    }
}