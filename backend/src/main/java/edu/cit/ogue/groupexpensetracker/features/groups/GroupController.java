package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups") // ✅ match your other APIs
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    // 🔥 USER-BASED GROUPS (FIX)
    @GetMapping("/user/{userId}")
    public List<Group> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    // ❌ REMOVE THIS (causes shared data bug)
    // @GetMapping
    // public List<Group> getAll() {
    //     return service.getAll();
    // }

    @PostMapping
    public Group create(@RequestBody Group group) {
        return service.create(group);
    }
}