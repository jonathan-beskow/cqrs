package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.CustomerDTO;
import br.com.boutique.api.entities.CustomerEntity;
import br.com.boutique.api.repositories.CustomerRepository;
import br.com.boutique.api.services.BrokerService;
import br.com.boutique.api.services.CustomerService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ConvertUtil<CustomerEntity, CustomerDTO> convertUtil = new ConvertUtil<>(CustomerEntity.class, CustomerDTO.class);

    private final BrokerService brokerService;

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        System.out.println(customerDTO);
        CustomerEntity entity = convertUtil.convertToSource(customerDTO);
        System.out.println(entity);
        CustomerEntity savedEntity = customerRepository.save(entity);
        sendCustomerToQueue(savedEntity);
        return convertUtil.convertToTarget(savedEntity);
    }

    @Override
    public void delete(Long id) {
        Optional<CustomerEntity> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.delete(customer.get());
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        Optional<CustomerEntity> customer = customerRepository.findById(customerDTO.getId());
        if (!customer.isPresent()) {
            throw new RuntimeException("Customer not found");
        }

        CustomerEntity customerEntity = convertUtil.convertToSource(customerDTO);
        customerEntity.setAppointments(customer.get().getAppointments());
        customerEntity.setCreatedAt(customer.get().getCreatedAt());
        CustomerDTO updatedCustomerDTO = convertUtil.convertToTarget(customerRepository.save(customerEntity));
        sendCustomerToQueue(convertUtil.convertToSource(customerDTO));
        return updatedCustomerDTO;
    }

    private void sendCustomerToQueue(CustomerEntity customerEntity) {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getName())
                .email(customerEntity.getEmail())
                .phone(customerEntity.getPhone())
                .build();
        brokerService.send("customer", customerDTO);
    }
}
