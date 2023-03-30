package net.yorksolutions.shift.services;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.dto.Credentials;
import net.yorksolutions.shift.models.Account;
import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.repositories.AccountRepository;
import net.yorksolutions.shift.repositories.ProfileRepository;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final AuthService authService;
    private final ProfileRepository profileRepository;

    public AccountService(AccountRepository repository, AuthService authService, ProfileRepository profileRepository) {
        this.repository = repository;
        this.authService = authService;
        this.profileRepository = profileRepository;
    }

    public UUID register(Credentials cred) {
        final Boolean usernameGood = Pattern.matches(".+", cred.username);
        if (!usernameGood) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username cannot be blank");
        }
        final Boolean userFound = repository.findAccountByUsername(cred.username).isPresent();
        if (userFound) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already taken");
        }
        final Account newAccount = new Account();
        newAccount.setUsername(cred.username);
        newAccount.setPassword(cred.password);
        final Account savedAccount = repository.save(newAccount);

        final Profile newProfile = new Profile();
        newProfile.setAccountId(savedAccount.getId());
        newProfile.setFirstName("");
        newProfile.setLastName("");
        newProfile.setAdmin(cred.admin);
        profileRepository.save(newProfile);

        return authService.addToken(savedAccount.getId());
    }

    public UUID checkCredentials(Credentials cred) {
        try {
            final Account foundAccount = repository.findAccountByUsernameAndPassword(cred.username, cred.password)
                    .orElseThrow();
            return authService.addToken(foundAccount.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid username or password");
        }
    }

    public Credentials getAccount(UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Account account = repository.findById(accountId).orElseThrow();
        final Profile profile = profileRepository.findByAccountId(accountId).orElseThrow();
        final var newCred = new Credentials();
        newCred.setFirstName(profile.getFirstName());
        newCred.setLastName(profile.getLastName());
        newCred.setUsername(account.getUsername());
        newCred.setAdmin(profile.isAdmin());
        return newCred;
    }

    public Credentials modAccount(Credentials cred, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Account oldAccount = repository.findById(accountId).orElseThrow();
        final Profile oldProfile = profileRepository.findByAccountId(accountId).orElseThrow();

        if (cred.firstName != null && cred.firstName.length() > 0) {
            oldProfile.setFirstName(cred.firstName);
        }
        if (cred.lastName != null && cred.lastName.length() > 0) {
            oldProfile.setLastName(cred.lastName);
        }
        if (cred.username != null && cred.username.length() > 0) {
            oldAccount.setUsername(cred.username);
        }
        if (cred.password != null && cred.password.length() > 0) {
            oldAccount.setPassword(cred.password);
        }
        profileRepository.save(oldProfile);
        repository.save(oldAccount);

        final var newCred = new Credentials();
        newCred.setFirstName(oldProfile.getFirstName());
        newCred.setLastName(oldProfile.getLastName());
        newCred.setUsername(oldAccount.getUsername());
        return newCred;
    }

    public Iterable<Profile> getProfiles() {
        return profileRepository.findByOrderByLastName();
    }
}
