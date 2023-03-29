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
    private final AuthService authService;

    public ShiftService(ShiftRepository repository, ProfileRepository profileRepository, AuthService authService) {
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.authService = authService;
    }

    public Shift createShift(Shift newShift, UUID token) {
        // todo: check admin permissions
        if (newShift.getProfile().getId() != null) {
            final Profile shiftProfile = profileRepository.findById(newShift.getProfile().getId()).orElseThrow();
            newShift.setProfile(shiftProfile);
        } else {
            newShift.setProfile(null);
        }
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

        // build array of shift arrays for each profile, and one for available shifts
        final List<List<Shift>> shiftList = new ArrayList<>();
        final List<Shift> availableShiftsNew = repository.findAllByProfileAndDateBetween(null, weekStart, weekEnd);
        if (availableShiftsNew.size() > 0) {
            shiftList.add(availableShiftsNew);
        }
        for (Profile p : profiles) {
            final List<Shift> filtered = repository.findAllByProfileAndDateBetween(p, weekStart, weekEnd);
            if (filtered.size() > 0) {
                shiftList.add(filtered);
            }
        }

        return shiftList;
    }

    public Shift surrenderShift(Shift shift, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        final Shift myShift = repository.findById(shift.getId()).orElseThrow();
        if (myProfile.equals(myShift.getProfile())) {
            myShift.setProfile(null);
        }
        return repository.save(myShift);
    }

    public Shift claimShift(Shift shift, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        final Shift myShift = repository.findById(shift.getId()).orElseThrow();
        if (myShift.getProfile() == null) {
            myShift.setProfile(myProfile);
        }
        return repository.save(myShift);
    }
}
