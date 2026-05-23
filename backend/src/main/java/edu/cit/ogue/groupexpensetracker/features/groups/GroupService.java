package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.stereotype.Service;
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

    public List<Group> getByUser(Long userId) {

        List<GroupMember> memberships =
                groupMemberRepository.findByUserId(userId);

        if (memberships == null || memberships.isEmpty()) {
            return List.of();
        }

        List<Long> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (groupIds.isEmpty()) {
            return List.of();
        }

        return repo.findAllById(groupIds);
    }

    public Group create(Group group) {
        return repo.save(group);
    }
}