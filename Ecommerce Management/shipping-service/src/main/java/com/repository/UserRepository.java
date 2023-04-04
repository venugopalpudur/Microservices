package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.User;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByEmail(String email);
	//public User findByEmail(String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByUserId(String userId);
	//public Optional<List<User>> findByRegisteredCourses(Course course);
	//public void deleteByCreatedCourses(String userId);
}
