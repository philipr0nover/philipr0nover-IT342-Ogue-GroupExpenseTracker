package edu.cit.ogue.groupexpensetracker.features.groups;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository repo;

    public GroupService(GroupRepository repo) {
        this.repo = repo;
    }

    public Group create(Group group) {
        return repo.save(group);
    }

    public List<Group> getAll() {
        return repo.findAll();
    }
}
