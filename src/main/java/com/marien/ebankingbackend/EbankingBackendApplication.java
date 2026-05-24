package com.marien.ebankingbackend;

import com.marien.ebankingbackend.dtos.BankAccountDTO;
import com.marien.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.marien.ebankingbackend.dtos.SavingBankAccountDTO;
import com.marien.ebankingbackend.entities.*;
import com.marien.ebankingbackend.enums.AccountStatus;
import com.marien.ebankingbackend.enums.OperationType;
import com.marien.ebankingbackend.exceptions.BalanceNotSufficientExeption;
import com.marien.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.marien.ebankingbackend.exceptions.CustomerNotFoundException;
import com.marien.ebankingbackend.repositories.AccountOperationRepository;
import com.marien.ebankingbackend.repositories.BankAccountRepository;
import com.marien.ebankingbackend.repositories.CustomerRepository;
import com.marien.ebankingbackend.services.BankAccountService;
import com.marien.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunnerDTO(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Marien", "Petula", "Elisa", "Mathis").forEach(name ->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+ "@gmail.com");

                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId() );



                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();

                }

            });

            List<BankAccountDTO> bankAccountList = bankAccountService.bankAccountListDTO();

            for (BankAccountDTO bankAccount: bankAccountList){
                for(int i=0; i<10; i++){
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId= ((SavingBankAccountDTO)bankAccount).getId();
                    }else{
                        accountId= ((CurrentBankAccountDTO)bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 10000 + Math.random()*120000,  "CREDIT" );
                    bankAccountService.debit(accountId, 1000+Math.random()*9000, "DEBIT");
                }

            }
        };

    }


    //@Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Marien", "Petula", "Elisa", "Mathis").forEach(name ->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+ "@gmail.com");

                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId() );

                    List<BankAccount> bankAccountList = bankAccountService.bankAccountList();

                    for (BankAccount bankAccount: bankAccountList){
                         for(int i=0; i<10; i++){
                                bankAccountService.credit(bankAccount.getId(), 10000 + Math.random()*120000,  "CREDIT" );
                                bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*9000, "DEBIT");
                         }

                    }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | BalanceNotSufficientExeption e){
                    e.printStackTrace();
                }

            });
        };

    }


    // @Bean
    CommandLineRunner commandLineRunnerTest(BankService bankService){
        return args -> {
              bankService.consulter();
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Marien", "Petula", "Elisa", "Mathis").forEach(name->{
                Customer customer= new Customer();
                customer.setName(name);
                customer.setEmail(name+ "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatdAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(Math.random()*1000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatdAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(acc->{
                for(int i=0; i<10; i++){
                    AccountOperation accountOperation= new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*1200);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }


            });
        };
    }

}
