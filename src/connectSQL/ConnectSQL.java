package connectSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSQL {
    private static Connection con = null;
    private static ConnectSQL instance = new ConnectSQL();
    private static final String URL = "jdbc:sqlserver://DESKTOP-DL0I36Q\\MSSQL:1433;databaseName=QLNH;encrypt=false;trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    public static ConnectSQL getInstance() {
        return instance;
    }

    public static void connect() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Kết nối cơ sở dữ liệu thất bại!");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Đóng kết nối thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con = null;
        }
    }

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return con;
    }
}