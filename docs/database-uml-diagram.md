# Database / Entity UML Diagrams

## 1. Complete Entity Class Diagram

```mermaid
classDiagram
    direction TB

    class StudentInfo {
        <<Entity: student>>
        +Integer id  PK
        +Integer studentId
        +Integer year
        +String fname
        +String lname
        +String mname
        +String stuEmail
        +String stuTelephone
        +String gender
        +String canadaStatus
        +String semester
        +String internshipStatus
        +String studentStatus
        +String country
    }

    class Login {
        <<Entity: login>>
        +Integer loginId  PK
        +Integer studentId  FK
        +String username
        +String pwd
        +String userType
        +String flag
    }

    class VbctLogin {
        <<Entity: vbct_login>>
        +String loginId  PK
        +String dsgnId
        +String loginName
        +String loginPassword
        +String firstName
        +String middleName
        +String lastName
        +String emailId
        +String dob
        +String phoneNo
        +String serviceFlg
        +String langId
        +String crtDt
        +String crtUsr
        +String lstUpdUsr
        +String lstUpdDt
        +String serviceExpiryDt
        +String forgotPwd
        +String frstLoginFlg
        +String pswdExpireDt
        +String lstPswdUpdDt
        +String loginStarttime
        +String loginEndtime
        +String ipaddr
    }

    class StudentEducation {
        <<Entity: student_edu>>
        +Integer stuEduId  PK
        +Integer studentId  FK
        +String degreeType
        +String major
        +String degreeGpa
        +String university
        +String universityLocation
    }

    class StudentCertificate {
        <<Entity: student_certificate>>
        +Integer certificateId  PK
        +String studentId  FK
        +String certificateTitle
        +String certificateBody
    }

    class StudentWork {
        <<Entity: student_experience>>
        +Integer stuWorkId  PK
        +Integer studentId  FK
        +String startDate
        +String endDate
        +String company
        +String companyLocation
        +String position
    }

    class StudentSkill {
        <<Entity: student_skill>>
        +Integer studentSkillId  PK
        +Integer skillId  FK
        +Integer studentId  FK
        +String skillName
    }

    class Skill {
        <<Entity: skill>>
        +Integer skillId  PK
        +String skillName
        +String skillType
    }

    class Company {
        <<Entity: company>>
        +Integer companyId  PK
        +String companyName
        +String address
        +String city
        +String postalCode
        +String country
        +String contactPersonFname
        +String contactPersonLname
        +String contactPersonPosition
        +String telephone
        +String email
        +String companyWebsite
        +String notes
    }

    class Job {
        <<Entity: job>>
        +Integer jobId  PK
        +String jobPosition
        +String description
        +String requirements
        +Integer salary
        +Integer companyId  FK
        +String responsibilities
        +String jobSkill
        +String internshipType
    }

    class InternshipType {
        <<Entity: internship>>
        +Integer internshipId  PK
        +String internshipType
        +String description
        +String internshipName
    }

    class StudentJobMapping {
        <<Entity: student_job_master>>
        +Integer studentJobId  PK
        +Integer jobId  FK
        +Integer studentId  FK
        +String flag
    }

    class StudentInternship {
        <<Entity: student_internship>>
        +Integer studentInternshipId  PK
        +Integer jobId  FK
        +Integer internshipId  FK
        +Integer companyId  FK
        +Integer studentId  FK
        +String internshipType
        +String internshipStatus
    }

    StudentInfo "1" --> "0..1" Login : studentId
    StudentInfo "1" --> "0..*" StudentEducation : studentId
    StudentInfo "1" --> "0..*" StudentCertificate : studentId
    StudentInfo "1" --> "0..*" StudentWork : studentId
    StudentInfo "1" --> "0..*" StudentSkill : studentId
    StudentInfo "1" --> "0..*" StudentJobMapping : studentId
    StudentInfo "1" --> "0..*" StudentInternship : studentId

    Skill "1" --> "0..*" StudentSkill : skillId

    Company "1" --> "0..*" Job : companyId
    Company "1" --> "0..*" StudentInternship : companyId

    Job "1" --> "0..*" StudentJobMapping : jobId
    Job "1" --> "0..*" StudentInternship : jobId

    InternshipType "1" --> "0..*" StudentInternship : internshipId
```

