package edu.cit.ogue.groupexpensetracker.features.group_members;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository repo;

    public GroupMemberService(GroupMemberRepository repo) {
        this.repo = repo;
    }

    public GroupMember addMember(Long groupId, Long userId) {

        if (repo.existsByGroupIdAndUserId(groupId, userId)) {
            throw new RuntimeException("User already in group");
        }

        return repo.save(new GroupMember(groupId, userId));
    }

    public List<GroupMember> getMembers(Long groupId) {
        return repo.findByGroupId(groupId);
    }
}
