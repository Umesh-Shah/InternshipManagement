package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_skill")
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_SKILL_ID")
    private Integer studentSkillId;

    @Column(name = "SKILL_ID")
    private Integer skillId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "SKILL_NAME")
    private String skillName;

    public Integer getStudentSkillId() { return studentSkillId; }
    public void setStudentSkillId(Integer v) { studentSkillId = v; }
    public Integer getSkillId() { return skillId; }
    public void setSkillId(Integer v) { skillId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String v) { skillName = v; }
}
