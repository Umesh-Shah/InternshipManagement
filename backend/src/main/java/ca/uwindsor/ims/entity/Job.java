package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "JOB_POSITION")
    private String jobPosition;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REQUIREMENTS")
    private String requirements;

    @Column(name = "SALARY")
    private Integer salary;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "RESPONSIBILITIES")
    private String responsibilities;

    @Column(name = "job_skill")
    private String jobSkill;

    @Column(name = "internship_type")
    private String internshipType;

    public Integer getJobId() { return jobId; }
    public void setJobId(Integer v) { jobId = v; }
    public String getJobPosition() { return jobPosition; }
    public void setJobPosition(String v) { jobPosition = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String v) { requirements = v; }
    public Integer getSalary() { return salary; }
    public void setSalary(Integer v) { salary = v; }
    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer v) { companyId = v; }
    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String v) { responsibilities = v; }
    public String getJobSkill() { return jobSkill; }
    public void setJobSkill(String v) { jobSkill = v; }
    public String getInternshipType() { return internshipType; }
    public void setInternshipType(String v) { internshipType = v; }
}
