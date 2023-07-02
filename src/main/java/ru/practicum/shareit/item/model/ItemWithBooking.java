package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ItemWithBooking {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingForItemDto lastBooking;

    private BookingForItemDto nextBooking;

    //private List<CommentReceiving> comments;

//    public void addBooking(SmallBooking lastBookingNew, SmallBooking nextBookingNew) {
//        if (lastBookingNew == null) {
//            lastBooking = nextBookingNew;
//            nextBooking = null;
//        } else {
//            lastBooking = lastBookingNew;
//            nextBooking = nextBookingNew;
//        }
//    }
//
//    public void addComments(List<CommentReceiving> list) {
//        if (list.isEmpty()) {
//            comments = List.of();
//        } else {
//            comments = list;
//        }
//    }
}
