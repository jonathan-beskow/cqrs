package br.com.beautique.ms_sync.listeners.impl;

import br.com.beautique.ms_sync.dto.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dto.customers.CustomerDTO;
import br.com.beautique.ms_sync.listeners.ListenerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
@Slf4j
public class ListenerConfigImpl implements ListenerConfig {

    private final ObjectMapper objectMapper;
    private final String ErrorWhileProcessingData = "rror processing data";

    @RabbitListener(queues = "customerQueue")
    @Override
    public void listenToCustomerQueue(String message) {
        try {
            CustomerDTO customer = objectMapper.readValue(message, CustomerDTO.class);
            //sync data here
            log.info("Message received from customerQueue: {}", customer.toString());
        } catch (JsonProcessingException e) {
            log.error(ErrorWhileProcessingData);
        }
    }

    @RabbitListener(queues = "appointmentQueue")
    @Override
    public void listenToAppointmentQueue(String message) {
        try {
            FullAppointmentDTO appointment = objectMapper.readValue(message, FullAppointmentDTO.class);
            log.info("Message received from appointmentQueue: {}", appointment.toString());
        } catch (JsonProcessingException e) {
            log.error(ErrorWhileProcessingData);
        }
    }

    @RabbitListener(queues = "beautyProcedureQueue")
    @Override
    public void listenToBeautyProcedureQueue(String message) {
        try {
            BeautyProcedureDTO beautyProcedure = objectMapper.readValue(message, BeautyProcedureDTO.class);
            log.info("Message received from beautyQueue: {}", beautyProcedure.toString());
        } catch (JsonProcessingException e) {
            log.error(ErrorWhileProcessingData);
        }
    }
}
