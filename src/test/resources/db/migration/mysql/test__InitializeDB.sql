
drop schema   if exists  auth CASCADE;
create schema auth;

RUNSCRIPT FROM 'classpath:db/migration/mysql/V2017.01.11.04.30.01__InitializeDB.sql';
RUNSCRIPT FROM 'classpath:db/migration/mysql/V2018.01.01.01.01.01__InsertData.sql';

SET SCHEMA auth;


