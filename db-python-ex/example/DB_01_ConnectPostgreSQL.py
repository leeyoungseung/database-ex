import psycopg2

# PostgreSQL 데이터베이스 연결
connection = psycopg2.connect(
    host="localhost",
    port="5433",
    user="admin",
    password="postgres",
    database="postgres"
)

# 커넥션 상태 확인
if connection:
    print("PostgreSQL DB 연결 성공")
else:
    print("PostgreSQL DB 연결 실패")
    exit(100)

# 접속된 데이터베이스에서 쿼리 실행
cursor = connection.cursor()
cursor.execute("SELECT * FROM category LIMIT 100")
result = cursor.fetchall()

# 쿼리 결과 출력
for row in result:
    print(row)

# 연결 종료
cursor.close()
connection.close()