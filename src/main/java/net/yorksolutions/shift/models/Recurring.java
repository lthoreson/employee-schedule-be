package net.yorksolutions.shift.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Recurring {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID branchRole;
    @ElementCollection
    private List<UUID> employee;
    private Date startTime;
    private Date endTime;
}
