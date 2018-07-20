package ca.uwindsor.ims.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.CompanyDao;
import ca.uwindsor.ims.model.CompanyBo;
import ca.uwindsor.ims.model.ReportBo;

@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyDao dao;

	@Override
	public boolean savecompany(CompanyBo companybo) throws Exception {
		return dao.savecompany(companybo);
	}

	@Override
	public List<CompanyBo> getcompanylist() throws Exception {
		return dao.getcompanylist();
	}

	@Override
	public List<CompanyBo> searchreportcompany(ReportBo r) throws Exception {
		return dao.searchreportcompany(r);
	}

}
