package edu.cit.ogue.groupexpensetracker.features.group_members;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private Long userId;

    public GroupMember() {}

    public GroupMember(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public Long getGroupId() { return groupId; }
    public Long getUserId() { return userId; }
}
