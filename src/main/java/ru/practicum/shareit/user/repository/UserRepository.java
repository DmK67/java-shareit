package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

//    User addUser(User user);
//
//    User getUserById(long id);
//
//    User updateUser(User user);
//
//    void deleteUser(Long id);
//
//    List<User> getListUsers();

}

