package com.Week5.SpringSecurity.controllers;

import com.Week5.SpringSecurity.dto.LoginDto;
import com.Week5.SpringSecurity.dto.SignUpDto;
import com.Week5.SpringSecurity.dto.UserDto;
import com.Week5.SpringSecurity.services.AuthService;
import com.Week5.SpringSecurity.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final AuthService authService;
    private final UserService userService;



    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto)
    {
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response)
    {
        String token =authService.signIn(loginDto);

        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }
}
