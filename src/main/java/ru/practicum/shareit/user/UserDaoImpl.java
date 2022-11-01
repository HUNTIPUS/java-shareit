package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.DublicateEmailException;
import ru.practicum.shareit.generate.GenerateId;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final GenerateId generateId;

    @Override
    public User createUser(User user) {
        if (users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).findFirst().isEmpty()) {
            user.setId(generateId.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new DublicateEmailException("Такой email уже занят");
        }
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()).getEmail().equals(user.getEmail())) {
            return doUpdateUser(user, users.get(user.getId()));
        }
        if (users.values().stream().noneMatch(x -> x.getEmail().equals(user.getEmail()))) {
            return doUpdateUser(user, users.get(user.getId()));
        }
        throw new DublicateEmailException("Такой email уже занят");
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    private User doUpdateUser(User user, User newUser) {
        if (user.getName() != null && !user.getName().isBlank()) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            newUser.setEmail(user.getEmail());
        }
        return newUser;
    }
}