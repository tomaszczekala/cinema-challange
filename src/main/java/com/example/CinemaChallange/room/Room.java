package com.example.CinemaChallange.room;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class Room {
  private int number;
  private Duration cleanupTime;
}
