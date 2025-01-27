package ru.natali.notes.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
/*
Запустить в командной строке mvn spring-boot:run -D spring-boot.run.profiles=sandbox
 */
@SpringBootTest
@ActiveProfiles("sandbox") // Активируем профиль sandbox для тестов
public class SandboxProfileTest {

    @Test
    public void testSandboxProfile() {
        System.out.println("Running tests with SANDBOX profile...");
    }
}
