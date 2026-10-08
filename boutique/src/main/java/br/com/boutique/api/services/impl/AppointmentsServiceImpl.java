package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.AppointmentDTO;
import br.com.boutique.api.dto.BeautyProcedureDTO;
import br.com.boutique.api.dto.CustomerDTO;
import br.com.boutique.api.dto.FullAppointmentDTO;
import br.com.boutique.api.entities.AppointmentsEntity;
import br.com.boutique.api.entities.BeautyProceduresEntity;
import br.com.boutique.api.entities.CustomerEntity;
import br.com.boutique.api.repositories.AppointmentRepository;
import br.com.boutique.api.repositories.BeautyProcedureRepository;
import br.com.boutique.api.repositories.CustomerRepository;
import br.com.boutique.api.services.AppointmentsService;
import br.com.boutique.api.services.BrokerService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentRepository appointmentRepository;
    private final BeautyProcedureRepository beautyProcedureRepository;
    private final CustomerRepository customerRepository;
    private final BrokerService brokerService;
    private final ModelMapper modelMapper;
    private final ConvertUtil<AppointmentsEntity, AppointmentDTO> convertUtil = new ConvertUtil<>(AppointmentsEntity.class, AppointmentDTO.class);


    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) {
        AppointmentsEntity appointmentsEntity = convertUtil.convertToSource(appointmentDTO);
        attachRelations(appointmentsEntity, appointmentDTO);
        AppointmentsEntity newAppointmentsEntity = appointmentRepository.save(appointmentsEntity);
        sendAppointmentsToQueue(newAppointmentsEntity);
        return buildAppointmentsDTO(newAppointmentsEntity);
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        Optional<AppointmentsEntity> currentAppointment = appointmentRepository.findById(appointmentDTO.getId());
        if (currentAppointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        AppointmentsEntity appointmentsEntity = convertUtil.convertToSource(appointmentDTO);
        appointmentsEntity.setCreatedAt(currentAppointment.get().getCreatedAt());
        attachRelations(appointmentsEntity, appointmentDTO);
        AppointmentsEntity updatedAppointment = appointmentRepository.save(appointmentsEntity);
        sendAppointmentsToQueue(updatedAppointment);
        return buildAppointmentsDTO(updatedAppointment);
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
        sendAppointmentsToQueue(updatedAppointmentEntity);
        return buildAppointmentsDTO(updatedAppointmentEntity);
    }

    private void sendAppointmentsToQueue(AppointmentsEntity appointmentsEntity) {
        CustomerDTO customerDTO = appointmentsEntity.getCustomer() != null ? modelMapper.map(appointmentsEntity.getCustomer(), CustomerDTO.class) : null;
        BeautyProcedureDTO beautyProcedureDTO = appointmentsEntity.getBeautyProcedure() != null ? modelMapper.map(appointmentsEntity.getBeautyProcedure(), BeautyProcedureDTO.class) : null;

        FullAppointmentDTO fullAppointmentDTO = FullAppointmentDTO.builder()
                .id(appointmentsEntity.getId())
                .dateTime(appointmentsEntity.getDateTime())
                .appointmentsOpen(appointmentsEntity.getAppointmentsOpen())
                .customer(customerDTO)
                .beautyProcedureDTO(beautyProcedureDTO)
                .build();

        brokerService.send("appointments", fullAppointmentDTO);

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

    private void attachRelations(AppointmentsEntity entity, AppointmentDTO dto) {
        if (dto.getCustomer() != null) {
            entity.setCustomer(findCustomerById(dto.getCustomer()));
        }
        if (dto.getBeautyProcedure() != null) {
            entity.setBeautyProcedure(findBeautyProcedureById(dto.getBeautyProcedure()));
        }
    }

    private AppointmentDTO buildAppointmentsDTO(AppointmentsEntity appointmentsEntity) {
        Long customerId = appointmentsEntity.getCustomer() != null
                ? appointmentsEntity.getCustomer().getId() : null;
        Long beautyProcedureId = appointmentsEntity.getBeautyProcedure() != null
                ? appointmentsEntity.getBeautyProcedure().getId() : null;
        return AppointmentDTO.builder()
                .id(appointmentsEntity.getId())
                .beautyProcedure(beautyProcedureId)
                .dateTime(appointmentsEntity.getDateTime())
                .appointmentsOpen(appointmentsEntity.getAppointmentsOpen())
                .customer(customerId)
                .build();
    }


}
