package com.sha.gateway.model.service;

import com.sha.gateway.model.entity.User;
import com.sha.gateway.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public abstract class AbstractUserService implements EntityService<User, Integer>
{

    @Autowired
    protected UserRepository repository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public abstract Optional<User> findByUserName(String username);
}
