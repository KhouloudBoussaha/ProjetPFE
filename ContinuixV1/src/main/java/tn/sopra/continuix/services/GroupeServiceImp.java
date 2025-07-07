package tn.sopra.continuix.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.GroupRepository;
import tn.sopra.continuix.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class GroupeServiceImp implements GroupeService {

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public Group updateGroup(Group existingGroup, GroupRequestDTO dto) {
        existingGroup.setName(dto.getName());
        existingGroup.setDescription(dto.getDescription());

        // Dissocier anciens membres
        List<Users> oldMembers = userRepository.findByGroup(existingGroup);
        for (Users oldUser : oldMembers) {
            oldUser.setGroup(null);
        }
        userRepository.saveAll(oldMembers);
        userRepository.flush();

        // Associer nouveaux membres
        List<Users> newMembers = new ArrayList<>();
        for (Long userId : dto.getMemberIds()) {
            userRepository.findById(userId).ifPresent(user -> {
                user.setGroup(existingGroup);
                newMembers.add(user);
            });
        }
        userRepository.saveAll(newMembers);
        userRepository.flush();

        // Mise à jour des membres du groupe
        existingGroup.getMembers().clear();
        existingGroup.getMembers().addAll(newMembers);

        return groupRepository.save(existingGroup);
    }
}
