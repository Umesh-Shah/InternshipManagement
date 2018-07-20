package ca.uwindsor.ims.model;

import javax.persistence.Transient;

public class SkillBo {

	private int skill_id;
	private String skill_name;
	private String skill_type;

	public int getSkill_id() {
		return skill_id;
	}

	public String getSkill_name() {
		return skill_name;
	}

	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}

	public String getSkill_type() {
		return skill_type;
	}

	public void setSkill_type(String skill_type) {
		this.skill_type = skill_type;
	}

	/**
	 * Gets a builder which is used to create skill objects.
	 * 
	 * @param skill_name
	 *            The skill name of the created skill set.
	 * @param lastName
	 *            The skill type of the created skill set.
	 * @return A new Builder instance.
	 */
	public static Builder getBuilder(String skillName, String skillType, int skillId) {
		return new Builder(skillName, skillType, skillId);
	}

	/**
	 * Gets the skill set.
	 * 
	 * @return The skill set.
	 */
	@Transient
	public String getSet() {
		StringBuilder name = new StringBuilder();

		name.append(skill_name);
		name.append(" ");
		name.append(skill_type);

		return name.toString();
	}

	public void update(String skillName, String skillType, int skillId) {
		this.skill_id = skill_id;
		this.skill_name = skill_name;
		this.skill_type = skill_type;
	}

	/**
	 * A Builder class used to create new skill set objects.
	 */
	public static class Builder {
		SkillBo built;

		/**
		 * Creates a new Builder instance.
		 * 
		 * @param skill
		 *            name The skill name of the created skill set object.
		 * @param skill
		 *            type The skill type of the created skill set object.
		 */
		Builder(String skillName, String skilltype, int skillId) {
			built = new SkillBo();
			built.skill_name = skillName;
			built.skill_type = skilltype;
			built.skill_id = skillId;

		}

		/**
		 * Builds the new skill set object.
		 * 
		 * @return The created skill set object.
		 */
		public SkillBo build() {
			return built;
		}
	}

	/**
	 * This setter method should only be used by unit tests.
	 * 
	 * @param id
	 */
	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}
}
