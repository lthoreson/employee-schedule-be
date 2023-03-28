package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.yorksolutions.shift.models.Request;

@Repository
public interface RequestRepository extends CrudRepository<Request, UUID> {

}
