package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;


/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
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
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "email", unique = true) // установлен параметр уникальности поля email
    private String email;

}
