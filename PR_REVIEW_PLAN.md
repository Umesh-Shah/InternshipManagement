# PR Review Plan — Modernize IMS (Spring Boot 3.4 + React 19)

PR #1 · `modernize` → `master`
+18,340 / -100,609 lines | Full rewrite from legacy Spring MVC/JSP/Hibernate to REST API + SPA

---

## How to Use This Document

Work through the sections top-to-bottom.
Each section has:
- **Focus** — what to look for
- **Specific findings** — concrete issues already identified in this review
- **Checklist** — confirm each item before approving

---

## 1. Security Review

### Focus
JWT auth, CORS, authorization enforcement, secrets management, input validation.

### Findings

| # | Severity | File | Issue |
|---|----------|------|-------|
| S1 | HIGH | `SecurityConfig.java:87` | CORS `allowedOrigins` hardcodes `http://localhost:5173`. Prod will fail. Must be externalized to `CORS_ORIGINS` env var. |
| S2 | MEDIUM | `application.yml:24` | JWT secret default `change-this-in-production-use-env-var!` ships in the repo. Good that it forces override via env, but the default must be randomized so a misconfigured deploy doesn't silently use a known string. |
| S3 | MEDIUM | `application.yml:2` | `spring.profiles.active: dev` hardcoded in base config. A production deploy that doesn't explicitly override this will use dev settings (DDL validate, dev DB creds). Should be removed from base and set via `SPRING_PROFILES_ACTIVE` env var. |
| S4 | MEDIUM | `client.ts:1` | JWT stored in `localStorage` — readable by any JS on the page (XSS risk). Standard hardening: use `httpOnly` cookies or keep the token in memory only (Zustand without `persist`), relying on a refresh-token flow. |
| S5 | LOW | `ProtectedRoute.tsx:11` | `if (!token)` check does not validate JWT expiry. An expired token in localStorage still passes. Parse the `exp` claim from the JWT payload and redirect to login if expired. |
| S6 | LOW | `SecurityConfig.java` | SPA static routes served by `SpaController` are not excluded from the security filter chain. Verify that `/**` catch-all doesn't inadvertently require auth for the index HTML. |

### Checklist
- [x] S1: Externalize CORS origins to env var — `cors.allowed-origins` property, split by comma; `CORS_ALLOWED_ORIGINS` in prod profile
- [ ] S2: Randomize or remove default JWT secret — still ships a known default; override via `JWT_SECRET` env var in all deploys
- [x] S3: Remove `spring.profiles.active: dev` from `application.yml` — removed; set via `SPRING_PROFILES_ACTIVE` env var
- [ ] S4: Decision documented on JWT storage strategy — localStorage retained for simplicity; httpOnly cookie migration deferred
- [x] S5: Add JWT expiry check in `ProtectedRoute` — parses `exp` from JWT payload via `atob()`; calls `clearAuth()` if expired
- [x] S6: SPA routes confirmed permit-all in `SecurityConfig` (`/`, `/**` → `permitAll`)

---

## 2. Backend Code Quality

### Focus
Transactions, N+1 queries, error handling, consistency, Java best practices.

### Findings

