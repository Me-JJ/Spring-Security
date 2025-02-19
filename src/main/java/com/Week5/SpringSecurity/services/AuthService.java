package com.Week5.SpringSecurity.services;

import com.Week5.SpringSecurity.dto.LoginDto;
import com.Week5.SpringSecurity.dto.LoginResponseDto;
import com.Week5.SpringSecurity.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public LoginResponseDto login(LoginDto loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(user.getId(),accessToken,refreshToken);

    }

    public LoginResponseDto refreshToken(String refreshToken)
    {
        Long userId=jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(userId,accessToken,refreshToken);
    }
}
