package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) { // Метод перевода объекта user в объект userDto
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) { // Метод перевода объекта userDto в объект user
        return new User(
                userDto.getId() != null ? userDto.getId() : null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static UserDtoForBooking userWithUserDtoForBooking(User user) {
        return UserDtoForBooking.builder()
                .id(user.getId())
                .build();
    }
}