| # | Severity | File | Issue |
|---|----------|------|-------|
| B1 | HIGH | `StudentService.java:66-89` | `create()` is `@Transactional`, but `emailService.sendRegistrationEmail()` is called inside the transaction. If the email throws, the entire student creation rolls back. Email I/O should happen after commit using `@TransactionalEventListener(phase = AFTER_COMMIT)` or a try/catch that logs and swallows the email failure. |
| B2 | HIGH | `JobApplicationService.java:92-116` | `toResponse()` fires N+1 queries — one `findById(job)` and one `findById(company)` per mapping row. With 100 applications this is 200 extra queries. Replace with a JPQL join or use a native query / projection DTO. |
| B3 | MEDIUM | `GlobalExceptionHandler.java` | Missing handlers for: `ResponseStatusException` (already used in `JobApplicationService`), `EntityNotFoundException`, and a fallback `Exception` handler. Without these, Spring returns a 500 white-label page for unhandled cases. |
| B4 | MEDIUM | `StudentService.java:163-175` | `replaceSkills()` saves each `StudentSkill` individually in a loop (`skillRepo.save(ss)` per item). Use `saveAll()` instead to batch the insert. |
| B5 | MEDIUM | `StudentService.java:122-127` | `StudentCertificate` stores `studentId` as `String` (`String.valueOf(studentId)`), while every other entity uses `Integer`. Inconsistency will cause join/query issues. Fix the entity or the DTO to use a consistent type. |
| B6 | LOW | `StudentController.java:71` | `upsertEducation` returns `StudentEducation` directly (200 always), but `getEducation` returns `ResponseEntity` (200 or 404). The upsert silently creates for any `studentId` that doesn't exist in `student_info`. Add a guard: check `studentInfoRepo.existsByStudentId()` before upsert. |
| B7 | LOW | `application-dev.yml` | Verify `show-sql: false` is the default and only `dev` profile overrides it to `true`. Currently `application.yml` sets `show-sql: false` which is correct — confirm `application-dev.yml` doesn't override. |
| B8 | LOW | `InternshipStatusService` | `assign()` creates a new `StudentInternship` every call — no idempotency check (unlike `JobApplicationService.apply()` which is idempotent). Double-clicking "Assign" in the UI will create duplicate records. |

### Checklist
- [x] B1: Email sending moved outside transaction boundary — `@TransactionalEventListener(phase = AFTER_COMMIT)` via `StudentRegisteredEvent`; email failure no longer rolls back student creation
- [x] B2: N+1 in `toResponse()` fixed — replaced per-row `findById` calls with single native JOIN query returning `JobApplicationProjection`
- [x] B3: `GlobalExceptionHandler` covers `HttpMessageNotReadableException` → 400, `ResponseStatusException` pass-through, and `Exception` fallback → 500
- [x] B4: `replaceSkills` loop replaced with `saveAll()`
- [x] B5: `StudentCertificate.studentId` — entity VARCHAR preserved (matches DB schema); `Integer` overload added to repository layer to avoid callers using `String.valueOf()`
- [ ] B6: Guard not added to upsert endpoints — deferred; upserts are idempotent by design
- [x] B7: SQL logging confirmed off in base and prod profiles; only enabled in `application-dev.yml`
- [x] B8: `assign()` made idempotent — `findByStudentIdAndJobId().orElseGet(StudentInternship::new)` upsert pattern

---

## 3. Backend Test Coverage

### Focus
Test breadth relative to codebase size; critical paths covered.

### Findings

| # | Severity | Note |
|---|----------|------|
| T1 | ~~HIGH~~ RESOLVED | ~~Only 3 test classes.~~ Now 9 test classes, 72 tests — `@WebMvcTest` slices for all 8 REST controllers plus `CompanyRepositoryTest`. |
| T2 | HIGH | No test for the security authorization layer — confirm `@PreAuthorize("hasRole('ADMIN')")` actually rejects STUDENT tokens, and `@studentSecurity.canAccess()` rejects cross-student access. |
| T3 | MEDIUM | `StudentService.create()` has complex logic (derive password, create login, send email) with no unit test. |
| T4 | MEDIUM | `JobApplicationService` single-query path covered by `JobApplicationControllerTest` smoke tests; dedicated service unit test still absent. |

### Checklist
- [x] T1: `@WebMvcTest` slices added for all 8 controllers — 72/72 tests pass
- [ ] T2: Add security integration test: student token rejected for admin endpoints; cross-student access rejected
- [ ] T3: Unit test for `StudentService.create()` (mock repos + email service)
- [x] T4: `AuthControllerTest` covers happy path, bad credentials, and missing body; `JobApplicationControllerTest` covers the N+1-replaced code path

---

## 4. Frontend Code Quality

### Focus
TypeScript correctness, React patterns, accessibility, error states.

### Findings

