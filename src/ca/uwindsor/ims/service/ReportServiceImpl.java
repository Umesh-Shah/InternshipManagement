package ca.uwindsor.ims.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uwindsor.ims.dao.impl.ReportDao;
import ca.uwindsor.ims.model.ReportlistBo;

@Service("reportService")
@Transactional
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private ReportDao dao;

	@Override
	public List<ReportlistBo> getstudentyear() throws Exception {
		
		return dao.getstudentyear();
	}

	@Override
	public List<ReportlistBo> getstudentcountry() throws Exception {
		
		return dao.getstudentcountry(); 
	}

	@Override
	public List<ReportlistBo> getuniversity() throws Exception {

		return dao.getuniversity();
	}

	@Override
	public List<ReportlistBo> getuniversity_location() throws Exception {

		return dao.getuniversity_location();
	}

}
