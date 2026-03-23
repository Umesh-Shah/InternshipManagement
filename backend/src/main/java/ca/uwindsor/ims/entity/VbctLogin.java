package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

// NOTE: The old hbm.xml mapped login_id as java.lang.String with an 'identity' generator.
// Identity generation on a String PK is non-standard. Mapped here without @GeneratedValue;
// verify the actual MySQL column type before enabling generation strategy.
@Entity
@Table(name = "vbct_login")
public class VbctLogin {

    @Id
    @Column(name = "LOGIN_ID")
    private String loginId;

    @Column(name = "DSGN_ID")
    private String dsgnId;

    @Column(name = "LOGIN_NAME")
    private String loginName;

    @Column(name = "LOGIN_PASSWORD")
    private String loginPassword;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "PHONE_NO")
    private String phoneNo;

    @Column(name = "SERVICE_FLG")
    private String serviceFlg;

    @Column(name = "LANG_ID")
    private String langId;

    @Column(name = "CRT_DT")
    private String crtDt;

    @Column(name = "CRT_USR")
    private String crtUsr;

    @Column(name = "LST_UPD_USR")
    private String lstUpdUsr;

    @Column(name = "LST_UPD_DT")
    private String lstUpdDt;

    @Column(name = "SERVICE_EXPIRY_DT")
    private String serviceExpiryDt;

    @Column(name = "FORGOT_PWD")
    private String forgotPwd;

    @Column(name = "FRST_LOGIN_FLG")
    private String frstLoginFlg;

    @Column(name = "PSWD_EXPIRE_DT")
    private String pswdExpireDt;

    @Column(name = "LST_PSWD_UPD_DT")
    private String lstPswdUpdDt;

    @Column(name = "LOGIN_STARTTIME")
    private String loginStarttime;

    @Column(name = "LOGIN_ENDTIME")
    private String loginEndtime;

    @Column(name = "IPADDR")
    private String ipaddr;

    public String getLoginId() { return loginId; }
    public void setLoginId(String v) { loginId = v; }
    public String getDsgnId() { return dsgnId; }
    public void setDsgnId(String v) { dsgnId = v; }
    public String getLoginName() { return loginName; }
    public void setLoginName(String v) { loginName = v; }
    public String getLoginPassword() { return loginPassword; }
    public void setLoginPassword(String v) { loginPassword = v; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { firstName = v; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String v) { middleName = v; }
    public String getLastName() { return lastName; }
    public void setLastName(String v) { lastName = v; }
    public String getEmailId() { return emailId; }
    public void setEmailId(String v) { emailId = v; }
    public String getDob() { return dob; }
    public void setDob(String v) { dob = v; }
    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String v) { phoneNo = v; }
    public String getServiceFlg() { return serviceFlg; }
    public void setServiceFlg(String v) { serviceFlg = v; }
    public String getLangId() { return langId; }
    public void setLangId(String v) { langId = v; }
    public String getCrtDt() { return crtDt; }
    public void setCrtDt(String v) { crtDt = v; }
    public String getCrtUsr() { return crtUsr; }
    public void setCrtUsr(String v) { crtUsr = v; }
    public String getLstUpdUsr() { return lstUpdUsr; }
    public void setLstUpdUsr(String v) { lstUpdUsr = v; }
    public String getLstUpdDt() { return lstUpdDt; }
    public void setLstUpdDt(String v) { lstUpdDt = v; }
    public String getServiceExpiryDt() { return serviceExpiryDt; }
    public void setServiceExpiryDt(String v) { serviceExpiryDt = v; }
    public String getForgotPwd() { return forgotPwd; }
    public void setForgotPwd(String v) { forgotPwd = v; }
    public String getFrstLoginFlg() { return frstLoginFlg; }
    public void setFrstLoginFlg(String v) { frstLoginFlg = v; }
    public String getPswdExpireDt() { return pswdExpireDt; }
    public void setPswdExpireDt(String v) { pswdExpireDt = v; }
    public String getLstPswdUpdDt() { return lstPswdUpdDt; }
    public void setLstPswdUpdDt(String v) { lstPswdUpdDt = v; }
    public String getLoginStarttime() { return loginStarttime; }
    public void setLoginStarttime(String v) { loginStarttime = v; }
    public String getLoginEndtime() { return loginEndtime; }
    public void setLoginEndtime(String v) { loginEndtime = v; }
    public String getIpaddr() { return ipaddr; }
    public void setIpaddr(String v) { ipaddr = v; }
}
