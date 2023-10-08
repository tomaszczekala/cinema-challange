package com.example.CinemaChallange.cinema;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class CinemaService {

  private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);

  private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);

  private static final LocalTime PRIMER_START_TIME = LocalTime.of(17, 0);

  private static final LocalTime PRIMER_END_TIME = LocalTime.of(21, 0);

  public LocalTime getPrimerStartTime() {
    return PRIMER_START_TIME;
  }

  public LocalTime getPrimerEndTime() {
    return PRIMER_END_TIME;
  }

  public LocalTime getOpeningTime() {
    return OPENING_TIME;
  }

  public LocalTime getClosingTime() {
    return CLOSING_TIME;
  }
}
