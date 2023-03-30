package net.yorksolutions.shift.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Recurring;
import net.yorksolutions.shift.models.Shift;
import net.yorksolutions.shift.repositories.ProfileRepository;
import net.yorksolutions.shift.repositories.RecurringRepository;
import net.yorksolutions.shift.repositories.ShiftRepository;
import net.yorksolutions.shift.repositories.TimeOffRepository;

@Service
public class ShiftService {
    private final ShiftRepository repository;
    private final ProfileRepository profileRepository;
    private final RecurringRepository recurringRepository;
    private final TimeOffRepository timeOffRepository;
    private final AuthService authService;

    public ShiftService(ShiftRepository repository, ProfileRepository profileRepository,
            RecurringRepository recurringRepository, TimeOffRepository timeOffRepository, AuthService authService) {
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.recurringRepository = recurringRepository;
        this.timeOffRepository = timeOffRepository;
        this.authService = authService;
    }

    public Shift createShift(Shift newShift, UUID token) {
        // todo: check admin permissions
        if (newShift.getProfile() != null) {
            final Profile shiftProfile = profileRepository.findById(newShift.getProfile().getId()).orElseThrow();
            newShift.setProfile(shiftProfile);
        } else {
            newShift.setProfile(null);
        }
        return repository.save(newShift);
    }

    public List<List<Shift>> getShifts(LocalDate date) {
        // get start and end of week Java version (1-7 = mon-sun)
        final var dayOfWeek = date.getDayOfWeek().getValue();
        final var sunday = date.minusDays(dayOfWeek % 7);
        final var saturday = date.plusDays(6 - dayOfWeek % 7);

        // get all profiles
        final Iterable<Profile> profiles = profileRepository.findByOrderByLastName();

        // build array of shift arrays for each profile, and one for available shifts
        final List<List<Shift>> shiftList = new ArrayList<>();
        final List<Shift> availableShiftsNew = repository.findAllByProfileAndDateBetween(null, sunday, saturday);
        if (availableShiftsNew.size() > 0) {
            shiftList.add(availableShiftsNew);
        }
        for (Profile p : profiles) {
            final List<Shift> filtered = repository.findAllByProfileAndDateBetween(p, sunday, saturday);
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

    public List<List<Shift>> generateWeek(LocalDate date, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin

        // convert current day of the week to JS version (0-6 = sun-sat)
        final var dayOfWeekJs = date.plusDays(1).getDayOfWeek().getValue() - 1;

        // get all recurrings
        final Iterable<Recurring> allRecurrings = recurringRepository.findAll();

        // create lists of shifts from recurrings
        final List<Shift> listOfShiftsToBeSaved = new ArrayList<>();
        for (Recurring r : allRecurrings) {
            final var newShift = new Shift();
            newShift.setDate(date.plusDays(r.getWeekday() - dayOfWeekJs));
            newShift.setStartTime(r.getStartTime());
            newShift.setEndTime(r.getEndTime());
            if (r.getProfile() != null) {
                final var profile = profileRepository.findById(r.getProfile().getId()).orElseThrow();
                // check for time off conflicts before assigning
                final var conflicts = timeOffRepository
                        .findAllByProfileAndStartDateBeforeAndEndDateAfterAndApprovalIsNotNull(profile,
                                newShift.getDate().minusDays(1), newShift.getDate().plusDays(1));
                if (conflicts.size() == 0) {
                    newShift.setProfile(profile);
                }
            }
            listOfShiftsToBeSaved.add(newShift);
        }
        for (Shift s : listOfShiftsToBeSaved) {
            repository.save(s);
        }

        return getShifts(date);
    }
}
