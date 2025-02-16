package ca.uwindsor.ims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "internship")
public record InternshipTypeBo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internship_id")
    int id,
    
    @Column(name = "INTERNSHIP_TYPE")
    String internshipType,
    
    @Column(name = "internship_desc")
    String description,
    
    @Column(name = "INTERNSHIP_NAME")
    String internshipName
) {
    public static InternshipTypeBo create(String internshipType, String description, String internshipName) {
        return new InternshipTypeBo(0, internshipType, description, internshipName);
    }
    
    public InternshipTypeBo withId(int id) {
        return new InternshipTypeBo(id, internshipType, description, internshipName);
    }
}
