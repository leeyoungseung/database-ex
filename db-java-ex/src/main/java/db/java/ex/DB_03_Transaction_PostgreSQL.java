package db.java.ex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DB_03_Transaction_PostgreSQL {

	static Connection con;
	static Map<String, DB_Env> dbs;

	public static void initEnv() {
		dbs = new HashMap<String, DB_Env>();
		dbs.put("oracle", new DB_Env("jdbc:oracle:thin:@localhost:1521:xe", "hr", "1234",
				"oracle.jdbc.driver.OracleDriver", "SELECT * FROM JOB_HISTORY WHERE ROWNUM <= 100"));
		dbs.put("mysql", new DB_Env("jdbc:mysql://localhost:3307/hr", "root", "mysql", "com.mysql.cj.jdbc.Driver",
				"SELECT * FROM city LIMIT 100"));
		dbs.put("postgresql", new DB_Env("jdbc:postgresql://localhost:5433/postgres", "admin", "postgres",
				"org.postgresql.Driver", "SELECT * FROM category LIMIT 100"));
	}

	public static void initCon() {
		// JDBC 연결 정보 설정
		DB_Env env = dbs.get("postgresql");

		String url = env.getUrl();
		String username = env.getUserName();
		String password = env.getPasswd();
		String driver = env.getDriver();
		String sql = env.getTestSql();

		// JDBC 드라이버 로드
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) { e.printStackTrace(); return; }

		try {
			con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(false);  // Transaction을 사용하기 위한 설정

			System.out.println("Connection Success -> " + env.toString());

		} catch (Exception e) { e.printStackTrace(); }
	}


	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("정상처리 0, 트랜젝션 테스트 1");
		int flg = sc.nextInt();

		if (!(flg == 0 || flg == 1)) {
			System.out.println("입력 값은 0 또는 1이어야함");
			System.exit(101);
		}

		initEnv();
		initCon();

		PreparedStatement ps;
        ResultSet rs;

        String selectOneSql1 = "SELECT update_count FROM STORE_HISTORY WHERE store_id = 1";
        String updateSql1 = "UPDATE STORE_HISTORY s SET update_count = update_count + 1 WHERE store_id = 1";
        String selectOneSql2 = "SELECT store_count FROM STORE WHERE store_id = 1";
        String updateSql2 = "UPDATE STORE s SET store_count = store_count + 1 WHERE store_id = 1";


        try {
            ps = con.prepareStatement(selectOneSql1);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("before store_count: " + rs.getInt(1));
            } else {
            	return;
            }

            ps = con.prepareStatement(updateSql1);
            int res = ps.executeUpdate();

            if (res > 0) {
            	System.out.println("Update1 Success : " + updateSql1);
            }


            ps = con.prepareStatement(selectOneSql1);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("after store_count: " + rs.getInt(1));
            } else {
            	return;
            }


            // 예외처리 발생시키기
            if (flg == 1) {
            	throw new Exception("의도적인 예외처리 발생 !!!");
            }

            ps = con.prepareStatement(selectOneSql2);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("before store_count: " + rs.getInt(1));
            } else {
            	return;
            }

            ps = con.prepareStatement(updateSql2);
            int res2 = ps.executeUpdate();

            if (res2 > 0) {
            	System.out.println("Update2 Success : " + updateSql2);
            }


            ps = con.prepareStatement(selectOneSql2);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("after store_count: " + rs.getInt(1));
            } else {
            	return;
            }

            con.commit();

            con.close();
        } catch (Exception e) {
        	e.printStackTrace();
        	try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        }



	}



}
