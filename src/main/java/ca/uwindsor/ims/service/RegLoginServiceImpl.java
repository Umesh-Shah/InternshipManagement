package ca.uwindsor.ims.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.uwindsor.ims.dao.RegLoginDao;
import ca.uwindsor.ims.model.VbctLoginBO;

@Service
@Transactional
public class RegLoginServiceImpl implements RegLoginService {
	
	@Autowired
	private RegLoginDao dao;

	@Override
	public boolean changePassword(String loginId, String newPass) {
		return dao.changePassword(loginId, newPass);
	}

	@Override
	public boolean checkPassword(String loginId, String oldPass) {
		return dao.checkPassword(loginId, oldPass);
	}

	@Override
	public Optional<VbctLoginBO> getEmployeeList(String username, String password) {
		return dao.getEmployeeList(username, password);
	}

	@Override
	public <T> T saveDataComon(T entity) {
		return dao.saveDataComon(entity);
	}

	@Override
	public boolean checkLogin(String username, String password) {
		return dao.checkLogin(username, password);
	}

}
