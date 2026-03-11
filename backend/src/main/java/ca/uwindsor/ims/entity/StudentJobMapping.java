package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_job_master")
public class StudentJobMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_JOB_ID")
    private Integer studentJobId;

    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "flag")
    private String flag;

    public Integer getStudentJobId() { return studentJobId; }
    public void setStudentJobId(Integer v) { studentJobId = v; }
    public Integer getJobId() { return jobId; }
    public void setJobId(Integer v) { jobId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getFlag() { return flag; }
    public void setFlag(String v) { flag = v; }
}
