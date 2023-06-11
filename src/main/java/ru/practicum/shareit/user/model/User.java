package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Validated
public class User {
    /**
     * id — уникальный идентификатор пользователя;
     * name — имя или логин пользователя;
     * email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
     */

    @Min(1)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

}
