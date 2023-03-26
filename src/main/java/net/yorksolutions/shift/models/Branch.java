package net.yorksolutions.shift.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Branch {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
    @ManyToMany
    @JsonManagedReference
    private List<Profile> profiles;
    @OneToMany
    private List<Recurring> recurrings;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Recurring> getRecurrings() {
        return recurrings;
    }

    public void setRecurrings(List<Recurring> recurrings) {
        this.recurrings = recurrings;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
