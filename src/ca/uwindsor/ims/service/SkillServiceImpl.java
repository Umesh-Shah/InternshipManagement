package ca.uwindsor.ims.service;


import java.util.List;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.uwindsor.ims.dao.impl.SkillDao;
import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;

@Service("skillService")
@Transactional
public class SkillServiceImpl implements SkillService {
	
	@Autowired
	private SkillDao dao;

	Logger log = Logger.getLogger(SkillServiceImpl.class);

	@Override
	public List<SkillBo> getskilllist() throws Exception {
		
		return dao.getskilllist();
		
	}

	@Override
	public List<StudentJobSkillbo> getstudentfromjjob(String job_id) throws Exception {
		 
		return dao.getstudentfromjjob(job_id);
	}

	@Override
	public void savestudent_skill(int student_id, String[] shah) {
		
		 dao.savestudent_skill(student_id, shah);
	}
	
	
	
}
