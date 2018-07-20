package ca.uwindsor.ims.service;

import ca.uwindsor.ims.model.VbctLoginBO;

public interface RegLoginService {
	
	public boolean changePassword(String login_id, String newPass) throws Exception;

	public boolean checkPassword(String login_id, String oldPass) throws Exception;

	public VbctLoginBO getEmployeeList(String username, String password) throws Exception;

	public <T> T saveDataComon(T t) throws Exception;

	public boolean checkLogin(String username, String password) throws Exception;

}
