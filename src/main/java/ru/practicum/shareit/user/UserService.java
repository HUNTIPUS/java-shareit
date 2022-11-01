package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        return userRepository.updateUser(user);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }
}
