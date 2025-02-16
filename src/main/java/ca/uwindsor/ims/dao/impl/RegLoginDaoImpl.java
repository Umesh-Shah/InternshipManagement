package ca.uwindsor.ims.dao.impl;

import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.exception.DatabaseException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RegLoginDaoImpl implements RegLoginDao {
    
    private static final Logger log = LogManager.getLogger(RegLoginDaoImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional(readOnly = true)
    public LoginBo validateLogin(String username, String password) {
        log.info("Validating login for user: {}", username);
        try {
            TypedQuery<LoginBo> query = entityManager.createQuery(
                "SELECT l FROM LoginBo l WHERE l.username = :username AND l.password = :password",
                LoginBo.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (NoResultException e) {
            log.warn("No user found with username: {}", username);
            return null;
        } catch (Exception e) {
            log.error("Error validating login: {}", e.getMessage(), e);
            throw new DatabaseException("Error validating login", e);
        }
    }
    
    @Override
    @Transactional
    public boolean registerUser(LoginBo loginBo) {
        log.info("Registering new user: {}", loginBo.getUsername());
        try {
            entityManager.persist(loginBo);
            return true;
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            throw new DatabaseException("Error registering user", e);
        }
    }
    
    @Override
    @Transactional
    public boolean updatePassword(String username, String newPassword) {
        log.info("Updating password for user: {}", username);
        try {
            LoginBo user = entityManager.createQuery(
                "SELECT l FROM LoginBo l WHERE l.username = :username", LoginBo.class)
                .setParameter("username", username)
                .getSingleResult();
            
            user.setPassword(newPassword);
            entityManager.merge(user);
            entityManager.flush();
            return true;
        } catch (NoResultException e) {
            log.warn("No user found with username: {}", username);
            return false;
        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage(), e);
            throw new DatabaseException("Error updating password", e);
        }
    }
}
