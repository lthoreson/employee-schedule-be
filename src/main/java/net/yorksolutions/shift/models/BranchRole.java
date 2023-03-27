package net.yorksolutions.shift.models;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class BranchRole {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @ManyToMany
    private List<Profile> people;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Profile> getPeople() {
        return people;
    }

    public void setPeople(List<Profile> people) {
        this.people = people;
    }
}
