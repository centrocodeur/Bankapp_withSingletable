package com.marien.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor  @Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // pour éviter les boucle infinies dues à
    // des relation bidirectionnelles  => dépendance cycle.
    // Quand je consult un client je n'ai pas besoin de consulter la liste des comptes
    private List<BankAccount> bankAccounts;

}
