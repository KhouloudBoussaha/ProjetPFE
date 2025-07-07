package tn.sopra.continuix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.UserDTO;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.Role;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.request.ResetPasswordRequest;
import tn.sopra.continuix.services.UserServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users users = userService.getUserById(id);
        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // Création d'un nouvel utilisateur
        Users users = new Users();
        users.setUsername(userDTO.getUsername());
        users.setPassword(userDTO.getPassword());
        users.setEmail(userDTO.getEmail());
        users.setRole(Role.valueOf(String.valueOf(userDTO.getRole())));

        if (userDTO.getSupervisor() != null) {
            Users supervisor = userService.findById(userDTO.getSupervisor())
                    .orElseThrow(() -> new RuntimeException("Superviseur non trouvé avec ID : " + userDTO.getSupervisor()));
            users.setSupervisor(supervisor);
        }

        // Appel de la méthode saveUser mise à jour avec groupes
        Users createdUsers = userService.saveUser(users, userDTO.getGroupIds());

        UserDTO createdUserDTO = userService.convertToDTO(createdUsers);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Users updatedData = new Users();
        updatedData.setUsername(userDTO.getUsername());
        updatedData.setEmail(userDTO.getEmail());
        updatedData.setRole(Role.valueOf(String.valueOf(userDTO.getRole())));

        Users updatedUsers = userService.updateUser(id, updatedData);
        UserDTO updatedUserDTO = userService.convertToDTO(updatedUsers);
        return ResponseEntity.ok(updatedUserDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{userId}/assign-supervisor/{supervisorId}")
    public Users assignSupervisor(@PathVariable Long userId, @PathVariable Long supervisorId) {
        return userService.assignSupervisor(userId, supervisorId);
    }

    @GetMapping("/{userId}/subordinates")
    public List<Users> getSubordinates(@PathVariable Long userId) {
        return userService.getSubordinates(userId);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.resetPassword(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getToken());
            response.put("message", "Mot de passe réinitialisé avec succès.");
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }


}
