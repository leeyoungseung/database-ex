import mysql.connector

# MySQL 데이터베이스에 접속
connection = mysql.connector.connect(
    host='localhost',
    port='3307',
    user='root',
    password='mysql',
    database='world'
)

# 커넥션 상태 확인
if connection.is_connected():
    print("MySQL DB 연결 성공")
else:
    print("MySQL DB 연결 실패")
    exit(100)

# 접속된 데이터베이스에서 쿼리 실행
cursor = connection.cursor()
cursor.execute("SELECT * FROM city LIMIT 100")
result = cursor.fetchall()

# 쿼리 결과 출력
for row in result:
    print(row)

# 연결 종료
cursor.close()
connection.close()


connection.close()
exit(0)