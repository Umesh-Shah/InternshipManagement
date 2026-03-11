package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "\"year\"")
    private Integer year;

    @Column(name = "FNAME")
    private String fname;

    @Column(name = "LNAME")
    private String lname;

    @Column(name = "MNAME")
    private String mname;

    @Column(name = "STU_EMAIL")
    private String stuEmail;

    @Column(name = "STU_TELEPHONE")
    private String stuTelephone;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "CANADA_STATUS")
    private String canadaStatus;

    @Column(name = "SEMESTER")
    private String semester;

    @Column(name = "INTERNSHIP_STATUS")
    private String internshipStatus;

    @Column(name = "student_status")
    private String studentStatus;

    @Column(name = "country")
    private String country;

    public Integer getId() { return id; }
    public void setId(Integer v) { id = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public Integer getYear() { return year; }
    public void setYear(Integer v) { year = v; }
    public String getFname() { return fname; }
    public void setFname(String v) { fname = v; }
    public String getLname() { return lname; }
    public void setLname(String v) { lname = v; }
    public String getMname() { return mname; }
    public void setMname(String v) { mname = v; }
    public String getStuEmail() { return stuEmail; }
    public void setStuEmail(String v) { stuEmail = v; }
    public String getStuTelephone() { return stuTelephone; }
    public void setStuTelephone(String v) { stuTelephone = v; }
    public String getGender() { return gender; }
    public void setGender(String v) { gender = v; }
    public String getCanadaStatus() { return canadaStatus; }
    public void setCanadaStatus(String v) { canadaStatus = v; }
    public String getSemester() { return semester; }
    public void setSemester(String v) { semester = v; }
    public String getInternshipStatus() { return internshipStatus; }
    public void setInternshipStatus(String v) { internshipStatus = v; }
    public String getStudentStatus() { return studentStatus; }
    public void setStudentStatus(String v) { studentStatus = v; }
    public String getCountry() { return country; }
    public void setCountry(String v) { country = v; }
}
