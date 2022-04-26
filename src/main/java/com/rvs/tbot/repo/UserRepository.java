package com.rvs.tbot.repo;

import com.rvs.tbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.notified = false " +
        "and u.phone IS NOT NULL and u.email IS NOT NULL ")
    List<User> findNewUsers();

    User findByChatId(Long id);
}
