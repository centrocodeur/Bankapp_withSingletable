package com.marien.ebankingbackend.repositories;

import com.marien.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository  extends JpaRepository<BankAccount, String> {
}
