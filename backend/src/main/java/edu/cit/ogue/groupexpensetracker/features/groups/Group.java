package edu.cit.ogue.groupexpensetracker.features.groups;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int members;

    // ✅ NEW: tracks who created this group
    @Column(name = "created_by")
    private Long createdBy;

    public Group() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getMembers() { return members; }
    public Long getCreatedBy() { return createdBy; }

    public void setName(String name) { this.name = name; }
    public void setMembers(int members) { this.members = members; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
}