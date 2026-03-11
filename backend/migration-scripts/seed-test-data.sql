-- =============================================================================
-- Test Data Seed Script
-- =============================================================================
-- Populates all tables with realistic fake data for development/testing.
--
-- Run against the `test` database (dev profile uses port 3307):
--   mysql -u root -p -P 3307 test < seed-test-data.sql
--
-- Run order respects FK dependencies:
--   vbct_login → student → login → student_edu/experience/certificate
--   skill → student_skill
--   company → internship → job → student_internship / student_job_master
-- =============================================================================

-- Disable FK checks for clean inserts
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================================================
-- 1. ADMIN LOGINS (vbct_login)
-- =============================================================================
TRUNCATE TABLE vbct_login;

INSERT INTO vbct_login (
  LOGIN_ID, DSGN_ID, LOGIN_NAME, LOGIN_PASSWORD,
  FIRST_NAME, MIDDLE_NAME, LAST_NAME,
  EMAIL_ID, DOB, PHONE_NO,
  SERVICE_FLG, LANG_ID,
  CRT_DT, CRT_USR, LST_UPD_USR, LST_UPD_DT,
  SERVICE_EXPIRY_DT, FORGOT_PWD, FRST_LOGIN_FLG,
  PSWD_EXPIRE_DT, LST_PSWD_UPD_DT,
  LOGIN_STARTTIME, LOGIN_ENDTIME, IPADDR
) VALUES
(
  'ADM001', 'ADMIN', 'admin', '{noop}admin123',
  'Sarah', '', 'Mitchell',
  'sarah.mitchell@uwindsor.ca', '1980-05-15', '519-555-0101',
  'Y', 'EN',
  '2024-01-01', 'SYSTEM', 'SYSTEM', '2024-01-01',
  '2026-12-31', 'N', 'N',
  '2026-12-31', '2024-01-01',
  '08:00:00', '18:00:00', '127.0.0.1'
),
(
  'ADM002', 'ADMIN', 'coordinator', '{noop}coord456',
  'James', 'R', 'Patterson',
  'james.patterson@uwindsor.ca', '1975-11-22', '519-555-0102',
  'Y', 'EN',
  '2024-01-01', 'SYSTEM', 'SYSTEM', '2024-01-01',
  '2026-12-31', 'N', 'N',
  '2026-12-31', '2024-01-01',
  '08:00:00', '18:00:00', '127.0.0.1'
);

-- =============================================================================
-- 2. STUDENTS
-- =============================================================================
TRUNCATE TABLE student;

INSERT INTO student (
  STUDENT_ID, year, FNAME, LNAME, MNAME,
  STU_EMAIL, STU_TELEPHONE, GENDER,
  CANADA_STATUS, SEMESTER, INTERNSHIP_STATUS, student_status, country
) VALUES
(1001, 3, 'Alice',   'Nguyen',    'T',  'alice.nguyen@uwindsor.ca',    '519-555-1001', 'Female', 'Citizen',         'Fall 2025',   'enrolled',   'active',   'Canada'),
(1002, 2, 'Marcus',  'Johnson',   '',   'marcus.johnson@uwindsor.ca',  '519-555-1002', 'Male',   'Permanent Resident','Winter 2025','enrolled',   'active',   'Canada'),
(1003, 4, 'Priya',   'Sharma',    'K',  'priya.sharma@uwindsor.ca',    '519-555-1003', 'Female', 'Study Permit',    'Fall 2025',   'completed',  'active',   'India'),
(1004, 1, 'Liam',    'Tremblay',  '',   'liam.tremblay@uwindsor.ca',   '519-555-1004', 'Male',   'Citizen',         'Winter 2025', 'not enrolled','active',  'Canada'),
(1005, 3, 'Fatima',  'Al-Hassan', 'S',  'fatima.alhassan@uwindsor.ca', '519-555-1005', 'Female', 'Study Permit',    'Fall 2025',   'enrolled',   'active',   'UAE');

-- =============================================================================
-- 3. STUDENT LOGIN CREDENTIALS (login)
-- =============================================================================
TRUNCATE TABLE login;

