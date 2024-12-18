package ru.natali.clinic.repository;

import ru.natali.clinic.model.Schedule;
import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule> {
    List<Schedule> findAllByDayOfWeek(String dayOfWeek);
}
