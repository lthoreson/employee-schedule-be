package net.yorksolutions.shift.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import net.yorksolutions.shift.models.Request;

public interface RequestRepository extends CrudRepository<Request, UUID> {

}
