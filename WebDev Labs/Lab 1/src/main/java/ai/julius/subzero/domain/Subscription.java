package ai.julius.subzero.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Core entity for managing user digital subscriptions.
 */
@Entity
@Table(name = "subscriptions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency; // e.g., USD, EUR, BYN

    @Column(nullable = false)
    private String billingCycle; // e.g., MONTHLY, YEARLY

    @Column(nullable = false)
    private LocalDate nextBillingDate;

    private String category;
    
    private boolean active = true;

    private String providerUrl;
}
