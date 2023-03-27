package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.BranchRole;

public interface BranchRoleRepository extends CrudRepository<BranchRole, UUID> {

}
