package net.yorksolutions.shift.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import net.yorksolutions.shift.dto.BranchPreview;
import net.yorksolutions.shift.models.Branch;
import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.BranchRole;
import net.yorksolutions.shift.repositories.BranchRepository;
import net.yorksolutions.shift.repositories.ProfileRepository;

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
        final BranchRole newAdminRole = new BranchRole();
        newAdminRole.setName("admin");
        newAdminRole.setPeople(List.of(myProfile));
        newBranch.setRoles(List.of(newAdminRole));
        // add my profile too
        newBranch.setProfiles(Set.of(myProfile));

        // save changes to repositories for branch and profile
        final Branch savedBranch = repository.save(newBranch);
        profileRepository.save(myProfile);

        return savedBranch;
    }

    // find a person's roles within a branch
    public List<String> getMyRoles(Branch branch, Profile profile) {
        final var myRoles = new ArrayList<String>();
        for (BranchRole r : branch.getRoles()) {
            final List<UUID> profileIds = r.getPeople().stream().map(person -> person.getId())
                    .collect(Collectors.toList());
            if (profileIds.contains(profile.getId())) {
                myRoles.add(r.getName());
            }
        }
        return myRoles;
    }

    public List<BranchPreview> getMyBranches(UUID token) {
        final UUID accountId = authService.checkToken(token);
        final Profile myProfile = profileRepository.findByAccountId(accountId).orElseThrow();
        final Set<Branch> myBranches = Set.of(new Branch()); // myProfile.getBranches();
        return myBranches.stream()
                .map(branch -> new BranchPreview(branch.getId(), branch.getName(), getMyRoles(branch, myProfile)))
                .collect(Collectors.toList());
    }
}
