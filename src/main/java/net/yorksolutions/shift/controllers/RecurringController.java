package net.yorksolutions.shift.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.models.Recurring;
import net.yorksolutions.shift.services.RecurringService;

@RestController
@CrossOrigin
@RequestMapping("/recurring")
public class RecurringController {
    private final RecurringService service;

    public RecurringController(RecurringService service) {
        this.service = service;
    }

    @PostMapping
    public Recurring createRecurring(@RequestBody Recurring recurring,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.creatRecurring(recurring, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping
    public Recurring editRecurring(@RequestBody Recurring recurring,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.editRecurring(recurring, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping
    public void deletRecurring(@RequestBody Recurring recurring,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            service.deleteRecurring(recurring, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<List<Recurring>> getAllRecurrings(@RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.getAllRecurrings(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
