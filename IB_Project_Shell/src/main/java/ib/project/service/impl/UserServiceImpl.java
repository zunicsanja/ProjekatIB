package ib.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ib.project.model.Authority;
import ib.project.model.User;
import ib.project.repository.AuthorityRepository;
import ib.project.repository.UserRepository;
import ib.project.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername( String username ) throws UsernameNotFoundException {
        User u = userRepository.findByUsername( username );
        return u;
    }

    public User findById( Long id ) throws AccessDeniedException {
        User u = userRepository.findOne( id );
        return u;
    }

    public List<User> findAll() throws AccessDeniedException {
        List<User> result = userRepository.findAll();
        return result;
    }
}

