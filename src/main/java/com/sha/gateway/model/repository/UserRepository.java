package com.sha.gateway.model.repository;

import com.sha.gateway.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>
{
    /*
    Optional is primarily intended for use as a method return type
    where there is a clear need to represent "no result,"
    and where using null is likely to cause errors.
    A variable whose type is Optional should never itself be null;
    it should always point to an Optional instance.
     */
    Optional<User> findByUsername(String username);
}
