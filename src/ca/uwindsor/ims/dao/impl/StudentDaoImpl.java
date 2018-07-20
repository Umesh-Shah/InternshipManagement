package ca.uwindsor.ims.dao.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.ApprovalStudentBo;
import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.ReportBo;
import ca.uwindsor.ims.model.StudentCertificateBo;
import ca.uwindsor.ims.model.StudentEducationBo;
import ca.uwindsor.ims.model.StudentGpaBo;
import ca.uwindsor.ims.model.StudentInfoBo;
import ca.uwindsor.ims.model.StudentInternshipBo;
import ca.uwindsor.ims.model.StudentWorkBo;


@Component
@Repository
public class StudentDaoImpl implements StudentDao {
	
	Logger log = Logger.getLogger(StudentDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	DateFormat dateandtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void savestudentinfo(StudentInfoBo info) throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		Transaction tr = null;
		boolean flag = false;
		try {
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			Serializable serializable = session.save(info);

			System.out.println(serializable);
			if (null != serializable) {
				flag = true;
			} else {
				flag = false;
			}
			tr.commit();
		} catch (Exception e) {
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			tr.rollback();
		} finally {
			log.info("END");
			session.close();
		}

	}

	@Override
	public void savestudentedu(StudentEducationBo edu, Integer stu_id) throws Exception {
		// TODO Auto-generated method stub
		log.info("START");

		String degree_type = edu.getDegree_type();
		String[] degree_type1 = degree_type.split(",");
		String gpa = edu.getDegree_gpa();
		String[] gpa1 = gpa.split(",");
		String major = edu.getMajor();
		String[] major1 = major.split(",");
		String uni = edu.getUniversity();
		String[] uni1 = uni.split(",");
		String uni_loc = edu.getUniversity_location();
		String[] uni_loc1 = uni_loc.split(",");

		Serializable serializable = null;
		for (int i = 0; i < degree_type1.length; i++) {
			Session session = null;
			Transaction tr = null;
			boolean flag = false;
			try {
				session = getSessionFactory().openSession();
				tr = session.beginTransaction();
				edu.setDegree_type(degree_type1[i]);
				edu.setDegree_gpa(gpa1[i]);
				edu.setMajor(major1[i]);
				edu.setUniversity(uni1[i]);
				edu.setUniversity_location(uni_loc1[i]);
				edu.setStudent_id(stu_id);

				serializable = session.save(edu);
				System.out.println(serializable);
				if (null != serializable) {
					flag = true;
				} else {
					flag = false;
				}
				tr.commit();
			} catch (Exception e) {
				log.error("ERROR OCCURED");
				log.error(e.getMessage(), e);
				e.printStackTrace();
				tr.rollback();
			} finally {
				log.info("END");
				session.close();
			}
		}

	}

	@Override
	public void savestucertificate(StudentCertificateBo certificatebo, Integer stu_id) throws Exception {
		// TODO Auto-generated method stub
		String title = certificatebo.getCertificate_title();
		String[] title1 = title.split(",");
		String body = certificatebo.getCertificate_body();
		String[] body1 = body.split(",");

		String student_id = stu_id + "";
		Serializable serializable = null;
		for (int i = 0; i < title1.length; i++) {
			Session session = null;
			Transaction tr = null;
			boolean flag = false;
			try {
				session = getSessionFactory().openSession();
				tr = session.beginTransaction();
				certificatebo.setCertificate_title(title1[i]);
				certificatebo.setCertificate_body(body1[i]);
				certificatebo.setStudent_id(student_id);

				serializable = session.save(certificatebo);
				System.out.println(serializable);
				if (null != serializable) {
					flag = true;
				} else {
					flag = false;
				}
				tr.commit();
			} catch (Exception e) {
				log.error("ERROR OCCURED");
				log.error(e.getMessage(), e);
				e.printStackTrace();
				tr.rollback();
			} finally {
				log.info("END");
				session.close();
			}
		}
	}

	@Override
	public void saveworkexp(StudentWorkBo work, Integer stu_id) throws Exception {
		// TODO Auto-generated method stub
		String company = work.getCompany();
		String[] company1 = company.split(",");
		String company_loc = work.getCompany_location();
		String[] company_loc1 = company_loc.split(",");
		String position = work.getCompany_location();
		String[] position1 = position.split(",");
		String start_date = work.getStart_date();
		String[] start_date1 = start_date.split(",");
		Serializable serializable = null;
		for (int i = 0; i < company1.length; i++) {
			Session session = null;
			Transaction tr = null;
			boolean flag = false;
			try {
				session = getSessionFactory().openSession();
				tr = session.beginTransaction();
				work.setCompany(company1[i]);
				work.setCompany_location(company_loc1[i]);
				work.setPosition(position1[i]);
				work.setStart_date(start_date1[i]);

				work.setStudent_id(stu_id);
				serializable = session.save(work);
				System.out.println(serializable);
				if (null != serializable) {
					flag = true;
				} else {
					flag = false;
				}
				tr.commit();
			} catch (Exception e) {
				log.error("ERROR OCCURED");
				log.error(e.getMessage(), e);
				e.printStackTrace();
				tr.rollback();
			} finally {
				log.info("END");
				session.close();
			}
		}
	}
	
