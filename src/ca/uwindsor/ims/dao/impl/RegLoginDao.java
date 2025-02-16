package ca.uwindsor.ims.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import ca.uwindsor.ims.model.VbctLoginBO;

@Repository
public interface RegLoginDao {
	
	boolean changePassword(String loginId, String newPass);

	boolean checkPassword(String loginId, String oldPass);

	Optional<VbctLoginBO> getEmployeeList(String username, String password);

	<T> T saveDataComon(T entity);

	boolean checkLogin(String username, String password);

}
