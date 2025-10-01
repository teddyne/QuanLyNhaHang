package connectSQL;

import java.sql.Connection;

public class TestConnect {
	    public static void main(String[] args) {
	        Connection conn = ConnectSQL.getConnection();
	        if (conn != null) {
	            System.out.println("Kết nối cơ sở dữ liệu thành công!");
	        } else {
	            System.out.println("Kết nối thất bại!");
	        }
	        ConnectSQL.getInstance().disconnect();
	    }
	}

