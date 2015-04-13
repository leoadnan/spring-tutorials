drop table if exists passwords;
drop table if exists grpshell;
create table if not exists passwords (user string, passwd string, uid int, gid int, userinfo string, home string, shell string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ':' LINES TERMINATED BY '10';
create table if not exists grpshell (shell string, count int);
load data local inpath '/etc/passwd' into table passwords;
INSERT OVERWRITE TABLE grpshell SELECT p.shell, count(*) FROM passwords p GROUP BY p.shell;

