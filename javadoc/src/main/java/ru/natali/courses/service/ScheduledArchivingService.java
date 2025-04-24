package ru.natali.courses.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Сервис для планирования архивации курсов.
 */
@Service
public class ScheduledArchivingService {
    private final CourseArchivingService archivingService;

    /**
     * Конструктор с внедрением зависимости.
     * @param archivingService сервис архивации курсов
     */
    public ScheduledArchivingService(CourseArchivingService archivingService) {
        this.archivingService = archivingService;
    }

    /**
     * Ежедневная архивация старых курсов.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void archiveOldCoursesDaily() {
        archivingService.archiveOldCourses();
    }
}