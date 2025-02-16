-- Insert test data into login table
INSERT INTO login (STUDENT_ID, USERNAME, PWD, USER_TYPE, FLAG)
VALUES 
(1, 'testuser1', 'password123', 'STUDENT', 'Y'),
(2, 'testuser2', 'password456', 'ADMIN', 'Y');

-- Insert test data into vbct_login table
INSERT INTO vbct_login (LOGIN_ID, LOGIN_NAME, LOGIN_PASSWORD, FIRST_NAME, LAST_NAME, EMAIL_ID, SERVICE_FLG)
VALUES 
('admin1', 'admin', 'adminpass', 'Admin', 'User', 'admin@test.com', 'Y'),
('user1', 'user', 'userpass', 'Test', 'User', 'user@test.com', 'Y');

-- Insert test data into skill table
INSERT INTO skill (SKILL_NAME, SKILL_TYPE)
VALUES 
('Java Programming', 'Technical'),
('Python', 'Technical'),
('Project Management', 'Soft Skill');

-- Insert test data into student_skill table
INSERT INTO student_skill (STUDENT_ID, SKILL_ID, SKILL_NAME)
VALUES 
(1, 1, 'Java Programming'),
(1, 2, 'Python'),
(2, 3, 'Project Management');

-- Insert test data into internship table
INSERT INTO internship (INTERNSHIP_TYPE, internship_desc, INTERNSHIP_NAME)
VALUES 
('Summer', 'Summer internship program', 'Summer 2024 Program'),
('Co-op', 'Co-op work term', 'Fall 2024 Co-op'),
('Part-time', 'Part-time internship', 'Winter 2024 Part-time'); 