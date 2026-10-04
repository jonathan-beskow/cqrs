package br.com.boutique.api.services;

import br.com.boutique.api.dto.BeautyProcedureDTO;

public interface BeautyProcedureService {

    BeautyProcedureDTO create(BeautyProcedureDTO beautyProcedureDTO);

    void deleteProcedure(Long id);

}
