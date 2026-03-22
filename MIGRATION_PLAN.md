# IMS Modernization Plan

## Overview

Migration of the Internship Management System from a legacy Eclipse/Spring MVC/JSP/Hibernate stack to a modern Spring Boot 3.4 + React 19 monorepo.

## Original Stack

- Java 1.7, Spring MVC 3.x, Hibernate (XML mappings)
- JSP views with jQuery
- Eclipse Dynamic Web Project (no Maven/Gradle)
- Apache Tomcat 6/7, MySQL
- Single monolithic `MainController.java` (842 lines)
- Session-based auth, two roles: admin and student

## Target Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.4, Spring Security, Spring Data JPA, Hibernate (JPA annotations) |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS v4, React Router, TanStack Query/Table |
| Database | MySQL 8.4 (Docker) |
| Testing | JUnit 5 + Spring Boot Test (backend), Vitest + React Testing Library (frontend) |

## Migration Phases

### Phase 1: Project Setup
- Created Spring Boot 3.4 project with Maven
- Configured Spring Data JPA, Spring Security, MySQL driver
- Set up application.yml with dev/test profiles

### Phase 2: Entity Migration
- Converted 28 Hibernate XML-mapped BO classes to JPA-annotated entities
- Replaced `.hbm.xml` files with `@Entity`, `@Table`, `@Column` annotations
- Explicit getters/setters (no Lombok — broken on Java 24)

### Phase 3: Repository Layer
- Created Spring Data JPA repositories for all entities
- Replaced hand-written DAO classes with `JpaRepository` interfaces
- Added custom query methods where needed

### Phase 4: Service Layer
- Migrated 8 service classes to Spring Boot `@Service` components
- Replaced Hibernate `SessionFactory` usage with JPA repository calls
- Maintained business logic from original services

### Phase 5: Security & Authentication
- Implemented Spring Security with JWT (stateless)
- Custom `ImsUserDetailsService` reading from two login tables (`login` for admin, `vbct_login` for student)
- BCrypt password encoding (migration script for existing plain-text passwords)
- Role-based access control (ADMIN, STUDENT)

### Phase 6: REST API Controllers
- Broke monolithic `MainController` into domain-specific REST controllers
- RESTful endpoints under `/api/` prefix
- Proper HTTP methods (GET, POST, PUT, DELETE)
- Request/response DTOs

### Phase 7: React Frontend
- Created React 19 SPA with Vite + TypeScript
- Tailwind CSS v4 with `@tailwindcss/vite` plugin
- React Router for client-side routing
- TanStack Query for server state management
- TanStack Table for data grids
- React Hook Form for form handling (built-in validators only)
- Separate admin and student dashboards

### Phase 8: Testing
- Backend: 72 JUnit 5 tests across 9 test classes — full `@WebMvcTest` slice coverage for all 8 REST controllers, repository tests, and application context test
- Frontend: 10 Vitest tests (component rendering, routing, API integration)
- All tests passing

### Phase 9: PR Review Fixes
- **Security**: Externalized CORS origins to `cors.allowed-origins` property / `CORS_ALLOWED_ORIGINS` env var; removed hardcoded `spring.profiles.active: dev` from base config; added JWT expiry check (`exp` claim) in `ProtectedRoute`
- **Performance**: Eliminated N+1 queries in `JobApplicationService` with a single native JOIN query returning a `JobApplicationProjection`; batched skill saves with `saveAll()`
- **Reliability**: Moved email send outside `@Transactional` boundary using `@TransactionalEventListener(phase = AFTER_COMMIT)`; made `InternshipStatusService.assign()` idempotent (upsert via `findByStudentIdAndJobId`); added `Integer` overload to `StudentCertificateRepository` to handle legacy VARCHAR `studentId` without entity changes
- **Error handling**: Added `HttpMessageNotReadableException` → 400, `ResponseStatusException` pass-through, and generic `Exception` → 500 handlers to `GlobalExceptionHandler`
- **Frontend**: Removed all `any` casts in `ReportsPage`; scoped `usePdfDownload` per panel; fixed wrong-role redirect to own portal (`/admin` or `/student`); added class-component `ErrorBoundary` at router level; fixed GPA table `key` to use stable `studentId`

## Key Decisions

1. **No Lombok** — broken on Java 24 (TypeTag.UNKNOWN removed in JDK-8341937)
2. **Tailwind v4** — uses Vite plugin instead of PostCSS config
3. **Volume-mounted seed data** — MySQL Docker auto-seeds via `/docker-entrypoint-initdb.d/`
4. **Two login tables preserved** — matches original schema design
5. **JWT auth** — replaces session-based auth for stateless API design

## Final Structure

```
├── backend/                  Spring Boot 3.4 application
│   ├── src/main/java/        Controllers, services, repositories, entities
│   ├── src/main/resources/   application.yml, static resources
│   ├── src/test/             JUnit 5 tests
│   └── migration-scripts/    SQL seed & password migration scripts
├── frontend/                 React 19 SPA
│   ├── src/                  Components, features, hooks, API layer
│   └── src/test/             Vitest + RTL tests
├── docker-compose.yml        MySQL 8.4 dev environment
├── CLAUDE.md                 AI assistant instructions
└── README.md                 Project documentation
```

## Verification

- `mvn clean compile` — backend compiles cleanly
- `mvn test` — 72/72 tests pass (9 test classes)
- `npm test` — 10/10 tests pass
- `npm run build` — frontend builds without errors
- `docker compose up -d` — MySQL starts and auto-seeds
