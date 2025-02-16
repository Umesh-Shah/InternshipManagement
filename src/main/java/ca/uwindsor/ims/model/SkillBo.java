package ca.uwindsor.ims.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "skill")
public class SkillBo implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SKILL_ID")
	private Integer skillId;
	
	@Column(name = "SKILL_NAME", nullable = false)
	private String skillName;
	
	@Column(name = "SKILL_TYPE")
	private String skillType;

	// Default constructor for JPA
	public SkillBo() {}

	// Constructor for creating new skills
	public SkillBo(String skillName, String skillType) {
		this.skillName = skillName;
		this.skillType = skillType;
	}

	// Constructor with all fields
	public SkillBo(Integer skillId, String skillName, String skillType) {
		this.skillId = skillId;
		this.skillName = skillName;
		this.skillType = skillType;
	}

	public Integer getSkillId() {
		return skillId;
	}

	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getSkillType() {
		return skillType;
	}

	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}

	public static SkillBo create(String skillName, String skillType) {
		return new SkillBo(skillName, skillType);
	}
}
