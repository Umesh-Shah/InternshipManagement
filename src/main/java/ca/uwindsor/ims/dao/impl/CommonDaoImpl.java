package ca.uwindsor.ims.dao.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import ca.uwindsor.ims.model.VbctLoginBO;
import ca.uwindsor.ims.exception.DatabaseException;
import ca.uwindsor.ims.dao.CommonDao;

@Component
@Repository
@Transactional
public class CommonDaoImpl implements CommonDao {

	private static final Logger log = LogManager.getLogger(CommonDaoImpl.class);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public boolean changePassword(String loginId, String newPass) {
		log.info("Starting password change for user: {}", loginId);
		try {
			var updateQuery = entityManager.createQuery("""
				UPDATE VbctLoginBO v 
				SET v.loginPassword = :newPass,
					v.lastPasswordUpdateDate = :updateTime
				WHERE v.loginId = :loginId
				""");
			
			updateQuery.setParameter("newPass", newPass)
					  .setParameter("updateTime", LocalDateTime.now())
					  .setParameter("loginId", loginId);
			
			int rowsAffected = updateQuery.executeUpdate();
			log.info("Password change affected {} rows", rowsAffected);
			return rowsAffected > 0;
		} catch (Exception e) {
			log.error("Failed to change password for user: {}", loginId, e);
			throw new DatabaseException("Failed to update password", e);
		}
	}
	
	@Override
	public <T> T saveDataComon(T entity) {
		log.info("Saving entity of type: {}", entity.getClass().getSimpleName());
		try {
			entityManager.persist(entity);
			return entity;
		} catch (Exception e) {
			log.error("Error saving entity: {}", entity.getClass().getSimpleName(), e);
			throw new DatabaseException("Failed to save entity", e);
		}
	}
	
	@Override
	public List<VbctLoginBO> getEmployeeList() {
		log.info("Retrieving all employees");
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<VbctLoginBO> query = cb.createQuery(VbctLoginBO.class);
			Root<VbctLoginBO> root = query.from(VbctLoginBO.class);
			
			query.select(root);
			
			TypedQuery<VbctLoginBO> typedQuery = entityManager.createQuery(query);
			return typedQuery.getResultList();
		} catch (Exception e) {
			log.error("Error retrieving employee list", e);
			throw new DatabaseException("Failed to retrieve employee list", e);
		}
	}
}

