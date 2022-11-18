package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ObjectExcistenceException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(updateUserIfParamIsNull(user));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new ObjectExcistenceException("Пользователь не сущестует");
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    private User updateUserIfParamIsNull(User user) {
        User userNew = getById(user.getId());
        if (user.getEmail() == null) {
            user.setEmail(userNew.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userNew.getName());
        }
        return user;
    }
}
