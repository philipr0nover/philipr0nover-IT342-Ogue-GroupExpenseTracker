package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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

    @Transactional  // ← no readOnly, causes commit issues with PgBouncer
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

    @Transactional
    public Group create(Group group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        return repo.save(group);
    }
}