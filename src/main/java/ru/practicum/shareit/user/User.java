package ru.practicum.shareit.user;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    //id — уникальный идентификатор пользователя;
    //name — имя или логин пользователя;
    //email — адрес электронной почты (учтите, что два пользователя не могут
    //иметь одинаковый адрес электронной почты).
    private long id;
    private String name;
    private String email;

}
