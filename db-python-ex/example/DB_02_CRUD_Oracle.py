import cx_Oracle
from datetime import datetime

cx_Oracle.init_oracle_client(r"C:\Users\leeyoungseung\project_source\database-ex\instantclient_11_2")

# Oracle 데이터베이스에 접속
dsn = cx_Oracle.makedsn(host='localhost', port='1521', sid='xe')
username = 'hr'
password = '1234'
dsn = dsn

def select_max_data():
    conn = cx_Oracle.connect(username, password, dsn)
    cursor = conn.cursor()
    count_result = 0

    try:
        cursor.execute("SELECT MAX(EMPLOYEE_ID) FROM EMPLOYEES")
        count_result = cursor.fetchone()[0]
        print("select_max_data Total number of employees:", count_result)
        print("SQL Query:", cursor.statement)
        return count_result
    except cx_Oracle.DatabaseError as e:
        print("select_max_data Error occurred:", e)
        print("SQL Query:", cursor.statement)
        return count_result
    finally:
        cursor.close()
        conn.close()

# INSERT 예제
def insert_data(id):
    conn = cx_Oracle.connect(username, password, dsn)
    cursor = conn.cursor()
    now_str = ""
    now_str = datetime.now()

    try:
        cursor.execute("INSERT INTO EMPLOYEES (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, "
                       "PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY, COMMISSION_PCT,"
                       "MANAGER_ID, DEPARTMENT_ID) "
                       "VALUES"
                       " (:1, :2, :3, :4, :5,"
                       "TO_DATE(:6, 'YYYY-MM-DD HH24:MI:SS'),"
                       ":7, :8, :9, :10, :11)",
                       (id, "koiking", "testName", "koiking@gmail.com", "000-1234-1234",
                        datetime.strftime(datetime.now(), "%Y-%m-%d %H:%M:%S"),
                        "IT_PROG", 12345, None, 103, 60))
        conn.commit()
        print("SQL Query:", cursor.statement)
        print("Data inserted successfully.")
    except cx_Oracle.DatabaseError as e:
        print("insert_data Error occurred:", e)
        print("SQL Query:", cursor.statement)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

# SELECT 예제
def select_data(id):
    conn = cx_Oracle.connect(username, password, dsn)
    cursor = conn.cursor()

    try:
        cursor.execute("SELECT * FROM EMPLOYEES WHERE EMPLOYEE_ID = :emp_id", emp_id=id)
        rows = cursor.fetchall()
        for row in rows:
            print(row)
        print("SQL Query:", cursor.statement)
        print("select_data successfully")
    except cx_Oracle.DatabaseError as e:
        print("select_data Error occurred:", e)
        print("SQL Query:", cursor.statement)
    finally:
        cursor.close()
        conn.close()

# UPDATE 예제
def update_data(id):
    conn = cx_Oracle.connect(username, password, dsn)
    cursor = conn.cursor()

    try:
        cursor.execute("UPDATE EMPLOYEES SET LAST_NAME = :1 , "
                       "HIRE_DATE = TO_DATE(:2, 'YYYY-MM-DD HH24:MI:SS'),"
                       "SALARY = :3"
                       "WHERE EMPLOYEE_ID = :4",
                       ("updatedtestName",
                        "2023-08-20 15:30:00",
                        11114,
                        id))
        conn.commit()
        print("SQL Query:", cursor.statement)
        print("Data updated successfully.")
    except cx_Oracle.DatabaseError as e:
        print("update_data Error occurred:", e)
        print("SQL Query:", cursor.statement)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

# DELETE 예제
def delete_data(id):
    conn = cx_Oracle.connect(username, password, dsn)
    cursor = conn.cursor()

    try:
        cursor.execute("DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID = :emp_id", emp_id=id)
        conn.commit()
        print("SQL Query:", cursor.statement)
        print("Data deleted successfully.")
    except cx_Oracle.DatabaseError as e:
        print("delete_data Error occurred:", e)
        print("SQL Query:", cursor.statement)
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    max_id = 0
    max_id = select_max_data()
    print("max_id : ",+ max_id)
    new_id = max_id + 1
    insert_data(new_id)
    select_data(new_id)
    update_data(new_id)
    select_data(new_id)
    delete_data(new_id)
