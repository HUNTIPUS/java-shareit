package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long userId);

    List<User> getUsers();

    void deleteUserById(Long userId);
}
