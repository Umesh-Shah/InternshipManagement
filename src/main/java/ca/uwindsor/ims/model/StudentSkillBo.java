package ca.uwindsor.ims.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_skill")
public class StudentSkillBo implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STUDENT_SKILL_ID")
	private Integer studentSkillId;
	
	@Column(name = "STUDENT_ID")
	private Integer studentId;
	
	@Column(name = "SKILL_ID")
	private Integer skillId;
	
	@Column(name = "SKILL_LEVEL")
	private String skillLevel;

	// Default constructor for JPA
	public StudentSkillBo() {}

	// Constructor for creating new student skills
	public StudentSkillBo(Integer studentId, Integer skillId, String skillLevel) {
		this.studentId = studentId;
		this.skillId = skillId;
		this.skillLevel = skillLevel;
	}

	// Constructor with all fields
	public StudentSkillBo(Integer studentSkillId, Integer studentId, Integer skillId, String skillLevel) {
		this.studentSkillId = studentSkillId;
		this.studentId = studentId;
		this.skillId = skillId;
		this.skillLevel = skillLevel;
	}

	public Integer getStudentSkillId() {
		return studentSkillId;
	}
	
	public void setStudentSkillId(Integer studentSkillId) {
		this.studentSkillId = studentSkillId;
	}
	
	public Integer getStudentId() {
		return studentId;
	}
	
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	public Integer getSkillId() {
		return skillId;
	}
	
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	
	public String getSkillLevel() {
		return skillLevel;
	}
	
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	// Static factory method
	public static StudentSkillBo create(Integer studentId, Integer skillId, String skillLevel) {
		return new StudentSkillBo(studentId, skillId, skillLevel);
	}
}
