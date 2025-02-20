package com.Week5.SpringSecurity.dto;

import com.Week5.SpringSecurity.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto
{
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
}
