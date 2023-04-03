package com.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Course;
import com.model.User;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Integer>  {// Integer
	public Optional<Course> findByCourseId(String courseId);
	@Transactional
	public void deleteByCourseId(String courseId);
	public List<Course> findByInstructor(User user);
	public Optional<List<Course>> findBySubscriber(User user);
	//public List<Course> findBySubscriber();
	//public void deleteByInstructor(User user);
}
