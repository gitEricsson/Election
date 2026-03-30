package services;

import election.data.models.User;
import election.data.repositories.UserRepository;
import election.exceptions.*;
import election.dtos.requests.UserRequest;
import election.dtos.responses.UserResponse;
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
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        
        User savedUser = new User("John Doe", "john@example.com");
        savedUser.setId(1L);
        
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse createdUser = userService.createUser(request);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should fail to create user with duplicate email")
    void shouldFailToCreateUserWithDuplicateEmail() {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
    }

    @Test
    @DisplayName("Should retrieve user by id")
    void shouldRetrieveUserById() {
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse retrievedUser = userService.getUserById(1L);

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
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmail("invalid-email");

        assertThrows(InvalidEmailFormatException.class, () -> userService.createUser(invalidRequest));
    }
}