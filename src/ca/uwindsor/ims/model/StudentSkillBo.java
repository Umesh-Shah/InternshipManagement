package ca.uwindsor.ims.model;

public class StudentSkillBo {
private int student_skill_id,student_id,skill_id;
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
}
