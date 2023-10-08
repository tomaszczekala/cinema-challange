package com.example.CinemaChallange.exceptions;

public class InvalidMovieException extends IllegalArgumentException {
  public InvalidMovieException(String message) {
    super(message);
  }
}
