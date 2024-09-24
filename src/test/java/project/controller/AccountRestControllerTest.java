package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.enums.LoginStatus;
import project.model.requests.account.UpdateAccountRequest;
import project.repository.entity.Account;
import project.repository.entity.Transaction;
import project.model.requests.account.CreateAccountRequest;
import project.service.impl.AccountServiceImpl;
import project.utility.CodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static project.utility.CommonUtils.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest {
    @MockBean
    private AccountServiceImpl accountServiceImpl;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void postAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        when(accountServiceImpl.createAccount(anyString(), eq(bank), eq(name), eq(surname), eq(phoneNumber), eq(emailAddress), anyString(), anyString(), anyString(), eq(currency), anyString(), anyString())).thenReturn(account);
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(bank, name, surname, phoneNumber, emailAddress, currency);
        mockMvc.perform(MockMvcRequestBuilders.post(ACCOUNT_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(createAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.bankName", is(bank)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.passNumber", is(passNumber)))
                .andExpect(jsonPath("$.verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn().getResponse().getContentAsString();
        verify(accountServiceImpl).createAccount(anyString(), eq(bank), eq(name), eq(surname), eq(phoneNumber), eq(emailAddress), anyString(), anyString(), anyString(), eq(currency), anyString(), anyString());
    }

    @Test
    void getAccountBySortCodeAndAccountNumber() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNumber = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNumber, sortCode, balance, currency, transactions, loginStatus);
        when(accountServiceImpl.getAccountBySortCodeAndAccountNumber(sortCode, accountNumber)).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_ENDPOINT)
                        .param(SORT_CODE, sortCode)
                        .param(ACCOUNT_NUMBER, accountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.bankName", is(bank)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.passNumber", is(passNumber)))
                .andExpect(jsonPath("$.verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNumber)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn();
        verify(accountServiceImpl).getAccountBySortCodeAndAccountNumber(sortCode, accountNumber);
    }

    @Test
    void getAccountById() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        when(accountServiceImpl.getAccountById(id)).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_ID_ENDPOINT, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.bankName", is(bank)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.passNumber", is(passNumber)))
                .andExpect(jsonPath("$.verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn();
        verify(accountServiceImpl).getAccountById(id);
    }

    @Test
    void getAccounts() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus));
        accounts.add(new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus));
        when(accountServiceImpl.getAccounts()).thenReturn(accounts);
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNTS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accounts[0].id", is(id)))
                .andExpect(jsonPath("$.accounts[0].bankName", is(bank)))
                .andExpect(jsonPath("$.accounts[0].firstName", is(name)))
                .andExpect(jsonPath("$.accounts[0].surname", is(surname)))
                .andExpect(jsonPath("$.accounts[0].phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.accounts[0].emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.accounts[0].customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.accounts[0].passNumber", is(passNumber)))
                .andExpect(jsonPath("$.accounts[0].verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.accounts[0].sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accounts[0].accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.accounts[0].balance", is(balance)))
                .andExpect(jsonPath("$.accounts[0].currency", is(currency)))
                .andExpect(jsonPath("$.accounts[0].transactions", is(transactions)))
                .andExpect(jsonPath("$.accounts[1].id", is(id)))
                .andExpect(jsonPath("$.accounts[1].bankName", is(bank)))
                .andExpect(jsonPath("$.accounts[1].firstName", is(name)))
                .andExpect(jsonPath("$.accounts[1].surname", is(surname)))
                .andExpect(jsonPath("$.accounts[1].phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.accounts[1].emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.accounts[1].customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.accounts[1].passNumber", is(passNumber)))
                .andExpect(jsonPath("$.accounts[1].verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.accounts[1].sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accounts[1].accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.accounts[1].balance", is(balance)))
                .andExpect(jsonPath("$.accounts[1].currency", is(currency)))
                .andExpect(jsonPath("$.accounts[1].transactions", is(transactions)))
                .andReturn();
        verify(accountServiceImpl).getAccounts();
    }

    @Test
    public void deleteAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        when(accountServiceImpl.getAccountById(id)).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.delete(ACCOUNT_ID_ENDPOINT, id))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();
        verify(accountServiceImpl).deleteAccount(id);
    }

    @Test
    public void updateAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = "Martin";
        String surname = "King";
        String bank = "NatWest";
        String phoneNumber = "+447587155942";
        String emailAddress = "william.cuthbert@fisglobal.com";
        String customerNumber = CodeGenerator.generateCustomerNumber();
        String passNumber = CodeGenerator.generatePassNumber();
        String verificationCode = CodeGenerator.generateVerificationCode();
        String currency = "GBP";
        String accountNo = CodeGenerator.generateAccountNumber();
        String sortCode = CodeGenerator.generateSortCode();
        double balance = 0;
        List<Transaction> transactions = new ArrayList<>();
        LoginStatus loginStatus = LoginStatus.LOGIN_ACCESS_GRANTED;

        Account account = new Account(id, name, surname, bank, phoneNumber, emailAddress, customerNumber, passNumber, verificationCode, accountNo, sortCode, balance, currency, transactions, loginStatus);
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest(phoneNumber, emailAddress);
        when(accountServiceImpl.getAccountById(id)).thenReturn(account);
        when(accountServiceImpl.updateAccount(account)).thenReturn(account);
        mockMvc.perform(MockMvcRequestBuilders.put(ACCOUNT_ID_ENDPOINT, id)
                        .content(new ObjectMapper().writeValueAsString(updateAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(name)))
                .andExpect(jsonPath("$.surname", is(surname)))
                .andExpect(jsonPath("$.bankName", is(bank)))
                .andExpect(jsonPath("$.phoneNumber", is(phoneNumber)))
                .andExpect(jsonPath("$.emailAddress", is(emailAddress)))
                .andExpect(jsonPath("$.customerNumber", is(customerNumber)))
                .andExpect(jsonPath("$.passNumber", is(passNumber)))
                .andExpect(jsonPath("$.verificationCode", is(verificationCode)))
                .andExpect(jsonPath("$.sortCode", is(sortCode)))
                .andExpect(jsonPath("$.accountNumber", is(accountNo)))
                .andExpect(jsonPath("$.balance", is(balance)))
                .andExpect(jsonPath("$.currency", is(currency)))
                .andExpect(jsonPath("$.transactions", is(transactions)))
                .andReturn();
        verify(accountServiceImpl).updateAccount(account);
    }
}