package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
//@RequiredArgsConstructor
@Validated
public class User {
    //id — уникальный идентификатор пользователя;
    //name — имя или логин пользователя;
    //email — адрес электронной почты (учтите, что два пользователя не могут
    //иметь одинаковый адрес электронной почты).
    @Min(1)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
