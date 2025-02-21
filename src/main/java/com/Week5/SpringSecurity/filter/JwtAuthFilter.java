package com.Week5.SpringSecurity.filter;

import com.Week5.SpringSecurity.entities.User;
import com.Week5.SpringSecurity.services.JwtService;
import com.Week5.SpringSecurity.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter
{

    private final JwtService jwtService;
    private final UserService userService;


    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try {
//            log.info("fetching token from headers");

            final String reqToken = request.getHeader("Authorization");

//            log.info("token from headers -> {}", reqToken);

            if (reqToken == null || !reqToken.startsWith("Bearer ")) {
                log.info("no token found in headers -> returned");

                filterChain.doFilter(request, response);
                return;
            }

            String token = reqToken.split("Bearer ")[1]; // Beared dsfjsdfjbdjhajkdfnkjfda =>> split ["","dsfagfvevfe"]

//            System.out.println("TOKEN -> " + token);

            log.info("fetching user form jwtService");
            Long userId = jwtService.getUserIdFromToken(token);


            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                log.info("if user not null");
                User user = userService.getUserById(userId);

                user.setPassword("");
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request) // useful info if we want to rate limit or other stuff
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception ex)
        {
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }

    }
}
