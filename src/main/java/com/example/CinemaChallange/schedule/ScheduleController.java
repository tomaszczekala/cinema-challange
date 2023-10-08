package com.example.CinemaChallange.schedule;

import com.example.CinemaChallange.exceptions.CollidingWithOtherMovieException;
import com.example.CinemaChallange.exceptions.ForbiddenActionException;
import com.example.CinemaChallange.exceptions.InvalidMovieException;
import com.example.CinemaChallange.exceptions.InvalidRoomException;
import com.example.CinemaChallange.exceptions.InvalidTimeException;
import com.example.CinemaChallange.security.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;

  @GetMapping(value = "/{date}")
  public ResponseEntity<DaySchedule> getScheduleForDay(@PathVariable("date") LocalDate date) {
    return ResponseEntity.of(scheduleService.getDaySchedule(date));
  }

  @GetMapping(value = "/{date}/{roomNo}")
  public ResponseEntity<RoomSchedule> getScheduleForDayAndRoom(@PathVariable("date") LocalDate date,
                                                               @PathVariable("roomNo") int roomNo) {
    return ResponseEntity.of(scheduleService.getDayRoomSchedule(date, roomNo));
  }

  @PutMapping(value = "/{date}/{roomNo}")
  public ResponseEntity<String> addMovie(@PathVariable("date") LocalDate date,
                                         @PathVariable("roomNo") int roomNo,
                                         @RequestBody MovieTime movieTime) {
    try {
      scheduleService.addMovieToSchedule(date, roomNo, movieTime.getMovie().getId(), movieTime.getTime(), User.builder().build());
      return ResponseEntity.ok().build();
    } catch (InvalidMovieException | InvalidRoomException | InvalidTimeException |
             CollidingWithOtherMovieException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    } catch (ForbiddenActionException ex) {
      return ResponseEntity.status(403).build();
    }
  }
}
