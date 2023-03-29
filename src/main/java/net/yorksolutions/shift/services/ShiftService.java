package net.yorksolutions.shift.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Shift;
import net.yorksolutions.shift.repositories.ProfileRepository;
import net.yorksolutions.shift.repositories.ShiftRepository;

@Service
public class ShiftService {
    private final ShiftRepository repository;
    private final ProfileRepository profileRepository;

    public ShiftService(ShiftRepository repository, ProfileRepository profileRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    public Shift createShift(Shift newShift, UUID token) {
        // todo: check permissions
        final Profile shiftProfile = profileRepository.findById(newShift.getProfile().getId()).orElseThrow();
        newShift.setProfile(shiftProfile);
        return repository.save(newShift);
    }

    public List<List<Shift>> getShifts(LocalDate date) {
        // get start and end of week
        final var dayOfWeek = date.getDayOfWeek().getValue();
        final var weekStart = date.minusDays(dayOfWeek % 7);
        final var weekEnd = date.plusDays(6 - dayOfWeek % 7);

        // get all profiles and shifts within the week
        final Iterable<Profile> profiles = profileRepository.findByOrderByLastName();
        final List<Shift> shifts = repository.findAllByDateBetween(weekStart, weekEnd);

        // build array of shift arrays for each profile
        final List<List<Shift>> shiftList = new ArrayList<>();
        for (Profile p : profiles) {
            final var filtered = shifts.stream().filter(s -> s.getProfile().getId().equals(p.getId()))
                    .collect(Collectors.toList());
            if (filtered.size() > 0) {
                shiftList.add(filtered);
            }
        }
        return shiftList;
    }
}
