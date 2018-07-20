package ca.uwindsor.ims.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.JobDao;
import ca.uwindsor.ims.model.JobBo;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService{
	
	@Autowired
	private JobDao dao;

	@Override
	public List<JobBo> getjoblist() throws Exception {
		return dao.getjoblist();
	}

	@Override
	public List<JobBo> getstudentjoblist(int student_id) throws Exception {
		return dao.getstudentjoblist(student_id);
	}

	@Override
	public List<JobBo> getjoblistfromcompany(String company_id) throws Exception {
		return dao.getjoblistfromcompany(company_id);
	}

	@Override
	public void updatestudentjobflag(String job_id, String student_id) throws Exception {
		dao.updatestudentjobflag(job_id, student_id);
		
	}

}
