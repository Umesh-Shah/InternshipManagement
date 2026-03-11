package ca.uwindsor.ims.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_ID")
    private Integer loginId;

    @Column(name = "STUDENT_ID")
    private Integer studentId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PWD")
    private String pwd;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "FLAG")
    private String flag;

    public Integer getLoginId() { return loginId; }
    public void setLoginId(Integer v) { loginId = v; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer v) { studentId = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { username = v; }
    public String getPwd() { return pwd; }
    public void setPwd(String v) { pwd = v; }
    public String getUserType() { return userType; }
    public void setUserType(String v) { userType = v; }
    public String getFlag() { return flag; }
    public void setFlag(String v) { flag = v; }
}
