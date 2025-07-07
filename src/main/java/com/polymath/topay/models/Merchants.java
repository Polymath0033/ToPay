package com.polymath.topay.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Merchants {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Length(min = 5, message = "Name must be at least characters")
    private String name;
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @Length(min = 10, message = "Phone number must be at least 10 characters")
    @Column(unique = true,nullable = false)
    private String phone;
    private String address;

    @Column(nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "merchants",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Teams> teams;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "test_api_key_id")
    private ApiKey testApiKey;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "live_api_key_id")
    private ApiKey liveApiKey;

    @OneToMany(mappedBy = "merchants",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Webhook> webhooks;

}
