package br.com.beautique.ms_sync.service;

import br.com.beautique.ms_sync.dto.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dto.customers.CustomerDTO;

public interface SyncService {

    void syncCustomer(CustomerDTO customer);

    void syncAppointment(FullAppointmentDTO appointmentDTO);

    void syncBeautyProcedures(BeautyProcedureDTO beautyProcedure);

}
