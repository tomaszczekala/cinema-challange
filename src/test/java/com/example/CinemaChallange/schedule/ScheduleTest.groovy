package com.example.CinemaChallange.schedule

import spock.lang.Specification

import java.time.LocalDate

class ScheduleTest extends Specification {

    def 'should return existing DaySchedule'() {
        given:
            def date = LocalDate.of(2023, 10, 10);
            DaySchedule daySchedule = DaySchedule.builder().build()
            Schedule schedule = new Schedule()
            schedule.schedule.put(date, daySchedule)

        when:
            def result = schedule.getForDay(date)

        then:
            result == daySchedule
    }

    def 'should return new DaySchedule when non present'() {
        given:
            def date = LocalDate.of(2023, 10, 10);
            def date2 = LocalDate.of(2023, 10, 11)
            DaySchedule daySchedule = DaySchedule.builder().build()
            Schedule schedule = new Schedule()
            schedule.schedule.put(date, daySchedule)

        when:
            def result = schedule.getForDay(date2)

        then:
            result.date == date2
            result.schedule.size() == 0;
    }
}
