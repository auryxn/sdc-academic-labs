package ai.julius.subzero.repository;

import ai.julius.subzero.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByActive(boolean active);
    List<Subscription> findByCategory(String category);
}
