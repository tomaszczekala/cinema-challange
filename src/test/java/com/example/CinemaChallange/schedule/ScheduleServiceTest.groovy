package com.example.CinemaChallange.schedule

import com.example.CinemaChallange.catalogue.Movie
import com.example.CinemaChallange.catalogue.MovieService
import com.example.CinemaChallange.cinema.CinemaService
import com.example.CinemaChallange.exceptions.*
import com.example.CinemaChallange.room.Room
import com.example.CinemaChallange.room.RoomService
import com.example.CinemaChallange.security.SecurityService
import com.example.CinemaChallange.security.User
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class ScheduleServiceTest extends Specification {

    MovieService movieService = Mock()

    CinemaService cinemaService = Mock()

    RoomService roomService = Mock()

    SecurityService securityService = Mock()

    ScheduleService scheduleService = new ScheduleService(movieService, cinemaService, roomService, securityService)

    def 'should return empty day schedule when no data present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            scheduleService.schedule.schedule.clear()

        when:
            def result = scheduleService.getDaySchedule(date)

        then:
            result.isPresent()
            result.get().schedule.size() == 0
            result.get().date == date
    }

    def 'should return day schedule when data present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def daySchedule = DaySchedule.builder()
                .date(date)
                .build()
            scheduleService.schedule.schedule.clear()
            scheduleService.schedule.schedule.put(date, daySchedule)

        when:
            def result = scheduleService.getDaySchedule(date)

        then:
            result.isPresent()
            result.get() == daySchedule
            result.get().date == date
    }

    def 'should return empty room schedule when no data present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            scheduleService.schedule.schedule.clear()

        when:
            def result = scheduleService.getDayRoomSchedule(date, roomNo)

        then:
            !result.isPresent()
    }

    def 'should return room schedule when data present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movies = List.of(MovieTime.builder()
                .time(LocalTime.of(10, 11, 12))
                .build())
            scheduleService.schedule.schedule.clear()
            scheduleService.schedule.schedule.put(date, DaySchedule.builder()
                .date(date)
                .schedule(Map.of(roomNo, RoomSchedule.builder()
                    .roomNo(roomNo)
                    .movies(movies)
                    .build()))
                .build())

        when:
            def result = scheduleService.getDayRoomSchedule(date, roomNo)

        then:
            result.isPresent()
            result.get().roomNo == roomNo
            result.get().movies == movies
    }

    def 'should throw when user unauthorized to add movie'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(10, 11, 12)
            def user = new User()
            securityService.canUserAdd(user) >> false

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(ForbiddenActionException)
    }

    def 'should throw when movie not present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(10, 11, 12)
            def user = new User()
            securityService.canUserAdd(user) >> true
            movieService.getMovieById(movieId) >> Optional.empty()

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidMovieException)
    }

    def 'should throw when room not present'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(10, 11, 12)
            def user = new User()
            def movie = Movie.builder().build()
            securityService.canUserAdd(user) >> true
            movieService.getMovieById(movieId) >> Optional.of(movie)
            roomService.getById(roomNo) >> Optional.empty()

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidRoomException)
    }

    def 'should throw when movie starts before opening time'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(7, 59, 59)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, false)

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidTimeException)
    }

    def 'should throw when movie ends after closing time'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(19, 0, 1)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, false)

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidTimeException)
    }

    def 'should throw when prime movie starts before prime opening time'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(9, 59, 59)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, true)

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidTimeException)
    }

    def 'should throw when prime movie ends after prime closing time'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(17, 0, 1)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, true)

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(InvalidTimeException)
    }

    def 'should throw when movie collides with one before'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(13, 0, 0)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, true)
            def presentMovies = new ArrayList()
            presentMovies.add(MovieTime.builder()
                .time(LocalTime.of(10, 0, 1))
                .movie(Movie.builder()
                    .duration(Duration.ofMinutes(120))
                    .isPrimer(true)
                    .build())
                .build())
            scheduleService.schedule.schedule.clear()
            scheduleService.schedule.schedule.put(date, DaySchedule.builder()
                .date(date)
                .schedule(Map.of(roomNo, RoomSchedule.builder()
                    .roomNo(roomNo)
                    .movies(presentMovies)
                    .build()))
                .build())

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(CollidingWithOtherMovieException)
    }

    def 'should throw when movie collides with one after'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(13, 0, 1)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, true)
            def presentMovies = new ArrayList()
            presentMovies.add(MovieTime.builder()
                .time(LocalTime.of(16, 0, 0))
                .movie(Movie.builder()
                    .duration(Duration.ofMinutes(120))
                    .isPrimer(true)
                    .build())
                .build())
            scheduleService.schedule.schedule.clear()
            scheduleService.schedule.schedule.put(date, DaySchedule.builder()
                .date(date)
                .schedule(Map.of(roomNo, RoomSchedule.builder()
                    .roomNo(roomNo)
                    .movies(presentMovies)
                    .build()))
                .build())

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            thrown(CollidingWithOtherMovieException)
    }

    def 'should add movie when movie don not collide'() {
        given:
            def date = LocalDate.of(2020, 10, 10)
            def roomNo = 1
            def movieId = 123
            def startTime = LocalTime.of(13, 0, 0)
            def user = new User()
            prepareMocks(date, roomNo, movieId, user, true)

        when:
            scheduleService.addMovieToSchedule(date, roomNo, movieId, startTime, user)

        then:
            scheduleService.schedule.schedule.get(date).getForRoom(roomNo).movies.size() == 3
    }

    def prepareMocks(date, roomNo, movieId, user, isPrimer) {
        def movie = Movie.builder()
            .duration(Duration.ofMinutes(120))
            .isPrimer(isPrimer)
            .build()
        def room = Room.builder()
            .cleanupTime(Duration.ofMinutes(60))
            .build()
        securityService.canUserAdd(user) >> true
        movieService.getMovieById(movieId) >> Optional.of(movie)
        roomService.getById(roomNo) >> Optional.of(room)
        cinemaService.getOpeningTime() >> LocalTime.of(8, 0, 0)
        cinemaService.getClosingTime() >> LocalTime.of(22, 00, 00)
        cinemaService.getPrimerStartTime() >> LocalTime.of(10, 0, 0)
        cinemaService.getPrimerEndTime() >> { LocalTime.of(19, 0, 0) }
        def presentMovies = new ArrayList()
        presentMovies.add(MovieTime.builder()
            .time(LocalTime.of(16, 0, 0))
            .movie(movie)
            .build())
        presentMovies.add(MovieTime.builder()
            .time(LocalTime.of(10, 0, 0))
            .movie(movie)
            .build())
        scheduleService.schedule.schedule.clear()
        scheduleService.schedule.schedule.put(date, DaySchedule.builder()
            .date(date)
            .schedule(Map.of(roomNo, RoomSchedule.builder()
                .roomNo(roomNo)
                .movies(presentMovies)
                .build()))
            .build())
    }

}
