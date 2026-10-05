package br.com.boutique.api.repositories;

import br.com.boutique.api.entities.AppointmentsEntity;
import br.com.boutique.api.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentsEntity, Long> {
}
