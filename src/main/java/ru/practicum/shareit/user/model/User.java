package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Validated
@Entity
@Table(name = "users", schema = "public")
public class User {
    /**
     * id — уникальный идентификатор пользователя;
     * name — имя или логин пользователя;
     * email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Min(1)
    private Long id;
    @NotBlank
    @Column
    private String name;
    @NotBlank
    @Email
    @Column
    private String email;

}
