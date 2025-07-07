package com.polymath.topay.repositories;

import com.polymath.topay.models.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Yusuf Olosan
 * @role software engineer
 * @createdOn 06 Sun Jul, 2025
 */
@Repository
public interface WebhookRepository extends JpaRepository<Webhook, Long> {

}