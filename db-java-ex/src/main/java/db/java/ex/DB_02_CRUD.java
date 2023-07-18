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

public class DB_02_CRUD {

	static Connection con;
	static Map<String, DB_Env> dbs;

	public static void initEnv() {
		dbs = new HashMap<String, DB_Env>();
		dbs.put("oracle", new DB_Env("jdbc:oracle:thin:@localhost:1521:xe", "hr", "1234",
				"oracle.jdbc.driver.OracleDriver", "SELECT * FROM JOB_HISTORY WHERE ROWNUM <= 100"));
		dbs.put("mysql", new DB_Env("jdbc:mysql://localhost:3307/world", "root", "mysql", "com.mysql.cj.jdbc.Driver",
				"SELECT * FROM city LIMIT 100"));
		dbs.put("postgresql", new DB_Env("jdbc:postgresql://localhost:5433/postgres", "admin", "postgres",
				"org.postgresql.Driver", "SELECT * FROM category LIMIT 100"));
	}

	public static void initCon() {
		// JDBC 연결 정보 설정
		DB_Env env = dbs.get("oracle");

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

		} catch (SQLException e) {
			e.printStackTrace();
		}
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

        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}


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
        		+ " '%s', '%s', TO_DATE('%s', 'YYYY-MM-DD HH24:MI:SS'),"
        		+ " '%s', 12345, null,"
        		+ " 103, 60)", employee_id, firstName, lastName, email, phoneNumber, parseLocalDateTime, jobId);

        System.out.println("Insert SQL : " + sql);

        try {
            ps = con.prepareStatement(sql);
            int res = ps.executeUpdate();

            if (res > 0) {
            	System.out.println("Insert Success : " + sql);
            }

        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}

        employee_id= 207;
        // (2) Select
        sql = "SELECT e.* FROM EMPLOYEES e WHERE EMPLOYEE_ID = "+employee_id;
        System.out.println("Select One SQL : " + sql);
        try {
            ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			selectTestOracle(rs);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // (3) Update

        lastName = "updatedtestName";
        int salary = 11113;
        LocalDateTime updatedHireDate =  hire_date.plusMonths(2);
        parseLocalDateTime = updatedHireDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sql = String.format("UPDATE EMPLOYEES e SET LAST_NAME = '%s', "
        		+ "HIRE_DATE = TO_DATE('%s', 'YYYY-MM-DD HH24:MI:SS'), "
        		+ "SALARY = %s"
        		+ " WHERE EMPLOYEE_ID = %s", lastName, parseLocalDateTime, salary, employee_id);
        System.out.println("Update One SQL : " + sql);
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // (4) Delete
        sql = String.format("DELETE FROM HR.EMPLOYEES WHERE EMPLOYEE_ID=%s", employee_id);
        System.out.println("Update One SQL : " + sql);
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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

class employees {
	private int employee_id;
	private String first_name;
	private String last_name;
	private String email;
	private String phone_number;
	private LocalDateTime hire_date;
	private String job_id;
	private int salary;
	private int commission_pct;
	private int manager_id;
	private int department_id;
	@Override
	public String toString() {
		return "employees [employee_id=" + employee_id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", email=" + email + ", phone_number=" + phone_number + ", hire_date=" + hire_date + ", job_id="
				+ job_id + ", salary=" + salary + ", commission_pct=" + commission_pct + ", manager_id=" + manager_id
				+ ", department_id=" + department_id + "]";
	}
	public employees(int employee_id, String first_name, String last_name, String email, String phone_number,
			LocalDateTime hire_date, String job_id, int salary, int commission_pct, int manager_id, int department_id) {
		super();
		this.employee_id = employee_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.hire_date = hire_date;
		this.job_id = job_id;
		this.salary = salary;
		this.commission_pct = commission_pct;
		this.manager_id = manager_id;
		this.department_id = department_id;
	}
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public LocalDateTime getHire_date() {
		return hire_date;
	}
	public void setHire_date(LocalDateTime hire_date) {
		this.hire_date = hire_date;
	}
	public String getJob_id() {
		return job_id;
	}
	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getCommission_pct() {
		return commission_pct;
	}
	public void setCommission_pct(int commission_pct) {
		this.commission_pct = commission_pct;
	}
	public int getManager_id() {
		return manager_id;
	}
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

}
