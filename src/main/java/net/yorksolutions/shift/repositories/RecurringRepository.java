package net.yorksolutions.shift.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Recurring;

@Repository
public interface RecurringRepository extends CrudRepository<Recurring, UUID> {
    List<Recurring> findAllByProfile(Profile profile);
}
