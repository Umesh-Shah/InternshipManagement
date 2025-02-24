@echo off
echo Initializing PostgreSQL database for Internship Management System...

REM Set PostgreSQL path
set PGBIN="C:\Program Files\PostgreSQL\17\bin"
set PGUSER=postgres
set PGPASSWORD=postgres

REM Create database and initialize schema
%PGBIN%\psql -U %PGUSER% -f src/main/resources/init-postgres.sql

echo Database initialization completed.
pause 