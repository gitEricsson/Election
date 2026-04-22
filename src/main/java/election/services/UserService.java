package election.services;

import election.data.models.User;
import election.data.repositories.UserRepository;
import election.dtos.requests.UserRequest;
import election.dtos.responses.UserResponse;
import election.exceptions.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (!userRequest.getEmail().contains("@")) {
            throw new InvalidEmailFormatException("Invalid email");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateEmailException("Duplicate email");
        }
        User user = new User(userRequest.getName(), userRequest.getEmail());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}