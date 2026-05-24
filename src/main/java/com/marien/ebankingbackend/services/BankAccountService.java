package com.marien.ebankingbackend.services;

import com.marien.ebankingbackend.dtos.*;
import com.marien.ebankingbackend.entities.BankAccount;
import com.marien.ebankingbackend.entities.CurrentAccount;
import com.marien.ebankingbackend.entities.Customer;
import com.marien.ebankingbackend.entities.SavingAccount;
import com.marien.ebankingbackend.exceptions.BalanceNotSufficientExeption;
import com.marien.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.marien.ebankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDTO saveCurrentBankAccountDTO(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;


    /*
     DTO
     */
    SavingBankAccountDTO saveSavingBankAccountDTO(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    BankAccountDTO getBankAccountDTO(String accountId) throws BankAccountNotFoundException;

    void debit (String accountId, double amount, String description ) throws BankAccountNotFoundException, BalanceNotSufficientExeption;


    void debitv1(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientExeption;

    void credit (String accountId, double amount, String description ) throws BankAccountNotFoundException;

    void creditv2(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer (String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExeption;

    List<BankAccount> bankAccountList();

    List<BankAccountDTO> bankAccountListDTO();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void  deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<CustomerDTO> searchCustomers2(String keyword);
}
