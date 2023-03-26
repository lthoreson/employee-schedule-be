package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Role;

public interface RoleRepository extends CrudRepository<Role, UUID> {

}
