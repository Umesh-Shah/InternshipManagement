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

import ca.uwindsor.ims.model.InternshipTypeBo;

@Component
@Repository
public class InternshipDaoImpl implements InternshipDao {
	
	Logger log = Logger.getLogger(InternshipDaoImpl.class);
	
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
	public List<InternshipTypeBo> getinternshiplist() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<InternshipTypeBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("from InternshipTypeBo");
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
	public void updatestudent_status(int i,String internship_type) throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("update StudentInfoBo set student_status='Hired',internship_status=? where student_id="+i);
	       query.setParameter(0, internship_type);
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
