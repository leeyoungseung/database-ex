use DBI;

# MySQL 데이터베이스 접속 정보
my $host = "localhost";
my $port = "3307";
my $user = "root";
my $password = "mysql";
my $database = "world";

# MySQL 데이터베이스에 접속
# MySQL 8.x부터는 아래의 설정을 해야함.
# alter user 'root' identified with 'mysql_native_password' by 'mysql';
my $dsn = "DBI:mysql:database=$database;host=$host;port=$port";
my $dbh = DBI->connect($dsn, $user, $password) or die "Cannot connect to the database: $DBI::errstr";

# SELECT 쿼리 실행
my $sql = "SELECT * FROM city LIMIT 10";
my $sth = $dbh->prepare($sql);
$sth->execute();

# 결과 출력
print "result 1 assign column\n";
while (my $row = $sth->fetchrow_hashref) {
    print "Column1: " . $row->{ID} . ", Column2: " . $row->{Name} . "\n";
}

$sth->execute();
print "result 2 array\n";
while (my @row = $sth->fetchrow_array) {
    print join(",", @row) . "\n";
}

# 연결 종료
$sth->finish;
$dbh->disconnect;