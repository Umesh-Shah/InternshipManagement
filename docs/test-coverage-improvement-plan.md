# Test Coverage Improvement Plan

**Generated:** 2026-03-25
**Baseline:** `mvn verify` — 155 tests, all passing

## Current State

| Layer | Lines | Branches | Methods |
|---|---|---|---|
| `controller` | 98% (162/165) | 95% (19/20) | 96% (73/76) |
| `service` | 71% (315/443) | **35% (34/98)** | 77% (72/93) |
| `security` | 86% (55/64) | 75% (15/20) | 73% (19/26) |
| `aspect` | **16% (4/25)** | **0% (0/2)** | 50% (2/4) |
| `filter` | 92% (11/12) | **25% (1/4)** | 100% (3/3) |
| `logging` | 56% (5/9) | N/A | 60% (3/5) |
| `config` | 92% (44/48) | **17% (2/12)** | 100% (18/18) |
| `exception` | 100% | 100% | 100% |
| **TOTAL** | **79% (625/796)** | **46% (73/158)** | **85% (199/235)** |

**Target:** 90%+ lines, 75%+ branches

---

## Priority 1 — `ReportService` (4% lines, 0% branches)

**File:** `service/ReportService.java`
**Test to create:** `service/ReportServiceTest.java`
**Test type:** `@DataJpaTest` + `@AutoConfigureTestDatabase(replace = ANY)` + `spring.jpa.hibernate.ddl-auto=create-drop`

**Why it's 4%:** `ReportControllerTest` mocks the service bean entirely, so the conditional JPQL-building logic (46 branches) never executes.

### Tests to write

```
ReportServiceTest
├── studentsReport_noFilters_returnsAll
├── studentsReport_withYear_filtersCorrectly
├── studentsReport_withCountry_filtersCorrectly
├── studentsReport_withSemester_filtersCorrectly
├── studentsReport_withInternshipStatus_filtersCorrectly
├── studentsReport_withStudentStatus_filtersCorrectly
├── studentsReport_allFilters_combined
├── companiesReport_noFilter_returnsAll
├── companiesReport_withCity_filtersCorrectly
├── internshipTypesReport_noFilter_returnsAll
├── internshipTypesReport_withType_filtersCorrectly
├── gpaReport_noFilters_returnsAll
├── gpaReport_withYear_filtersCorrectly
├── gpaReport_withDegreeType_filtersCorrectly
├── jobsReport_noFilter_returnsAll
├── jobsReport_withCompanyId_filtersCorrectly
└── getFilters_returnsDistinctValues
```

**Approach:** Seed minimal data via `@Sql` or `@BeforeEach` entity saves, then call `ReportService` directly (inject via `@Autowired`).

---

## Priority 2 — `EmailService` (21% lines, 0% branches)

**File:** `service/EmailService.java`
**Test to create:** `service/EmailServiceTest.java`
**Test type:** Plain Mockito (`@ExtendWith(MockitoExtension.class)`)

**Why it's 21%:** Only the `enabled=false` early-return path is incidentally hit. The send path and the catch block are untested.

### Tests to write

```
EmailServiceTest
├── sendRegistrationEmail_whenDisabled_skipsMailSender
│     inject enabled=false via ReflectionTestUtils, verify mailSender never called
├── sendRegistrationEmail_whenEnabled_sendsCorrectMessage
│     inject enabled=true, verify mailSender.send() called with correct to/subject/body
└── sendRegistrationEmail_whenMailSenderThrows_logsWarningAndDoesNotRethrow
      inject enabled=true, mailSender.send() throws MailException, verify no exception propagates
```

**Approach:** Use `@InjectMocks` + `ReflectionTestUtils.setField(service, "enabled", true)` to toggle the flag.

---

## Priority 3 — `LoggingAspect` (16% lines, 0% branches)

**File:** `aspect/LoggingAspect.java`
**Test to create:** `aspect/LoggingAspectTest.java`
**Test type:** `@SpringBootTest(webEnvironment = NONE)` — needs a real Spring context to activate AOP proxies

**Why it's 16%:** `@WebMvcTest` does not apply AOP proxies. Aspects only fire when called through a Spring-managed proxy.

### Tests to write

```
LoggingAspectTest
├── logController_normalInvocation_proceedsAndReturns
│     Call a controller method through MockMvc with full Spring context
├── logController_slowRequest_triggersWarnLog
│     Set slowRequestThresholdMs=0 in test props, assert WARN log contains "Slow request"
├── logController_exceptionThrown_logsErrorAndRethrows
│     Mock a service to throw RuntimeException, assert it propagates and is logged
└── logService_normalInvocation_proceedsAndReturns
      Call a service bean through its proxy, verify result is returned
```