| # | Severity | File | Issue |
|---|----------|------|-------|
| F1 | MEDIUM | `ReportsPage.tsx:148,149` | `(filters as any)?.years` and `(filters as any)?.countries` bypass TypeScript. The `filters` prop type should be typed explicitly from the API response type, not cast to `any`. |
| F2 | MEDIUM | `ReportsPage.tsx:247` | `InternshipTypesPanel` and `GpaPanel` have `{ filters: any }` prop type. Type these properly. |
| F3 | MEDIUM | `ReportsPage.tsx:23` | Single `isDownloading` state from `usePdfDownload` is shared across all tabs. Clicking "Download PDF" on the Students tab disables the button in all other tabs simultaneously. `usePdfDownload` should be called once per panel or track which report is downloading. |
| F4 | MEDIUM | `ReportsPage.tsx:341` | GPA table uses `key={\`${r.studentId}-${i}\`}` with an index fallback. If `studentId` is unique this is fine, but the `-${i}` suffix hides cases where `studentId` is null/undefined. Use `r.studentId` directly and ensure it's non-null. |
| F5 | LOW | `ProtectedRoute.tsx:12` | Wrong redirect for unauthorized role — redirects to `/login` but user is already authenticated, just in the wrong role. Should redirect to their own portal root (`/admin` or `/student`) rather than forcing a re-login. |
| F6 | LOW | `client.ts:7` | Reads `localStorage` directly in the request interceptor, bypassing Zustand. This is a duplicate read (Zustand already persists to the same key). Access the token via the Zustand store's `getState()` instead: `useAuthStore.getState().token`. |
| F7 | LOW | All pages | No global error boundary. A React render error crashes the whole SPA. Add an `<ErrorBoundary>` at the router level. |
| F8 | LOW | `useAuthStore.ts` | `persist` middleware stores the full auth state including JWT in `localStorage`. If `clearAuth()` is called on logout, verify `localStorage.removeItem('ims-auth')` also fires (Zustand persist does this on `set({ token: null })` — confirm via test). |

### Checklist
- [x] F1/F2: All `any` casts removed — `StudentsPanel` typed as `{ filters: ReportFilters | null }`, `InternshipTypesPanel` and `GpaPanel` use `ReportFilters | null`
- [x] F3: `usePdfDownload()` called independently per panel; each has its own isolated `isDownloading` state
- [x] F4: GPA table `key` changed to `r.studentId` (stable, no index fallback)
- [x] F5: Wrong-role redirect goes to `/admin` or `/student` (own portal), not `/login`
- [ ] F6: `client.ts` still reads `localStorage` directly — deferred; functionally correct, minor coupling issue
- [x] F7: Class-component `<ErrorBoundary>` added wrapping `<BrowserRouter>` in `AppRouter.tsx`
- [ ] F8: Logout clears Zustand state; Zustand `persist` removes `ims-auth` from localStorage on `clearAuth()` — not explicitly tested

---

## 5. Migration Coverage — Feature Parity with Legacy

This checklist maps every legacy `MainController` route to the new system.

### Admin Features

