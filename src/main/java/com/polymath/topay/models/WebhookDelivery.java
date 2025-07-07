package com.polymath.topay.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "webhook_delivery")
public class WebhookDelivery {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "webhook_id",nullable = false)
    private Webhook webhook;
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String payload;
    @Column(nullable = false)
    private int attempts = 0;
    @Column(nullable = false)
    private int maxAttempts = 3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    private Integer statusCode;
    private String responseBody;
    private String errorMessage;

    private long createdAt;
    private long updatedAt;
    private LocalDateTime lastAttemptAt;
    private LocalDateTime nextAttemptAt;

    public enum DeliveryStatus {
        PENDING, DELIVERED, FAILED,EXPIRED
    }

}