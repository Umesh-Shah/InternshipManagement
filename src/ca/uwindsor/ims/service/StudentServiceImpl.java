package ca.uwindsor.ims.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.StudentDao;
import ca.uwindsor.ims.model.ApprovalStudentBo;
import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.ReportBo;
import ca.uwindsor.ims.model.StudentCertificateBo;
import ca.uwindsor.ims.model.StudentEducationBo;
import ca.uwindsor.ims.model.StudentGpaBo;
import ca.uwindsor.ims.model.StudentInfoBo;
import ca.uwindsor.ims.model.StudentWorkBo;

@Service("studentService")
@Transactional
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentDao dao;

	@Override
	public void savestudentinfo(StudentInfoBo i) throws Exception {
			dao.savestudentinfo(i);
	}

	@Override
	public void savestudentedu(StudentEducationBo e, Integer stu_id) throws Exception {
			dao.savestudentedu(e, stu_id);
	}

	@Override
	public void savestucertificate(StudentCertificateBo certificatebo, Integer stu_id) throws Exception {
			dao.savestucertificate(certificatebo, stu_id);
	}

	@Override
	public void saveworkexp(StudentWorkBo work, Integer stu_id) throws Exception {
			dao.saveworkexp(work, stu_id);
	}

	@Override
	public LoginBo getStudentList(String username, String password) throws Exception {
		return dao.getStudentList(username, password);
	}

	@Override
	public List<StudentInfoBo> searchreport(ReportBo r) throws Exception {
		return dao.searchreport(r);
	}

	@Override
	public List<StudentInfoBo> searchinterntype_student(ReportBo r) throws Exception {
		return dao.searchinterntype_student(r);
	}

	@Override
	public List<StudentInfoBo> getstudentinternshipwise() throws Exception {
		return dao.getstudentinternshipwise();
	}

	@Override
	public List<StudentGpaBo> getstudentavggpa() throws Exception {
		return dao.getstudentavggpa();
	}

	@Override
	public List<StudentGpaBo> search_gpa(ReportBo r) throws Exception {
		return dao.search_gpa(r);
	}

	@Override
	public List<StudentInfoBo> getstudentlistfromid(int student_id) throws Exception {
		return dao.getstudentlistfromid(student_id);
	}

	@Override
	public List<StudentEducationBo> getstudentedulistfromid(int student_id) throws Exception {
		return dao.getstudentedulistfromid(student_id);
	}

	@Override
	public void updateeducationinfo(StudentEducationBo r) throws Exception {
		dao.updateeducationinfo(r);
	}

	@Override
	public List<StudentCertificateBo> getstudentcertilistfromid(String student_id) throws Exception {
		return dao.getstudentcertilistfromid(student_id);
	}

	@Override
	public void updateCERTIcationinfo(StudentCertificateBo r) throws Exception {
		dao.updateCERTIcationinfo(r);
	}

	@Override
	public List<StudentInfoBo> getallstudentdata() throws Exception {
		return dao.getallstudentdata();
	}

	@Override
	public List<StudentWorkBo> getstudentworklistfromid(String student_id) throws Exception {
		return dao.getstudentworklistfromid(student_id);
	}

	@Override
	public void updateworkcationinfo(StudentWorkBo r) throws Exception {
		dao.updateworkcationinfo(r);
	}

	@Override
	public List<ApprovalStudentBo> getstudentapprovedlist() throws Exception {
		return dao.getstudentapprovedlist();
	}
	
	
	
	

}
