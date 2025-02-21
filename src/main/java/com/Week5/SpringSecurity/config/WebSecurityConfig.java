package com.Week5.SpringSecurity.config;

import com.Week5.SpringSecurity.filter.JwtAuthFilter;
import com.Week5.SpringSecurity.handler.OAuth2SuccessHandler;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.Week5.SpringSecurity.entities.enums.Permission.*;
import static com.Week5.SpringSecurity.entities.enums.Role.ADMIN;
import static com.Week5.SpringSecurity.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig
{
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final String[] publicRoutes = {"/home.html", "/error", "/auth/**"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(publicRoutes).permitAll()
                            .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/posts/**").hasAnyRole(ADMIN.name(), CREATOR.name())
                            .requestMatchers(HttpMethod.POST,"/posts/**")
                            .hasAnyAuthority(POST_CREATE.name())
                            .requestMatchers(HttpMethod.GET,"/posts/**")
                            .hasAnyAuthority(POST_VIEW.name())
                            .requestMatchers(HttpMethod.PUT,"/posts/**")
                            .hasAnyAuthority(POST_UPDATE.name())
                            .requestMatchers(HttpMethod.DELETE,"/posts/**").hasAnyAuthority(POST_DELETE.name())
                            .anyRequest().authenticated())
                    .csrf(csrfConfig -> csrfConfig.disable()) // disable csrf tag
                    .sessionManagement(sesConfig -> sesConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no session stored -> used in cased of JWT authentication
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .oauth2Login(oAuthConfig -> oAuthConfig
                            .failureUrl("/login?error=true")
                            .successHandler(oAuth2SuccessHandler)
                    );
//                .formLogin(Customizer.withDefaults());

            return httpSecurity.build();
    }


//    @Bean
//    UserDetailsService InMemoryUser()
//    {
//        UserDetails normalUser = User.withUsername("jatin")
//                .password(passwordEncoder().encode("pass"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser= User.withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser,adminUser);
//    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
