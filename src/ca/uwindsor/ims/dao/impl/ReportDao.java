package ca.uwindsor.ims.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import ca.uwindsor.ims.model.ReportlistBo;

@Component
public interface ReportDao {
	
	public List<ReportlistBo> getstudentyear()throws Exception;
	public List<ReportlistBo> getstudentcountry()throws Exception;
	public List<ReportlistBo> getuniversity()throws Exception;
	public List<ReportlistBo> getuniversity_location()throws Exception;

}
