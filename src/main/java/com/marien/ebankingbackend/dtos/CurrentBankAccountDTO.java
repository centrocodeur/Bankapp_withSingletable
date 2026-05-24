package com.marien.ebankingbackend.dtos;


import com.marien.ebankingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CurrentBankAccountDTO extends BankAccountDTO{

         private String id;

        private double balance;

        private Date creatdAt;

         private AccountStatus status;


        private CustomerDTO customerDTO;

        private double overDraft;

    }
