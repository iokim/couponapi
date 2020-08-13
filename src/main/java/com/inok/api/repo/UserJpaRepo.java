package com.inok.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inok.api.entity.User;

public interface UserJpaRepo extends JpaRepository<User, String> {

	Optional<User> findById(String id);

}
