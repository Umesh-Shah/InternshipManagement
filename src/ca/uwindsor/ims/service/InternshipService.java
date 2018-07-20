package ca.uwindsor.ims.service;

import java.util.List;

import ca.uwindsor.ims.model.InternshipTypeBo;

public interface InternshipService {
	
	public List<InternshipTypeBo> getinternshiplist() throws Exception;
	
	public void updatestudent_status(int i,String internship_type) throws Exception;

}
