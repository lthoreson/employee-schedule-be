package net.yorksolutions.shift.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import net.yorksolutions.shift.dto.BranchPreview;
import net.yorksolutions.shift.models.Branch;
import net.yorksolutions.shift.services.BranchService;

@RestController
@RequestMapping("/branch")
public class BranchController {
    private final BranchService service;

    public BranchController(BranchService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Branch createBranch(@RequestBody UUID token, @RequestParam String name) {
        try {
            return service.createBranch(token, name);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/account")
    public List<BranchPreview> getMyBranches(@RequestHeader(HttpHeaders.AUTHORIZATION) String requestHeader) {
        try {
            final UUID token = UUID.fromString(requestHeader);
            return service.getMyBranches(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
