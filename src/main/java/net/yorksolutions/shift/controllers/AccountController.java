package net.yorksolutions.shift.controllers;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.dto.Credentials;
import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.services.AccountService;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UUID register(@RequestBody Credentials cred) {
        try {
            return service.register(cred);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public UUID login(@RequestBody Credentials cred) {
        try {
            return service.checkCredentials(cred);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public Credentials getAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.getAccount(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/profile")
    public Credentials modAccount(@RequestBody Credentials cred,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.modAccount(cred, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/profiles")
    public Iterable<Profile> getProfiles() {
        try {
            return service.getProfiles();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
