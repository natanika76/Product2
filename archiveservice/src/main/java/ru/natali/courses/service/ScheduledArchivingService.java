package ru.natali.courses.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledArchivingService {

    private final CourseArchivingService archivingService;

    public ScheduledArchivingService(CourseArchivingService archivingService) {
        this.archivingService = archivingService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Запускать каждый день в полночь
    public void archiveOldCoursesDaily() {
        archivingService.archiveOldCourses();
    }
}
