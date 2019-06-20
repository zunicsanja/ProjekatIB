package ib.project.service;

import ib.project.model.User;

public interface UserService {
	void save(User user);

    User findByUsername(String username);
}
