package net.yorksolutions.shift.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.models.Shift;
import net.yorksolutions.shift.services.ShiftService;

@RestController
@CrossOrigin
@RequestMapping("/shift")
public class ShiftController {
    private final ShiftService service;

    public ShiftController(ShiftService service) {
        this.service = service;
    }

    @PostMapping
    public Shift createShift(@RequestBody Shift newShift,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.createShift(newShift, token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<List<Shift>> getShifts(@RequestParam String dateParam) {
        try {
            final var date = LocalDate.parse(dateParam);
            return service.getShifts(date);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
