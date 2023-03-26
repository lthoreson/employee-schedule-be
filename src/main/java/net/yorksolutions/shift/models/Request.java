package net.yorksolutions.shift.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Request {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID employee;
    private UUID approval;
    private UUID branch;
    private Date startTime;
    private Date endTime;
}
