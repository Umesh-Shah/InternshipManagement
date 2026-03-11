package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_edu")
public class StudentEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EDU_ID")
    private Integer stuEduId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "DEGREE_TYPE")
    private String degreeType;

    @Column(name = "MAJOR")
    private String major;

    @Column(name = "DEGREE_GPA")
    private String degreeGpa;

    @Column(name = "UNIVERSITY")
    private String university;

    @Column(name = "UNIVERSITY_LOCATION")
    private String universityLocation;

    public Integer getStuEduId() { return stuEduId; }
    public void setStuEduId(Integer v) { stuEduId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getDegreeType() { return degreeType; }
    public void setDegreeType(String v) { degreeType = v; }
    public String getMajor() { return major; }
    public void setMajor(String v) { major = v; }
    public String getDegreeGpa() { return degreeGpa; }
    public void setDegreeGpa(String v) { degreeGpa = v; }
    public String getUniversity() { return university; }
    public void setUniversity(String v) { university = v; }
    public String getUniversityLocation() { return universityLocation; }
    public void setUniversityLocation(String v) { universityLocation = v; }
}
