package com.Week5.SpringSecurity.repositories;

import com.Week5.SpringSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long>
{
    Optional<User> findByEmail(String userName);

}
