package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.enums.LoginStatus;
import project.model.requests.LoginRequest;
import project.repository.entity.Account;
import project.repository.entity.Transaction;

import project.service.impl.LoginServiceImpl;

import project.service.interfaces.AccountService;
import project.utility.CodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static project.utility.CommonUtils.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(LoginRestController.class)
public class LoginRestControllerTest {

    @MockBean
    private LoginServiceImpl loginService;
    @MockBean
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    Account currentAccount;
    String sourceAccountId;
    String name;
    String surname;
    String bankName;
    String phoneNumber;
    String emailAddress;
    String customerNumber;
    String passNumber;
    String verificationCode;
    String currency;
    String accountNumber;
    String sortCode;
    double balance;
    List<Transaction> transactions;
    LoginStatus loginStatusOn;
    LoginStatus loginStatusOff;

    @BeforeEach
    void setupData() {
        sourceAccountId = UUID.randomUUID().toString();
        name = "Martin";
        surname = "King";
        bankName = "NatWest";
        phoneNumber = "+447587155942";
        emailAddress = "william.cuthbert@fisglobal.com";
        customerNumber = CodeGenerator.generateCustomerNumber();
        passNumber = CodeGenerator.generatePassNumber();
        verificationCode = CodeGenerator.generateVerificationCode();
        currency = "GBP";
        accountNumber = CodeGenerator.generateAccountNumber();
        sortCode = CodeGenerator.generateSortCode();
        balance = 0;
        transactions = new ArrayList<>();
        loginStatusOn = LoginStatus.LOGIN_ACCESS_GRANTED;
        loginStatusOff = LoginStatus.LOGIN_ACCESS_FAILED;
        currentAccount = new Account(sourceAccountId, name, surname, bankName, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, new ArrayList<>(), loginStatusOff);
    }

    @Test
    public void openSessionTest() throws Exception {
        when(loginService.openSession(customerNumber, passNumber)).thenReturn(currentAccount);

        LoginRequest loginRequest = new LoginRequest(customerNumber, passNumber);
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(loginService).openSession(customerNumber, passNumber);
    }

    @Test
    public void closeSessionTest() throws Exception {
        when(loginService.closeSession(sourceAccountId)).thenReturn(currentAccount);

        mockMvc.perform(MockMvcRequestBuilders.post(LOGOUT_ENDPOINT, sourceAccountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(loginService).closeSession(sourceAccountId);
    }

    @Test
    public void verifyAccountTest() throws Exception {
        when(loginService.verifyLogin(sourceAccountId, verificationCode)).thenReturn(true);
        when(accountService.getAccountById(sourceAccountId)).thenReturn(currentAccount);
        mockMvc.perform(MockMvcRequestBuilders.post(VERIFY_LOGIN_ENDPOINT, sourceAccountId, verificationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(sourceAccountId)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.bankName", is(bankName)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.passNumber", is(passNumber)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNumber)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andExpect(jsonPath("$.loginStatus", is(loginStatusOn.getStatus())))
                .andReturn();

        verify(loginService).verifyLogin(sourceAccountId, verificationCode);
    }
}
