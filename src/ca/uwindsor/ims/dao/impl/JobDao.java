package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.JobBo;

@Component
public interface JobDao {
	
	public List<JobBo> getjoblist()throws Exception;
	
	public List<JobBo> getstudentjoblist(int student_id)throws Exception;
	
	public List<JobBo> getjoblistfromcompany(String company_id)throws Exception;
	
	public void updatestudentjobflag(String job_id, String student_id)throws Exception;

}
