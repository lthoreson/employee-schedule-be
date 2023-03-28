package net.yorksolutions.shift.services;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.yorksolutions.shift.models.Shift;
import net.yorksolutions.shift.repositories.ShiftRepository;

@Service
public class ShiftService {
    private final ShiftRepository repository;

    public ShiftService(ShiftRepository repository) {
        this.repository = repository;
    }

    public Shift createShift(Shift newShift, UUID token) {
        // todo: check permissions
        return repository.save(newShift);
    }

    public Iterable<Shift> getShifts(LocalDate date) {
        final var dayOfWeek = date.getDayOfWeek().getValue();
        final var weekStart = date.minusDays(dayOfWeek % 7);
        final var weekEnd = date.plusDays(6 - dayOfWeek % 7);

        return repository.findAllByDateBetween(weekStart, weekEnd);
    }
}
