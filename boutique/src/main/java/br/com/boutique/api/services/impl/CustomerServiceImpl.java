package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.CustomerDTO;
import br.com.boutique.api.entities.CustomerEntity;
import br.com.boutique.api.repositories.CustomerRepository;
import br.com.boutique.api.services.CustomerService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ConvertUtil<CustomerEntity, CustomerDTO> convertUtil = new ConvertUtil<>(CustomerEntity.class, CustomerDTO.class);

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        System.out.println(customerDTO);
        CustomerEntity entity = convertUtil.convertToSource(customerDTO);
        System.out.println(entity);
        CustomerEntity savedEntity = customerRepository.save(entity);

        return convertUtil.convertToTarget(savedEntity);
    }
}
