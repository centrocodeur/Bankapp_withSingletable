package com.marien.ebankingbackend.repositories;

import com.marien.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository  extends JpaRepository<BankAccount, String> {

    List<BankAccount> findByCustomerId(String customerId);
}
