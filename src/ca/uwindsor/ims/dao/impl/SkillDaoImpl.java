package ca.uwindsor.ims.dao.impl;

import java.util.List;

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

import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;
import ca.uwindsor.ims.model.StudentSkillBo;
import ca.uwindsor.ims.exception.DatabaseException;

@Repository
@Transactional
public class SkillDaoImpl implements SkillDao {
	
	private static final Logger log = LogManager.getLogger(SkillDaoImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<SkillBo> getskilllist() throws Exception {
		log.info("START");
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<SkillBo> query = cb.createQuery(SkillBo.class);
			Root<SkillBo> root = query.from(SkillBo.class);
			query.select(root);
			
			TypedQuery<SkillBo> typedQuery = entityManager.createQuery(query);
			return typedQuery.getResultList();
		} catch (Exception e) {
			log.error("Error retrieving skill list", e);
			throw new DatabaseException("Error retrieving skill list", e);
		} finally {
			log.info("END");
		}
	}
	
	@Override
	public List<StudentJobSkillbo> getstudentfromjjob(String jobId) throws Exception {
		log.info("START");
		try {
			String sql = """
				SELECT new ca.uwindsor.ims.model.StudentJobSkillbo(
					j.studentId as studentId,
					string_agg(sk.skillName, ', ') as skillName
				)
				FROM StudentJobMaster j
				JOIN StudentSkillBo s ON j.studentId = s.studentId
				JOIN SkillBo sk ON s.skillName = sk.skillId
				WHERE j.jobId = :jobId
				GROUP BY j.studentId
				""";
			
			return entityManager.createQuery(sql, StudentJobSkillbo.class)
				.setParameter("jobId", jobId)
				.getResultList();
		} catch (Exception e) {
			log.error("Error retrieving student job skills", e);
			throw new DatabaseException("Error retrieving student job skills", e);
		} finally {
			log.info("END");
		}
	}
	
	@Override
	public void savestudent_skill(int studentId, String[] skills) {
		log.info("START");
		try {
			for (String skill : skills) {
				StudentSkillBo studentSkill = StudentSkillBo.create(studentId, 0, skill);
				entityManager.persist(studentSkill);
			}
		} catch (Exception e) {
			log.error("Error saving student skills", e);
			throw new DatabaseException("Error saving student skills", e);
		} finally {
			log.info("END");
		}
	}
	
	public SkillBo saveSkill(SkillBo skill) {
		log.info("START");
		try {
			entityManager.persist(skill);
			return skill;
		} catch (Exception e) {
			log.error("Error saving skill", e);
			throw new DatabaseException("Error saving skill", e);
		} finally {
			log.info("END");
		}
	}
}
