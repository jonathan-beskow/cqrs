package br.com.beautique.ms_sync.service;

import br.com.beautique.ms_sync.dto.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dto.customers.CustomerDTO;

public interface AppointmentService {

    void saveAppointment(FullAppointmentDTO appointment);

    void updateAppointmentCustomer(CustomerDTO customer);

    void updateAppointmentBeautyProcedures(BeautyProcedureDTO beautyProcedure);
}
