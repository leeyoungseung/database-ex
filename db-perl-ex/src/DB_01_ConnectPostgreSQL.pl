use DBI;

# PostgreSQL 데이터베이스 접속 정보
my $host = "localhost";
my $port = "5433";
my $user = "admin";
my $password = "postgres";
my $database = "postgres";

# PostgreSQL 데이터베이스에 접속
my $dsn = "DBI:Pg:database=$database;host=$host;port=$port";
my $dbh = DBI->connect($dsn, $user, $password) or die "Cannot connect to the database: $DBI::errstr";

# SELECT 쿼리 실행
my $sql = "SELECT * FROM category LIMIT 20";
my $sth = $dbh->prepare($sql);
$sth->execute();

# 결과 출력
print "result 1 assign column\n";
while (my $row = $sth->fetchrow_hashref) {
    print "Column1: " . $row->{category_major_cd} . ", Column2: " . $row->{category_major_name} . "\n";
}

$sth->execute();
print "result 2 array\n";
while (my @row = $sth->fetchrow_array) {
    print join(",", @row) . "\n";
}

# 연결 종료
$sth->finish;
$dbh->disconnect;