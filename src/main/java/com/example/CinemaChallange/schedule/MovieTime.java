package com.example.CinemaChallange.schedule;

import com.example.CinemaChallange.catalogue.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieTime {
  private LocalTime time;
  private Movie movie;
}