INSERT INTO login (STUDENT_ID, USERNAME, PWD, USER_TYPE, FLAG) VALUES
(1001, 'alice.nguyen',    '{noop}pass1001', 'student', 'Y'),
(1002, 'marcus.johnson',  '{noop}pass1002', 'student', 'Y'),
(1003, 'priya.sharma',    '{noop}pass1003', 'student', 'Y'),
(1004, 'liam.tremblay',   '{noop}pass1004', 'student', 'Y'),
(1005, 'fatima.alhassan', '{noop}pass1005', 'student', 'Y');

-- =============================================================================
-- 4. STUDENT EDUCATION
-- =============================================================================
TRUNCATE TABLE student_edu;

INSERT INTO student_edu (STUDENT_ID, DEGREE_TYPE, MAJOR, DEGREE_GPA, UNIVERSITY, UNIVERSITY_LOCATION) VALUES
-- Alice
(1001, 'Bachelor', 'Computer Science',      '3.7', 'University of Windsor', 'Windsor, ON'),
(1001, 'Bachelor', 'Mathematics (Minor)',   '3.5', 'University of Windsor', 'Windsor, ON'),
-- Marcus
(1002, 'Bachelor', 'Software Engineering',  '3.4', 'University of Windsor', 'Windsor, ON'),
-- Priya
(1003, 'Master',   'Computer Science',      '3.9', 'University of Windsor', 'Windsor, ON'),
(1003, 'Bachelor', 'Information Technology','3.8', 'IIT Bombay',            'Mumbai, India'),
-- Liam
(1004, 'Bachelor', 'Computer Science',      '3.2', 'University of Windsor', 'Windsor, ON'),
-- Fatima
(1005, 'Bachelor', 'Computer Engineering',  '3.6', 'University of Windsor', 'Windsor, ON'),
(1005, 'Bachelor', 'Computer Science',      '3.8', 'UAE University',        'Al Ain, UAE');

-- =============================================================================
-- 5. STUDENT WORK EXPERIENCE
-- =============================================================================
TRUNCATE TABLE student_experience;

INSERT INTO student_experience (STUDENT_ID, START_DATE, END_DATE, COMPANY, COMPANY_LOCATION, POSITION) VALUES
-- Alice
(1001, '2024-05-01', '2024-08-31', 'TechNova Inc.',       'Toronto, ON',    'Junior Developer Intern'),
(1001, '2023-09-01', '2023-12-31', 'University of Windsor', 'Windsor, ON',  'Teaching Assistant'),
-- Marcus
(1002, '2024-01-01', '2024-04-30', 'DataBridge Solutions', 'Waterloo, ON',  'QA Automation Intern'),
-- Priya
(1003, '2023-05-01', '2023-08-31', 'Infosys Limited',     'Pune, India',    'Software Engineer'),
(1003, '2022-06-01', '2023-04-30', 'TCS',                 'Mumbai, India',  'Junior Developer'),
-- Fatima
(1005, '2024-05-01', '2024-08-31', 'Cisco Systems',       'Ottawa, ON',     'Network Engineering Intern');

-- =============================================================================
-- 6. STUDENT CERTIFICATES
-- =============================================================================
TRUNCATE TABLE student_certificate;

INSERT INTO student_certificate (STUDENT_ID, CERTIFICATE_TITLE, CERTIFICATE_BODY) VALUES
('1001', 'AWS Certified Cloud Practitioner',   'Issued by Amazon Web Services — June 2024'),
('1001', 'Oracle Java SE 11 Certified',        'Issued by Oracle — January 2024'),
('1003', 'Google Professional Data Engineer',  'Issued by Google Cloud — March 2024'),
('1003', 'Certified Kubernetes Administrator', 'Issued by CNCF — August 2023'),
('1005', 'Cisco CCNA',                         'Issued by Cisco — September 2024'),
('1002', 'Microsoft Azure Fundamentals AZ-900','Issued by Microsoft — February 2024');

-- =============================================================================
-- 7. SKILLS CATALOG
-- =============================================================================
TRUNCATE TABLE skill;

