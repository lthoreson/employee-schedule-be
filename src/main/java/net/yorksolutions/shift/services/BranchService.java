package net.yorksolutions.shift.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import net.yorksolutions.shift.models.Branch;
import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Role;
import net.yorksolutions.shift.repositories.BranchRepository;
import net.yorksolutions.shift.repositories.ProfileRepository;
import net.yorksolutions.shift.repositories.RoleRepository;

@Service
public class BranchService {
    private final BranchRepository repository;
    private final AuthService authService;
    private final ProfileRepository profileRepository;

    public BranchService(BranchRepository repository, AuthService authService, ProfileRepository profileRepository) {
        this.repository = repository;
        this.authService = authService;
        this.profileRepository = profileRepository;
    }

    public Branch createBranch(UUID token, String name) {
        // get account and profile from token
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();

        // create and name new branch
        final Branch newBranch = new Branch();
        newBranch.setName(name);

        // assign an admin role in the branch
        final Role newAdminRole = new Role();
        newAdminRole.setName("admin");
        newAdminRole.setPeople(List.of(myProfile));
        newBranch.setRoles(List.of(newAdminRole));
        // add my profile too
        newBranch.setProfiles(List.of(myProfile));

        // save changes to repositories for branch and profile
        final Branch savedBranch = repository.save(newBranch);
        profileRepository.save(myProfile);

        return savedBranch;
    }

    public List<Branch> getMyBranches(UUID token) {
        final UUID accountId = authService.checkToken(token);

        return new ArrayList<Branch>();
    }
}
