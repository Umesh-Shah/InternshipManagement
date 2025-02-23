-- Password is 'password' encoded with BCrypt
INSERT INTO users (email, password, first_name, last_name, role, enabled)
VALUES 
('admin@uwindsor.ca', '$2a$10$rKN3vmWShK5wNgR.YJFLz.NxPsX0zJcR8mZ2hq6uRv8.h3gZGGsHe', 'Admin', 'User', 'ADMIN', true),
('student@uwindsor.ca', '$2a$10$rKN3vmWShK5wNgR.YJFLz.NxPsX0zJcR8mZ2hq6uRv8.h3gZGGsHe', 'Student', 'User', 'STUDENT', true),
('faculty@uwindsor.ca', '$2a$10$rKN3vmWShK5wNgR.YJFLz.NxPsX0zJcR8mZ2hq6uRv8.h3gZGGsHe', 'Faculty', 'User', 'FACULTY', true)
ON DUPLICATE KEY UPDATE email=email; 