package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.MedicalRecord;
import ru.natali.clinic.model.Patient;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MedicalRecordsRepositoryJdbcTemplateImpl implements MedicalRecordsRepository {
    private final String SQL_SELECT_ALL_RECORDS = """
            SELECT * FROM medical_records
            """;
    private final String SQL_RECORD_BY_ID = """
            SELECT * FROM medical_records WHERE id = ?
            """;
    private final String SQL_INSERT_RECORD = """
            INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, record_date) VALUES (?, ?, ?, ?, ?)
            """;
    private final String SQL_UPDATE_RECORD = """
            UPDATE medical_records SET patient_id = ?, doctor_id = ?, diagnosis = ?, treatment = ?, record_date = ? WHERE id = ?
            """;
    private final String SQL_DELETE_RECORD_BY_ID = """
            DELETE FROM medical_records WHERE id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public MedicalRecordsRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    private static class MedicalRecordRowMapper implements RowMapper<MedicalRecord> {
        @Override
        public MedicalRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setId(rs.getLong("id"));
            medicalRecord.setPatient(new Patient(rs.getLong("patient_id")));
            medicalRecord.setDoctor(new Doctor(rs.getLong("doctor_id")));
            medicalRecord.setDiagnosis(rs.getString("diagnosis"));
            medicalRecord.setTreatment(rs.getString("treatment"));
            medicalRecord.setRecordDate(rs.getTimestamp("record_date").toLocalDateTime());
            return medicalRecord;
        }
    }
    @Override
    public Optional<MedicalRecord> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_RECORD_BY_ID, new Object[]{id}, new MedicalRecordRowMapper()));
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        jdbcTemplate.update(
                SQL_INSERT_RECORD,
                medicalRecord.getPatient().getId(),
                medicalRecord.getDoctor().getId(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatment(),
                medicalRecord.getRecordDate()
        );
    }

    @Override
    public void update(MedicalRecord medicalRecord) {
        jdbcTemplate.update(
                SQL_UPDATE_RECORD,
                medicalRecord.getPatient().getId(),
                medicalRecord.getDoctor().getId(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatment(),
                medicalRecord.getRecordDate(),
                medicalRecord.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_RECORD_BY_ID, id);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_RECORDS, new MedicalRecordRowMapper());
    }
}


