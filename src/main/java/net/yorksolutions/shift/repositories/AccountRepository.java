package net.yorksolutions.shift.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    Optional<Account> findAccountByUsername(String username);

    Optional<Account> findAccountByUsernameAndPassword(String username, String password);
}
