package edu.cit.ogue.groupexpensetracker.features.groups;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int members;

    public Group() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getMembers() { return members; }

    public void setName(String name) { this.name = name; }
    public void setMembers(int members) { this.members = members; }
}
