package ca.uwindsor.ims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_skill")
public class StudentSkillBo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STUDENT_SKILL_ID")
	private int student_skill_id;
	
	@Column(name = "STUDENT_ID")
	private int student_id;
	
	@Column(name = "SKILL_ID")
	private int skill_id;
	
	@Column(name = "SKILL_NAME")
	private String skill_name;

	public int getStudent_skill_id() {
		return student_skill_id;
	}
	
	public void setStudent_skill_id(int student_skill_id) {
		this.student_skill_id = student_skill_id;
	}
	
	public int getStudent_id() {
		return student_id;
	}
	
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	
	public int getSkill_id() {
		return skill_id;
	}
	
	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}
	
	public String getSkill_name() {
		return skill_name;
	}
	
	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}

	public static StudentSkillBo create(int studentId, int skillId, String skillName) {
		StudentSkillBo studentSkill = new StudentSkillBo();
		studentSkill.setStudent_id(studentId);
		studentSkill.setSkill_id(skillId);
		studentSkill.setSkill_name(skillName);
		return studentSkill;
	}
}
