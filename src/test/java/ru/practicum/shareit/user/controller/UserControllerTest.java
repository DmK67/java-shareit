package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


//    @Test
//    void addUser_WhenUserDtoNotValid_ThenReturnBadRequest() throws Exception {
//        UserDto userDto2 = new UserDto(2L, "", "user2@email.ru");
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto2))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never()).addUser(toUser(userDto));
//    }

    @Test
    void updateUser() {

    }

    @Test
    void testUpdateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getListUsers() {
    }
}