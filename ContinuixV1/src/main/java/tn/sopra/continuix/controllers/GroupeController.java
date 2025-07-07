package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.services.GroupeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupeController {

    private final GroupeService groupeService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO dto) {
        Group group = new Group();
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());

        List<Users> members = new ArrayList<>();
        for (Long userId : dto.getMemberIds()) {
            userRepository.findById(userId).ifPresent(user -> {
                user.setGroup(group); // Mettre à jour le lien inverse
                members.add(user);
            });
        }
        group.setMembers(members);

        Group saved = groupeService.createGroup(group);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupeService.getAllGroups();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupeService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDTO dto) {
        Group existingGroup = groupeService.getGroupById(id);
        if (existingGroup == null) {
            return ResponseEntity.notFound().build();
        }

        Group updatedGroup = groupeService.updateGroup(existingGroup, dto);
        return ResponseEntity.ok(updatedGroup);
    }

}
