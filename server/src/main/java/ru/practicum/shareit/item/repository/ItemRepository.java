package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long userId, Pageable page); // Метод поиска списка вещей по id владельца

    @Query("select i from Item  as i where i.requestId in :list")
    List<Item> findAllByRequestIds(@Param("list") List<Long> list);

    @Query("SELECT i FROM Item i JOIN FETCH i.owner o " +
            "where i.available = true " +
            "and (upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItemsByNameContaining(String text, Pageable page);

    @Query("select i from Item as i where i.requestId = ?1")
    List<Item> findAllByRequestId(long requestId);
}
