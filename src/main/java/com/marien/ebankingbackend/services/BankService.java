package com.marien.ebankingbackend.services;


import com.marien.ebankingbackend.entities.BankAccount;
import com.marien.ebankingbackend.entities.CurrentAccount;
import com.marien.ebankingbackend.entities.SavingAccount;
import com.marien.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){

        BankAccount bankAccount =
                bankAccountRepository.findById("099a6516-b986-4c22-ab6b-0341208d9c94").orElse(null);

        if (bankAccount != null) {
            System.out.println("***************************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getCreatdAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft => " + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate => " + ((SavingAccount) bankAccount).getInterestRate());
            }

            System.out.println("===============================================");
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }

    }
}
