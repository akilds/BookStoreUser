package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.UserData;

public interface UserRepository extends JpaRepository<UserData, Integer>{

	Optional<UserData> findByEmailId(String emailId);

	
}
