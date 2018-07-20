package ca.uwindsor.ims.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.RegLoginDao;
import ca.uwindsor.ims.model.VbctLoginBO;

@Service("regLoginService")
@Transactional
public class RegLoginServiceImpl implements RegLoginService {
	
	@Autowired
	private RegLoginDao dao;

	@Override
	public boolean changePassword(String login_id, String newPass) throws Exception {
		return dao.changePassword(login_id, newPass);
	}

	@Override
	public boolean checkPassword(String login_id, String oldPass) throws Exception {
		return dao.checkPassword(login_id, oldPass);
	}

	@Override
	public VbctLoginBO getEmployeeList(String username, String password) throws Exception {
		return dao.getEmployeeList(username, password);
	}

	@Override
	public <T> T saveDataComon(T t) throws Exception {
		return dao.saveDataComon(t);
	}

	@Override
	public boolean checkLogin(String username, String password) throws Exception {
		return dao.checkLogin(username, password);
	}

}
