package tn.sopra.continuix.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.entities.Group;

import java.util.List;

public interface GroupeService {
    List<Group> getAllGroups();
    Group createGroup(Group group);
    void deleteGroup(Long id);
    Group getGroupById(Long id);
    Group updateGroup(Group existingGroup, GroupRequestDTO dto);



}
