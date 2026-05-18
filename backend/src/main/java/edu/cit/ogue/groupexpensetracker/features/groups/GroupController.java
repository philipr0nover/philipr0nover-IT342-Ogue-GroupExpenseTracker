package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<Group> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Group create(@RequestBody Group group) {
        return service.create(group);
    }
}