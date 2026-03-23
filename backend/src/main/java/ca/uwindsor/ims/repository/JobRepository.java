package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByCompanyId(Integer companyId);
}
