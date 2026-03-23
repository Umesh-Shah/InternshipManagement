package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILL_ID")
    private Integer skillId;

    @Column(name = "SKILL_NAME")
    private String skillName;

    @Column(name = "SKILL_TYPE")
    private String skillType;

    public Integer getSkillId() { return skillId; }
    public void setSkillId(Integer v) { skillId = v; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String v) { skillName = v; }
    public String getSkillType() { return skillType; }
    public void setSkillType(String v) { skillType = v; }
}
