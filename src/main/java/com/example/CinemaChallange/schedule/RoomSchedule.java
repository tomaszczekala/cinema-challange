package com.example.CinemaChallange.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSchedule {
  private Integer roomNo;
  @Builder.Default
  private List<MovieTime> movies = new ArrayList<>();
}
