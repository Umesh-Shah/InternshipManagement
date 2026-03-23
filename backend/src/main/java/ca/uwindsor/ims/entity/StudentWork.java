package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_experience")
public class StudentWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STU_WORK_ID")
    private Integer stuWorkId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "COMPANY_LOCATION")
    private String companyLocation;

    @Column(name = "POSITION")
    private String position;

    public Integer getStuWorkId() { return stuWorkId; }
    public void setStuWorkId(Integer v) { stuWorkId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String v) { startDate = v; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String v) { endDate = v; }
    public String getCompany() { return company; }
    public void setCompany(String v) { company = v; }
    public String getCompanyLocation() { return companyLocation; }
    public void setCompanyLocation(String v) { companyLocation = v; }
    public String getPosition() { return position; }
    public void setPosition(String v) { position = v; }
}
