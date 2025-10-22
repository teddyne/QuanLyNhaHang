package starting;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import gui.FrmDangNhap;

public class starting {

	 public static void main(String[] args) {
    	 UIManager.put("TableHeader.font", new Font("Times New Roman", Font.BOLD, 20));
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
			
	 }
}
