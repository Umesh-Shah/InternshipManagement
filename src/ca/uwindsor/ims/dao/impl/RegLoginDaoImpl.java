package ca.uwindsor.ims.dao.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.VbctLoginBO;
import ca.uwindsor.ims.exception.DatabaseException;

@Repository
@Transactional
public class RegLoginDaoImpl implements RegLoginDao {
    
    private static final Logger log = LogManager.getLogger(RegLoginDaoImpl.class);
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
        } catch (PersistenceException e) {
            log.error("Failed to change password for user: {}", loginId, e);
            throw new DatabaseException("Failed to update password", e);
        }
    }

    @Override
    public boolean checkPassword(String loginId, String oldPassword) {
        log.info("Verifying password for user: {}", loginId);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<VbctLoginBO> root = query.from(VbctLoginBO.class);
            
            query.select(cb.count(root))
                 .where(cb.and(
                     cb.equal(root.get("loginId"), loginId),
                     cb.equal(root.get("loginPassword"), oldPassword)
                 ));
            
            return entityManager.createQuery(query)
                              .getSingleResult() > 0;
        } catch (PersistenceException e) {
            log.error("Error checking password for user: {}", loginId, e);
            throw new DatabaseException("Failed to verify password", e);
        }
    }

    @Override
    public Optional<VbctLoginBO> getEmployeeList(String username, String password) {
        log.info("Retrieving employee information for username: {}", username);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<VbctLoginBO> query = cb.createQuery(VbctLoginBO.class);
            Root<VbctLoginBO> root = query.from(VbctLoginBO.class);
            
            query.select(root)
                 .where(cb.and(
                     cb.equal(root.get("loginName"), username),
                     cb.equal(root.get("loginPassword"), password)
                 ));
            
            return Optional.ofNullable(entityManager.createQuery(query)
                                                  .getSingleResult());
        } catch (NoResultException e) {
            log.info("No employee found for username: {}", username);
            return Optional.empty();
        } catch (PersistenceException e) {
            log.error("Error retrieving employee information for username: {}", username, e);
            throw new DatabaseException("Failed to retrieve employee information", e);
        }
    }

    @Override
    public <T> T saveDataComon(T entity) {
        log.info("Saving entity of type: {}", entity.getClass().getSimpleName());
        try {
            entityManager.persist(entity);
            return entity;
        } catch (PersistenceException e) {
            log.error("Error saving entity: {}", entity.getClass().getSimpleName(), e);
            throw new DatabaseException("Failed to save entity", e);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        log.info("Checking login for username: {}", username);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<LoginBo> root = query.from(LoginBo.class);
            
            query.select(cb.count(root))
                 .where(cb.and(
                     cb.equal(root.get("username"), username),
                     cb.equal(root.get("password"), password)
                 ));
            
            return entityManager.createQuery(query)
                              .getSingleResult() > 0;
        } catch (PersistenceException e) {
            log.error("Error checking login for username: {}", username, e);
            throw new DatabaseException("Failed to verify login", e);
        }
    }
}
