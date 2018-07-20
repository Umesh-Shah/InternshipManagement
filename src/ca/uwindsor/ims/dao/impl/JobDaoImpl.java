package ca.uwindsor.ims.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.JobBo;
import ca.uwindsor.ims.model.Student_Job_mapping;

@Component
@Repository
public class JobDaoImpl implements JobDao{
	
Logger log = Logger.getLogger(JobDaoImpl.class);
	
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
	public List<JobBo> getjoblist() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<JobBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("from JobBo");
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
	public List<JobBo> getstudentjoblist(int student_id)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<Student_Job_mapping> list = null;
		List<JobBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From Student_Job_mapping where student_id='"+student_id+"'");
			list = query.list();
			int job_id= 0;
			if(list.size() > 0)
			{
			String s = "";
			for(int z=0;z<list.size();z++)
			{
				// list=(List<Student_Job_mapping>) list.get(z);
				s = s + "'"+list.get(z).getJob_id()+"', ";
//				student_id=list.get(z).getStudent_id();
			}
			s = s.substring(0, s.length()-2);
			System.out.println(s);
			Query query1=session.createQuery("From JobBo where job_id IN ("+s+")");
			
			   list1=query1.list();
			}
			
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
	public List<JobBo> getjoblistfromcompany(String company_id) throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<JobBo> list1=null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From JobBo where company_id=?");
			query.setParameter(0, Integer.parseInt(company_id));
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
		return list1;	}
	
	
	@Override
	public void updatestudentjobflag(String job_id, String student_id)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("update Student_Job_mapping set flag='Y' where student_id=? and job_id=?");
	       query.setParameter(0, Integer.parseInt(student_id));
	       query.setParameter(1, Integer.parseInt(job_id));

			query.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			log.info("END");
			session.close();
		}
	}


}
