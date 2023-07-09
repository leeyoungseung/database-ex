import cx_Oracle

cx_Oracle.init_oracle_client(r"C:\Users\leeyoungseung\project_source\database-ex\instantclient_11_2")

# Oracle 데이터베이스에 접속
dsn = cx_Oracle.makedsn(host='localhost', port='1521', sid='xe')
connection = cx_Oracle.connect(user='hr', password='1234', dsn=dsn)

# 커넥션 상태 확인
if connection:
    print("Oracle DB 연결 성공")
else:
    print("Oracle DB 연결 실패")
    exit(100)

# 접속된 데이터베이스에서 쿼리 실행
cursor = connection.cursor()
cursor.execute("SELECT * FROM JOBS WHERE ROWNUM <= 100")
result = cursor.fetchall()

# 쿼리 결과 출력
for row in result:
    print(row)

# 연결 종료
connection.close()
exit(0)