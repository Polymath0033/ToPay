package com.polymath.topay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "webhooks")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Webhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id",nullable = false)
    private Merchants merchants;
    @Column(nullable = false)
    private String secret;

    @CreationTimestamp
    @Column(updatable = false)
    private long createdAt;

    @UpdateTimestamp
    private long updatedAt;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "webhook_events",joinColumns = @JoinColumn(name = "webhook_id"))
    @Column(name = "event_type")
    private List<WebhookEventType> subscribedEvents;

    @OneToMany(mappedBy = "webhook", cascade = CascadeType.ALL)
    private List<WebhookDelivery> deliveries;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;

    public enum WebhookEventType {
        PAYMENT_SUCCESSFUL,
        PAYMENT_FAILED,
        PAYMENT_PENDING,
        PAYMENT_CANCELLED,
        REFUND_SUCCESSFUL,
        REFUND_FAILED,
        CHARGEBACK_INITIATED,
        SUBSCRIPTION_CREATED,
        SUBSCRIPTION_CANCELLED
    }

    public enum Environment {
        TEST, LIVE
    }

}
