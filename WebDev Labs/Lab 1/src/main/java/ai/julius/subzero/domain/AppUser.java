package ai.julius.subzero.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * User entity for managing account owners of subscriptions.
 */
@Entity
@Table(name = "app_users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private boolean active = true;
}
