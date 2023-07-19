package db.java.ex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB_02_CRUD_MySQL {

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
		DB_Env env = dbs.get("mysql");

		String url = env.getUrl();
		String username = env.getUserName();
		String password = env.getPasswd();
		String driver = env.getDriver();
		String sql = env.getTestSql();

		// JDBC 드라이버 로드
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		// 데이터베이스 연결
		PreparedStatement ps;
		ResultSet rs;

		try {
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connection Success -> " + env.toString());

		} catch (Exception e) { e.printStackTrace(); }
	}

	public static void main(String[] args) {
		initEnv();
		initCon();

		//
		PreparedStatement ps;
        ResultSet rs;

        // Select로 ID의 가장 큰값을 가져오기
        int employee_id = 0;
        String sql = "SELECT MAX(EMPLOYEE_ID) AS MAX FROM EMPLOYEES";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                employee_id = rs.getInt("MAX");
                System.out.println("Total MAX: " + employee_id);
            }

        } catch (Exception e) { e.printStackTrace(); }


        // (1) Insert
        employee_id++;
        String firstName = "koiking";
        String lastName = "testName";
        String email = "koiking@gmail.com";
        String phoneNumber = "000-1234-1234";
        String jobId = "IT_PROG";

        LocalDateTime hire_date = LocalDateTime.now();
        String parseLocalDateTime = hire_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        sql = String.format("INSERT INTO EMPLOYEES "
        		+ "(EMPLOYEE_ID, FIRST_NAME, LAST_NAME, "
        		+ "EMAIL, PHONE_NUMBER, HIRE_DATE, "
        		+ "JOB_ID, SALARY, COMMISSION_PCT, "
        		+ "MANAGER_ID, DEPARTMENT_ID"
        		+ ") "
        		+ "VALUES"
        		+ " (%s, '%s', '%s',"
        		+ " '%s', '%s', '%s',"
        		+ " '%s', 12345, null,"
        		+ " 103, 60)", employee_id, firstName, lastName, email, phoneNumber, parseLocalDateTime, jobId);

        System.out.println("Insert SQL : " + sql);

        try {
            ps = con.prepareStatement(sql);
            int res = ps.executeUpdate();

            if (res > 0) {
            	System.out.println("Insert Success : " + sql);
            }

        } catch (Exception e) { e.printStackTrace(); }

        employee_id= 207;
        // (2) Select
        sql = "SELECT e.* FROM EMPLOYEES e WHERE EMPLOYEE_ID = "+employee_id;
        System.out.println("Select One SQL : " + sql);
        try {
            ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			selectTestOracle(rs);

		} catch (Exception e) { e.printStackTrace(); }

        // (3) Update

        lastName = "updatedtestName";
        int salary = 11113;
        LocalDateTime updatedHireDate =  hire_date.plusMonths(2);
        parseLocalDateTime = updatedHireDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sql = String.format("UPDATE EMPLOYEES e SET LAST_NAME = '%s', "
        		+ "HIRE_DATE = '%s', "
        		+ "SALARY = %s"
        		+ " WHERE EMPLOYEE_ID = %s", lastName, parseLocalDateTime, salary, employee_id);
        System.out.println("Update SQL : " + sql);
        try {
            ps = con.prepareStatement(sql);
            int res = ps.executeUpdate();

            if (res > 0) {
            	System.out.println("Update Success : " + sql);
            }
            sql = "SELECT e.* FROM EMPLOYEES e WHERE EMPLOYEE_ID = "+employee_id;
            ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			selectTestOracle(rs);

		} catch (Exception e) { e.printStackTrace(); }

        // (4) Delete
        sql = String.format("DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID=%s", employee_id);
        System.out.println("Delete SQL : " + sql);
        try {
            ps = con.prepareStatement(sql);
            int res = ps.executeUpdate();

            if (res > 0) {
            	System.out.println("Delete Success : " + sql);
            }
            sql = "SELECT e.* FROM EMPLOYEES e WHERE EMPLOYEE_ID = "+employee_id;
            ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			selectTestOracle(rs);

		} catch (Exception e) { e.printStackTrace(); }


	}

	public static void selectTestOracle(ResultSet rs) throws SQLException, Exception {
		while (rs.next()) {
			int employeeId = rs.getInt("EMPLOYEE_ID");
			String firstName = rs.getString("FIRST_NAME");
			String lastName = rs.getString("LAST_NAME");
			String email = rs.getString("EMAIL");
			Date temp = rs.getDate("HIRE_DATE");
			String jobId = rs.getString("JOB_ID");
			int salary = rs.getInt("SALARY");

			// LocalDateTime hireDate = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); -> 이건 안됬음
			LocalDateTime hireDate = Instant.ofEpochMilli(temp.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
			String hireDateStr = hireDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			System.out.printf("%s, %s, %s, %s, %s, %s, %s \n", employeeId, firstName, lastName, email, hireDateStr, jobId, salary);
		}
	}

}
