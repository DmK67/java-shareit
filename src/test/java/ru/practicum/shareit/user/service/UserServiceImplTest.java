package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserServiceImpl userService;

    User user1;
    User user2;
    UserDto userDto1;
    UserDto userDto2;
    User userAllFieldsNull;
    UserDto userDtoAllFieldsNull;
    User userNull;
    UserDto userDtoNull;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("userDto1")
                .email("userDto1@ya.ru")
                .build();
        user1 = toUser(userDto1);

        userDto2 = UserDto.builder()
                .id(2L)
                .name("userDto2")
                .email("userDto2@ya.ru")
                .build();
        user2 = toUser(userDto2);

        userAllFieldsNull = new User();

        userDtoAllFieldsNull = new UserDto();

        userNull = null;
        userDtoNull = null;

    }

    @Test
    void addUser_WhenAllIsOk_ThenReturnedAddedUser() {
        userService.addUser(user1);

        List<User> users = userService.getListUsers();

        Long id = users.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);

        User userFromDb = userService.getUserById(id);

        assertEquals(1, users.size());
        assertEquals(user1.getName(), userFromDb.getName());
        assertEquals(user1.getEmail(), userFromDb.getEmail());
    }

    @Test
    void addUser_WhenEmailInvalid_ThenReturnConstraintViolationException() {
        user1.setEmail("wrong email");

        assertThrows(ConstraintViolationException.class,
                () -> userService.addUser(user1));
    }

    @Test
    void addUser_WhenEmailIsBlank_ThenReturnValidateException() {
        user1.setEmail("");

        assertThrows(ValidateException.class,
                () -> userService.addUser(user1));
    }

    @Test
    void addUser_WhenEmailIsNull_ThenReturnValidateException() {
        user1.setEmail(null);

        assertThrows(ValidateException.class,
                () -> userService.addUser(user1));
    }

    @Test
    void getUserById_WhenUserIsOk() {
        User savedUser = userService.addUser(user1);

        User user = userService.getUserById(savedUser.getId());

        assertNotNull(user.getId());
        assertEquals(user.getName(), user1.getName());
        assertEquals(user.getEmail(), user1.getEmail());
    }

    @Test
    void getUserById_WhenUserIsNotFound_ThenReturnedNotFoundException() {
        User savedUser = userService.addUser(user1);

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(9000L));
    }

    @Test
    void updateUser_WhenUserNameIsNull_ThenReturnedUpdatedUser() {
        User addedUser = userService.addUser(user1);

        List<User> beforeUpdateUser = userService.getListUsers();
        Long id = beforeUpdateUser.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, addedUser.getId());

        User userFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getName(), user1.getName());
        assertEquals(userFromDbBeforeUpdate.getEmail(), user1.getEmail());

        user2.setId(addedUser.getId());
        user2.setName(null);
        userService.updateUser(user2, user2.getId());

        User userFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getId(), userFromDbAfterUpdate.getId());
        assertEquals(userFromDbAfterUpdate.getName(), user1.getName());
        assertEquals(userFromDbAfterUpdate.getEmail(), user2.getEmail());
    }

    @Test
    void updateUser_WhenUserEmailIsNull_ThenReturnedUpdatedUser() {
        User addedUser = userService.addUser(user1);

        List<User> beforeUpdateUsers = userService.getListUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, addedUser.getId());

        User userFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getName(), user1.getName());
        assertEquals(userFromDbBeforeUpdate.getEmail(), user1.getEmail());

        user2.setId(addedUser.getId());
        user2.setEmail(null);
        userService.updateUser(user2, user2.getId());

        User userDtoFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getId(), userDtoFromDbAfterUpdate.getId());
        assertEquals(userDtoFromDbAfterUpdate.getName(), user2.getName());
        assertEquals(userDtoFromDbAfterUpdate.getEmail(), user1.getEmail());
    }

    @Test
    void updateUser_WhenAllIsOk_ThenReturnUpdatedUser() {
        User addedUser = userService.addUser(user1);

        List<User> beforeUpdateUsers = userService.getListUsers();
        Long id = beforeUpdateUsers.stream()
                .filter(u -> u.getEmail().equals(user1.getEmail()))
                .findFirst()
                .map(User::getId).orElse(null);
        assertNotNull(id);
        assertEquals(id, addedUser.getId());

        User userFromDbBeforeUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getName(), user1.getName());
        assertEquals(userFromDbBeforeUpdate.getEmail(), user1.getEmail());

        user2.setId(addedUser.getId());
        userService.updateUser(user2, user2.getId());

        User userFromDbAfterUpdate = userService.getUserById(id);

        assertEquals(userFromDbBeforeUpdate.getId(), userFromDbAfterUpdate.getId());
        assertEquals(userFromDbAfterUpdate.getName(), userDto2.getName());
        assertEquals(userFromDbAfterUpdate.getEmail(), userDto2.getEmail());
    }

    @Test
    void getListUsers() {
        List<User> listUsers = List.of(user1, user2);
        userService.addUser(user1);
        userService.addUser(user2);

        List<User> result = userService.getListUsers();

        assertEquals(listUsers.size(), result.size());
        for (User user : listUsers) {
            assertThat(result, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(user.getName())),
                    hasProperty("email", equalTo(user.getEmail()))
            )));
        }
    }

    @Test
    void userMapperTest_ToUser_WhenAllIsOk() {
        user1 = toUser(userDto1);
        assertEquals(userDto1.getId(), user1.getId());
        assertEquals(userDto1.getName(), user1.getName());
        assertEquals(userDto1.getEmail(), user1.getEmail());
    }

    @Test
    void userMapperTest_ToUser_WhenAllFieldsUserDtoAreNull() {
        userNull = toUser(userDtoAllFieldsNull);
        assertEquals(userDtoAllFieldsNull.getId(), userNull.getId());
        assertEquals(userDtoAllFieldsNull.getName(), userNull.getName());
        assertEquals(userDtoAllFieldsNull.getEmail(), userNull.getEmail());
    }

    @Test
    void userMapperTest_ToUserDto_whenAllIsOk() {
        userDto1 = toUserDto(user1);
        assertEquals(user1.getId(), userDto1.getId());
        assertEquals(user1.getName(), userDto1.getName());
        assertEquals(user1.getEmail(), userDto1.getEmail());
    }

    @Test
    void userMapperTest_ToUserDto_whenAllFieldsAreNull() {
        userDtoNull = toUserDto(userAllFieldsNull);
        assertEquals(userAllFieldsNull.getId(), userDtoNull.getId());
        assertEquals(userAllFieldsNull.getName(), userDtoNull.getName());
        assertEquals(userAllFieldsNull.getEmail(), userDtoNull.getEmail());
    }

}