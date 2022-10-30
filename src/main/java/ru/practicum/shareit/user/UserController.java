package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ObjectExcistenceException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Пользователь создан");
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") @Positive Integer userId,
                           @RequestBody @Valid UserDto userDto) {
        if (userId > 0) {
            log.info("Пользователь обновился");
            userDto.setId(userId);
            return userService.updateUser(userDto);
        } else {
            throw new ObjectExcistenceException("Пользователь не существует.");
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        log.info("Пользователь с id = " + userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Вывод всех созданных пользователей");
        return userService.getUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Integer userId) {
        log.info("Удаление пользователя с id = " + userId);
        userService.deleteUserById(userId);
    }
}
