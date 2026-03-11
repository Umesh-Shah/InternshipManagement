package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.InternshipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipTypeRepository extends JpaRepository<InternshipType, Integer> {
}
