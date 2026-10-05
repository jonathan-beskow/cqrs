package br.com.boutique.api.services;

import br.com.boutique.api.dto.AppointmentDTO;

public interface AppointmentsService {

    AppointmentDTO create(AppointmentDTO appointmentDTO);

    AppointmentDTO update(AppointmentDTO appointmentDTO);

    void deleteById(Long id);

    AppointmentDTO setCustomerToAppointment(AppointmentDTO appointmentDTO);

}
