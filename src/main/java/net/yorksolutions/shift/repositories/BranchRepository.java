package net.yorksolutions.shift.repositories;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.Branch;

@Repository
public interface BranchRepository extends CrudRepository<Branch, UUID> {
}
