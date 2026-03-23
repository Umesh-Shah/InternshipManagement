# Internship Management System (IMS)

An academic web application for managing student internships, companies, and job postings. Modernized from a legacy Spring MVC/JSP stack to **Spring Boot 3.4 + React 19**.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.4, Spring Security, Spring Data JPA, Hibernate |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS v4, React Router, TanStack Query/Table |
| Database | MySQL 8.4 |
| Testing | JUnit 5, Spring Boot Test (backend); Vitest, React Testing Library (frontend) |

## Prerequisites

- Docker (for MySQL)
- Java 21+
- Node.js 18+
- Maven 3.9+

## Quick Start

### 1. Start MySQL

```bash
docker compose up -d
```

This starts MySQL on port **3307** and auto-seeds test data on first run.

### 2. Start Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

### 3. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173` and proxies API requests to the backend.

## Running Tests

```bash
# Backend (8 tests)
cd backend && mvn test

# Frontend (10 tests)
cd frontend && npm test
```

## Production Build

```bash
# Backend
cd backend && mvn clean package -DskipTests
# JAR at backend/target/ims-backend-0.0.1-SNAPSHOT.jar

# Frontend
cd frontend && npm run build
# Static files at frontend/dist/
```

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Student | alice.nguyen | pass1001 |

## Project Structure

```
├── backend/                  Spring Boot application
│   ├── src/main/java/        Java source (controllers, services, repos, entities)
│   ├── src/main/resources/   application.yml, Hibernate mappings
│   ├── src/test/             JUnit tests
│   └── migration-scripts/    SQL seed & migration scripts
├── frontend/                 React SPA
│   ├── src/                  Components, features, hooks, API layer
│   └── src/test/             Vitest tests
├── docker-compose.yml        MySQL dev environment
└── docker/mysql/             MySQL Docker customization
```
