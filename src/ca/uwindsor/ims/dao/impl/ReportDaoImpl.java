package ca.uwindsor.ims.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.ReportlistBo;

@Component
@Repository
public class ReportDaoImpl implements ReportDao {
	
Logger log = Logger.getLogger(ReportDaoImpl.class);
	
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
	public List<ReportlistBo> getstudentyear() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<ReportlistBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery(" select distinct year as year from StudentInfoBo").setResultTransformer(Transformers.aliasToBean(ReportlistBo.class));
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
	public List<ReportlistBo> getstudentcountry() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<ReportlistBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery(" select distinct country as country from StudentInfoBo").setResultTransformer(Transformers.aliasToBean(ReportlistBo.class));
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
	public List<ReportlistBo> getuniversity() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<ReportlistBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery(" select distinct university as university from StudentEducationBo").setResultTransformer(Transformers.aliasToBean(ReportlistBo.class));
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
	public List<ReportlistBo> getuniversity_location() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<ReportlistBo> lstboo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery(" select distinct university_location as university_location from StudentEducationBo").setResultTransformer(Transformers.aliasToBean(ReportlistBo.class));
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

}
