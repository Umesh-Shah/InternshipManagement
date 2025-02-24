package ca.uwindsor.ims.dao.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ca.uwindsor.ims.model.InternshipTypeBo;
import ca.uwindsor.ims.model.StudentInfoBo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Component
@Repository
public class InternshipDaoImpl implements InternshipDao {
	
	private static final Logger log = LogManager.getLogger(InternshipDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<InternshipTypeBo> getinternshiplist() throws Exception {
		log.info("START");
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InternshipTypeBo> criteria = builder.createQuery(InternshipTypeBo.class);
			Root<InternshipTypeBo> root = criteria.from(InternshipTypeBo.class);
			criteria.select(root);
			return session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			log.error("Error getting internship list", e);
			throw e;
		} finally {
			log.info("END");
		}
	}

	@Override
	public void updatestudent_status(int studentId, String internshipType) throws Exception {
		log.info("START");
		try (Session session = getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "update StudentInfoBo s set s.student_status = 'Hired', s.internship_status = :type where s.student_id = :id";
			session.createQuery(hql)
				.setParameter("type", internshipType)
				.setParameter("id", studentId)
				.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			log.error("Error updating student status", e);
			throw e;
		} finally {
			log.info("END");
		}
	}
}
