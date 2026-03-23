package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

// NOTE: The old hbm.xml mapped student_id as java.lang.String — anomalous vs all other
// student-related tables that use int. Verify the actual MySQL column type; if the column
// is INT, change studentId to Integer here.
@Entity
@Table(name = "student_certificate")
public class StudentCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CERTIFICATE_ID")
    private Integer certificateId;

    @Column(name = "STUDENT_ID")
    private String studentId;

    @Column(name = "CERTIFICATE_TITLE")
    private String certificateTitle;

    @Column(name = "CERTIFICATE_BODY")
    private String certificateBody;

    public Integer getCertificateId() { return certificateId; }
    public void setCertificateId(Integer v) { certificateId = v; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String v) { studentId = v; }
    public String getCertificateTitle() { return certificateTitle; }
    public void setCertificateTitle(String v) { certificateTitle = v; }
    public String getCertificateBody() { return certificateBody; }
    public void setCertificateBody(String v) { certificateBody = v; }
}
