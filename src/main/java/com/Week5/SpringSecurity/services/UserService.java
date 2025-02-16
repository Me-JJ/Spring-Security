package com.Week5.SpringSecurity.services;

import com.Week5.SpringSecurity.dto.SignUpDto;
import com.Week5.SpringSecurity.dto.UserDto;
import com.Week5.SpringSecurity.entities.User;
import com.Week5.SpringSecurity.exceptions.ResourceNotFoundException;
import com.Week5.SpringSecurity.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;

        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userRepo.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException(("User not found with username -> "+username)));
    }

    public User getUserById(Long id)
    {
        return userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(("User not found with id ->"+id)));
    }
    public UserDto signUp(SignUpDto signUpDto) {
     Optional<User> user=userRepo.findByEmail(signUpDto.getEmail());

     if(user.isPresent())
     {
         throw new BadCredentialsException("User wit email already exits" + signUpDto.getEmail());
     }

     User newUser= modelMapper.map(signUpDto,User.class);
     newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
     return modelMapper.map(userRepo.save(newUser),UserDto.class);
    }



}
