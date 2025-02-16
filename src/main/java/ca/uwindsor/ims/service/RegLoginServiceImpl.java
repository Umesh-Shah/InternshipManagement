package ca.uwindsor.ims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.model.LoginBo;

@Service
@Transactional
public class RegLoginServiceImpl implements RegLoginService {
	
	@Autowired
	private RegLoginDao dao;

	@Override
	@Transactional(readOnly = true)
	public LoginBo validateLogin(String username, String password) {
		return dao.validateLogin(username, password);
	}

	@Override
	public boolean registerUser(LoginBo loginBo) {
		return dao.registerUser(loginBo);
	}

	@Override
	public boolean updatePassword(String username, String newPassword) {
		return dao.updatePassword(username, newPassword);
	}

}
