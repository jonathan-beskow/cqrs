package br.com.boutique.api.services;

import br.com.boutique.api.dto.CustomerDTO;

public interface CustomerService {

    CustomerDTO create(CustomerDTO customerDTO);

    void delete(Long id);

    CustomerDTO update(CustomerDTO customerDTO);

}
