package ca.uwindsor.ims.dao.impl;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.VbctLoginBO;
import org.hibernate.query.Query;

@Component
@Repository
public class CommonDaoImpl implements CommonDao {

	private static final Logger log = LogManager.getLogger(CommonDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	DateFormat dateandtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public boolean changePassword(String login_id, String newPass)
			throws Exception {
		log.info("START");
		Session session = null;
		Transaction tr = null;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			Query query= session.createQuery("UPDATE VbctLoginBO SET login_password = '"+newPass+"' where login_id='"+login_id+"'");
			int r = query.executeUpdate();
			log.info("ROWS AFFECTED -- > "+r);
			tr.commit();
			return true;
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			tr.rollback();
			return false;
		}
		finally{
			log.info("END");
			session.close();
		}
	}

	public boolean checkPassword(String login_id, String oldPassword)
			throws Exception {
		log.info("START");
		List<VbctLoginBO> UserVerifiedList = null;
		Session s = getSessionFactory().openSession();
		StringBuffer strQuery = new StringBuffer(" from VbctLoginBO  where "
				+ "login_id = ? and login_password = ? ");

		Query query = s.createQuery(strQuery.toString());
		query.setParameter(0, login_id);
		query.setParameter(1, oldPassword);
		UserVerifiedList = query.list();
		s.close();
		if (UserVerifiedList.size() != 0) {
			log.info("END");
			return true;
		} else {
			log.info("END");
			return false;
		}
	}
	public VbctLoginBO getEmployeeList(String username, String password)
			throws Exception {
		log.info("START");
		Session session = null;
		List<VbctLoginBO> list = null;
		VbctLoginBO loginbo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("From VbctLoginBO where login_name = '"+username+"' and login_password = '"+password+"'");
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
	public <T> T saveDataComon(T t) throws Exception {
		// TODO Auto-generated method stub
		log.info("--START-----Inside common save method");
		Session session = null;
		Transaction tr = null;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			session.save(t);
			tr.commit();
		}catch(Exception e){
			tr.rollback();
			e.printStackTrace();
			log.error("ERROR OCCURED");
			log.error(e.getMessage(),e);
		}
		finally{
			log.info("--END-----Inside common save method");
			session.close();
		}
		return t;
	}
	
	
	

	
}

