package com.example.CinemaChallange.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
  @Builder.Default
  private Map<LocalDate, DaySchedule> schedule = new HashMap<>();

  public DaySchedule getForDay(LocalDate date) {
    if (!schedule.containsKey(date)) {
      schedule.put(date, DaySchedule.builder()
          .date(date)
          .build());
    }
    return schedule.get(date);
  }
}
