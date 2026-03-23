# IMS Modernization — Remaining Work

Migration from legacy Spring MVC/JSP/Hibernate to Spring Boot 3.4 + React 19 is complete and merged (PR #1). All remaining items below have been resolved.

## Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.4, Spring Security, Spring Data JPA |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS v4, React Router, TanStack Query/Table |
| Database | MySQL 8.4 (Docker, port 3307) |
| Testing | JUnit 5 + Spring Boot Test (85 tests), Vitest + React Testing Library (11 tests) |

## Resolved Items

- **S2** — `application-prod.yml` now requires `JWT_SECRET` with no default; startup fails if unset. Dev profile retains a known-insecure default documented in the config comment.
- **F6** — `client.ts` Axios interceptor now reads token via `useAuthStore.getState().token` instead of parsing `localStorage` directly.
- **F8** — `useAuthStore.test.ts` now explicitly tests that `clearAuth()` persists nulled state to `localStorage` via the Zustand persist middleware.
- **Infrastructure** — MySQL pinned to `8.0` LTS in `docker-compose.yml`; `JWT_SECRET` requirement documented in `docker-compose.yml`; `seed-test-data.sql` carries a DEV-ONLY warning about `{noop}` passwords; `application-prod.yml` explicitly sets `ddl-auto: validate`; `build/` and `*.class` already excluded in `.gitignore`.

## Confirm with Stakeholders

- Static pages (`aboutUs`, `gallery`, `trustees`, `contact`) — dropped from migration
- Admin user management — out of scope; admins managed directly in DB
