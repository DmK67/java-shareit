package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Validated
@Entity
@Table(name = "items", schema = "public")
public class Item {
    /**
     * id — уникальный идентификатор вещи;
     * name — краткое название; description — развёрнутое описание;
     * available — статус о том, доступна или нет вещь для аренды;
     * owner — владелец вещи;
     * request — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка
     * на соответствующий запрос.
     */

    //@Entity, @Table, @Column, @Id. Для поля status в классе Booking вам также пригодится @Enumerated.
    // Добавьте соответствующие аннотации для сущностей.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private String name;
    @Column()
    private String description;
    @Column()
    private Boolean available;
    @Column()
    private Long owner;
    @Transient
    private ItemRequest request;

    public Item(Long id, String name, String description, Boolean available, Long owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}

