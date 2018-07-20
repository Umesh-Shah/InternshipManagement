package ca.uwindsor.ims.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.InternshipDao;
import ca.uwindsor.ims.model.InternshipTypeBo;

@Service("internshipService")
@Transactional
public class InternshipServiceImpl implements InternshipService {
	
	@Autowired
	private InternshipDao dao;

	@Override
	public List<InternshipTypeBo> getinternshiplist() throws Exception {
		
		return dao.getinternshiplist(); 
	}

	@Override
	public void updatestudent_status(int i, String internship_type) throws Exception {
		dao.updatestudent_status(i, internship_type);
		
	}

}
