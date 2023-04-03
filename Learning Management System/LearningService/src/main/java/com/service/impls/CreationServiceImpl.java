package com.service.impls;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dto.CourseDTO;
import com.entity.Course;
import com.feignclient.EmailService;
import com.funcs.Funcs;
import com.model.Roles;
import com.model.User;
import com.repository.CourseRepository;
import com.repository.UserRepository;
import com.service.CreationService;

@Service
public class CreationServiceImpl implements CreationService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Funcs funcs;

	@Override
	public Course addCourse(CourseDTO courseDTO, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (userRepository.findByUserId(user.getUserId()).isPresent() && courseDTO != null) {
			Course course = new Course();
			System.out.println(courseDTO.getDescription());
			course.setDescription(courseDTO.getDescription());
			course.setTitle(courseDTO.getTitle());
			//course.setContent(courseDTO.getContent());
			//course.setEnrollments(courseDTO.getEnrollments());
			course.setInstructor(userRepository.findByUserId(user.getUserId()).get());
			
			funcs.sendNotify("New course : "+course.getTitle()+" is created.", user);			
			
			return courseRepository.save(course);
		}
		return null;
	}

	@Override
	public List<Course> getCourses(String userId) {
		if (userRepository.findByUserId(userId).isPresent()) {
			return courseRepository.findByInstructor(userRepository.findByUserId(userId).get());
		}
		return null;
	}

	@Override
	public List<Course> getRegisteredCourses(String userId) {
		if (userRepository.findByUserId(userId).isPresent()) {
			return courseRepository.findBySubscriber(userRepository.findByUserId(userId).get()).get();
		}
		return null;
	}

	@Override
	public List<User> getSubscribers(String courseId, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (courseRepository.findByCourseId(courseId).isPresent()) {
			for (Course course : user.getCreatedCourses()) {
				if (course.getCourseId().compareTo(courseId) == 0) {
					return userRepository.findByRegisteredCourses(courseRepository.findByCourseId(courseId).get())
							.get();
				}
			}
		}
		return null;
	}

	@Override
	public Course updateCourse(CourseDTO courseDTO, String courseId, Authentication authentication) {

		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (courseRepository.findByCourseId(courseId).isPresent() && courseDTO != null) {
			for (Course course : user.getCreatedCourses()) {
				if (course.getCourseId().compareTo(courseId) == 0) {
					Course toUpdateCourse = courseRepository.findByCourseId(courseId).get();
					toUpdateCourse.setTitle(courseDTO.getTitle());
					toUpdateCourse.setDescription(courseDTO.getDescription());
					//toUpdateCourse.setContent(courseDTO.getContent());
					toUpdateCourse.setInstructor(user);
					funcs.sendNotify("Course : "+course.getTitle()+" is updated.", user);
					return courseRepository.save(toUpdateCourse);
				}
			}
		}
		return null;
	}

	@Override
	public String deleteCourse(String courseId, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (courseRepository.findByCourseId(courseId).isPresent()) {
			for (Course course : user.getCreatedCourses()) {
				if (course.getCourseId().compareTo(courseId) == 0) {
					System.out.println(course);
					courseRepository.deleteById(course.getId());
					//courseRepository.deleteByCourseId(courseId);
					funcs.sendNotify("course : "+course.getTitle()+" is deleted.", user);
					return "Course Deleted";
				}	
			}
			return "Course cannot be deleted";
		}
		return "Course cannot be deleted";
	}

	@Override
	public String deleteAllCourses(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Optional<User> user = userRepository.findByEmail(userPrincipal.getUsername());
		if (user.isPresent() && !user.get().getCreatedCourses().isEmpty()) {
			
			Iterator<Course> iterator = user.get().getCreatedCourses().iterator();
			while (iterator.hasNext()) {
				Course course = iterator.next();
				courseRepository.deleteByCourseId(course.getCourseId());
				funcs.sendNotify("Ccourse : "+course.getTitle()+" is deleted.", user.get());
			}
			return "All courses deleted";
			
		}
		return "All courses cannot be deleted";
	}

	@Override
	public List<Course> getAllCourses() {
		return courseRepository.findAll();
	}

}
