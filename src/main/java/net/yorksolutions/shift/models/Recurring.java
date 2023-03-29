package net.yorksolutions.shift.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Recurring {
    @Id
    @GeneratedValue
    private UUID id;
    private Integer startTime;
    private Integer endTime;
    private Integer weekday;
    @ManyToOne
    private Profile profile;
}
