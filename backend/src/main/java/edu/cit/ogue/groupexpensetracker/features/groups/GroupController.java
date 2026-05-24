package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    // Get all groups for a user
    @GetMapping("/user/{userId}")
    public List<Group> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    // ✅ NEW: Get a single group by ID (needed so frontend can read createdBy)
    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable Long id) {
        Group group = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        return ResponseEntity.ok(group);
    }

    // ✅ UPDATED: expects createdBy in the request body
    // Frontend must send: { name: "...", createdBy: <userId> }
    @PostMapping
    public ResponseEntity<Group> create(@RequestBody Group group) {
        if (group.getCreatedBy() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "createdBy is required");
        }
        return ResponseEntity.ok(service.create(group));
    }
}