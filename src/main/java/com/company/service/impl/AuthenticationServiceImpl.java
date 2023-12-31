package com.company.service.impl;

import com.company.model.dao.entities.Role;
import com.company.model.dao.entities.Token;
import com.company.model.dao.entities.User;
import com.company.model.dao.repository.TokenRepository;
import com.company.model.dao.repository.UserRepository;
import com.company.model.dto.request.SignUpRequest;
import com.company.model.dto.request.SigninRequest;
import com.company.model.dto.response.JwtAuthenticationResponse;
import com.company.service.inter.AuthenticationService;
import com.company.service.inter.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("${refresh.token.expires.in}")
    Long expireSeconds;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (request != null &&
                request.getPassword() != null &&
                request.getConfirmPassword() != null &&
                !request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        Token token = new Token();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiryDate(Date.from(Instant.now().plusMillis(expireSeconds)));
        tokenRepository.save(token);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        if (request == null)
            return null;
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return null;
        }
        Token foundedToken = tokenRepository.findByUserId(user.getId());
        String token = foundedToken.getToken();
        try {
            if (jwtService.isTokenValid(token, user)) {
                var jwt = jwtService.generateToken(user);
                return JwtAuthenticationResponse.builder().token(jwt).build();
            }
        } catch (Exception e) {
            JwtAuthenticationResponse refreshToken = refresh(request);
            return JwtAuthenticationResponse.builder().token(refreshToken.getToken()).build();
        }
        return null;
    }



    @Override
    public JwtAuthenticationResponse refresh(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        var jwt = jwtService.generateToken(user);
        Token token = tokenRepository.findByUserId(user.getId());
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        tokenRepository.save(token);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
