package net.yorksolutions.shift.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Profile;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
    Optional<Profile> findByAccountId(UUID accountId);
}