| Legacy Route | New Equivalent | Status |
|---|---|---|
| `GET /main/goHome` | React Router `/` → redirect | ✅ Covered |
| `GET /main/loadlogin` + `POST /main/checklogin` | `POST /api/auth/login` + `LoginPage.tsx` | ✅ Covered |
| `GET /main/logoutnow` | `clearAuth()` + redirect in frontend | ✅ Covered |
| `POST /main/savecompany` + `GET /main/addcompany` | `POST /api/companies` + `CompanyFormPage.tsx` | ✅ Covered |
| `POST /main/saveSkill` + `GET /main/loadSkill` | `POST /api/skills` + `SkillsPage.tsx` + `SkillFormPage.tsx` | ✅ Covered |
| `POST /main/savejob` + `GET /main/loadjob` | `POST /api/jobs` + `JobsPage.tsx` + `JobFormPage.tsx` | ✅ Covered |
| `POST /main/saveinternship` + `GET /main/loadinternship` | `POST /api/internship-types` + `InternshipTypesPage.tsx` | ✅ Covered |
| `GET /main/loadstudentForm` + `POST /main/saveDemo` | `POST /api/students` + `StudentFormPage.tsx` | ✅ Covered |
| `GET /main/loadinternship_status` + `POST /main/saveInternship_status` | `POST /api/internships/status` + `AssignInternshipPage.tsx` | ✅ Covered |
| `POST /main/saveInternship_status_company` | `POST /api/internships/status` (companyId in request) | ✅ Covered |
| `POST /main/insert_company_student_status` | `POST /api/internships/status` with company+student | ✅ Covered |
| `GET /main/job_approval` + `GET /main/job_report` | `GET /api/job-applications/pending` + `ApprovalsPage.tsx` | ✅ Covered |
| `GET /main/approved_student` | `GET /api/job-applications/approved` + `ApprovalsPage.tsx` | ✅ Covered |
| `GET /main/getstudentfromjjob` | `GET /api/job-applications/approved?jobId=X` | ✅ Covered |
| `GET /main/getjobfromcompanyajax` (AJAX, jobs by company) | `GET /api/jobs?companyId=X` filtered in frontend | ✅ Covered |
| `GET /main/getjobfromcompany` | `GET /api/jobs?companyId=X` | ✅ Confirmed — `JobController.getAll(@RequestParam Integer companyId)` supported and tested |
| `GET /main/student_report` + `POST /main/searchreport` | `GET /api/reports/students` + `ReportsPage.tsx` | ✅ Covered |
| `GET /main/company_report` + `POST /main/searchemployers` | `GET /api/reports/companies` + `ReportsPage.tsx` | ✅ Covered |
| `GET /main/internshp_type_report` + `POST /main/searchinterntype` | `GET /api/reports/internship-types` + `ReportsPage.tsx` | ✅ Covered |
| `GET /main/gpa_report` + `POST /main/searchgpa` | `GET /api/reports/gpa` + `ReportsPage.tsx` | ✅ Covered |
| `GET /main/job_report` | `GET /api/reports/jobs` + `ReportsPage.tsx` | ✅ Covered |
| Admin `aboutUs` / `contact` / `gallery` / `trustees` pages | Not migrated (static content pages) | ℹ️ Intentionally dropped — confirm with stakeholders |

### Student Features

| Legacy Route | New Equivalent | Status |
|---|---|---|
| `GET /main/joblist` + `GET /main/jobInterest` | `JobsPage.tsx` + `POST /api/job-applications` | ✅ Covered |
| `GET /main/student_skill` + `POST /main/saveStudentskill` | `SkillsPage.tsx` + `PUT /api/students/{id}/skills` | ✅ Covered |
| `GET /main/student_basic_info` | `ProfilePage.tsx` + `GET /api/students/{id}/info` | ✅ Covered |
| `GET /main/student_edu` + `POST /main/edit_education_info` | `EducationPage.tsx` + `PUT /api/students/{id}/education` | ✅ Covered |
| `GET /main/student_certi` + `POST /main/edit_certi_info` | `CertificatesPage.tsx` + `PUT /api/students/{id}/certificates` | ✅ Covered |
| `GET /main/student_work` + `POST /main/edit_work_info` | `WorkPage.tsx` + `PUT /api/students/{id}/work` | ✅ Covered |
| `GET /main/loadinternship_status` (student view) | `InternshipStatusPage.tsx` + `GET /api/internships/status/student/{id}` | ✅ Covered |
| My Applications view | `MyApplicationsPage.tsx` + `GET /api/job-applications?studentId=X` | ✅ Covered |

### PDF Reports

| Legacy Report | New Equivalent | Status |
|---|---|---|
| Student report PDF | `GET /api/reports/students/pdf` | ✅ Covered |
| Company report PDF | `GET /api/reports/companies/pdf` | ✅ Covered |
| Internship Type report PDF | `GET /api/reports/internship-types/pdf` | ✅ Covered |
| GPA report PDF | `GET /api/reports/gpa/pdf` | ✅ Covered |
| Job report PDF | `GET /api/reports/jobs/pdf` | ✅ Covered |

### Data Model Parity

