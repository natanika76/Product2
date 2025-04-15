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

@Service
public class StudentService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

   /* public void checkPassword() {
        boolean isMatch = passwordEncoder.matches("123", "{noop}123"); // Должно вернуть true
        System.out.println(isMatch);
    }*/
   public boolean checkPassword(String rawPassword, String encodedPassword) {
       boolean isMatch = passwordEncoder.matches(rawPassword, encodedPassword);
       System.out.println("Пароль совпадает? " + isMatch);
       return isMatch;
   }

    public List<Student> getStudentsByCourse(Course course) {
        return studentRepository.findByCourse(course);
    }
    // Метод для создания студента
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setCourse(studentDetails.getCourse()); // Добавляем привязку к курсу
            return studentRepository.save(existingStudent);
        } else {
            throw new RuntimeException("Студент с ID " + studentId + " не найден");
        }
    }
    // Метод для получения студентов по курсу и первой букве фамилии
    public List<Student> findStudentsByCourseAndLastNameStartsWith(Course course, char initialLetter) {
        return studentRepository.findByCourseAndLastNameStartingWithIgnoreCase(course, Character.toString(initialLetter));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));

        // Преобразуем Role в коллекцию GrantedAuthority
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(student.getRole());

        return new org.springframework.security.core.userdetails.User(
                student.getUsername(),
                student.getPassword(),
                authorities
        );
    }

    /**
     * Нахождение студента по имени пользователя.
     *
     * @param username имя пользователя
     * @return экземпляр студента
     */
    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }
}
