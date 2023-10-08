package com.example.CinemaChallange.schedule;

import com.example.CinemaChallange.catalogue.Movie;
import com.example.CinemaChallange.catalogue.MovieService;
import com.example.CinemaChallange.cinema.CinemaService;
import com.example.CinemaChallange.exceptions.CollidingWithOtherMovieException;
import com.example.CinemaChallange.exceptions.ForbiddenActionException;
import com.example.CinemaChallange.exceptions.InvalidMovieException;
import com.example.CinemaChallange.exceptions.InvalidRoomException;
import com.example.CinemaChallange.exceptions.InvalidTimeException;
import com.example.CinemaChallange.room.Room;
import com.example.CinemaChallange.room.RoomService;
import com.example.CinemaChallange.security.SecurityService;
import com.example.CinemaChallange.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final MovieService movieService;

  private final CinemaService cinemaService;

  private final RoomService roomService;

  private final SecurityService securityService;

  private final Schedule schedule = new Schedule();

  public Optional<DaySchedule> getDaySchedule(LocalDate date) {
    return Optional.ofNullable(schedule.getForDay(date));
  }

  public Optional<RoomSchedule> getDayRoomSchedule(LocalDate date, int roomNo) {
    return Optional.ofNullable(schedule.getForDay(date))
        .map(daySchedule -> daySchedule.getSchedule().get(roomNo));
  }

  public void addMovieToSchedule(LocalDate date, int roomNo, int movieId, LocalTime startTime, User user) {
    if (!securityService.canUserAdd(user)) {
      throw new ForbiddenActionException("User " + user.getName() + " is not allowed to edit schedule");
    }
    Movie movie = movieService.getMovieById(movieId)
        .orElseThrow(() -> new InvalidMovieException("No movie with id " + movieId));
    Room room = roomService.getById(roomNo).orElseThrow(() -> new InvalidRoomException("No room with id " + roomNo));
    DaySchedule daySchedule = schedule.getForDay(date);
    RoomSchedule roomSchedule = daySchedule.getForRoom(roomNo);
    LocalTime endTimeWithoutCleanup = startTime.plus(movie.getDuration());
    LocalTime endTimeWithCleanup = endTimeWithoutCleanup.plus(room.getCleanupTime());
    if (startTime.isBefore(cinemaService.getOpeningTime()) || endTimeWithCleanup.isAfter(cinemaService.getClosingTime())) {
      throw new InvalidTimeException("Movie must be in cinema working hours");
    }

    if (movie.isPrimer() && (startTime.isBefore(cinemaService.getPrimerStartTime())
        || endTimeWithoutCleanup.isAfter(cinemaService.getPrimerEndTime()))) {
      throw new InvalidTimeException("Primer must be in primer hours");
    }

    if (roomSchedule.getMovies().stream()
        .anyMatch(existingMovie -> moviesCollide(
            startTime, movie.getDuration(),
            existingMovie.getTime(), existingMovie.getMovie().getDuration(),
            room.getCleanupTime()))) {
      throw new CollidingWithOtherMovieException("Movie can't collide with other movies");
    }
    roomSchedule.getMovies().add(MovieTime.builder()
        .movie(movie)
        .time(startTime)
        .build());
  }

  private boolean moviesCollide(LocalTime start1,
                                Duration movie1Duration,
                                LocalTime start2,
                                Duration movie2Duration,
                                Duration roomCleanupDuration) {
    LocalTime end1 = start1.plus(movie1Duration).plus(roomCleanupDuration);
    LocalTime end2 = start2.plus(movie2Duration).plus(roomCleanupDuration);
    return (start1.isBefore(end2) && start2.isBefore(end1));
  }
}
