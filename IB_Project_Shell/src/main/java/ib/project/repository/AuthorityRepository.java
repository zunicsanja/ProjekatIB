package ib.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ib.project.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
