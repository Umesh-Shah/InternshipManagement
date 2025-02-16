package ca.uwindsor.ims.dao;

import java.util.List;
import ca.uwindsor.ims.model.VbctLoginBO;

public interface CommonDao {
    boolean changePassword(String loginId, String newPass);
    <T> T saveDataComon(T entity);
    List<VbctLoginBO> getEmployeeList();
} 