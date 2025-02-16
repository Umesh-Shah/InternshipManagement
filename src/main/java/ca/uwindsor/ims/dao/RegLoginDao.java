package ca.uwindsor.ims.dao;

import java.util.Optional;
import ca.uwindsor.ims.model.VbctLoginBO;

public interface RegLoginDao {
    boolean changePassword(String loginId, String newPass);
    boolean checkPassword(String loginId, String oldPass);
    Optional<VbctLoginBO> getEmployeeList(String username, String password);
    <T> T saveDataComon(T entity);
    boolean checkLogin(String username, String password);
} 