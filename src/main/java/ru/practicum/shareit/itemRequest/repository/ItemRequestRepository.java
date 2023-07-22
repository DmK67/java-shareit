package ru.practicum.shareit.itemRequest.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.itemRequest.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> getAllByRequestorIdOrderByCreatedDesc(Long requesterId);

    @Query("select i from ItemRequest i where i.requestor.id <> ?1 order by i.created")
    List<ItemRequest> getItemRequestByRequesterIdIsNotOrderByCreated(Long userId, Pageable pageable);

    // List<ItemRequest> getAllByRequester_Id(Long userId, Pageable pageable);

}
