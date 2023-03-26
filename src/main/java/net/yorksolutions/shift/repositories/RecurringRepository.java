package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Recurring;

public interface RecurringRepository extends CrudRepository<Recurring, UUID> {

}
