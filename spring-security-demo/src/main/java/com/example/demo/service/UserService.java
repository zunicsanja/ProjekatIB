package com.example.demo.service;

import java.util.List;

import com.example.demo.model.User;

/**
 * Created by fan.jin on 2016-10-15.
 */
public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
}
