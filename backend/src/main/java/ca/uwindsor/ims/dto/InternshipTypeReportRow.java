package ca.uwindsor.ims.dto;

public class InternshipTypeReportRow {
    private String internshipType;
    private Long studentCount;

    public InternshipTypeReportRow() {}

    public InternshipTypeReportRow(String internshipType, Long studentCount) {
        this.internshipType = internshipType;
        this.studentCount = studentCount;
    }

    public String getInternshipType() { return internshipType; }
    public void setInternshipType(String v) { internshipType = v; }
    public Long getStudentCount() { return studentCount; }
    public void setStudentCount(Long v) { studentCount = v; }
}
