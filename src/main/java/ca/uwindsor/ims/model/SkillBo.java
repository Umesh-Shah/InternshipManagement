package ca.uwindsor.ims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "skill")
public record SkillBo(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SKILL_ID")
	int skillId,
	
	@Column(name = "SKILL_NAME")
	String skillName,
	
	@Column(name = "SKILL_TYPE")
	String skillType
) {
	public static SkillBo create(String skillName, String skillType) {
		return new SkillBo(0, skillName, skillType);
	}
	
	public SkillBo withId(int id) {
		return new SkillBo(id, skillName, skillType);
	}
}
