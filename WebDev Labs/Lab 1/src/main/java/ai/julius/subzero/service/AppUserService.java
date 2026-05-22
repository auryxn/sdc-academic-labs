package ai.julius.subzero.service;

import ai.julius.subzero.domain.AppUser;
import ai.julius.subzero.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    public Page<AppUser> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<AppUser> getById(Long id) {
        return repository.findById(id);
    }

    public AppUser save(AppUser user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
