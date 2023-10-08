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
public class DaySchedule {

  @Builder.Default
  private Map<Integer, RoomSchedule> schedule = new HashMap<>();
  private LocalDate date;

  public RoomSchedule getForRoom(int roomNo) {
    if (!schedule.containsKey(roomNo)) {
      schedule.put(roomNo, RoomSchedule.builder()
          .roomNo(roomNo)
          .build());
    }
    return schedule.get(roomNo);
  }
}
