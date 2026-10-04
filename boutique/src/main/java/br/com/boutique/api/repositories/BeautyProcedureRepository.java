package br.com.boutique.api.repositories;

import br.com.boutique.api.entities.BeautyProceduresEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeautyProcedureRepository extends JpaRepository<BeautyProceduresEntity, Long> {
}
