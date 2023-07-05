package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //List<Item> findAllByOwnerOrderById(User owner); // Метод поиска списка вещей по id владельца

    List<Item> findAllByOwnerId(Long owner); // Метод поиска списка вещей по id владельца

    @Query("SELECT i FROM Item i JOIN FETCH i.owner o " +
            "where i.available = true " +
            "and (upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItemsByNameContaining(String text);

}
