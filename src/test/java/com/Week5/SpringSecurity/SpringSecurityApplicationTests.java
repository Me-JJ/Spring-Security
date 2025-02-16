package com.Week5.SpringSecurity;

import com.Week5.SpringSecurity.entities.User;
import com.Week5.SpringSecurity.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityApplicationTests
{
	@Autowired
	private JwtService jwtService;


    @Test
	void contextLoads()
	{
//		User user = new User(4124L,"jat@gmail.com","1234");
//
//		System.out.println("USER --> "+ user.toString());
//		String token = jwtService.generateToken(user);
//		System.out.println("TOKEN------> "+token);
//
//
//		System.out.println("USER id from Token ----> "+jwtService.getUserIdFromToken(token));


	}

}
