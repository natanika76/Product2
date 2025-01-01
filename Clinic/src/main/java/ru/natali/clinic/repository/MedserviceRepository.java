package ru.natali.clinic.repository;

import ru.natali.clinic.model.Medservice;
import java.util.Optional;

public interface MedserviceRepository extends CrudRepository<Medservice> {
    Optional<Medservice> findByName(String name);
    Medservice findById(Long id);
    void updateById(Long id, Medservice updatedMedservice);
}
