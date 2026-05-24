package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.cit.ogue.groupexpensetracker.features.group_members.GroupMember;
import edu.cit.ogue.groupexpensetracker.features.group_members.GroupMemberRepository;

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

    // ✅ NEW: used by GroupController and isCreator check
    public Optional<Group> findById(Long groupId) {
        if (groupId == null) return Optional.empty();
        return repo.findById(groupId);
    }

    @Transactional
    public Group create(Group group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        return repo.save(group);
    }

    // Used by GroupMemberService and ExpenseService
    public boolean isCreator(Long groupId, Long userId) {
        if (groupId == null || userId == null) return false;
        return repo.findById(groupId)
                .map(g -> userId.equals(g.getCreatedBy()))
                .orElse(false);
    }
}