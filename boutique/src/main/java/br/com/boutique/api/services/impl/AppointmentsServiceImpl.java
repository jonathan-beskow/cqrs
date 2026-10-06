package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.AppointmentDTO;
import br.com.boutique.api.entities.AppointmentsEntity;
import br.com.boutique.api.entities.BeautyProceduresEntity;
import br.com.boutique.api.entities.CustomerEntity;
import br.com.boutique.api.repositories.AppointmentRepository;
import br.com.boutique.api.repositories.BeautyProcedureRepository;
import br.com.boutique.api.repositories.CustomerRepository;
import br.com.boutique.api.services.AppointmentsService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentRepository appointmentRepository;
    private final BeautyProcedureRepository beautyProcedureRepository;
    private final CustomerRepository customerRepository;
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
        CustomerEntity customerEntity = findCustomerById(appointmentDTO.getCustomer());
        BeautyProceduresEntity beautyProceduresEntity = findBeautyProcedureById(appointmentDTO.getBeautyProcedure());
        AppointmentsEntity appointmentsEntity = findAppointmentById(appointmentDTO.getId());

        appointmentsEntity.setCustomer(customerEntity);
        appointmentsEntity.setBeautyProcedure(beautyProceduresEntity);
        appointmentsEntity.setAppointmentsOpen(false);
        AppointmentsEntity updatedAppointmentEntity = appointmentRepository.save(appointmentsEntity);
        return buildAppointmentsDTO(updatedAppointmentEntity);
    }

    private AppointmentsEntity findAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    private BeautyProceduresEntity findBeautyProcedureById(Long id) {
        return beautyProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BeautyProcedure not found"));
    }

    private CustomerEntity findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BeautyProcedure not found"));
    }

    private AppointmentDTO buildAppointmentsDTO(AppointmentsEntity appointmentsEntity) {
        return AppointmentDTO.builder()
                .id(appointmentsEntity.getId())
                .beautyProcedure(appointmentsEntity.getBeautyProcedure().getId())
                .dateTime(appointmentsEntity.getDateTime())
                .appointmentsOpen(appointmentsEntity.getAppointmentsOpen())
                .customer(appointmentsEntity.getCustomer().getId())
                .build();
    }

}
