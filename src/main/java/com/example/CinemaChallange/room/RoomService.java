package com.example.CinemaChallange.room;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomService {

  private Map<Integer, Room> roomMap = new HashMap<>();

  public RoomService() {
    roomMap.put(1, Room.builder()
        .number(1)
        .cleanupTime(Duration.ofMinutes(20))
        .build());
    roomMap.put(2, Room.builder()
        .number(2)
        .cleanupTime(Duration.ofMinutes(30))
        .build());
  }

  public Optional<Room> getById(int id) {
    return Optional.ofNullable(roomMap.get(id));
  }
}
