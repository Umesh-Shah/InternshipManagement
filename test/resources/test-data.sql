-- Test data for VBCT_LOGIN table
INSERT INTO VBCT_LOGIN (
    LOGIN_ID, LOGIN_NAME, LOGIN_PASSWORD, FIRST_NAME, LAST_NAME, 
    EMAIL_ID, PHONE_NO, SERVICE_FLG, LANG_ID, CRT_DT, 
    CRT_USR, FRST_LOGIN_FLG
) VALUES (
    'test001', 'testuser', 'password123', 'Test', 'User',
    'test@example.com', '1234567890', 'Y', 'en', CURRENT_TIMESTAMP,
    'SYSTEM', 'Y'
);

-- Test data for LOGIN table
INSERT INTO LOGIN (
    USERNAME, PWD, ROLE
) VALUES (
    'testuser', 'password123', 'STUDENT'
);

-- Test data for STUDENT_INFO table
INSERT INTO STUDENT_INFO (
    STUDENT_ID, FNAME, LNAME, EMAIL, PHONE,
    COUNTRY, YEAR, STATUS
) VALUES (
    1001, 'John', 'Doe', 'john.doe@example.com', '9876543210',
    'Canada', '2024', 'ACTIVE'
);

-- Test data for STUDENT_EDUCATION table
INSERT INTO STUDENT_EDUCATION (
    STU_EDU_ID, STUDENT_ID, DEGREE_TYPE, MAJOR,
    UNIVERSITY, UNIVERSITY_LOCATION, DEGREE_GPA
) VALUES (
    1, 1001, 'Bachelor', 'Computer Science',
    'University of Windsor', 'Windsor, ON', '3.8'
);

-- Test data for STUDENT_CERTIFICATE table
INSERT INTO STUDENT_CERTIFICATE (
    CERTIFICATE_ID, STUDENT_ID, CERTIFICATE_TITLE,
    CERTIFICATE_BODY
) VALUES (
    1, 1001, 'AWS Certified Developer',
    'Amazon Web Services'
);

-- Test data for STUDENT_WORK table
INSERT INTO STUDENT_WORK (
    WORK_ID, STUDENT_ID, COMPANY_NAME,
    POSITION, START_DATE, END_DATE
) VALUES (
    1, 1001, 'Tech Corp',
    'Software Developer Intern', '2023-05-01', '2023-08-31'
); 