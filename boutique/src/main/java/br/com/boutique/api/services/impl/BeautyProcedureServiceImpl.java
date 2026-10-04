package br.com.boutique.api.services.impl;

import br.com.boutique.api.dto.BeautyProcedureDTO;
import br.com.boutique.api.entities.BeautyProceduresEntity;
import br.com.boutique.api.repositories.BeautyProcedureRepository;
import br.com.boutique.api.services.BeautyProcedureService;
import br.com.boutique.api.utils.ConvertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeautyProcedureServiceImpl implements BeautyProcedureService {

    private final BeautyProcedureRepository beautyProcedureRepository;

    private final ConvertUtil<BeautyProceduresEntity, BeautyProcedureDTO> convertUtil = new ConvertUtil<>(BeautyProceduresEntity.class, BeautyProcedureDTO.class);

    @Override
    public BeautyProcedureDTO create(BeautyProcedureDTO beautyProcedureDTO) {
        BeautyProceduresEntity beautyProceduresEntity = convertUtil.convertToSource(beautyProcedureDTO);
        BeautyProceduresEntity newBeautyProcedure = beautyProcedureRepository.save(beautyProceduresEntity);
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
}
