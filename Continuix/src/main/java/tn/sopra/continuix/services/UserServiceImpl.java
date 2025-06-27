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



    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }



    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        // Vérifier si l'email est utilisé par un autre utilisateur
        Optional<User> userWithSameEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("L'email existe déjà : " + updatedUser.getEmail());
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());

        // Le mot de passe et le token ne sont pas modifiés ici
        return userRepository.save(existingUser);
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
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        if (user.getSupervisor() != null) {
            dto.setSupervisor(user.getSupervisor().getId());
            dto.setSupervisorName(user.getSupervisor().getUsername());
        } else {
            dto.setSupervisor(null);
            dto.setSupervisorName(null);
        }
        return dto;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User assignSupervisor(Long userId, Long supervisorId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        if (!isValidHierarchy(user.getRole(), supervisor.getRole())) {
            throw new RuntimeException("Invalid hierarchy");
        }

        user.setSupervisor(supervisor);
        return userRepository.save(user);
    }

    private boolean isValidHierarchy(Role userRole, Role supervisorRole) {
        if (userRole == Role.Admin) return false;
        if (userRole == Role.MANAGER && supervisorRole == Role.Admin) return true;
        if (userRole == Role.TEAMLEADER && supervisorRole == Role.MANAGER) return true;
        if (userRole == Role.COLLABORATOR && supervisorRole == Role.TEAMLEADER) return true;
        if (userRole == Role.SECURITYAGENT && supervisorRole == Role.TEAMLEADER) return true;
        return false;
    }
    public List<User> getSubordinates(Long userId) {
        return userRepository.findBySupervisorId(userId);
    }

//    public List<UserDTO> getOrgChart() {
//        List<User> admins = userRepository.findByRole(Role.Admin);
//        return admins.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }


    @Transactional
    public User saveUser(User user) {
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("L'email existe déjà : " + user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
        } else {
            user.setPassword(null);
        }

        String resetToken = jwtUtil.generateResetToken(user.getEmail());
        user.setResetPasswordToken(resetToken);
        user.setTokenExpirationDate(LocalDateTime.now().plusHours(24));

        User savedUser = userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(savedUser.getEmail(), resetToken);
            logger.info("Email envoyé à : {}", savedUser.getEmail());
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email à {} : {}", savedUser.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email", e);
        }

        return savedUser;
    }


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public List<UserNodeDTO> getOrgChart() {
        // Récupérer l'unique admin
        User admin = userRepository.findByRole(Role.Admin)
                .orElseThrow(() -> new IllegalStateException("No admin found"));
        return Collections.singletonList(buildUserNode(admin));
    }

    private UserNodeDTO buildUserNode(User user) {
        UserNodeDTO node = new UserNodeDTO();
        node.setId(user.getId());
        node.setUsername(user.getUsername());
        node.setRole(user.getRole().name());
        node.setAccepted(hasAcceptedNotification(user.getId()));
        node.setSubordinates(buildUserNodes(userRepository.findBySupervisorId(user.getId())));
        return node;
    }

    private List<UserNodeDTO> buildUserNodes(List<User> users) {
        List<UserNodeDTO> nodes = new ArrayList<>();
        for (User user : users) {
            nodes.add(buildUserNode(user));
        }
        return nodes;
    }

    private boolean hasAcceptedNotification(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        List<NotificationRecipient> recipientRecords = notificationRecipientRepository.findByRecipient(user);
        return recipientRecords.stream().anyMatch(rec -> Boolean.TRUE.equals(rec.getAccepted())
        );
    }




    public List<Notification> getNotificationHistory() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }
    @Transactional
    public void resetPassword(String newPassword,String token) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Token invalide.");
        }

        // Vérifier si le token n'a pas expiré
        if (user.getTokenExpirationDate() == null || user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le token a expiré.");
        }

        // Vérifier la validité du token avec JwtUtil
        if (!jwtUtil.validateResetToken(token, user.getEmail())) {
            throw new IllegalArgumentException("Token invalide ou non associé à cet utilisateur.");
        }

        // Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        // Invalider le token après utilisation

        user.setTokenExpirationDate(null);

        userRepository.save(user);
    }
}
