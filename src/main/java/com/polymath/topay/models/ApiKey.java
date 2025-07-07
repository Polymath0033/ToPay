package com.polymath.topay.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "api_key")
@NoArgsConstructor
@Getter
@Setter
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true,nullable = false)
    private String publicKey;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String secretKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;

    @Column(nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastUsedAt;

    @OneToMany(mappedBy = "apiKey", cascade = CascadeType.ALL)
    private List<ApiKeyUsage> usageHistory;

    public enum Environment {
        TEST, LIVE
    }
}
