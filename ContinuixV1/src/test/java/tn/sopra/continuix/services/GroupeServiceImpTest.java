package tn.sopra.continuix.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.repositories.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupeServiceImpTest {

    @InjectMocks private GroupeServiceImp groupeService;

    @Mock private GroupRepository groupRepository;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGroup_Success() {
        Group group = new Group();
        group.setName("Dev");
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupeService.createGroup(group);
        assertEquals("Dev", result.getName());
    }

    @Test
    void testGetAllGroups() {
        when(groupRepository.findAll()).thenReturn(List.of(new Group(), new Group()));
        assertEquals(2, groupeService.getAllGroups().size());
    }
}
