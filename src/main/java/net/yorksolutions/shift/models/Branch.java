package net.yorksolutions.shift.models;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Branch {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<BranchRole> roles;
    @ManyToMany
    private Set<Profile> profiles;
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

    public List<BranchRole> getRoles() {
        return roles;
    }

    public void setRoles(List<BranchRole> roles) {
        this.roles = roles;
    }

    public List<Recurring> getRecurrings() {
        return recurrings;
    }

    public void setRecurrings(List<Recurring> recurrings) {
        this.recurrings = recurrings;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }
}
