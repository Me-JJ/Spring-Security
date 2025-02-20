package com.Week5.SpringSecurity.handler;

import com.Week5.SpringSecurity.entities.User;
import com.Week5.SpringSecurity.services.JwtService;
import com.Week5.SpringSecurity.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        log.info("oauth user name-> {} , email -> {} ",oAuth2User.getAttributes().get("name") , oAuth2User.getAttribute("email"));

        String email=oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);
        //if user not found in our DB we save the user
        if(user == null)
        {
            User newUser  = User.builder()
                    .name(oAuth2User.getAttribute("name"))
                    .email(email)
                    .build();

//            log.info("user created or found -> {}",user);
            user = userService.save(newUser);
        }

        //create AT & RT from user
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);

        // add RT to cookie

        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


         //transfer AT to client

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

        getRedirectStrategy().sendRedirect(request,response,frontEndUrl);
    }
}
