package com.example.CinemaChallange.schedule

import spock.lang.Specification

class DayScheduleTest extends Specification {

    def 'should return existing RoomSchedule'() {
        given:
            MovieTime movieTime = new MovieTime();
            def movies = List.of(movieTime)
            RoomSchedule roomSchedule = RoomSchedule.builder()
                .movies(movies)
                .build()
            DaySchedule daySchedule = new DaySchedule()
            daySchedule.schedule.put(1, roomSchedule)

        when:
            def result = daySchedule.getForRoom(1)

        then:
            result.movies == movies
    }

    def 'should return new RoomSchedule when non present'() {
        given:
            MovieTime movieTime = new MovieTime();
            def movies = List.of(movieTime)
            RoomSchedule roomSchedule = RoomSchedule.builder()
                .movies(movies)
                .build()
            DaySchedule daySchedule = new DaySchedule()
            daySchedule.schedule.put(1, roomSchedule)

        when:
            def result = daySchedule.getForRoom(2)

        then:
            result.roomNo == 2
            result.movies.size() == 0
    }
}
