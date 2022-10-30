package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);
    User updateUser(UserDto user);
    Optional<User> getUserById(Integer userId);
    List<User> getUsers();
    void deleteUserById(Integer userId);
}
