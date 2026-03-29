package election.services;

import election.data.models.User;
import election.data.repositories.UserRepository;
import election.exceptions.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailFormatException("Invalid email");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Duplicate email");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}