import psycopg2
from datetime import datetime

# PostgreSQL 데이터베이스 연결
conn = psycopg2.connect(
    host="localhost",
    port="5433",
    user="admin",
    password="postgres",
    database="postgres"
)

def select_max_data():
    cursor = conn.cursor()
    count_result = 0

    try:
        cursor.execute("SELECT MAX(EMPLOYEE_ID) FROM EMPLOYEES")
        count_result = cursor.fetchone()[0]
        print("select_max_data Total number of employees:", count_result)
        print("SQL Query:", cursor.query)
        return count_result
    except psycopg2.Error as e:
        print("select_max_data Error occurred:", e)
        print("SQL Query:", cursor.query)
        return count_result
    finally:
        cursor.close()

# INSERT 예제
def insert_data(id):
    cursor = conn.cursor()
    now_str = ""
    now_str = datetime.now()

    try:
        cursor.execute("INSERT INTO EMPLOYEES (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, "
                       "PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY, COMMISSION_PCT,"
                       "MANAGER_ID, DEPARTMENT_ID) "
                       "VALUES"
                       " (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                       (id, "koiking", "testName", "koiking@gmail.com", "000-1234-1234", datetime.now(), "IT_PROG", 12345, None, 103, 60))
        conn.commit()
        print("SQL Query:", cursor.query)
        print("Data inserted successfully.")
    except psycopg2.Error as e:
        print("insert_data Error occurred:", e)
        print("SQL Query:", cursor.query)
        conn.rollback()
    finally:
        cursor.close()

# SELECT 예제
def select_data(id):
    cursor = conn.cursor()

    try:
        cursor.execute("SELECT * FROM EMPLOYEES WHERE EMPLOYEE_ID = %s", (id,))
        rows = cursor.fetchall()
        for row in rows:
            print(row)
        print("select_data successfully")
    except psycopg2.Error as e:
        print("select_data Error occurred:", e)
        print("SQL Query:", cursor.query)
    finally:
        cursor.close()

# UPDATE 예제
def update_data(id):
    cursor = conn.cursor()

    try:
        cursor.execute("UPDATE EMPLOYEES SET LAST_NAME = %s , "
                       "HIRE_DATE = %s,"
                       "SALARY = %s"
                       " WHERE EMPLOYEE_ID = %s",
                       ("updatedtestName",
                        "2023-08-20 15:30:00",
                        11114,
                        id))
        conn.commit()
        print("SQL Query:", cursor.query)
        print("Data updated successfully.")
    except psycopg2.Error as e:
        print("update_data Error occurred:", e)
        print("SQL Query:", cursor.query)
        conn.rollback()
    finally:
        cursor.close()

# DELETE 예제
def delete_data(id):
    cursor = conn.cursor()

    try:
        cursor.execute("DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID = %s", (id,))
        conn.commit()
        print("Data deleted successfully.")
    except psycopg2.Error as e:
        print("delete_data Error occurred:", e)
        print("SQL Query:", cursor.query)
        conn.rollback()
    finally:
        cursor.close()

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
    conn.close()