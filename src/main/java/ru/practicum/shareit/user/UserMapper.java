package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public static List<UserDto> toListUserDto(List<User> users) {
        return users
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
