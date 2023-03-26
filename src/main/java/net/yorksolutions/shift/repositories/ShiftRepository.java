package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Shift;

public interface ShiftRepository extends CrudRepository<Shift, UUID> {

}