INSERT INTO skill (SKILL_NAME, SKILL_TYPE) VALUES
-- Technical
('Java',            'Technical'),
('Python',          'Technical'),
('JavaScript',      'Technical'),
('TypeScript',      'Technical'),
('React',           'Technical'),
('Spring Boot',     'Technical'),
('SQL',             'Technical'),
('MySQL',           'Technical'),
('MongoDB',         'Technical'),
('Docker',          'Technical'),
('Kubernetes',      'Technical'),
('AWS',             'Technical'),
('Git',             'Technical'),
('REST APIs',       'Technical'),
('Machine Learning','Technical'),
-- Soft Skills
('Communication',   'Soft'),
('Teamwork',        'Soft'),
('Problem Solving', 'Soft'),
('Time Management', 'Soft'),
-- Languages
('English',         'Language'),
('French',          'Language'),
('Hindi',           'Language'),
('Arabic',          'Language');

-- =============================================================================
-- 8. STUDENT SKILLS
-- =============================================================================
TRUNCATE TABLE student_skill;

-- Alice (1001) — Java, React, SQL, Git, Communication
INSERT INTO student_skill (SKILL_ID, STUDENT_ID, SKILL_NAME)
SELECT SKILL_ID, 1001, SKILL_NAME FROM skill WHERE SKILL_NAME IN ('Java','React','SQL','Git','Communication','English');

-- Marcus (1002) — Python, SQL, Docker, Teamwork
INSERT INTO student_skill (SKILL_ID, STUDENT_ID, SKILL_NAME)
SELECT SKILL_ID, 1002, SKILL_NAME FROM skill WHERE SKILL_NAME IN ('Python','SQL','Docker','Teamwork','English');

-- Priya (1003) — Java, Spring Boot, MySQL, Kubernetes, Machine Learning, Problem Solving
INSERT INTO student_skill (SKILL_ID, STUDENT_ID, SKILL_NAME)
SELECT SKILL_ID, 1003, SKILL_NAME FROM skill WHERE SKILL_NAME IN ('Java','Spring Boot','MySQL','Kubernetes','Machine Learning','Problem Solving','English','Hindi');

-- Liam (1004) — JavaScript, TypeScript, React, Git
INSERT INTO student_skill (SKILL_ID, STUDENT_ID, SKILL_NAME)
SELECT SKILL_ID, 1004, SKILL_NAME FROM skill WHERE SKILL_NAME IN ('JavaScript','TypeScript','React','Git','English','French');

-- Fatima (1005) — Python, AWS, Docker, REST APIs, Arabic
INSERT INTO student_skill (SKILL_ID, STUDENT_ID, SKILL_NAME)
SELECT SKILL_ID, 1005, SKILL_NAME FROM skill WHERE SKILL_NAME IN ('Python','AWS','Docker','REST APIs','Communication','English','Arabic');

-- =============================================================================
-- 9. COMPANIES
-- =============================================================================
TRUNCATE TABLE company;

INSERT INTO company (
  COMPANY_NAME, ADDRESS, CITY, POSTAL_CODE, COUNTRY,
  CONTACT_PEARSON_FNAME, CONTACT_PERSON_LNAME, CONTACT_PEARSON_POSITION,
  TELEPHONE, EMAIL, COMPANY_WEBSITE, NOTES
) VALUES
('TechNova Solutions',  '123 Innovation Dr',  'Toronto',   'M5H 2N2', 'Canada', 'Emily',  'Chen',     'HR Manager',       '416-555-2001', 'hr@technova.ca',    'www.technova.ca',    'Leading software consultancy'),
('DataBridge Corp',     '456 Data Ave',       'Waterloo',  'N2L 3G1', 'Canada', 'Robert', 'Kowalski', 'Talent Acquisition','519-555-2002', 'jobs@databridge.ca','www.databridge.ca',  'Data analytics and BI firm'),
('CloudPeak Systems',   '789 Cloud Blvd',     'Ottawa',    'K1P 1A4', 'Canada', 'Nadia',  'Okonkwo',  'Recruiter',        '613-555-2003', 'careers@cloudpeak.ca','www.cloudpeak.ca', 'Cloud infrastructure provider'),
('PixelForge Studio',   '321 Creative Lane',  'Windsor',   'N9A 5M3', 'Canada', 'Daniel', 'Leblanc',  'CTO',              '519-555-2004', 'dan@pixelforge.ca', 'www.pixelforge.ca',  'UI/UX and web development studio');

