package connectSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectSQL {
    private static Connection connection = null;
    private static ConnectSQL instance = new ConnectSQL();

    // Cấu hình kết nối
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaHang;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

  
    private ConnectSQL() {
        
    }

    public static ConnectSQL getInstance() {
        return instance;
    }
    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối mới: " + con.hashCode() + " - Hợp lệ: " + !con.isClosed());
            return con;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "LỖI KẾT NỐI DB: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

}