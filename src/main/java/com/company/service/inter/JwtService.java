package com.company.service.inter;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);
    Long extractUserId(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
