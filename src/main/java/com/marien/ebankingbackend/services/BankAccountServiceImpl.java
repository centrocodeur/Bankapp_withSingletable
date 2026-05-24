package com.marien.ebankingbackend.services;

import com.marien.ebankingbackend.dtos.*;
import com.marien.ebankingbackend.entities.*;
import com.marien.ebankingbackend.enums.OperationType;
import com.marien.ebankingbackend.exceptions.BalanceNotSufficientExeption;
import com.marien.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.marien.ebankingbackend.exceptions.CustomerNotFoundException;
import com.marien.ebankingbackend.mappers.BankAccountMapperImpl;
import com.marien.ebankingbackend.repositories.AccountOperationRepository;
import com.marien.ebankingbackend.repositories.BankAccountRepository;
import com.marien.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements  BankAccountService{

    private CustomerRepository customerRepository;

    private BankAccountRepository  bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl dtoMapper;







    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer");
        Customer savedCustomer = customerRepository.save(customer);

        return savedCustomer;
    }





    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }


        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatdAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
         CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);

        return savedBankAccount;
    }




    /*
     DTO
     */

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccountDTO(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }


        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatdAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }



    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }


        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatdAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return  savedBankAccount;
    }



 /*
  DTO
  */
    @Override
    public SavingBankAccountDTO saveSavingBankAccountDTO(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }


        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatdAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return  dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customerRepository.findAll();

        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());

        /* // programmation imperative
        List<CustomerDTO> customerDTOS =new ArrayList<>();
        for(Customer customer: customers){
            CustomerDTO customerDTO= dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }

         */
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=  bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));


        return bankAccount;
    }

    @Override
    public BankAccountDTO getBankAccountDTO(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount=  bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;

            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }


    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientExeption {
      BankAccount bankAccount = getBankAccount(accountId);

      if(bankAccount.getBalance()<amount)
           throw  new BalanceNotSufficientExeption("Balance not sufficent");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }


    /*
    DTO
     */


    @Override
    public void debitv1(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientExeption {
        BankAccount bankAccount=  bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));


        if(bankAccount.getBalance()<amount)
            throw  new BalanceNotSufficientExeption("Balance not sufficent");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }


    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);


    }


    @Override
    public void creditv2(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount=  bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExeption {

        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+ accountIdSource);
    }


    @Override
    public  List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }

    @Override
    public  List<BankAccountDTO> bankAccountListDTO(){
       List<BankAccount> bankAccounts=  bankAccountRepository.findAll();

      List<BankAccountDTO> bankAccountDTOS =  bankAccounts.stream().map(bankAccount -> {
           if(bankAccount instanceof SavingAccount){
               SavingAccount savingAccount= (SavingAccount) bankAccount;
               return dtoMapper.fromSavingBankAccount(savingAccount);
           }else {
               CurrentAccount currentAccount = (CurrentAccount) bankAccount;
               return dtoMapper.fromCurrentBankAccount(currentAccount);

           }
       }).collect(Collectors.toList());

      return bankAccountDTOS;
    }


    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));

        return dtoMapper.fromCustomer(customer);

    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {

        log.info("Update new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }


    @Override
    public void  deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }


    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){

        List<AccountOperation>  accountOperations= accountOperationRepository.findByBankAccount_Id(accountId);
        accountOperations.forEach(accountOperation -> {
            for(int i =0; i<10; i++){
                System.out.println(accountOperation);
            }
        });

        return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw  new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();

        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
         List<Customer> customers= customerRepository.findByNameContains(keyword);
        List<CustomerDTO> customerDTOS= customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
          return customerDTOS;
    }

    @Override
    public List<CustomerDTO> searchCustomers2(String keyword) {
        List<Customer> customers= customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS= customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

}