-- =============================================================================
-- 10. INTERNSHIP TYPES
-- =============================================================================
TRUNCATE TABLE internship;

INSERT INTO internship (INTERNSHIP_TYPE, internship_desc, INTERNSHIP_NAME) VALUES
('COOP',     'Paid work term integrated into academic program',      'Co-op Work Term'),
('SUMMER',   'Summer internship May through August',                 'Summer Internship'),
('SEMESTER', 'Part-time internship during an academic semester',     'Semester Internship'),
('RESEARCH', 'Research-focused internship with faculty supervision', 'Research Internship');

-- =============================================================================
-- 11. JOB POSTINGS
-- =============================================================================
TRUNCATE TABLE job;

INSERT INTO job (JOB_POSITION, DESCRIPTION, REQUIREMENTS, SALARY, company_id, RESPONSIBILITIES, job_skill, internship_type) VALUES
(
  'Backend Java Developer',
  'Build and maintain microservices for our financial platform.',
  '2+ years Java experience, Spring Boot, REST APIs',
  22,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'TechNova Solutions'),
  'Design APIs, write unit tests, participate in code reviews',
  'Java,Spring Boot,REST APIs,SQL',
  'COOP'
),
(
  'Frontend React Developer',
  'Develop modern React applications for client-facing dashboards.',
  'Experience with React, TypeScript, CSS frameworks',
  20,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'TechNova Solutions'),
  'Build UI components, integrate REST APIs, improve UX',
  'React,TypeScript,JavaScript,Git',
  'SUMMER'
),
(
  'Data Engineer Intern',
  'Build ETL pipelines and maintain data warehouse infrastructure.',
  'Python, SQL, experience with cloud data services',
  21,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'DataBridge Corp'),
  'Develop ETL jobs, optimize queries, document data flows',
  'Python,SQL,MySQL,Docker',
  'COOP'
),
(
  'ML Research Intern',
  'Assist research team with NLP and computer vision projects.',
  'Strong Python skills, familiarity with ML frameworks',
  23,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'DataBridge Corp'),
  'Prototype models, run experiments, write research notes',
  'Python,Machine Learning,SQL',
  'RESEARCH'
),
(
  'Cloud Operations Intern',
  'Support cloud infrastructure provisioning and monitoring.',
  'AWS fundamentals, scripting (Python or Bash), Docker',
  20,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'CloudPeak Systems'),
  'Provision cloud resources, monitor uptime, write runbooks',
  'AWS,Docker,Python,REST APIs',
  'COOP'
),
(
  'Full Stack Web Developer',
  'Work on internal tools and client web applications.',
  'JavaScript/TypeScript, React, Node.js or Spring Boot',
  19,
  (SELECT company_id FROM company WHERE COMPANY_NAME = 'PixelForge Studio'),
  'Develop features end-to-end, maintain CI/CD pipelines',
  'JavaScript,TypeScript,React,Git',
  'SEMESTER'
);

-- =============================================================================
-- 12. STUDENT JOB APPLICATIONS (student_job_master)
-- =============================================================================
TRUNCATE TABLE student_job_master;

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1001, 'accepted'  FROM job j WHERE j.JOB_POSITION = 'Backend Java Developer';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1001, 'applied'   FROM job j WHERE j.JOB_POSITION = 'Full Stack Web Developer';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1002, 'accepted'  FROM job j WHERE j.JOB_POSITION = 'Data Engineer Intern';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1002, 'applied'   FROM job j WHERE j.JOB_POSITION = 'ML Research Intern';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1003, 'accepted'  FROM job j WHERE j.JOB_POSITION = 'ML Research Intern';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1003, 'applied'   FROM job j WHERE j.JOB_POSITION = 'Backend Java Developer';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1004, 'applied'   FROM job j WHERE j.JOB_POSITION = 'Frontend React Developer';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1004, 'applied'   FROM job j WHERE j.JOB_POSITION = 'Full Stack Web Developer';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1005, 'shortlisted' FROM job j WHERE j.JOB_POSITION = 'Cloud Operations Intern';

