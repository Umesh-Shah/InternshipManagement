package ca.uwindsor.ims.service;

import java.util.List;

import ca.uwindsor.ims.model.CompanyBo;
import ca.uwindsor.ims.model.ReportBo;

public interface CompanyService {
	
	public boolean savecompany(CompanyBo companybo) throws Exception;
	
	public List<CompanyBo> getcompanylist() throws Exception;
	
	public List<CompanyBo> searchreportcompany(ReportBo r) throws Exception;

}
