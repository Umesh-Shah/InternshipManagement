package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