INSERT INTO student_job_master (JOB_ID, STUDENT_ID, flag)
SELECT j.JOB_ID, 1005, 'applied'   FROM job j WHERE j.JOB_POSITION = 'Data Engineer Intern';

-- =============================================================================
-- 13. STUDENT INTERNSHIPS (student_internship)
-- =============================================================================
TRUNCATE TABLE student_internship;

-- Alice — accepted COOP at TechNova as Backend Java Developer
INSERT INTO student_internship (job_id, INTERNSHIP_ID, COMPANY_ID, STUDENT_ID, INTERNSHIP_TYPE, INTERNSHIP_STATUS)
SELECT
  j.JOB_ID,
  i.internship_id,
  c.company_id,
  1001,
  'COOP',
  'active'
FROM job j
JOIN company c ON c.COMPANY_NAME = 'TechNova Solutions'
JOIN internship i ON i.INTERNSHIP_TYPE = 'COOP'
WHERE j.JOB_POSITION = 'Backend Java Developer';

-- Marcus — accepted COOP at DataBridge as Data Engineer
INSERT INTO student_internship (job_id, INTERNSHIP_ID, COMPANY_ID, STUDENT_ID, INTERNSHIP_TYPE, INTERNSHIP_STATUS)
SELECT
  j.JOB_ID,
  i.internship_id,
  c.company_id,
  1002,
  'COOP',
  'active'
FROM job j
JOIN company c ON c.COMPANY_NAME = 'DataBridge Corp'
JOIN internship i ON i.INTERNSHIP_TYPE = 'COOP'
WHERE j.JOB_POSITION = 'Data Engineer Intern';

-- Priya — completed Research Internship at DataBridge
INSERT INTO student_internship (job_id, INTERNSHIP_ID, COMPANY_ID, STUDENT_ID, INTERNSHIP_TYPE, INTERNSHIP_STATUS)
SELECT
  j.JOB_ID,
  i.internship_id,
  c.company_id,
  1003,
  'RESEARCH',
  'completed'
FROM job j
JOIN company c ON c.COMPANY_NAME = 'DataBridge Corp'
JOIN internship i ON i.INTERNSHIP_TYPE = 'RESEARCH'
WHERE j.JOB_POSITION = 'ML Research Intern';

-- Fatima — shortlisted for COOP at CloudPeak
INSERT INTO student_internship (job_id, INTERNSHIP_ID, COMPANY_ID, STUDENT_ID, INTERNSHIP_TYPE, INTERNSHIP_STATUS)
SELECT
  j.JOB_ID,
  i.internship_id,
  c.company_id,
  1005,
  'COOP',
  'pending'
FROM job j
JOIN company c ON c.COMPANY_NAME = 'CloudPeak Systems'
JOIN internship i ON i.INTERNSHIP_TYPE = 'COOP'
WHERE j.JOB_POSITION = 'Cloud Operations Intern';

-- Re-enable FK checks
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- Quick verification counts
-- =============================================================================
SELECT 'vbct_login'          AS tbl, COUNT(*) AS `rows` FROM vbct_login
UNION ALL SELECT 'student',          COUNT(*) FROM student
UNION ALL SELECT 'login',            COUNT(*) FROM login
UNION ALL SELECT 'student_edu',      COUNT(*) FROM student_edu
UNION ALL SELECT 'student_experience', COUNT(*) FROM student_experience
UNION ALL SELECT 'student_certificate', COUNT(*) FROM student_certificate
UNION ALL SELECT 'skill',            COUNT(*) FROM skill
UNION ALL SELECT 'student_skill',    COUNT(*) FROM student_skill
UNION ALL SELECT 'company',          COUNT(*) FROM company
UNION ALL SELECT 'internship',       COUNT(*) FROM internship
UNION ALL SELECT 'job',              COUNT(*) FROM job
UNION ALL SELECT 'student_job_master', COUNT(*) FROM student_job_master
UNION ALL SELECT 'student_internship', COUNT(*) FROM student_internship;
