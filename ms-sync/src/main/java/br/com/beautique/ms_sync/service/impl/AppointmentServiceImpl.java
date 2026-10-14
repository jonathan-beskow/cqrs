package br.com.beautique.ms_sync.service.impl;

import br.com.beautique.ms_sync.dto.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dto.customers.CustomerDTO;
import br.com.beautique.ms_sync.repositories.AppointmentRepository;
import br.com.beautique.ms_sync.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void saveAppointment(FullAppointmentDTO appointment) {
        try {
            log.info("Saving appointment: {}", appointment.getId());
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            log.error("Error saving appointment: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void updateAppointmentCustomer(CustomerDTO customer) {
        try {
            log.info("Updating appointment customer: {}", customer.getId());
            Query query = new Query(Criteria.where("customer.id").is(customer.getId()));
            Update update = new Update().set("customer", customer);
            mongoTemplate.updateMulti(query, update, FullAppointmentDTO.class);
        } catch (Exception e) {
            log.error("Error updating appointment customer: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void updateAppointmentBeautyProcedures(BeautyProcedureDTO beautyProcedure) {
        try {
            log.info("Updating appointment beauty procedure: {}", beautyProcedure.getId());
            Query query = new Query(Criteria.where("beautyProcedure.id").is(beautyProcedure.getId()));
            Update update = new Update()
                    .set("beautyProcedure.name", beautyProcedure.getName())
                    .set("beautyProcedure.description", beautyProcedure.getDescription());

            mongoTemplate.updateMulti(query, update, FullAppointmentDTO.class);
        } catch (Exception e) {
            log.error("Error updating appointment beauty procedure: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }
}
