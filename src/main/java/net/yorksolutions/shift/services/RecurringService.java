package net.yorksolutions.shift.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Recurring;
import net.yorksolutions.shift.repositories.ProfileRepository;
import net.yorksolutions.shift.repositories.RecurringRepository;

@Service
public class RecurringService {
    private final RecurringRepository repository;
    private final AuthService authService;
    private final ProfileRepository profileRepository;

    public RecurringService(RecurringRepository repository, AuthService authService,
            ProfileRepository profileRepository) {
        this.repository = repository;
        this.authService = authService;
        this.profileRepository = profileRepository;
    }

    public Recurring creatRecurring(Recurring recurring, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin status
        final Recurring newRecurring = new Recurring();
        newRecurring.setProfile(recurring.getProfile());
        newRecurring.setStartTime(recurring.getStartTime());
        newRecurring.setEndTime(recurring.getEndTime());
        newRecurring.setWeekday(recurring.getWeekday());
        return repository.save(newRecurring);
    }

    public Recurring editRecurring(Recurring recurring, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin status
        final Recurring oldRecurring = repository.findById(recurring.getId()).orElseThrow();
        if (recurring.getProfile() != null) {
            final Profile assignee = profileRepository.findById(recurring.getProfile().getId()).orElseThrow();
            oldRecurring.setProfile(assignee);
        }
        oldRecurring.setStartTime(recurring.getStartTime());
        oldRecurring.setEndTime(recurring.getEndTime());
        oldRecurring.setWeekday(recurring.getWeekday());
        return repository.save(oldRecurring);
    }

    public void deleteRecurring(Recurring recurring, UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin status
        repository.deleteById(recurring.getId());
    }

    public List<List<Recurring>> getAllRecurrings(UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        // todo: check admin status

        // get all profiles
        final Iterable<Profile> profiles = profileRepository.findByAdminFalseOrderByLastName();

        // build array of recurring arrays for each profile, and unassigned
        final List<List<Recurring>> recurringsList = new ArrayList<>();
        final List<Recurring> unassigned = repository.findAllByProfile(null);
        if (unassigned.size() > 0) {
            recurringsList.add(unassigned);
        }
        for (Profile p : profiles) {
            final List<Recurring> filtered = repository.findAllByProfile(p);
            if (filtered.size() > 0) {
                recurringsList.add(filtered);
            }
        }
        return recurringsList;
    }

}
