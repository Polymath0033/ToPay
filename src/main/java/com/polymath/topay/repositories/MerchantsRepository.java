package com.polymath.topay.repositories;

import com.polymath.topay.models.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 06 Sun Jul, 2025
 */
@Repository
public interface MerchantsRepository extends JpaRepository<Merchants, UUID> {
    Optional<Merchants> findByEmail(String email);
    Merchants findByTeamsEmail(String email);
    @Query("SELECT merchant FROM Merchants merchant WHERE merchant.liveApiKey.id = :apiKeyId OR merchant.testApiKey.id = :apiKeyId")
    Optional<Merchants> findByApiKeyId(@Param("apiKeyId") Long apiKeyId);
}