package ca.uwindsor.ims.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.CommonDao;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService{
	
	//
	@Autowired
	private CommonDao dao;

	@Override
	public <T> T saveDataComon(T t) throws Exception {
		return dao.saveDataComon(t);
	}
	
	

}
