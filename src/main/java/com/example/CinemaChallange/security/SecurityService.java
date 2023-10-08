package com.example.CinemaChallange.security;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  public boolean canUserAdd(User user) {
    return true;  // Dummy security check
  }
}
