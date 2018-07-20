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

import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;
import ca.uwindsor.ims.model.StudentSkillBo;

@Component
@Repository
public class SkillDaoImpl implements SkillDao {
	
	Logger log = Logger.getLogger(SkillDaoImpl.class);
	
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
	public List<SkillBo> getskilllist() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<SkillBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("from SkillBo");
			lstboo = query.list();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
		return lstboo;
	}
	
	@Override
	public List<StudentJobSkillbo> getstudentfromjjob(String job_id) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<StudentJobSkillbo> info=null;
		List<SkillBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query= null;
			String sql =" select  j.student_id as student_id,string_agg(sk.skill_name,', ')  as skill_name from  student_job_master j,student_skill s,skill sk where j.student_id=s.student_id and s.skill_name=sk.skill_id and j.job_id='"+job_id+"' group by j.student_id";
			query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(StudentJobSkillbo.class));
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
	public void savestudent_skill(int student_id, String[] shah) {

		StudentSkillBo s = new StudentSkillBo();
		String student_id1 = student_id + "";
		Serializable serializable = null;
		for (int i = 0; i < shah.length; i++) {
			Session session = null;
			Transaction tr = null;
			boolean flag = false;
			try {
				session = getSessionFactory().openSession();
				tr = session.beginTransaction();

				s.setStudent_id(student_id);
				s.setSkill_name(shah[i]);
				serializable = session.save(s);
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

	

}
