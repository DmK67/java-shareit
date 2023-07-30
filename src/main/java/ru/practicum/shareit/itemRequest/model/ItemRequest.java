package ru.practicum.shareit.itemRequest.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itemRequest", schema = "public")
public class ItemRequest {

    /**
     * id — уникальный идентификатор запроса;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    /**
     * description — текст запроса, содержащий описание требуемой вещи;
     */
    @Column()
    private String description;
    /**
     * requestor — пользователь, создавший запрос;
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor")
    private User requestor;
    /**
     * created — дата и время создания запроса.
     */
    @Column(name = "created")
    private LocalDateTime created;
    @Transient
    private List<Item> items;
}
