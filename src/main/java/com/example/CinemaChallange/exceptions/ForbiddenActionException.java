package com.example.CinemaChallange.exceptions;

public class ForbiddenActionException extends RuntimeException {
  public ForbiddenActionException(String message) {
    super(message);
  }
}
