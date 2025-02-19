package com.Week5.SpringSecurity.controllers;

import com.Week5.SpringSecurity.dto.LoginDto;
import com.Week5.SpringSecurity.dto.LoginResponseDto;
import com.Week5.SpringSecurity.dto.SignUpDto;
import com.Week5.SpringSecurity.dto.UserDto;
import com.Week5.SpringSecurity.services.AuthService;
import com.Week5.SpringSecurity.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final AuthService authService;
    private final UserService userService;
    private final String REFRESH_TOKEN = "refreshToken";


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto)
    {
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response)
    {
        LoginResponseDto loginResponseDto =authService.login(loginDto);

        Cookie cookie = new Cookie(REFRESH_TOKEN,loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true); // no one can access the cookie

        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest httpServletRequest)
    {
        String refreshToken = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh Token not found inside cookie"));

        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }
}
