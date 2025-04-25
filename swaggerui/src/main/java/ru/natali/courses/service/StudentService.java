package ru.natali.courses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы со студентами.
 */
@Service
public class StudentService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Проверяет соответствие пароля.
     * @param rawPassword исходный пароль
     * @param encodedPassword закодированный пароль
     * @return true, если пароли совпадают
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("Пароль совпадает? " + isMatch);
        return isMatch;
    }

    /**
     * Получает студентов по курсу.
     * @param course курс для поиска
     * @return список студентов на курсе
     */
    public List<Student> getStudentsByCourse(Course course) {
        return studentRepository.findByCourse(course);
    }

    /**
     * Создает нового студента.
     * @param student данные студента
     * @return созданный студент
     */
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Обновляет данные студента.
     * @param studentId идентификатор студента
     * @param studentDetails новые данные студента
     * @return обновленный студент
     */
    public Student updateStudent(Long studentId, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setCourse(studentDetails.getCourse());
            return studentRepository.save(existingStudent);
        } else {
            throw new RuntimeException("Студент с ID " + studentId + " не найден");
        }
    }

    /**
     * Находит студентов по курсу и начальной букве фамилии.
     * @param course курс для поиска
     * @param initialLetter начальная буква фамилии
     * @return список подходящих студентов
     */
    public List<Student> findStudentsByCourseAndLastNameStartsWith(Course course, char initialLetter) {
        return studentRepository.findByCourseAndLastNameStartingWithIgnoreCase(course, Character.toString(initialLetter));
    }

    /**
     * Загружает данные пользователя для аутентификации.
     * @param username имя пользователя
     * @return UserDetails с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(student.getRole());

        return new org.springframework.security.core.userdetails.User(
                student.getUsername(),
                student.getPassword(),
                authorities
        );
    }

    /**
     * Находит студента по имени пользователя.
     * @param username имя пользователя
     * @return найденный студент или null
     */
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }
}