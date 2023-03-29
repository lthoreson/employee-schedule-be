package net.yorksolutions.shift.repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.Profile;
import net.yorksolutions.shift.models.Shift;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, UUID> {
        List<Shift> findAllByDate(LocalDate date);

        List<Shift> findAllByDateBetween(
                        LocalDate startDate,
                        LocalDate endDate);

        List<Shift> findAllByProfileAndDateBetween(Profile profile, LocalDate startDate,
                        LocalDate endDate);

        // @Query(value = "SELECT s FROM shift s WHERE s.profile_id=?3 AND s.date
        // BETWEEN ?1 AND ?2", nativeQuery = true)
        // List<Shift> findAllByCustom(LocalDate startDate, LocalDate endDate, UUID
        // profileId);
}
