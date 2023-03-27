package net.yorksolutions.shift.repositories;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Branch;

public interface BranchRepository extends CrudRepository<Branch, UUID> {
}
