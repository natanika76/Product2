package ru.natali.medregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.natali.medregistry.model.Doctor;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Modifying
    @Query("UPDATE Doctor d SET d.deleted = true WHERE d.id = ?1")
    void softDelete(Long id);

    @Query("SELECT d FROM Doctor d WHERE d.deleted = false")
    List<Doctor> findAllActive();
}
