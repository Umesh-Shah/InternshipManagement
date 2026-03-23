package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_internship")
public class StudentInternship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_INTERNSHIP_ID")
    private Integer studentInternshipId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "INTERNSHIP_ID")
    private Integer internshipId;

    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "INTERNSHIP_TYPE")
    private String internshipType;

    @Column(name = "INTERNSHIP_STATUS")
    private String internshipStatus;

    public Integer getStudentInternshipId() { return studentInternshipId; }
    public void setStudentInternshipId(Integer v) { studentInternshipId = v; }
    public Integer getJobId() { return jobId; }
    public void setJobId(Integer v) { jobId = v; }
    public Integer getInternshipId() { return internshipId; }
    public void setInternshipId(Integer v) { internshipId = v; }
    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer v) { companyId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getInternshipType() { return internshipType; }
    public void setInternshipType(String v) { internshipType = v; }
    public String getInternshipStatus() { return internshipStatus; }
    public void setInternshipStatus(String v) { internshipStatus = v; }
}
