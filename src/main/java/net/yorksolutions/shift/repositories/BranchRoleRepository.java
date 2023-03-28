package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.BranchRole;

@Repository
public interface BranchRoleRepository extends CrudRepository<BranchRole, UUID> {

}
