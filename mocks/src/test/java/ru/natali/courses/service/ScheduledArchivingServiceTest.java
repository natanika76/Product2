package ru.natali.courses.service;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduledArchivingServiceTest {

    @Mock
    private CourseArchivingService courseArchivingService;

    @InjectMocks
    private ScheduledArchivingService scheduledArchivingService;

    @Test
    void testArchiveOldCoursesDaily() {
        // Проверяем, вызывает ли метод archiveOldCoursesDaily метод архивирования
        scheduledArchivingService.archiveOldCoursesDaily();

        verify(courseArchivingService, times(1)).archiveOldCourses(); // Убедимся, что метод вызван ровно один раз
    }

    // Если необходимо проверить поведение аннотаций cron, можно добавить дополнительный тест
    // Но в большинстве случаев достаточно убедиться, что сам метод правильно делегирует работу зависимому сервису
}