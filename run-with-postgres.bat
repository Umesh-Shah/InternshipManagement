@echo off
echo Starting Internship Management System with PostgreSQL...

REM Set PostgreSQL path
set PGBIN="C:\Program Files\PostgreSQL\17\bin"

REM Check if PostgreSQL is running
%PGBIN%\pg_isready -h localhost -p 5432
if %ERRORLEVEL% NEQ 0 (
    echo PostgreSQL is not running. Please start PostgreSQL service.
    pause
    exit /b 1
)

REM Run the application
mvn spring-boot:run

pause 