---

## 2. Student Domain (Zoomed In)

```mermaid
classDiagram
    direction LR

    class StudentInfo {
        <<student>>
        +Integer id PK
        +Integer studentId
        +String fname
        +String lname
        +String mname
        +String stuEmail
        +String stuTelephone
        +String gender
        +String canadaStatus
        +Integer year
        +String semester
        +String internshipStatus
        +String studentStatus
        +String country
    }

    class Login {
        <<login>>
        +Integer loginId PK
        +Integer studentId FK
        +String username
        +String pwd
        +String userType
        +String flag
    }

    class StudentEducation {
        <<student_edu>>
        +Integer stuEduId PK
        +Integer studentId FK
        +String degreeType
        +String major
        +String degreeGpa
        +String university
        +String universityLocation
    }

    class StudentCertificate {
        <<student_certificate>>
        +Integer certificateId PK
        +String studentId FK
        +String certificateTitle
        +String certificateBody
    }

    class StudentWork {
        <<student_experience>>
        +Integer stuWorkId PK
        +Integer studentId FK
        +String startDate
        +String endDate
        +String company
        +String companyLocation
        +String position
    }

    class StudentSkill {
        <<student_skill>>
        +Integer studentSkillId PK
        +Integer skillId FK
        +Integer studentId FK
        +String skillName
    }

    StudentInfo "1" -- "0..1" Login : credentials
    StudentInfo "1" -- "0..*" StudentEducation : education
    StudentInfo "1" -- "0..*" StudentCertificate : certificates
    StudentInfo "1" -- "0..*" StudentWork : work history
    StudentInfo "1" -- "0..*" StudentSkill : skills
```

---

## 3. Company & Job Domain (Zoomed In)

```mermaid
classDiagram
    direction LR

    class Company {
        <<company>>
        +Integer companyId PK
        +String companyName
        +String address
        +String city
        +String postalCode
        +String country
        +String contactPersonFname
        +String contactPersonLname
        +String contactPersonPosition
        +String telephone
        +String email
        +String companyWebsite
        +String notes
    }

    class Job {
        <<job>>
        +Integer jobId PK
        +Integer companyId FK
        +String jobPosition
        +String description
        +String requirements
        +Integer salary
        +String responsibilities
        +String jobSkill
        +String internshipType
    }

    class InternshipType {
        <<internship>>
        +Integer internshipId PK
        +String internshipType
        +String description
        +String internshipName
    }

    class Skill {
        <<skill>>
        +Integer skillId PK
        +String skillName
        +String skillType
    }

    Company "1" -- "0..*" Job : posts
```

---

## 4. Junction / Mapping Tables (Zoomed In)

```mermaid
classDiagram
    direction TB

    class StudentJobMapping {
        <<student_job_master>>
        +Integer studentJobId PK
        +Integer jobId FK
        +Integer studentId FK
        +String flag  N=pending A=approved
    }

    class StudentInternship {
        <<student_internship>>
        +Integer studentInternshipId PK
        +Integer jobId FK
        +Integer internshipId FK
        +Integer companyId FK
        +Integer studentId FK
        +String internshipType
        +String internshipStatus
    }

    class StudentInfo {
        <<student>>
        +Integer studentId
    }

    class Job {
        <<job>>
        +Integer jobId
    }

    class Company {
        <<company>>
        +Integer companyId
    }

    class InternshipType {
        <<internship>>
        +Integer internshipId
    }

    StudentInfo "1" -- "0..*" StudentJobMapping : applies
    Job "1" -- "0..*" StudentJobMapping : receives applications
    StudentJobMapping ..> StudentInternship : approved then assigned

    StudentInfo "1" -- "0..*" StudentInternship : assigned to
    Job "1" -- "0..*" StudentInternship : for position
    Company "1" -- "0..*" StudentInternship : hosted by
    InternshipType "1" -- "0..*" StudentInternship : type of
```

