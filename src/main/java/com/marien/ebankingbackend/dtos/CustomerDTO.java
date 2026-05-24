package com.marien.ebankingbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marien.ebankingbackend.entities.BankAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;


}