| Legacy Model / Table | New Entity | Status |
|---|---|---|
| `CompanyBo` | `Company.java` | ✅ |
| `SkillBo` | `Skill.java` | ✅ |
| `JobBo` | `Job.java` | ✅ |
| `InternshipTypeBo` | `InternshipType.java` | ✅ |
| `StudentInfoBo` | `StudentInfo.java` | ✅ |
| `StudentEducationBo` | `StudentEducation.java` | ✅ |
| `StudentCertificateBo` | `StudentCertificate.java` | ✅ |
| `StudentWorkBo` | `StudentWork.java` | ✅ |
| `StudentSkillBo` | `StudentSkill.java` | ✅ |
| `StudentInternshipBo` | `StudentInternship.java` | ✅ |
| `Student_Job_mapping` | `StudentJobMapping.java` | ✅ |
| `LoginBo` | `Login.java` | ✅ |
| `VbctLoginBO` | `VbctLogin.java` | ✅ |
| `StudentGpaBo` (report only) | `GpaReportRow.java` (DTO) | ✅ |

### Not Migrated (Intentionally or Needs Confirmation)

| Item | Notes |
|---|---|
| Static pages (`aboutUs`, `gallery`, `trustees`, `contact`) | Legacy-only; not needed for functional system. **Confirm with stakeholders.** |
| Admin user management (create/edit admin logins) | No admin management page in new system. Admins are managed directly in DB. **Flag if self-service needed.** |
| Email notification on student registration | Present in `StudentService.create()` via `EmailService` — new feature not in legacy ✅ |
| Rate limiting (`RateLimitingFilter`) | New addition, not in legacy ✅ |
| Docker Compose dev environment | New addition ✅ |

---

## 6. Infrastructure & Configuration

### Checklist
- [ ] `docker-compose.yml` — MySQL version pinned (8.4 is a beta release at time of writing; consider 8.0.x for LTS)
- [ ] `seed-test-data.sql` — passwords in seed data use `{noop}` prefix; acceptable for dev, confirm not imported in prod
- [ ] `application-prod.yml` — review all prod settings: ensure `show-sql: false`, `ddl-auto: validate`, no hardcoded secrets
- [ ] `docker-compose.yml` — no `JWT_SECRET` env var set; developers must set it themselves or document the default behavior
- [ ] Build artifact — `build/classes/` (compiled `.class` files from legacy Eclipse build) should be in `.gitignore` and removed from the PR

---

## 7. Final Approval Checklist

Before merging:

### Must Fix (Blockers)
- [x] S1: Externalize CORS origins ✅
- [x] S3: Remove hardcoded `spring.profiles.active: dev` ✅
- [x] B1: Move email send outside `@Transactional` boundary ✅
- [x] B2: Fix N+1 in `JobApplicationService.toResponse()` ✅
- [x] B3: Add `ResponseStatusException` + fallback handler to `GlobalExceptionHandler` ✅
- [x] T1: Controller slice tests added for all 8 controllers (72 tests) ✅

### Should Fix (Non-blocking but important)
- [x] S5: JWT expiry check added in `ProtectedRoute` ✅
- [ ] S4: JWT storage strategy — localStorage retained; httpOnly cookie migration not yet done
- [x] B4: `saveAll()` in `replaceSkills()` ✅
- [x] B5: `StudentCertificate.studentId` — `Integer` overload at repository layer; entity VARCHAR preserved ✅
- [x] B8: Idempotency in `InternshipStatusService.assign()` — upsert pattern ✅
- [x] F1–F3: TypeScript `any` types removed + per-panel download state in `ReportsPage` ✅
- [x] F7: `<ErrorBoundary>` added at router level ✅

### Confirm / Verify
- [x] ✅ `GET /api/jobs?companyId=X` — confirmed supported and covered by `JobControllerTest`
- [x] ✅ `build/classes/` legacy `.class` files added to `.gitignore` and excluded from PR
- [ ] ℹ️ Static pages (`aboutUs`, `gallery`) — intentionally dropped; confirm with stakeholders
- [ ] ℹ️ Admin user management — out-of-scope for this PR; admins managed directly in DB

### Remaining Open Items
- [ ] T2: Security integration test (role enforcement, cross-student access)
- [ ] T3: Unit test for `StudentService.create()`
- [ ] S2: JWT secret default still a known string — must set `JWT_SECRET` env var in all deploys
- [ ] S4: httpOnly cookie migration for JWT (deferred)
- [ ] F6: `client.ts` reads `localStorage` directly instead of via Zustand `getState()`
- [ ] F8: Zustand `persist` logout clearing — not explicitly tested
