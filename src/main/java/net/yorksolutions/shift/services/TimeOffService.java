package net.yorksolutions.shift.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.TimeOff;
import net.yorksolutions.shift.repositories.ProfileRepository;
import net.yorksolutions.shift.repositories.TimeOffRepository;

@Service
public class TimeOffService {
    private final TimeOffRepository repository;
    private final ProfileRepository profileRepository;
    private final AuthService authService;

    public TimeOffService(TimeOffRepository repository, ProfileRepository profileRepository, AuthService authService) {
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.authService = authService;
    }

    public TimeOff requestTimeOff(LocalDate starDate, LocalDate endDate, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        final TimeOff newTimeOffRequest = new TimeOff();
        newTimeOffRequest.setStartDate(starDate);
        newTimeOffRequest.setEndDate(endDate);
        newTimeOffRequest.setProfile(myProfile);
        return repository.save(newTimeOffRequest);
    }
}
