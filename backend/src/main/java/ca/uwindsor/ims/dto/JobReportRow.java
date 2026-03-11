package ca.uwindsor.ims.dto;

public class JobReportRow {
    private Integer jobId;
    private String jobPosition;
    private String companyName;
    private Long applicantCount;

    public JobReportRow() {}

    public JobReportRow(Integer jobId, String jobPosition, String companyName, Long applicantCount) {
        this.jobId = jobId;
        this.jobPosition = jobPosition;
        this.companyName = companyName;
        this.applicantCount = applicantCount;
    }

    public Integer getJobId() { return jobId; }
    public void setJobId(Integer v) { jobId = v; }
    public String getJobPosition() { return jobPosition; }
    public void setJobPosition(String v) { jobPosition = v; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String v) { companyName = v; }
    public Long getApplicantCount() { return applicantCount; }
    public void setApplicantCount(Long v) { applicantCount = v; }
}
