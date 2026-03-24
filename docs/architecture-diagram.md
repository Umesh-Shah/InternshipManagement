# Internship Management System — Architecture Diagrams

## High-Level Overview

```mermaid
graph TB
    User["Browser"]

    subgraph Frontend["Frontend — React SPA"]
        FE["SPA"]
    end

    subgraph Backend["Backend — Spring Boot"]
        BE["REST API"]
    end

    subgraph Database["MySQL"]
        DB[("Database")]
    end

    User -->|"HTTP"| FE
    FE -->|"REST /api/*"| BE
    BE -->|"Spring Data JPA"| DB
```

---

## Detailed Diagrams

| Document | Description |
|----------|-------------|
| [Frontend Architecture](frontend-architecture.md) | React feature modules, routing, API client layer, shared components |
| [Backend Architecture](backend-architecture.md) | Security, controller, service, and repository layers |
| [Database Schema](database-schema.md) | ER diagram and column definitions |
| [Workflows](workflows.md) | Authentication, job application, and student registration sequence diagrams |
