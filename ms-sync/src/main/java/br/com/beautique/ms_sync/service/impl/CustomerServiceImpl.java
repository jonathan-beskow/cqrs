package br.com.beautique.ms_sync.service.impl;

import br.com.beautique.ms_sync.dto.customers.CustomerDTO;
import br.com.beautique.ms_sync.repositories.CustomerRepository;
import br.com.beautique.ms_sync.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public void saveCustomer(CustomerDTO customer) {
        try {
            log.info("Saving customer: {}", customer.getId());
            customerRepository.save(customer);
        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }
}
