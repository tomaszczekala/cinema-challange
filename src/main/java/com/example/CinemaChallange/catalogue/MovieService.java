package com.example.CinemaChallange.catalogue;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieService {

  private final Map<Integer, Movie> movieCatalogue = new HashMap<>();

  public MovieService() {
    movieCatalogue.put(1, Movie.builder()
        .name("MovieName1")
        .duration(Duration.of(120, ChronoUnit.MINUTES))
        .is3d(false)
        .isPrimer(true)
        .build());
    movieCatalogue.put(2, Movie.builder()
        .name("MovieName2")
        .duration(Duration.of(140, ChronoUnit.MINUTES))
        .is3d(true)
        .isPrimer(false)
        .build());
  }

  public Optional<Movie> getMovieById(int id) {
    return Optional.ofNullable(movieCatalogue.get(id));
  }

  public List<Movie> getAll() {
    return movieCatalogue.values().stream().toList();
  }
}
