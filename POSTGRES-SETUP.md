# PostgreSQL Setup for Internship Management System

This document provides instructions on how to set up and run the Internship Management System with PostgreSQL.

## Prerequisites

1. PostgreSQL 17 installed on your system
   - Installation path: `C:\Program Files\PostgreSQL\17\bin`
   - Default port: 5432
   - Default username: postgres
   - Password: postgres (or your custom password)

2. Java 21 or higher
3. Maven

## Setup Instructions

### 1. Configure PostgreSQL

Ensure PostgreSQL service is running. You can check this from the Services application in Windows or by running:

```
"C:\Program Files\PostgreSQL\17\bin\pg_isready" -h localhost -p 5432
```

### 2. Initialize the Database

Run the initialization script to create the database and required schema:

```
init-postgres-db.bat
```

This script will:
- Create a database named `internship_management`
- Set up the necessary permissions
- Initialize the database schema

### 3. Update Configuration (if needed)

The application is already configured to use PostgreSQL. The main configuration files are:

- `src/main/resources/application.properties` - Main application configuration
- `src/main/resources/AppDb.properties` - Database connection properties

If you used a different username, password, or port during PostgreSQL installation, update these files accordingly.

### 4. Run the Application

Run the application using the provided script:

```
run-with-postgres.bat
```

Or manually with Maven:

```
mvn spring-boot:run
```

## Troubleshooting

### Connection Issues

If you encounter connection issues:

1. Verify PostgreSQL is running
2. Check the username and password in the configuration files
3. Ensure the port (5432) is not blocked by a firewall
4. Check PostgreSQL logs for any errors

### Database Migration Issues

If you're migrating from MySQL to PostgreSQL and encounter issues:

1. Check for MySQL-specific SQL syntax in your application code
2. Verify that all table and column names follow PostgreSQL naming conventions
3. Review any custom SQL queries for compatibility with PostgreSQL

## Default Users

The system comes with the following default users:

- Admin: admin@uwindsor.ca / password
- Student: student@uwindsor.ca / password
- Faculty: faculty@uwindsor.ca / password 