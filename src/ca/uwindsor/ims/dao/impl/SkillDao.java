package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;

@Component
public interface SkillDao {
	

	public List<SkillBo> getskilllist()throws Exception;
	
	public List<StudentJobSkillbo> getstudentfromjjob(String job_id)throws Exception;
	
	public void savestudent_skill(int student_id, String[] shah);
	

}