**Approach:** Use `@SpringBootTest` with `@MockBean` for repositories. Use `ListAppender` from Logback to capture log output.

---

## Priority 4 — `SkillService` (64% lines)

**File:** `service/SkillService.java`
**Existing test:** `service/SkillServiceTest.java` (4 tests — add to this file)

**Missing coverage:** `findAll()`, `findById()`, `update()` happy path.

### Tests to add

```
SkillServiceTest (additions)
├── findAll_delegatesToRepo
├── findById_existingId_returnsSkill
├── findById_unknownId_returnsEmpty
└── update_existingSkill_updatesAndReturns
```

---

## Priority 5 — `ImsUserDetails` (61% lines)

**File:** `security/ImsUserDetails.java`
**Test to create:** `security/ImsUserDetailsTest.java`
**Test type:** Plain unit test (no Spring context needed)

### Tests to write

```
ImsUserDetailsTest
├── fromLogin_adminUserType_assignsRoleAdmin
├── fromLogin_studentUserType_assignsRoleStudent
├── fromLogin_preservesStudentId
├── fromVbctLogin_assignsRoleAdmin
├── fromVbctLogin_studentIdIsNull
├── getAuthorities_returnsCorrectAuthority
├── isAccountNonExpired_returnsTrue
├── isAccountNonLocked_returnsTrue
├── isCredentialsNonExpired_returnsTrue
└── isEnabled_returnsTrue
```

---

## Priority 6 — `AuditLogger` (56% lines)

**File:** `logging/AuditLogger.java`
**Existing test:** `logging/` — no test file exists yet
**Test to create:** `logging/AuditLoggerTest.java`
**Test type:** Plain unit test with Logback `ListAppender`

**Missing:** `logLogout()` and `logAdminAction()` are never called.

### Tests to write

```
AuditLoggerTest
├── logLogin_success_writesToAuditLogger
├── logLogin_failure_writesToAuditLogger
├── logLogout_writesToAuditLogger
├── logDataAccess_writesToAuditLogger
└── logAdminAction_writesToAuditLogger
```

---

## Priority 7 — `MdcFilter` branch coverage (25% branches)

**File:** `filter/MdcFilter.java`
**Existing test:** `filter/MdcFilterTest.java` (add to this file)

**Missing branches:** `resolveClientIp()` — the `X-Forwarded-For` present branch and the multi-IP (comma-separated) case.

### Tests to add

```
MdcFilterTest (additions)
├── resolveClientIp_withXForwardedFor_usesFirstIp
└── resolveClientIp_withMultipleForwardedIps_usesFirst
```

---

## Priority 8 — `SecurityConfig` / `config` branch coverage (17% branches)

**File:** `config/SecurityConfig.java`
**Existing test:** `security/SecurityIntegrationTest.java` (add cases)

**Missing branches:** `cookieBearerTokenResolver` — cookie-absent path, cookie-present-but-blank, header fallback; `jwtAuthenticationConverter` — null role claim.

### Tests to add

```
SecurityIntegrationTest (additions)
├── bearerTokenResolver_noCookie_fallsBackToHeader
├── bearerTokenResolver_cookiePresent_usesCookieToken
├── bearerTokenResolver_cookiePresentButBlank_fallsBackToHeader
└── jwtConverter_missingRoleClaim_returnsEmptyAuthorities
```

---

## Implementation Order

1. `ReportServiceTest` — highest complexity, highest impact on branch %
2. `EmailServiceTest` — quick win, ~15 min
3. `SkillServiceTest` additions — quick win, ~10 min
4. `ImsUserDetailsTest` — quick win, ~10 min
5. `AuditLoggerTest` — quick win, ~10 min
6. `MdcFilterTest` additions — quick win, ~10 min
7. `LoggingAspectTest` — requires `@SpringBootTest`, slower to run
8. `SecurityIntegrationTest` additions — requires real JWT tokens

## Expected Outcome After All Phases

| Metric | Before | After |
|---|---|---|
| Lines | 79% | ~92% |
| Branches | 46% | ~78% |
| Methods | 85% | ~95% |
| Test count | 155 | ~205 |

## Notes

- All `@DataJpaTest` classes must include `@AutoConfigureTestDatabase(replace = ANY)` and `spring.jpa.hibernate.ddl-auto=create-drop` per project constraints.
- All `@WebMvcTest` classes must exclude `SecurityAutoConfiguration`, `SecurityFilterAutoConfiguration`, `OAuth2ResourceServerAutoConfiguration`.
- Java 21 target — Lombok is broken; use explicit getters/setters in test fixtures.
- Do not use `@hookform/resolvers` — irrelevant here but noted for frontend parity.
