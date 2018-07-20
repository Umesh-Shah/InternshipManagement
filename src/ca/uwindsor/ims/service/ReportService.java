package ca.uwindsor.ims.service;

import java.util.List;

import ca.uwindsor.ims.model.ReportlistBo;

public interface ReportService {

	public List<ReportlistBo> getstudentyear() throws Exception;
	public List<ReportlistBo> getstudentcountry() throws Exception;
	public List<ReportlistBo> getuniversity() throws Exception;
	public List<ReportlistBo> getuniversity_location() throws Exception;
}
