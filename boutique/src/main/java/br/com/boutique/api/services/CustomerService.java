package br.com.boutique.api.services;

import br.com.boutique.api.dto.CustomerDTO;
import br.com.boutique.api.entities.CustomerEntity;

public interface CustomerService {

    CustomerDTO create(CustomerDTO customerDTO);

}
