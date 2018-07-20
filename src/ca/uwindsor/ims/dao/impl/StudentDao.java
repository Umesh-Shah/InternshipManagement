package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.ApprovalStudentBo;
import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.ReportBo;
import ca.uwindsor.ims.model.StudentCertificateBo;
import ca.uwindsor.ims.model.StudentEducationBo;
import ca.uwindsor.ims.model.StudentGpaBo;
import ca.uwindsor.ims.model.StudentInfoBo;
import ca.uwindsor.ims.model.StudentWorkBo;

@Component
public interface StudentDao {

	public void savestudentinfo(StudentInfoBo i) throws Exception;

	public void savestudentedu(StudentEducationBo e, Integer stu_id) throws Exception;

	public void savestucertificate(StudentCertificateBo certificatebo, Integer stu_id) throws Exception;

	public void saveworkexp(StudentWorkBo work, Integer stu_id) throws Exception;
	
	public LoginBo getStudentList(String username, String password);
	
	public List<StudentInfoBo> searchreport(ReportBo r)throws Exception;
	
	public List<StudentInfoBo> searchinterntype_student(ReportBo r) throws Exception;

	public List<StudentInfoBo> getstudentinternshipwise() throws Exception;

	public List<StudentGpaBo> getstudentavggpa() throws Exception;

	public List<StudentGpaBo> search_gpa(ReportBo r) throws Exception;

	public List<StudentInfoBo> getstudentlistfromid(int student_id) throws Exception;

	public List<StudentEducationBo> getstudentedulistfromid(int student_id) throws Exception;

	public void updateeducationinfo(StudentEducationBo r) throws Exception;

	public List<StudentCertificateBo> getstudentcertilistfromid(String student_id) throws Exception;

	public void updateCERTIcationinfo(StudentCertificateBo r) throws Exception;
	
	public List<StudentInfoBo> getallstudentdata()throws Exception;
	
	public List<StudentWorkBo> getstudentworklistfromid(String student_id)throws Exception;
	
	public void updateworkcationinfo(StudentWorkBo r)throws Exception;
	
	public List<ApprovalStudentBo> getstudentapprovedlist()throws Exception;
	
	
}
