package br.com.beautique.ms_sync.service.impl;

import br.com.beautique.ms_sync.dto.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dto.customers.CustomerDTO;
import br.com.beautique.ms_sync.service.AppointmentService;
import br.com.beautique.ms_sync.service.BeautyProcedureService;
import br.com.beautique.ms_sync.service.CustomerService;
import br.com.beautique.ms_sync.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {

    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final BeautyProcedureService beautyProcedureService;

    @Override
    public void syncCustomer(CustomerDTO customer) {
        try {
            log.info("Saving customer: {}", customer.getId());
            customerService.saveCustomer(customer);
            log.info("Updating appointment customer: {}", customer.getId());
            appointmentService.updateAppointmentCustomer(customer);
        } catch (Exception e) {
            log.error("Error saving customer: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void syncAppointment(FullAppointmentDTO appointmentDTO) {
        try {
            log.info("Saving appointment: {}", appointmentDTO.getId());
            appointmentService.saveAppointment(appointmentDTO);
        } catch (Exception e) {
            log.error("Error saving appointment: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void syncBeautyProcedures(BeautyProcedureDTO beautyProcedure) {
        try {
            log.info("Saving beauty procedure: {}", beautyProcedure.getId());
            beautyProcedureService.saveBeautyProcedure(beautyProcedure);
            log.info("Updating appointment beauty procedure: {}", beautyProcedure.getId());
            appointmentService.updateAppointmentBeautyProcedures(beautyProcedure);
        } catch (Exception e) {
            log.error("Error saving beauty procedure: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }
}
