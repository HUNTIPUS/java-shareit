package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(User user);

    User getById(Long userId);

    List<User> getAll();

    void deleteById(Long userId);
}
