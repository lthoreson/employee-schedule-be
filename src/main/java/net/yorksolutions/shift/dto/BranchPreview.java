package net.yorksolutions.shift.dto;

import java.util.List;
import java.util.UUID;

public class BranchPreview {
    public BranchPreview(UUID id, String name, List<String> myRoles) {
        this.id = id;
        this.name = name;
        this.myRoles = myRoles;
    }

    public UUID id;
    public String name;
    public List<String> myRoles;
}
