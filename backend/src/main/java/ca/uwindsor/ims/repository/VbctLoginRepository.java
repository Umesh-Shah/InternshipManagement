package ca.uwindsor.ims.repository;

import ca.uwindsor.ims.entity.VbctLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VbctLoginRepository extends JpaRepository<VbctLogin, String> {
    Optional<VbctLogin> findByLoginName(String loginName);
}
