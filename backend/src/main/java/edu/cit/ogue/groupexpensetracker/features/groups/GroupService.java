package edu.cit.ogue.groupexpensetracker.features.groups;

import edu.cit.ogue.groupexpensetracker.features.group_members.GroupMemberRepository;
import edu.cit.ogue.groupexpensetracker.features.group_members.GroupMember;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository repo;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository repo,
                        GroupMemberRepository groupMemberRepository) {
        this.repo = repo;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Transactional
    public List<Group> getByUser(Long userId) {
        if (userId == null) return List.of();

        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);
        if (memberships == null || memberships.isEmpty()) return List.of();

        List<Long> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        return groupIds.isEmpty() ? List.of() : repo.findAllById(groupIds);
    }

    public Optional<Group> findById(Long groupId) {
        if (groupId == null) return Optional.empty();
        return repo.findById(groupId);
    }

    @Transactional
    public Group create(Group group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        return repo.save(group);
    }

    public boolean isCreator(Long groupId, Long userId) {
        if (groupId == null || userId == null) return false;
        return repo.findById(groupId)
                .map(g -> userId.equals(g.getCreatedBy()))
                .orElse(false);
    }

    // ✅ NEW: delete group and all its members — only creator allowed
    @Transactional
    public void deleteGroup(Long groupId, Long requesterId) {
        Group group = repo.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        if (!group.getCreatedBy().equals(requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the group creator can delete this group");
        }

        // Delete all memberships first to avoid FK constraint errors
        groupMemberRepository.deleteByGroupId(groupId);

        repo.delete(group);
    }
}