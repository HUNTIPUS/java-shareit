package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public User updateUser(UserDto userDto) {
        getUserById(userDto.getId());
        return userRepository.updateUser(userDto);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(Integer userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new ObjectExcistenceException("Пользователь не существует"));
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteUserById(userId);
    }
}
