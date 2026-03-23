# IMS Modernization — Remaining Work

Migration from legacy Spring MVC/JSP/Hibernate to Spring Boot 3.4 + React 19 is complete and merged (PR #1).

## Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.4, Spring Security, Spring Data JPA |
| Frontend | React 19, TypeScript, Vite, Tailwind CSS v4, React Router, TanStack Query/Table |
| Database | MySQL 8.4 (Docker, port 3307) |
| Testing | JUnit 5 + Spring Boot Test (86 tests), Vitest + React Testing Library (11 tests) |

## Confirm with Stakeholders

- Static pages (`aboutUs`, `gallery`, `trustees`, `contact`) — dropped from migration
- Admin user management — out of scope; admins managed directly in DB
