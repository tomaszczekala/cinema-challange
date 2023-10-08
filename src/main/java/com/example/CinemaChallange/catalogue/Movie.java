package com.example.CinemaChallange.catalogue;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Builder
@Data
public class Movie {
  private int id;
  private String name;
  private Duration duration;
  private boolean is3d;
  private boolean isPrimer;
}
