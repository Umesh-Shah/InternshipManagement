package ca.uwindsor.ims.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.model.VbctLoginBO;
import ca.uwindsor.ims.exception.DatabaseException;
import java.util.Optional;

@Repository
@Transactional
public class RegLoginDaoImpl implements RegLoginDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean changePassword(String loginId, String newPass) {
        try {
            String jpql = "UPDATE VbctLoginBO SET loginPassword = :newPass WHERE loginName = :loginId";
            int result = entityManager.createQuery(jpql)
                .setParameter("newPass", newPass)
                .setParameter("loginId", loginId)
                .executeUpdate();
            entityManager.flush();
            entityManager.clear();
            return result > 0;
        } catch (Exception e) {
            throw new DatabaseException("Error changing password", e);
        }
    }

    @Override
    public boolean checkPassword(String loginId, String oldPassword) {
        try {
            String jpql = "FROM VbctLoginBO WHERE loginName = :loginId AND loginPassword = :password";
            VbctLoginBO result = entityManager.createQuery(jpql, VbctLoginBO.class)
                .setParameter("loginId", loginId)
                .setParameter("password", oldPassword)
                .getSingleResult();
            return result != null;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            throw new DatabaseException("Error checking password", e);
        }
    }

    @Override
    public Optional<VbctLoginBO> getEmployeeList(String username, String password) {
        try {
            String jpql = "FROM VbctLoginBO WHERE loginName = :username AND loginPassword = :password";
            VbctLoginBO result = entityManager.createQuery(jpql, VbctLoginBO.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving employee", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T saveDataComon(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new DatabaseException("Failed to save entity", e);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        try {
            String jpql = "FROM VbctLoginBO WHERE loginName = :username AND loginPassword = :password";
            VbctLoginBO result = entityManager.createQuery(jpql, VbctLoginBO.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
            return result != null;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            throw new DatabaseException("Error checking login", e);
        }
    }
}
