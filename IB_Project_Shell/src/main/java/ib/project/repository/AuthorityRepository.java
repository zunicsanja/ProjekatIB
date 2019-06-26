package ib.project.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ib.project.model.Authority;
import ib.project.model.User;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
