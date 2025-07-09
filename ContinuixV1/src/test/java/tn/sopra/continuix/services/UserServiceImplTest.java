package tn.sopra.continuix.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.*;
import tn.sopra.continuix.config.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private EmailService emailService;
    @Mock private NotificationRecipientRepository notificationRecipientRepository;
    @Mock private GroupRepository groupRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_Success() {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(notificationRecipientRepository).deleteByRecipientId(userId);

        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        Users existing = new Users();
        existing.setId(1L);
        existing.setEmail("old@mail.com");

        Users update = new Users();
        update.setEmail("duplicate@mail.com");

        Users otherUser = new Users();
        otherUser.setId(2L);
        otherUser.setEmail("duplicate@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("duplicate@mail.com")).thenReturn(Optional.of(otherUser));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, update));
    }
}
