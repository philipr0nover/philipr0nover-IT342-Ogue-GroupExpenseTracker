package edu.cit.ogue.groupexpensetracker.features.group_members;

import edu.cit.ogue.groupexpensetracker.features.groups.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository repo;
    private final GroupService groupService;

    public GroupMemberService(GroupMemberRepository repo, GroupService groupService) {
        this.repo = repo;
        this.groupService = groupService;
    }

    @Transactional
    public GroupMember addMember(Long groupId, Long userId) {
        if (groupId == null || userId == null) {
            throw new RuntimeException("groupId and userId are required");
        }
        if (repo.existsByGroupIdAndUserId(groupId, userId)) {
            throw new RuntimeException("User already in group");
        }
        return repo.save(new GroupMember(groupId, userId));
    }

    @Transactional
    public List<GroupMember> getMembers(Long groupId) {
        if (groupId == null) return List.of();
        List<GroupMember> members = repo.findByGroupId(groupId);
        return members != null ? members : List.of();
    }

    // ✅ NEW: only the group creator can remove a member
    @Transactional
    public void removeMember(Long groupMemberId, Long requesterId) {
        GroupMember membership = repo.findById(groupMemberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (!groupService.isCreator(membership.getGroupId(), requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the group creator can remove members");
        }

        repo.delete(membership);
    }
}