package ru.natali.pizzeria.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.natali.pizzeria.dto.UserDTO;
import ru.natali.pizzeria.model.ERole;
import ru.natali.pizzeria.model.Role;
import ru.natali.pizzeria.model.User;
import ru.natali.pizzeria.repository.RoleRepository;
import ru.natali.pizzeria.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, modelMapper);
    }

    @Test
    void createUser_ShouldSuccessfullyCreateUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setEmail("test@example.com");
        userDTO.setRoles(Set.of("ROLE_USER"));

        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("test@example.com");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(any(ERole.class))).thenReturn(Optional.of(role));
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowExceptionWhenUsernameExists() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existinguser");
        userDTO.setEmail("test@example.com");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void createUser_ShouldThrowExceptionWhenEmailExists() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("existing@example.com");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void deleteUser_ShouldSoftDeleteUser() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).softDelete(userId);
    }

    @Test
    void getUserById_ShouldReturnUser() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getAllUsers_ShouldReturnAllActiveUsers() {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setUsername("user1");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setUsername("user2");

        when(userRepository.findAllActive()).thenReturn(users);
        when(modelMapper.map(user1, UserDTO.class)).thenReturn(userDTO1);
        when(modelMapper.map(user2, UserDTO.class)).thenReturn(userDTO2);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    void convertToDto_ShouldConvertUserToUserDTO() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        user.setRoles(Set.of(role));

        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setId(1L);
        expectedDTO.setUsername("testuser");
        expectedDTO.setEmail("test@example.com");
        expectedDTO.setRoles(Set.of("ROLE_USER"));

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedDTO);

        UserDTO result = userService.convertToDto(user);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getUsername(), result.getUsername());
        assertEquals(expectedDTO.getEmail(), result.getEmail());
        assertTrue(result.getRoles().contains("ROLE_USER"));
    }

    @Test
    void convertToEntity_ShouldConvertUserDTOToUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setEmail("test@example.com");

        User expectedUser = new User();
        expectedUser.setUsername("testuser");
        expectedUser.setPassword("password");
        expectedUser.setEmail("test@example.com");

        when(modelMapper.map(userDTO, User.class)).thenReturn(expectedUser);

        User result = userService.convertToEntity(userDTO);

        assertNotNull(result);
        assertEquals(expectedUser.getUsername(), result.getUsername());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertNull(result.getId());
    }
}
