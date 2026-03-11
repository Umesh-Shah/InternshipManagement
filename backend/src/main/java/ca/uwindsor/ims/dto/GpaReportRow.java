package ca.uwindsor.ims.dto;

public class GpaReportRow {
    private Integer studentId;
    private String fname;
    private String lname;
    private String university;
    private String universityLocation;
    private String degreeType;
    private String degreeGpa;

    public GpaReportRow() {}

    public GpaReportRow(Integer studentId, String fname, String lname,
                        String university, String universityLocation,
                        String degreeType, String degreeGpa) {
        this.studentId = studentId;
        this.fname = fname;
        this.lname = lname;
        this.university = university;
        this.universityLocation = universityLocation;
        this.degreeType = degreeType;
        this.degreeGpa = degreeGpa;
    }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getFname() { return fname; }
    public void setFname(String v) { fname = v; }
    public String getLname() { return lname; }
    public void setLname(String v) { lname = v; }
    public String getUniversity() { return university; }
    public void setUniversity(String v) { university = v; }
    public String getUniversityLocation() { return universityLocation; }
    public void setUniversityLocation(String v) { universityLocation = v; }
    public String getDegreeType() { return degreeType; }
    public void setDegreeType(String v) { degreeType = v; }
    public String getDegreeGpa() { return degreeGpa; }
    public void setDegreeGpa(String v) { degreeGpa = v; }
}
