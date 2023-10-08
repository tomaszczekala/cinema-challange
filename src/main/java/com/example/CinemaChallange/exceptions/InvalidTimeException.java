package com.example.CinemaChallange.exceptions;

public class InvalidTimeException extends IllegalArgumentException {
  public InvalidTimeException(String message) {
    super(message);
  }
}
