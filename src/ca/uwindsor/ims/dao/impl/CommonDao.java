package ca.uwindsor.ims.dao.impl;


import org.springframework.stereotype.Component;


@Component
public interface CommonDao {
	
	public <T> T saveDataComon(T t) throws Exception;
		
}
