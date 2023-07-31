package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoForBooking;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

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
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDtoForBooking userWithUserDtoForBooking(User user) {
        return UserDtoForBooking.builder()
                .id(user.getId())
                .build();
    }

    public static List<UserDto> toListUserDto(List<User> userList) { // Метод перевода объекта user в объект userDto
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}
