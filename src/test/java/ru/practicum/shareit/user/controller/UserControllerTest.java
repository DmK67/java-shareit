package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    UserDto userDto;
    User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@ya.ru")
                .build();

        user = toUser(userDto);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser_WhenAllIsOk_ReturnUserDto() throws Exception {

        when(userService.addUser(any())).thenReturn(toUser(userDto));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect((jsonPath("$.email").value(user.getEmail())));
    }

    @Test
    void updateUser_WhenAllIsOk_ReturnUser() throws Exception {
        UserDto updateUserDto = UserDto.builder()
                .id(1L)
                .name("updateUser")
                .email("updateuser@ya.ru")
                .build();
        User updateUser = toUser(updateUserDto);

        when(userService.updateUser(any(), anyLong()))
                .thenReturn(updateUser);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).updateUser(updateUser, 1L);

    }

    @Test
    void getUserById_WhenAllIsOk_ReturnUser() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        String result = mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(user), result);
    }

    @Test
    void getUserById_WhenUserIsNotExist_ThenReturnNotFoundException() throws Exception {
        when(userService.getUserById(100L))
                .thenThrow(new NotFoundException(String.format("Пользователь по id=%d не существует!", 100L)));

        mockMvc.perform(get("/users/{id}", 100L))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", user.getId()))

                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(user.getId());
    }

    @Test
    void getListUsers() throws Exception {
        when(userService.getListUsers())
                .thenReturn(List.of(user));

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(user)), result);
    }
}