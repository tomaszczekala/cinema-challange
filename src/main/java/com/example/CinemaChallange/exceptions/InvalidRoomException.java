package com.example.CinemaChallange.exceptions;

public class InvalidRoomException extends IllegalArgumentException {
  public InvalidRoomException(String message) {
    super(message);
  }
}
