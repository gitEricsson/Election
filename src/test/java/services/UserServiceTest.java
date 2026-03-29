package services;

import election.data.models.User;
import election.data.repositories.UserRepository;
import election.exceptions.*;
import election.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        User user = new User("John Doe", "john@example.com");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should fail to create user with duplicate email")
    void shouldFailToCreateUserWithDuplicateEmail() {
        User user = new User("John Doe", "john@example.com");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
    }

    @Test
    @DisplayName("Should retrieve user by id")
    void shouldRetrieveUserById() {
        User user = new User("John Doe", "john@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserById(1L);

        assertEquals("John Doe", retrievedUser.getName());
    }

    @Test
    @DisplayName("Should fail to retrieve non existent user")
    void shouldFailToRetrieveNonExistentUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    @DisplayName("Should validate user email format")
    void shouldValidateUserEmailFormat() {
        User invalidUser = new User("John Doe", "invalid-email");

        assertThrows(InvalidEmailFormatException.class, () -> userService.createUser(invalidUser));
    }
}