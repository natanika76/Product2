package ru.natali.students.repository;

import org.postgresql.jdbc.PgArray;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.students.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        PgArray pgArray = (PgArray) rs.getObject("courses");

        List<String> courses = extractCourses(pgArray);

        return new Student(id, fullName, email, courses);
    }

    private List<String> extractCourses(PgArray pgArray) throws SQLException {
        if (pgArray != null) {
            String[] dbArray = (String[]) pgArray.getArray();
            return Arrays.asList(dbArray);
        }
        return new ArrayList<>();
    }
}
