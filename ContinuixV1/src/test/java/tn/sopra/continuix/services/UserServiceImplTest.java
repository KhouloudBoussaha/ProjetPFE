package tn.sopra.continuix.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.sopra.continuix.config.JwtUtil;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.*;



import java.util.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_shouldSendPasswordResetEmail() throws MessagingException {
        // Arrange
        Users newUser = new Users();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateResetToken(anyString())).thenReturn("reset-token");
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // On simule que l'envoi d'email ne lance pas d'exception
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        // Act
        Users savedUser = userService.saveUser(newUser, List.of());

        // Assert
        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), eq("reset-token"));
        assert savedUser.getPassword().equals("encodedPassword");
        assert savedUser.getResetPasswordToken().equals("reset-token");
    }

    @Test
    void testSaveUser_emailSendingThrowsException() throws MessagingException {
        // Arrange
        Users newUser = new Users();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateResetToken(anyString())).thenReturn("reset-token");
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // On simule que l'envoi d'email lance une exception
        doThrow(new MessagingException("Email sending failed")).when(emailService).sendPasswordResetEmail(anyString(), anyString());

        // Act & Assert
        try {
            userService.saveUser(newUser, List.of());
            assert false; // on ne doit pas arriver ici
        } catch (RuntimeException e) {
            // L'exception RuntimeException encapsule la MessagingException dans le service
            assert e.getMessage().contains("Échec de l'envoi de l'email");
        }

        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), eq("reset-token"));
    }
}
