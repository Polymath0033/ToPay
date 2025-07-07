package com.polymath.topay.repositories;

import com.polymath.topay.models.Teams;
import com.polymath.topay.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 07 Mon Jul, 2025
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Token findByTeamEmail(String email);
    boolean existsByTeamEmail(String email);
}