---

## 5. ER Diagram (Database-Level View)

```mermaid
erDiagram
    student ||--o| login : "has credentials"
    student ||--o{ student_edu : "has education"
    student ||--o{ student_certificate : "has certificates"
    student ||--o{ student_experience : "has work history"
    student ||--o{ student_skill : "has skills"
    student ||--o{ student_job_master : "applies to jobs"
    student ||--o{ student_internship : "assigned internships"

    skill ||--o{ student_skill : "assigned to students"

    company ||--o{ job : "posts jobs"

    job ||--o{ student_job_master : "receives applications"
    job ||--o{ student_internship : "linked to internship"

    company ||--o{ student_internship : "hosts intern"
    internship ||--o{ student_internship : "type of"

    student {
        int ID PK
        int STUDENT_ID UK
        int year
        varchar FNAME
        varchar LNAME
        varchar MNAME
        varchar STU_EMAIL
        varchar STU_TELEPHONE
        varchar GENDER
        varchar CANADA_STATUS
        varchar SEMESTER
        varchar INTERNSHIP_STATUS
        varchar student_status
        varchar country
    }
    login {
        int LOGIN_ID PK
        int STUDENT_ID FK
        varchar USERNAME
        varchar PWD
        varchar USER_TYPE
        varchar FLAG
    }
    vbct_login {
        varchar LOGIN_ID PK
        varchar LOGIN_NAME
        varchar LOGIN_PASSWORD
        varchar FIRST_NAME
        varchar LAST_NAME
        varchar EMAIL_ID
    }
    company {
        int company_id PK
        varchar COMPANY_NAME
        varchar ADDRESS
        varchar CITY
        varchar POSTAL_CODE
        varchar COUNTRY
        varchar CONTACT_PEARSON_FNAME
        varchar CONTACT_PERSON_LNAME
        varchar TELEPHONE
        varchar EMAIL
        varchar COMPANY_WEBSITE
        varchar NOTES
    }
    job {
        int JOB_ID PK
        int company_id FK
        varchar JOB_POSITION
        varchar DESCRIPTION
        varchar REQUIREMENTS
        int SALARY
        varchar RESPONSIBILITIES
        varchar job_skill
        varchar internship_type
    }
    skill {
        int SKILL_ID PK
        varchar SKILL_NAME
        varchar SKILL_TYPE
    }
    internship {
        int internship_id PK
        varchar INTERNSHIP_TYPE
        varchar internship_desc
        varchar INTERNSHIP_NAME
    }
    student_edu {
        int EDU_ID PK
        int STUDENT_ID FK
        varchar DEGREE_TYPE
        varchar MAJOR
        varchar DEGREE_GPA
        varchar UNIVERSITY
        varchar UNIVERSITY_LOCATION
    }
    student_certificate {
        int CERTIFICATE_ID PK
        varchar STUDENT_ID FK
        varchar CERTIFICATE_TITLE
        varchar CERTIFICATE_BODY
    }
    student_experience {
        int STU_WORK_ID PK
        int STUDENT_ID FK
        varchar START_DATE
        varchar END_DATE
        varchar COMPANY
        varchar COMPANY_LOCATION
        varchar POSITION
    }
    student_skill {
        int STUDENT_SKILL_ID PK
        int SKILL_ID FK
        int STUDENT_ID FK
        varchar SKILL_NAME
    }
    student_job_master {
        int STUDENT_JOB_ID PK
        int JOB_ID FK
        int STUDENT_ID FK
        varchar flag
    }
    student_internship {
        int STUDENT_INTERNSHIP_ID PK
        int job_id FK
        int INTERNSHIP_ID FK
        int COMPANY_ID FK
        int STUDENT_ID FK
        varchar INTERNSHIP_TYPE
        varchar INTERNSHIP_STATUS
    }
```
