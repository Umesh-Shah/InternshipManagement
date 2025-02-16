package ca.uwindsor.ims.service;

import ca.uwindsor.ims.model.LoginBo;

public interface RegLoginService {
	
	/**
	 * Validates user login credentials
	 * @param username the username
	 * @param password the password
	 * @return LoginBo if credentials are valid, null otherwise
	 */
	LoginBo validateLogin(String username, String password);
	
	/**
	 * Registers a new user
	 * @param loginBo the login details
	 * @return true if registration successful, false otherwise
	 */
	boolean registerUser(LoginBo loginBo);
	
	/**
	 * Updates user password
	 * @param username the username
	 * @param newPassword the new password
	 * @return true if password updated successfully, false otherwise
	 */
	boolean updatePassword(String username, String newPassword);

}
