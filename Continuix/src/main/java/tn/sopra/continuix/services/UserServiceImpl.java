package tn.sopra.continuix.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.sopra.continuix.config.JwtUtil;
import tn.sopra.continuix.entities.NotificationRecipient;
import tn.sopra.continuix.dtos.UserDTO;
import tn.sopra.continuix.dtos.UserNodeDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.NotificationRecipientRepository;
import tn.sopra.continuix.repositories.NotificationRepository;
import tn.sopra.continuix.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Optional;


@Service
public class UserServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService,NotificationRepository notificationRepository,NotificationRecipientRepository notificationRecipientRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
        this.notificationRecipientRepository = notificationRecipientRepository;

    }



    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }



    public Users updateUser(Long id, Users updatedUsers) {
        Users existingUsers = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        // Vérifier si l'email est utilisé par un autre utilisateur
        Optional<Users> userWithSameEmail = userRepository.findByEmail(updatedUsers.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("L'email existe déjà : " + updatedUsers.getEmail());
        }

        existingUsers.setUsername(updatedUsers.getUsername());
        existingUsers.setEmail(updatedUsers.getEmail());
        existingUsers.setRole(updatedUsers.getRole());

        // Le mot de passe et le token ne sont pas modifiés ici
        return userRepository.save(existingUsers);
    }



    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        // Supprimer d'abord toutes les notifications liées à ce user
        notificationRecipientRepository.deleteByRecipientId(id);

        // Puis supprimer l'utilisateur
        userRepository.deleteById(id);
    }
    public UserDTO convertToDTO(Users users) {
        UserDTO dto = new UserDTO();
        dto.setId(users.getId());
        dto.setUsername(users.getUsername());
        dto.setPassword(users.getPassword());
        dto.setEmail(users.getEmail());
        dto.setRole(users.getRole());
        if (users.getSupervisor() != null) {
            dto.setSupervisor(users.getSupervisor().getId());
            dto.setSupervisorName(users.getSupervisor().getUsername());
        } else {
            dto.setSupervisor(null);
            dto.setSupervisorName(null);
        }
        return dto;
    }


    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users assignSupervisor(Long userId, Long supervisorId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Users supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        if (!isValidHierarchy(users.getRole(), supervisor.getRole())) {
            throw new RuntimeException("Invalid hierarchy");
        }

        users.setSupervisor(supervisor);
        return userRepository.save(users);
    }

    private boolean isValidHierarchy(Role userRole, Role supervisorRole) {
        if (userRole == Role.Admin) return false;
        if (userRole == Role.MANAGER && supervisorRole == Role.Admin) return true;
        if (userRole == Role.TEAMLEADER && supervisorRole == Role.MANAGER) return true;
        if (userRole == Role.COLLABORATOR && supervisorRole == Role.TEAMLEADER) return true;
        if (userRole == Role.SECURITYAGENT && supervisorRole == Role.TEAMLEADER) return true;
        return false;
    }
    public List<Users> getSubordinates(Long userId) {
        return userRepository.findBySupervisorId(userId);
    }

//    public List<UserDTO> getOrgChart() {
//        List<User> admins = userRepository.findByRole(Role.Admin);
//        return admins.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }


    @Transactional
    public Users saveUser(Users users) {
        Optional<Users> existingUserOpt = userRepository.findByEmail(users.getEmail());
        if (existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("L'email existe déjà : " + users.getEmail());
        }

        if (users.getPassword() != null && !users.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(users.getPassword());
            users.setPassword(hashedPassword);
        } else {
            users.setPassword(null);
        }

        String resetToken = jwtUtil.generateResetToken(users.getEmail());
        users.setResetPasswordToken(resetToken);
        users.setTokenExpirationDate(LocalDateTime.now().plusHours(24));

        Users savedUsers = userRepository.save(users);

        try {
            emailService.sendPasswordResetEmail(savedUsers.getEmail(), resetToken);
            logger.info("Email envoyé à : {}", savedUsers.getEmail());
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {} : {}", savedUsers.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email", e);
        }

        return savedUsers;
    }


    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }
    public List<UserNodeDTO> getOrgChart() {
        // Récupérer l'unique admin
        Users admin = userRepository.findByRole(Role.Admin)
                .orElseThrow(() -> new IllegalStateException("No admin found"));
        return Collections.singletonList(buildUserNode(admin));
    }

    private UserNodeDTO buildUserNode(Users users) {
        UserNodeDTO node = new UserNodeDTO();
        node.setId(users.getId());
        node.setUsername(users.getUsername());
        node.setRole(users.getRole().name());
        node.setAccepted(hasAcceptedNotification(users.getId()));
        node.setSubordinates(buildUserNodes(userRepository.findBySupervisorId(users.getId())));
        return node;
    }

    private List<UserNodeDTO> buildUserNodes(List<Users> users) {
        List<UserNodeDTO> nodes = new ArrayList<>();
        for (Users user : users) {
            nodes.add(buildUserNode(user));
        }
        return nodes;
    }

    private boolean hasAcceptedNotification(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;

        Users users = userOpt.get();
        List<NotificationRecipient> recipientRecords = notificationRecipientRepository.findByRecipient(users);
        return recipientRecords.stream().anyMatch(rec -> Boolean.TRUE.equals(rec.getAccepted())
        );
    }




    public List<Notification> getNotificationHistory() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }
    @Transactional
    public void resetPassword(String newPassword,String token) {
        Users users = userRepository.findByResetPasswordToken(token);
        if (users == null) {
            throw new IllegalArgumentException("Token invalide.");
        }

        // Vérifier si le token n'a pas expiré
        if (users.getTokenExpirationDate() == null || users.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le token a expiré.");
        }

        // Vérifier la validité du token avec JwtUtil
        if (!jwtUtil.validateResetToken(token, users.getEmail())) {
            throw new IllegalArgumentException("Token invalide ou non associé à cet utilisateur.");
        }

        // Mettre à jour le mot de passe
        users.setPassword(passwordEncoder.encode(newPassword));
        // Invalider le token après utilisation

        users.setTokenExpirationDate(null);

        userRepository.save(users);
    }
}
