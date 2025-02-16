-- Sample employee data
INSERT INTO vbct_login (
    login_id, login_name, login_password, first_name, middle_name, last_name,
    dsgn_id, email_id, dob, phone_no, service_flg, lang_id,
    crt_dt, crt_usr, lst_upd_usr, lst_upd_dt, service_expiry_dt,
    forgot_pwd, frst_login_flg, pswd_expire_dt, lst_pswd_upd_dt,
    login_starttime, login_endtime, ipaddr
) VALUES (
    'TEST001', 'testadmin', 'admin123', 'Test', 'A', 'Admin',
    'ADMIN', 'admin@test.com', '1980-01-01', '1234567890', 'A', 'EN',
    CURRENT_TIMESTAMP(), 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP(),
    DATEADD('YEAR', 1, CURRENT_TIMESTAMP()),
    'N', 'N', DATEADD('MONTH', 3, CURRENT_TIMESTAMP()),
    CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(),
    '127.0.0.1'
);

-- Sample student login data
INSERT INTO login (
    student_id, username, pwd, user_type, flag
) VALUES (
    2001, 'teststudent', 'student123', 'STUDENT', 'A'
); 