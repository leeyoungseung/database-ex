## Perl을 사용해한 오라클접속은 결국 시간내에 달성하지 못함.
# strawberry perl 환경에서 두개 라이브러리를 사용해 봤지만 실패했음.
# DBD::Oracle : 애초에 cpan으로 설치가 안됨. 전제조건인 오라클 클라이언트 설치 및 환경변수 설정은 제대로 되었지만 안됨...노답 
# DBD::ODBC   : 설치는 됬지만 더 투자할 시간이 아까워 디질것 같음. 

use DBI;

# 데이터베이스 연결 설정
my $hostname = "localhost";
my $port = 5432;
my $username = "hr";
my $password = "1521";
my $database = "xe";

# Oracle 데이터베이스에 접속
my $dsn = "dbi:ODBC:DSN=$hostname:$port:";
my $dbh = DBI->connect($dsn, $username, $password);

if ($dbh) {
    print "Database connection success \n";
} else {
    print "Database connection error: $DBI::errstr\n";
    exit;
}

$dbh->disconnect();