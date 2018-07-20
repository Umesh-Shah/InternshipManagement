package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.CompanyBo;
import ca.uwindsor.ims.model.ReportBo;

@Component
public interface CompanyDao {
	
	public boolean savecompany(CompanyBo companybo) throws Exception;
	
	public List<CompanyBo> getcompanylist()throws Exception;
	
	public List<CompanyBo> searchreportcompany(ReportBo r)throws Exception;

}
