package edu.cit.ogue.groupexpensetracker.features.group_members;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findByUserId(Long userId);
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

    // ✅ NEW: needed to clean up members before deleting a group
    void deleteByGroupId(Long groupId);
}