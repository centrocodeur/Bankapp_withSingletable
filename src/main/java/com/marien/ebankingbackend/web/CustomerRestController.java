package com.marien.ebankingbackend.web;


import com.marien.ebankingbackend.dtos.CustomerDTO;
import com.marien.ebankingbackend.entities.Customer;
import com.marien.ebankingbackend.exceptions.CustomerNotFoundException;
import com.marien.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")

    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }



    @GetMapping("/customers/search")

    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return bankAccountService.searchCustomers(keyword);
    }

    @GetMapping("/customers/search2")
    public List<CustomerDTO> searchCustomers2(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return bankAccountService.searchCustomers(keyword);
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer( @PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
     CustomerDTO customerDTO= bankAccountService.getCustomer(customerId);

     return customerDTO;
    }


    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }


    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer( @PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
         customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }


    @DeleteMapping("/customers/{id}")
    public void deleteCustomer( @PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
