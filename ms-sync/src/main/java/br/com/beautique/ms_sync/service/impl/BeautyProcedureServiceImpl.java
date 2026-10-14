package br.com.beautique.ms_sync.service.impl;

import br.com.beautique.ms_sync.dto.beautyProcedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.repositories.BeautyProcedureRepository;
import br.com.beautique.ms_sync.service.BeautyProcedureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeautyProcedureServiceImpl implements BeautyProcedureService {

    private final BeautyProcedureRepository beautyProcedureRepository;

    @Override
    public void saveBeautyProcedure(BeautyProcedureDTO beautyProcedure) {
        try {
            log.info("Saving beauty procedure: {}", beautyProcedure.getId());
            beautyProcedureRepository.save(beautyProcedure);
        } catch (Exception e) {
            log.error("Error saving beauty procedure: {}", e.getMessage());
            log.trace(Arrays.toString(e.getStackTrace()));
        }
    }
}
