# CLAUDE.md

## Project Overview

Internship Management System (IMS) — a Spring Boot 3.4 + React 19 monorepo for managing student internships, companies, and job postings. Modernized from a legacy Spring MVC/JSP/Hibernate stack.

## Dev Commands

```bash
# Database
docker compose up -d                    # MySQL on port 3307, auto-seeds test data

# Backend
cd backend && mvn spring-boot:run       # http://localhost:8080
cd backend && mvn test                  # 8 tests
cd backend && mvn clean compile         # Compile check

# Frontend
cd frontend && npm run dev              # http://localhost:5173
cd frontend && npm test                 # 10 tests
cd frontend && npm run build            # Production build
```

## Architecture

```
React SPA (frontend/)
    ↓ REST API (proxied via Vite in dev)
Spring Boot (backend/)
    ├── Controllers (REST endpoints under /api/)
    ├── Services
    ├── Repositories (Spring Data JPA)
    └── Entities (JPA annotations)
    ↓
MySQL 8.4 (Docker, port 3307)
```

## Key Files

| Path | Purpose |
|------|---------|
| `backend/src/main/java/ca/uwindsor/ims/` | Java source root |
| `backend/src/main/resources/application.yml` | Spring Boot config (profiles: dev/test) |
| `backend/migration-scripts/seed-test-data.sql` | Dev seed data |
| `backend/migration-scripts/migrate-passwords.sql` | BCrypt password migration |
| `frontend/src/features/` | Feature modules (auth, students, companies, jobs, internships) |
| `frontend/src/api/` | Axios API client |
| `frontend/src/components/` | Shared UI components |
| `frontend/vite.config.ts` | Vite config (imports from vitest/config) |
| `frontend/src/index.css` | Tailwind v4 theme config |
| `docker-compose.yml` | MySQL dev environment |

## Key Constraints

- **Java 21** target (machine has Java 24 — Lombok is broken, use explicit getters/setters)
- **Tailwind v4** — uses `@tailwindcss/vite` plugin, no `tailwind.config.js`
- **`verbatimModuleSyntax`** — always use `import type { Foo }` for type-only imports
- **No `@hookform/resolvers`** — use React Hook Form built-in validators only
- **Two login tables**: `login` (admin) and `vbct_login` (student)
- **`vite.config.ts`** imports from `vitest/config` (not `vite`)
- **`@WebMvcTest`**: exclude `SecurityAutoConfiguration`, `SecurityFilterAutoConfiguration`, `OAuth2ResourceServerAutoConfiguration`
- **`@DataJpaTest`**: add `@AutoConfigureTestDatabase(replace = ANY)` + `spring.jpa.hibernate.ddl-auto=create-drop`

## Two User Roles

- **Admin**: manages companies, jobs, students, internships
- **Student**: views/applies to jobs, manages profile and skills

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Student | alice.nguyen | pass1001 |
