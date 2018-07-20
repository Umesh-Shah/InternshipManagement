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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.CompanyBo;
import ca.uwindsor.ims.model.ReportBo;

@Component
@Repository
public class CompanyDaoImpl implements CompanyDao{
	
Logger log = Logger.getLogger(CompanyDaoImpl.class);
	
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
	public boolean savecompany(CompanyBo companybo) throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		Transaction tr = null;
		boolean flag = false;
		try{
			session = getSessionFactory().openSession();
			tr = session.beginTransaction();
			System.out.println(companybo.getCity());
			Serializable serializable = session.save(companybo);
			
			System.out.println(serializable);
			if(null != serializable){
				flag = true;
			}else{
				flag = false;
			}
			tr.commit();
		}catch(Exception e){
			log.error("ERROR OCCURED");
			log.error(e.getMessage(), e);
			e.printStackTrace();
			tr.rollback();
		}
		finally{
			log.info("END");
			session.close();
		}
		return flag;
		
	}
	
	@Override
	public List<CompanyBo> getcompanylist() throws Exception {
		// TODO Auto-generated method stub
		log.info("START");
		Session session = null;
		List<CompanyBo> lstboo = null;
		CompanyBo boo = null;
		try{
			session = getSessionFactory().openSession();
			Query query= session.createQuery("from CompanyBo");
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
	public List<CompanyBo> searchreportcompany(ReportBo r) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		List<CompanyBo> info=null;
		try{
			session = getSessionFactory().openSession();
			Query query = null;
			String sql = "from  CompanyBo";
			if(null != r.getCity() && !"".equalsIgnoreCase(r.getCity())){
				sql = sql + " where ";
			}
			if(null != r.getCity() && !"".equalsIgnoreCase(r.getCity())){
				sql = sql + "upper(city) = '"+r.getCity().toUpperCase() +"' and ";
			}
			if(null != r.getCity() && !"".equalsIgnoreCase(r.getCity())){
				sql = sql + " 1=1 ";
			}
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



}
