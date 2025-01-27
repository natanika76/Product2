package ru.natali.notes.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
/*
Запустить в командной строке mvn spring-boot:run -D spring-boot.run.profiles=production
 */
@SpringBootTest
@ActiveProfiles("production")
public class ProductionProfileTest {

    @Value("${custom.message}")
    private String customMessage;

    @Test
    public void testDebugProfile() {
        assertThat(customMessage).isEqualTo("This is the PRODUCTION profile!");
    }
}
