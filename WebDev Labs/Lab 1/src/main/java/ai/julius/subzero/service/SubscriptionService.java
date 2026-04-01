package ai.julius.subzero.service;

import ai.julius.subzero.domain.Subscription;
import ai.julius.subzero.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public List<Subscription> getAll() {
        return repository.findAll();
    }

    public List<Subscription> getActive() {
        return repository.findByActive(true);
    }

    public Optional<Subscription> getById(Long id) {
        return repository.findById(id);
    }

    public Subscription save(Subscription subscription) {
        return repository.save(subscription);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
