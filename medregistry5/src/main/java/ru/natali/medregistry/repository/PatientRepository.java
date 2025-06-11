package ru.natali.medregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.natali.medregistry.model.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Добавьте эти методы
    boolean existsByInsuranceNumber(String insuranceNumber);
    Optional<Patient> findByInsuranceNumber(String insuranceNumber);

    @Modifying
    @Query("UPDATE Patient p SET p.deleted = true WHERE p.id = ?1")
    void softDelete(Long id);

    @Query("SELECT p FROM Patient p WHERE p.deleted = false")
    List<Patient> findAllActive();
}
