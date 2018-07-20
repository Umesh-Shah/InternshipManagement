package ca.uwindsor.ims.dao.impl;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.VbctLoginBO;

@Component
public interface RegLoginDao {
	
	public boolean changePassword(String login_id, String newPass) throws Exception;

	public boolean checkPassword(String login_id, String oldPass) throws Exception;

	public VbctLoginBO getEmployeeList(String username, String password) throws Exception;

	public <T> T saveDataComon(T t) throws Exception;

	public boolean checkLogin(String username, String password) throws Exception;

}
