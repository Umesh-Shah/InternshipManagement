package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.InternshipTypeBo;

@Component
public interface InternshipDao {
	
	public List<InternshipTypeBo> getinternshiplist()throws Exception;
	public void updatestudent_status(int i,String internship_type)throws Exception;
	
	

}
