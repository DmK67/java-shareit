package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name")
    private String name;
    @Column()
    private String description;
    @Column()
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @Column(name = "request")
    private Long requestId;

    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

}

