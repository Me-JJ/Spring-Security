package com.Week5.SpringSecurity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
public class LoginDto
{
    private String email,password;
}
