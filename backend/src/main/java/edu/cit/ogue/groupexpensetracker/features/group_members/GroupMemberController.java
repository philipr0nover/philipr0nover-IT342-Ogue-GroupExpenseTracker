package edu.cit.ogue.groupexpensetracker.features.group_members;

import edu.cit.ogue.groupexpensetracker.features.auth.User;
import edu.cit.ogue.groupexpensetracker.features.auth.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/group-members")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupMemberController {

    private final GroupMemberService service;
    private final UserRepository userRepo;

    public GroupMemberController(GroupMemberService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @PostMapping
    public GroupMember addMember(@RequestBody GroupMember gm) {

        if (gm.getGroupId() == null || gm.getUserId() == null) {
            throw new RuntimeException("groupId and userId are required");
        }

        return service.addMember(gm.getGroupId(), gm.getUserId());
    }

    @GetMapping("/{groupId}")
    public List<Map<String, Object>> getMembers(@PathVariable Long groupId) {

        List<GroupMember> members = service.getMembers(groupId);

        if (members == null || members.isEmpty()) {
            return List.of();
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (GroupMember m : members) {

            User user = userRepo.findById(m.getUserId()).orElse(null);

            if (user == null) continue;

            Map<String, Object> data = new HashMap<>();
            data.put("id", m.getId());
            data.put("user", user);

            result.add(data);
        }

        return result;
    }
}