package net.yorksolutions.shift.services;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.dto.Credentials;
import net.yorksolutions.shift.models.Account;
import net.yorksolutions.shift.models.Branch;
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
        newProfile.setBranches(new ArrayList<Branch>());
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

    public Account getAccount(UUID token) {
        final UUID accountId = authService.checkToken(token);
        return repository.findById(accountId).orElseThrow();
    }
}
