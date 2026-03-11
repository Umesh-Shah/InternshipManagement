package ca.uwindsor.ims.dto;

public class StudentReportRow {
    private Integer studentId;
    private String fname;
    private String lname;
    private Integer year;
    private String country;
    private String semester;
    private String internshipStatus;
    private String studentStatus;

    public StudentReportRow() {}

    public StudentReportRow(Integer studentId, String fname, String lname, Integer year,
                            String country, String semester, String internshipStatus, String studentStatus) {
        this.studentId = studentId;
        this.fname = fname;
        this.lname = lname;
        this.year = year;
        this.country = country;
        this.semester = semester;
        this.internshipStatus = internshipStatus;
        this.studentStatus = studentStatus;
    }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getFname() { return fname; }
    public void setFname(String v) { fname = v; }
    public String getLname() { return lname; }
    public void setLname(String v) { lname = v; }
    public Integer getYear() { return year; }
    public void setYear(Integer v) { year = v; }
    public String getCountry() { return country; }
    public void setCountry(String v) { country = v; }
    public String getSemester() { return semester; }
    public void setSemester(String v) { semester = v; }
    public String getInternshipStatus() { return internshipStatus; }
    public void setInternshipStatus(String v) { internshipStatus = v; }
    public String getStudentStatus() { return studentStatus; }
    public void setStudentStatus(String v) { studentStatus = v; }
}
