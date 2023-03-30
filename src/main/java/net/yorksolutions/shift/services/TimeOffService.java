package net.yorksolutions.shift.services;

import java.time.LocalDate;
import java.util.List;
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

    public TimeOff requestTimeOff(TimeOff newRequest, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        final TimeOff newTimeOffRequest = new TimeOff();
        newTimeOffRequest.setStartDate(newRequest.getStartDate());
        newTimeOffRequest.setEndDate(newRequest.getEndDate());
        newTimeOffRequest.setProfile(myProfile);
        newTimeOffRequest.setApproval(null);
        return repository.save(newTimeOffRequest);
    }

    public List<TimeOff> getPendingTimeOff() {
        return repository.findAllByEndDateAfter(LocalDate.now().minusDays(1));
    }

    public TimeOff approveOrDenyTimeOff(TimeOff timeOff, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin
        final TimeOff oldTimeOff = repository.findById(timeOff.getId()).orElseThrow();
        if (timeOff.getProfile() == null) {
            repository.deleteById(oldTimeOff.getId());
            return oldTimeOff;
        }
        oldTimeOff.setApproval(myProfile);
        return repository.save(oldTimeOff);
    }
}
