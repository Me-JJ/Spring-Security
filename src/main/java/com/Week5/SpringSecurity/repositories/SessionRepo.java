package com.Week5.SpringSecurity.repositories;

import com.Week5.SpringSecurity.entities.SessionEntity;
import com.Week5.SpringSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<SessionEntity,Long> {
    List<SessionEntity> findByUser(User user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
