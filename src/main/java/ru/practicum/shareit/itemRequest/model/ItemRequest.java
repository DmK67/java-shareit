package ru.practicum.shareit.itemRequest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itemRequest", schema = "public")
public class ItemRequest {
    //id — уникальный идентификатор запроса;
    //description — текст запроса, содержащий описание требуемой вещи;
    //requestor — пользователь, создавший запрос;
    //created — дата и время создания запроса.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @Column()
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor")
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    private List<Item> items;
}