	@Override
	public LoginBo getStudentList(String username, String password) {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<LoginBo> list = null;
		LoginBo loginbo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From LoginBo where username = '"+username+"' and pwd = '"+password+"'");
			list = query.list();
			if(list.size() != 0)
			{
				loginbo = list.get(0);
			}
			return loginbo;
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return loginbo;	
	}
	
	@Override
	public List<StudentInfoBo> getstudentinternshipwise()
			throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentInternshipBo> list = null;
		List<StudentInfoBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query = null;
			String sql = "from  StudentInternshipBo";
			
			query = session.createQuery(sql);
		
			list = query.list();
			int student_id= 0;
			String s = "";
			for(int z=0;z<list.size();z++)
			{
				// list=(List<Student_Job_mapping>) list.get(z);
				s = s + "'"+list.get(z).getStudent_id()+"', ";
//				student_id=list.get(z).getStudent_id();
			}
			s = s.substring(0, s.length()-2);
			Query query1=session.createQuery("From StudentInfoBo where student_id IN ("+s+")");
			list1=query1.list();
			return list1;
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;
	}

	@Override
	public List<StudentGpaBo> getstudentavggpa() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentGpaBo> info=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("select e.student_id as student_id,i.fname as fname,i.lname as lname,e.degree_gpa as degree_gpa,i.country as country,e.degree_type as degree_type from  StudentEducationBo e,StudentInfoBo i where e.student_id=i.student_id ").setResultTransformer(Transformers.aliasToBean(StudentGpaBo.class));

			
			
		
			info = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		
		
		
		return info;	}

	@Override
	public List<StudentGpaBo> search_gpa(ReportBo r) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentGpaBo> info=null;
		try{
			session = getSessionFactory().openSession();
			Query query= null;
			String sql =" select e.student_id as student_id,i.fname as fname,i.lname as lname,e.degree_gpa as degree_gpa,i.country as country,e.degree_type as degree_type from  StudentEducationBo e,StudentInfoBo i where e.student_id=i.student_id";
			System.out.println(r);
			if(!"".equalsIgnoreCase(r.getYear()) || !"".equalsIgnoreCase(r.getCountry())||!"".equalsIgnoreCase(r.getUniversity()) || !"".equalsIgnoreCase(r.getUniversity_location())||!"".equalsIgnoreCase(r.getDegree_type())){
				sql = sql + " and ";
			}
			if(null != r.getYear() && !"".equalsIgnoreCase(r.getYear())){
				sql = sql + "i.year = '"+ r.getYear().trim() +"' and " ;
			}
			
			if(null != r.getCountry() && !"".equalsIgnoreCase(r.getCountry())){
				sql = sql + "i.country = '"+r.getCountry() +"' and ";
			}
			if(null != r.getUniversity() && !"".equalsIgnoreCase(r.getUniversity())){
				sql = sql + "e.university = '"+r.getUniversity() +"' and ";
			}
			if(null != r.getUniversity_location() && !"".equalsIgnoreCase(r.getUniversity_location())){
				sql = sql + "e.university_location = '"+r.getUniversity_location() +"' and ";
			}
			if(null != r.getDegree_type() && !"".equalsIgnoreCase(r.getDegree_type())){
				sql = sql + "e.degree_type = '"+r.getDegree_type() +"' and ";
			}
			if(!"".equalsIgnoreCase(r.getYear()) || !"".equalsIgnoreCase(r.getCountry())||  !"".equalsIgnoreCase(r.getUniversity()) ||  !"".equalsIgnoreCase(r.getUniversity_location()) ||  !"".equalsIgnoreCase(r.getDegree_type())){
				sql = sql + " 1=1 ";
			}
			System.out.println("Final sql ==>>" + sql);
			query = session.createQuery(sql).setResultTransformer(Transformers.aliasToBean(StudentGpaBo.class));
			
			
		
			info = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		
		
		
		return info;
	}
	
	@SuppressWarnings("unchecked")
	
	/**
	 * Gives the student info by the ID 
	 * @param Student Id
	 * @return List containing student information
	 */
	
	@Override
	public List<StudentInfoBo> getstudentlistfromid(int student_id) throws Exception {
		Session session = null;
		List<StudentInfoBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From StudentInfoBo where student_id=?");
			query.setParameter(0, student_id);
			System.out.println(query);
			list1=query.list();
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;	
		}

	@Override
	public List<StudentEducationBo> getstudentedulistfromid(int student_id)
			throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentEducationBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From StudentEducationBo where student_id=?");
			query.setParameter(0, student_id);
			System.out.println(query);
			list1=query.list();
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;	
	}

	@Override
	public void updateeducationinfo(StudentEducationBo r) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tr = null;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			session.update(r);
			
			tr.commit();
			
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			
			
		}
		finally{
			log.info("END");
			session.close();
		
	}
	}

	@Override
	public List<StudentCertificateBo> getstudentcertilistfromid(String student_id)
			throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentCertificateBo> list1=null;
		try{
			String student_id1=student_id+"";
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From StudentCertificateBo where student_id=?");
			query.setParameter(0, student_id1);
			System.out.println(query);
			list1=query.list();
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;	
	}

	@Override
	public void updateCERTIcationinfo(StudentCertificateBo r) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tr = null;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			session.update(r);
			
			tr.commit();
			
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			
			
		}
		finally{
			log.info("END");
			session.close();
		
	}
	}
	
	@Override
	public List<StudentInfoBo> searchreport(ReportBo r) throws Exception {
		log.info("START");
		Session session = null;
		List<StudentInfoBo> info=null;
		try{
			session = getSessionFactory().openSession();
			Query query = null;
			String sql = "from StudentInfoBo";
			System.out.println(r);
			if(!"".equalsIgnoreCase(r.getYear()) || !"".equalsIgnoreCase(r.getStudent_status()) || !"".equalsIgnoreCase(r.getInternship_type()) || !"".equalsIgnoreCase(r.getSemester()) || !"".equalsIgnoreCase(r.getCountry())){
				sql = sql + " where ";
			}
			if(null != r.getYear() && !"".equalsIgnoreCase(r.getYear())){
				sql = sql + "year = '"+ r.getYear().trim() +"' and " ;
			}
			if(null != r.getStudent_status() && !"".equalsIgnoreCase(r.getStudent_status()))
			{
				sql = sql + "student_status like '%"+ r.getStudent_status() +"' and ";
			}
			if(null != r.getInternship_type() && !"".equalsIgnoreCase(r.getInternship_type())){
				sql = sql + "internship_status = '"+ r.getInternship_type()+"' and ";
			}
			if(null != r.getCountry() && !"".equalsIgnoreCase(r.getCountry())){
				sql = sql + "country = '"+r.getCountry() +"' and ";
			}
			if(null != r.getSemester() && !"".equalsIgnoreCase(r.getSemester())){
				sql = sql + "semester = '"+r.getSemester() +"' and ";
			}
			if(!"".equalsIgnoreCase(r.getYear()) || !"".equalsIgnoreCase(r.getStudent_status()) || !"".equalsIgnoreCase(r.getInternship_type()) || !"".equalsIgnoreCase(r.getCountry())){
				sql = sql + " 1=1 ";
			}
			System.out.println("Final sql ==>>" + sql);
			query = session.createQuery(sql);
		
			info = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		
		
		
		return info;
		
	}
	
	@Override
	public List<StudentInfoBo> searchinterntype_student(ReportBo r)
			throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentInternshipBo> list = null;
		List<StudentInfoBo> list1=null;
		Query query = null;
		try{
			session = getSessionFactory().openSession();
			String sql = "from  StudentInternshipBo";
			if(null != r.getInternship_type() && !"".equalsIgnoreCase(r.getInternship_type())){
				sql = sql + " where ";
			}
			if(null != r.getInternship_type() && !"".equalsIgnoreCase(r.getInternship_type())){
				sql = sql + "internship_id = '"+r.getInternship_type() +"' and ";
			}
			if(null != r.getInternship_type() && !"".equalsIgnoreCase(r.getInternship_type())){
				sql = sql + " 1=1 ";
			}
			query = session.createQuery(sql);
		
			list = query.list();
			String s = "";
//			int student_id= 0;
			for(int z=0;z<list.size();z++)
			{
				// list=(List<Student_Job_mapping>) list.get(z);
				s = s + "'"+list.get(z).getStudent_id()+"', ";
//				student_id=list.get(z).getStudent_id();
			}
			s = s.substring(0, s.length()-2);
			Query query1=session.createQuery("From StudentInfoBo where student_id IN ("+s+")");
			list1=query1.list();
			return list1;
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;	}

	@Override
	public List<StudentInfoBo> getallstudentdata() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<StudentInfoBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("from StudentInfoBo");
			lstboo = query.list();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return lstboo;	}

	

	
	
	
	
	
	

	@Override
	public List<ApprovalStudentBo> getstudentapprovedlist() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<ApprovalStudentBo> info=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("select s.student_id as student_id,j.job_id as job_id,s.lname as lname,s.fname as fname,js.job_position as job_position,js.company_id as company_id,c.company_name as company_name  from StudentInfoBo s,Student_Job_mapping j,JobBo js,CompanyBo c where s.student_id=j.student_id and j.flag='Y' and j.job_id=js.job_id and js.company_id=c.company_id group by s.student_id,j.job_id,s.lname,s.fname,js.job_position,js.company_id,c.company_name").setResultTransformer(Transformers.aliasToBean(ApprovalStudentBo.class));
			info = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		
		
		
		return info;
		}

	

	@Override
	public List<StudentWorkBo> getstudentworklistfromid(String student_id)
			throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentWorkBo> list1=null;
		try{
			
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From StudentWorkBo where student_id=?");
			query.setParameter(0, Integer.parseInt(student_id));
			System.out.println(query);
			list1=query.list();
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return list1;	
		}

	@Override
	public void updateworkcationinfo(StudentWorkBo r) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tr = null;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			session.update(r);
			
			tr.commit();
			
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			
			
		}
		finally{
			log.info("END");
			session.close();
		
	}
	}




}
