package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.generate.GenerateId;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl implements UserRepository {

    private Map<Integer, User> users = new HashMap<>();
    private GenerateId generateId = new GenerateId();

    @Override
    public User createUser(User user) {
        if (users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).findFirst().isEmpty()) {
            user.setId(generateId.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Ошибка валидации");
        }
    }

    @Override
    public User updateUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).findFirst().isEmpty()) {
            User userNew = users.get(user.getId());
            if (user.getName() == null && user.getEmail() != null) {
                userNew.setEmail(user.getEmail());
                return userNew;
            } else if (user.getName() != null && user.getEmail() == null) {
                userNew.setName(user.getName());
                return userNew;
            } else if (user.getName() != null && user.getEmail() != null) {
                users.put(user.getId(), user);
                return user;
            }
        }
        throw new ValidationException("Ошибка валидации");
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Integer userId) {
        users.remove(userId);
    }

}
