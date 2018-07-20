package ca.uwindsor.ims.service;

import java.util.List;

import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;

public interface SkillService {
	
	public List<SkillBo> getskilllist() throws Exception;
	
	public List<StudentJobSkillbo> getstudentfromjjob(String job_id) throws Exception;
	
	public void savestudent_skill(int student_id, String[] shah);

}
