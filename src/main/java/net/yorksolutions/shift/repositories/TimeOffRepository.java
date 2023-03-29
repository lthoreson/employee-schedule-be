package net.yorksolutions.shift.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.TimeOff;

@Repository
public interface TimeOffRepository extends CrudRepository<TimeOff, UUID> {
    List<TimeOff> findAllByStartDateAfterAndEndDateBefore(
            LocalDate startDate,
            LocalDate endDate);
}
