package com.polymath.topay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "api_key_usage")
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyUsage {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id")
    private ApiKey apiKey;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private int statusCode;

    @CreationTimestamp
    @Column(updatable = false)
    private long accessedAt;

    private String userAgent;

    private String requestId;

}