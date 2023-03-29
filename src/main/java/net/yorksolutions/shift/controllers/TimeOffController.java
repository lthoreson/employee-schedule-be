package net.yorksolutions.shift.controllers;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.models.TimeOff;
import net.yorksolutions.shift.services.TimeOffService;

@RestController
@CrossOrigin
@RequestMapping("/timeOff")
public class TimeOffController {
    private final TimeOffService service;

    public TimeOffController(TimeOffService service) {
        this.service = service;
    }

    @PostMapping
    public TimeOff requesTimeOff(@RequestBody TimeOff newRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.requestTimeOff(newRequest, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
