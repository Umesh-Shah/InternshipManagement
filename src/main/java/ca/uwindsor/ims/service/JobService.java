package ca.uwindsor.ims.service;

import java.util.List;

import ca.uwindsor.ims.model.JobBo;

public interface JobService {
	
	public List<JobBo> getjoblist()throws Exception;
	
	public List<JobBo> getstudentjoblist(int student_id) throws Exception;
	
	public List<JobBo> getjoblistfromcompany(String company_id) throws Exception;
	
	public void updatestudentjobflag(String job_id, String student_id) throws Exception;

}
