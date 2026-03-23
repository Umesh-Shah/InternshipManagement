package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "internship")
public class InternshipType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internship_id")
    private Integer internshipId;

    @Column(name = "INTERNSHIP_TYPE")
    private String internshipType;

    @Column(name = "internship_desc")
    private String description;

    @Column(name = "INTERNSHIP_NAME")
    private String internshipName;

    public Integer getInternshipId() { return internshipId; }
    public void setInternshipId(Integer v) { internshipId = v; }
    public String getInternshipType() { return internshipType; }
    public void setInternshipType(String v) { internshipType = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public String getInternshipName() { return internshipName; }
    public void setInternshipName(String v) { internshipName = v; }
}
