package db.java.ex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DB_01_Connection {

	static Map<String, DB_Env> dbs;

    public static void init() {
    	dbs = new HashMap<String, DB_Env>();
    	dbs.put("oracle", new DB_Env("jdbc:oracle:thin:@localhost:1521:xe",
    			"hr",
    			"1234",
    			"oracle.jdbc.driver.OracleDriver",
    			"SELECT * FROM JOB_HISTORY WHERE ROWNUM <= 100"));
    	dbs.put("mysql", new DB_Env("jdbc:mysql://localhost:3307/world",
    			"root",
    			"mysql",
    			"com.mysql.cj.jdbc.Driver",
    			"SELECT * FROM city LIMIT 100"));
    	dbs.put("postgresql", new DB_Env("jdbc:postgresql://localhost:5433/postgres",
    			"admin",
    			"postgres",
    			"org.postgresql.Driver",
    			"SELECT * FROM category LIMIT 100"));
	}

    // 실행시 파라미터로 oracle 또는 mysql 또는 postgresql을 설정한다.
	public static void main(String[] args) {
		init();

		if (args.length == 0 || args[0].equals("") || !dbs.containsKey(args[0])) {
			System.out.println("Param Error : "+args[0]);
			System.exit(100);
		}

		// JDBC 연결 정보 설정
		DB_Env env = dbs.get(args[0]);

        String url      = env.getUrl();
        String username = env.getUserName();
        String password = env.getPasswd();
        String driver   = env.getDriver();
        String sql      = env.getTestSql();

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

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection Success -> " + env.toString());

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            switch (args[0]) {
			case "oracle":
				selectTestOracle(rs); break;
			case "mysql":
				selectTesMySQL(rs); break;
			case "postgresql":
				selectTestPostgreSQL(rs); break;
			default: break;
			}

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static void selectTestOracle(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int employeeId = rs.getInt("EMPLOYEE_ID");
			Date startDate = rs.getDate("START_DATE");
			Date endDate = rs.getDate("END_DATE");
			String jobId = rs.getString("JOB_ID");
			System.out.printf("%s, %s, %s, %s \n", employeeId, startDate, endDate, jobId);
		}
	}

	public static void selectTesMySQL(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int id = rs.getInt("ID");
			String name = rs.getString("Name");
			String countryCode = rs.getString("CountryCode");
			String district = rs.getString("District");
			int population = rs.getInt("Population");
			System.out.printf("%s, %s, %s, %s %s \n", id, name, countryCode, district, population);
		}
	}

	public static void selectTestPostgreSQL(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String category_major_cd = rs.getString("category_major_cd");
			String category_major_name = rs.getString("category_major_name");
			String category_medium_cd = rs.getString("category_medium_cd");
			String category_medium_name = rs.getString("category_medium_name");
			String category_small_cd = rs.getString("category_small_cd");
			String category_small_name = rs.getString("category_small_name");
			System.out.printf("%s, %s, %s, %s, %s, %s \n",
					category_major_cd, category_major_name, category_medium_cd,
					category_medium_name, category_small_cd, category_small_name);
		}
	}

}

// DB 접속을 위한 설정값을 넣기위한 클래스
class DB_Env {
	private String url;
	private String userName;
	private String passwd;
	private String driver;
	private String testSql;

	public DB_Env(String url, String userName, String passwd, String driver, String testSql) {
		super();
		this.url = url;
		this.userName = userName;
		this.passwd = passwd;
		this.driver = driver;
		this.testSql = testSql;
	}

	@Override
	public String toString() {
		return "DB_Env [url=" + url + ", userName=" + userName + ", passwd=" + passwd + ", driver=" + driver + ", testSql=" + testSql + "]";
	}

	public String getUrl() { return url; }
	public String getUserName() { return userName; }
	public String getPasswd() { return passwd; }
	public String getDriver() { return driver; }
	public String getTestSql() { return testSql; }

}
