package ca.uwindsor.ims.service;

import java.util.Optional;
import ca.uwindsor.ims.model.VbctLoginBO;

public interface RegLoginService {
	
	public boolean changePassword(String loginId, String newPass);

	public boolean checkPassword(String loginId, String oldPass);

	public Optional<VbctLoginBO> getEmployeeList(String username, String password);

	public <T> T saveDataComon(T entity);

	public boolean checkLogin(String username, String password);

}
