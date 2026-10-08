package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.BeautyProcedureDTO;
import br.com.boutique.api.entities.BeautyProceduresEntity;
import br.com.boutique.api.repositories.BeautyProcedureRepository;
import br.com.boutique.api.services.BeautyProcedureService;
import br.com.boutique.api.services.BrokerService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeautyProcedureServiceImpl implements BeautyProcedureService {

    private final BeautyProcedureRepository beautyProcedureRepository;
    private final BrokerService brokerService;

    private final ConvertUtil<BeautyProceduresEntity, BeautyProcedureDTO> convertUtil = new ConvertUtil<>(BeautyProceduresEntity.class, BeautyProcedureDTO.class);

    @Override
    public BeautyProcedureDTO create(BeautyProcedureDTO beautyProcedureDTO) {
        BeautyProceduresEntity beautyProceduresEntity = convertUtil.convertToSource(beautyProcedureDTO);
        BeautyProceduresEntity newBeautyProcedure = beautyProcedureRepository.save(beautyProceduresEntity);
        sendBeautyProceduresToQueue(newBeautyProcedure);
        return convertUtil.convertToTarget(newBeautyProcedure);
    }

    @Override
    public void deleteProcedure(Long id) {
        Optional<BeautyProceduresEntity> beautyProceduresEntityOptional = beautyProcedureRepository.findById(id);
        if (beautyProceduresEntityOptional.isEmpty()) {
            throw new RuntimeException("Beauty Procedure not found");
        }
        beautyProcedureRepository.deleteById(id);
    }

    @Override
    public BeautyProcedureDTO update(BeautyProcedureDTO beautyProcedureDTO) {
        Optional<BeautyProceduresEntity> beautyProceduresEntityOptional = beautyProcedureRepository.findById(beautyProcedureDTO.getId());
        if (beautyProceduresEntityOptional.isEmpty()) {
            throw new RuntimeException("Beauty Procedure not found");
        }
        BeautyProceduresEntity beautyProceduresEntity = convertUtil.convertToSource(beautyProcedureDTO);
        beautyProceduresEntity.setAppointments(beautyProceduresEntityOptional.get().getAppointments());
        beautyProceduresEntity.setCreatedAt(beautyProceduresEntityOptional.get().getCreatedAt());
        BeautyProceduresEntity updatedBeautyProcedureEntity = beautyProcedureRepository.save(beautyProceduresEntity);

        sendBeautyProceduresToQueue(updatedBeautyProcedureEntity);

        return convertUtil.convertToTarget(updatedBeautyProcedureEntity);
    }

    private void sendBeautyProceduresToQueue(BeautyProceduresEntity beautyProceduresEntity) {
        BeautyProcedureDTO beautyProcedureDTO = BeautyProcedureDTO.builder()
                .id(beautyProceduresEntity.getId())
                .name(beautyProceduresEntity.getName())
                .description(beautyProceduresEntity.getDescription())
                .price(beautyProceduresEntity.getPrice())
                .build();

        brokerService.send("beautyProcedures", beautyProcedureDTO);
    }

}
