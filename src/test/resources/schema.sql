CREATE TABLE vbct_login (
    login_id VARCHAR(50) PRIMARY KEY,
    login_name VARCHAR(100) NOT NULL,
    login_password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100),
    middle_name VARCHAR(100),
    last_name VARCHAR(100),
    dsgn_id VARCHAR(50),
    email_id VARCHAR(100),
    dob VARCHAR(10),
    phone_no VARCHAR(20),
    service_flg CHAR(1),
    lang_id VARCHAR(10),
    crt_dt TIMESTAMP,
    crt_usr VARCHAR(50),
    lst_upd_usr VARCHAR(50),
    lst_upd_dt TIMESTAMP,
    service_expiry_dt TIMESTAMP,
    forgot_pwd CHAR(1),
    frst_login_flg CHAR(1),
    pswd_expire_dt TIMESTAMP,
    lst_pswd_upd_dt TIMESTAMP,
    login_starttime TIMESTAMP,
    login_endtime TIMESTAMP,
    ipaddr VARCHAR(50)
);

CREATE TABLE login (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    pwd VARCHAR(100) NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    flag VARCHAR(1)
);

CREATE TABLE skill (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL,
    skill_type VARCHAR(50)
);

CREATE TABLE student_skill (
    student_skill_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    skill_id INT NOT NULL,
    skill_name VARCHAR(100),
    FOREIGN KEY (skill_id) REFERENCES skill(skill_id)
);

CREATE TABLE internship (
    internship_id INT AUTO_INCREMENT PRIMARY KEY,
    internship_type VARCHAR(50) NOT NULL,
    internship_desc TEXT,
    internship_name VARCHAR(100) NOT NULL
); 