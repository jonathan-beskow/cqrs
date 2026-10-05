package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.AppointmentDTO;
import br.com.boutique.api.entities.AppointmentsEntity;
import br.com.boutique.api.repositories.AppointmentRepository;
import br.com.boutique.api.services.AppointmentsService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentRepository appointmentRepository;
    private final ConvertUtil<AppointmentsEntity, AppointmentDTO> convertUtil = new ConvertUtil<>(AppointmentsEntity.class, AppointmentDTO.class);


    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) {
        AppointmentsEntity appointmentsEntity = convertUtil.convertToSource(appointmentDTO);
        AppointmentsEntity newAppointmentsEntity = appointmentRepository.save(appointmentsEntity);
        return convertUtil.convertToTarget(newAppointmentsEntity);
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        Optional<AppointmentsEntity> currentAppointment = appointmentRepository.findById(appointmentDTO.getId());
        if (currentAppointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        AppointmentsEntity appointmentsEntity = convertUtil.convertToSource(appointmentDTO);
        appointmentsEntity.setCreatedAt(currentAppointment.get().getCreatedAt());
        AppointmentsEntity updatedAppointment = appointmentRepository.save(appointmentsEntity);

        return convertUtil.convertToTarget(updatedAppointment);
    }

    @Override
    public void deleteById(Long id) {
        Optional<AppointmentsEntity> currentAppointment = appointmentRepository.findById(id);
        if (currentAppointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        appointmentRepository.delete(currentAppointment.get());
    }

    @Override
    public AppointmentDTO setCustomerToAppointment(AppointmentDTO appointmentDTO) {
        return null;
    }
}
