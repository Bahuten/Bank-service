package project.service.interfaces;

import project.repository.entity.Account;

public interface LoginService {
    Account openSession(String customerNumber, String passNumber);
    Account closeSession(String id);
    boolean verifyLogin(String id, String verificationCode);
    boolean isLoggedOn(String customerNumber, String passNumber);
}
