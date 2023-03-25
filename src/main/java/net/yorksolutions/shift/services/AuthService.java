package net.yorksolutions.shift.services;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private HashMap<UUID, UUID> tokenMap = new HashMap<>();

    // adds token/userId pair to hash map
    public UUID addToken(UUID id) {
        final var token = UUID.randomUUID();
        tokenMap.put(token, id);
        return token;
    }

    // return user id for token, else null
    public UUID checkToken(UUID token) {
        final UUID accountId = tokenMap.get(token);
        if (accountId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "session expired or invalid");
        }
        return accountId;
    }

    public void deleteToken(UUID token) {
        final UUID accountIdOfDeletedToken = tokenMap.remove(token);
        if (accountIdOfDeletedToken == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid token");
        }
    }
}
