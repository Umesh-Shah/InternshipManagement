package ca.uwindsor.ims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_skill")
public record StudentSkillBo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_SKILL_ID")
    int studentSkillId,
    
    @Column(name = "STUDENT_ID")
    int studentId,
    
    @Column(name = "SKILL_ID")
    int skillId,
    
    @Column(name = "SKILL_NAME")
    String skillName
) {
    public static StudentSkillBo create(int studentId, int skillId, String skillName) {
        return new StudentSkillBo(0, studentId, skillId, skillName);
    }
    
    public StudentSkillBo withId(int id) {
        return new StudentSkillBo(id, studentId, skillId, skillName);
    }
}
