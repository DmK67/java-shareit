package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    //@Column(name = "user_id", nullable = false, updatable = false, unique = true)
    @Column(name = "user_id")
    private Long id;
    @NotBlank
    @Column(name = "user_name")
    private String name;
    @NotBlank
    @Email
    @Column(name = "email", unique = true) // установлен параметр уникальности поля email
    private String email;
}
