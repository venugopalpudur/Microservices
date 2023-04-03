package com.service.impls;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.Instanceof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.dto.PaymentDTO;
import com.dto.ResponseDTO;
import com.dto.ReviewDTO;
import com.dto.RoleDTO;
import com.entity.Course;
import com.entity.Notifications;
import com.entity.Review;
import com.entity.UserRole;
import com.feignclient.EmailService;
import com.feignclient.PaymentService;
import com.funcs.Funcs;
import com.model.Roles;
import com.model.User;
import com.repository.CourseRepository;
import com.repository.ReviewRepository;
import com.repository.UserRepository;
import com.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private Funcs funcs;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PaymentService paymentService;

	public Integer savePayment(PaymentDTO payment, String courseId, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		if (userRepository.findByEmail(userPrincipal.getUsername()).isPresent()) {
			User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
			payment.setUserId(user.getUserId());
			payment.setCourseId(courseId);
			payment.setEmail(user.getEmail());
			int id = (Integer) paymentService.savePayment(payment).getBody();
			return id;
		}
		return null;
	}

	@Override
	public Course subscribeCourse(String courseId, int payId, Authentication authentication) {
		// do i have to set registeredCourses in user and subscriber in Course classes ?
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		if (userRepository.findByEmail(userPrincipal.getUsername()).isPresent()
				&& courseRepository.findByCourseId(courseId).isPresent()) {
			User user = userRepository.findByEmail(userPrincipal.getUsername()).get();

			if (paymentService.getPayment(payId, user.getUserId(), courseId)) {
				Course toSubscribe = courseRepository.findByCourseId(courseId).get();
				List<User> usr = new ArrayList<>();
				usr.addAll(toSubscribe.getSubscriber());
				usr.add(user);
				toSubscribe.setEnrollments(toSubscribe.getEnrollments() + 1);
				toSubscribe.setSubscriber(usr);
				Course course = courseRepository.save(toSubscribe);

				Roles roles = new Roles();
				roles.setUserRole(UserRole.STUDENT);
				roles.setDescription("Student role allows to access premium courses");

				//Iterator<Roles> iterator = user.getRoles().iterator();
				Set<Roles> listOfRoles = new HashSet<>();
				listOfRoles.addAll(user.getRoles());
				listOfRoles.add(roles);
				user.setRoles(new ArrayList<>(listOfRoles));
				userRepository.save(user);
				/*while (iterator.hasNext()) {
					Roles role = iterator.next();
					if (!role.equals(roles)) {

						break;
					}
					//listOfRoles.remove(role);
				}*/
				
				// make payment here & set role to STUDENT
				// send mail to user and admin with invoice
				funcs.sendNotify("Your enrollment to the course : " + toSubscribe.getTitle() + " is successfull.",
						user);
				funcs.sendNotify("Your course : " + toSubscribe.getTitle() + " has got new enrollment. Student Name : "
						+ userPrincipal.getUsername() + ".", toSubscribe.getInstructor()); // send notifications to both
																							// user and admin [DONE]
				return course;
			}
			return null;
		}
		return null;
	}

	@Override
	public String unSubscribeCourse(String courseId, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		if (userRepository.findByEmail(userPrincipal.getUsername()).isPresent()
				&& courseRepository.findByCourseId(courseId).isPresent()) {
			User removeUser = userRepository.findByEmail(userPrincipal.getUsername()).get();
			Course course = courseRepository.findByCourseId(courseId).get();
			List<User> usr = new ArrayList<>();
			Iterator<User> iterator = course.getSubscriber().iterator();
			while (iterator.hasNext()) {
				User user = iterator.next();
				if (user.getUserId().compareTo(removeUser.getUserId()) == 0) {
					iterator.remove();
					break;
				}
				usr.add(user);
			}
			course.setSubscriber(usr);
			courseRepository.save(course);
			// send mail & notifications to both user and admin
			// amount cannot be refunded
			funcs.sendNotify("Your Un-enrollment to the course : " + course.getTitle() + " is successfull.",
					removeUser);
			funcs.sendNotify("Student Name : " + userPrincipal.getUsername() + ", has un-enrolled from your course : "
					+ course.getTitle(), course.getInstructor());
			return "Course removed.";
		}
		return null;
	}

	@Override
	public List<Course> addReviewToCourse(String courseId, ReviewDTO review, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		if (courseRepository.findByCourseId(courseId).isPresent()
				&& userRepository.findByEmail(userPrincipal.getUsername()).isPresent()) {
			Course course = courseRepository.findByCourseId(courseId).get();
			Review reviews = new Review();
			reviews.setGrade(review.getGrade());
			reviews.setComment(review.getComment());
			reviews.setUser(userRepository.findByEmail(userPrincipal.getUsername()).get());
			reviews.setCreateDate(LocalDate.now());
			reviews.setUpdatedDate(null);
			List<Review> rw = new ArrayList<>();
			rw.addAll(course.getReviews());
			rw.add(reviews);
			course.setReviews(rw);
			courseRepository.save(course);
			// send likes/comment notification to author [DONE]
			funcs.sendNotify("Your course : \"" + course.getTitle() + "\" has got comments and review from User : \""
					+ userPrincipal.getUsername() + "\".", course.getInstructor());
			return courseRepository.findAll();
		}
		return null;
	}

	@Override
	public List<Course> updateReviewToCourse(int reviewId, String courseId, ReviewDTO review,
			Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (reviewRepository.findById(reviewId).get().getUser().getUserId().compareTo(user.getUserId()) == 0) {
			if (courseRepository.findByCourseId(courseId).isPresent()
					&& userRepository.findByUserId(user.getUserId()).isPresent() && review != null) {
				Review rw = new Review();
				rw.setId((long) reviewId);
				rw.setGrade(review.getGrade());
				rw.setComment(review.getComment());
				rw.setUser(user);
				rw.setCreateDate(reviewRepository.findById(reviewId).get().getCreateDate());
				rw.setUpdatedDate(reviewRepository.findById(reviewId).get().getUpdatedDate());
				reviewRepository.save(rw);
				return courseRepository.findAll();
			}

		}
		return null;
	}

	@Override
	public String deleteReview(int reviewId, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
		if (reviewRepository.findById(reviewId).get().getUser().getUserId().compareTo(user.getUserId()) == 0
				&& userRepository.findByUserId(user.getUserId()).isPresent()) {
			reviewRepository.deleteById(reviewId);
			return "Deleted review";
		}
		return "Review cannot be deleted";
	}

	@Override
	public String assignRoles(String userId, RoleDTO roleDTO, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		// System.out.println(userRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(()
		// -> new UsernameNotFoundException(MessageFormat.format("User with email {0}
		// cannot be found.", "agsdg"))));
		//for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
		//Iterator itr = userPrincipal.getAuthorities().iterator();
		 //while(itr.hasNext()) {
			 //GrantedAuthority authority = (GrantedAuthority) itr.next();
		for (Iterator iterator = userPrincipal.getAuthorities().iterator(); iterator.hasNext();) {
			GrantedAuthority authority = (GrantedAuthority) iterator.next();
			System.out.println(authority);
			
		
			//System.out.println( userPrincipal.getAuthorities().toString());
			if (authority.getAuthority().compareTo("ROLE_ADMIN") == 0
					|| authority.getAuthority().compareTo("ROLE_MODERATOR") == 0) {
				User user = userRepository.findByUserId(userId).get();
				if (user.getRoles().isEmpty()) {
					userRepository.save(funcs.setRoles(user, roleDTO));
					// send mail & notifications to both user and admin
					funcs.sendNotify("You profile has been assigned with role : " + roleDTO.getUserRole()
							+ ". by the admin : " + userPrincipal.getUsername(), user);
					funcs.sendNotify(
							"You assigned with role : " + roleDTO.getUserRole() + " to the user : "
									+ userPrincipal.getUsername(),
							userRepository.findByEmail(userPrincipal.getUsername()).get());

					return "Role is set for user " + user.getName();
				} else {
					for (Roles role : user.getRoles()) {
						if (role.getUserRole().compareTo(roleDTO.getUserRole()) != 0) {
							userRepository.save(funcs.setRoles(user, roleDTO));
							// send mail
							funcs.sendNotify("You profile has been assigned with role : " + roleDTO.getUserRole()
									+ ". by the admin : " + userPrincipal.getUsername(), user);
							funcs.sendNotify(
									"You assigned with role : " + roleDTO.getUserRole() + " to the user : "
											+ userPrincipal.getUsername(),
									userRepository.findByEmail(userPrincipal.getUsername()).get());

							return "Role is set for user " + user.getName();
						}
					}
				}
				return "Your are admin";
			} 
		}
		return null;
	}

	@Override
	public String revokeRoles(String userId, RoleDTO roleDTO, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			if (authority.getAuthority().compareTo("ROLE_ADMIN") == 0
					|| authority.getAuthority().compareTo("ROLE_MODERATOR") == 0) {
				User user = userRepository.findByUserId(userId).get();
				if (user.getRoles().isEmpty()) {
					return "No mentioned Role was set for user " + user.getName();
				} else {
					List<Roles> roles = new ArrayList<>();
					Iterator<Roles> iterator = user.getRoles().iterator();
					while (iterator.hasNext()) {
						Roles role = iterator.next();
						if (role.getUserRole().compareTo(roleDTO.getUserRole()) == 0) {
							iterator.remove();
							Authentication auth = SecurityContextHolder.getContext().getAuthentication();
							List<GrantedAuthority> updatedAuthorities = auth.getAuthorities().stream()
									.filter(r -> !role.getUserRole().equals(r.getAuthority()))
									.collect(Collectors.toList());
							Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
									auth.getCredentials(), updatedAuthorities);
							SecurityContextHolder.getContext().setAuthentication(newAuth);
							break;
						}
						roles.add(role);
					}
					user.setRoles(roles);
					userRepository.save(user);

					funcs.sendNotify("You profile has been revoked with role : " + roleDTO.getUserRole()
							+ ". by the admin : " + userPrincipal.getUsername(), user);
					funcs.sendNotify(
							"You revoked with role : " + roleDTO.getUserRole() + " to the user : "
									+ userPrincipal.getUsername(),
							userRepository.findByEmail(userPrincipal.getUsername()).get());

					// send mail & notifications to both user and admin
					return "Role is deleted for user " + user.getName();
				}
			}
			return null;
		}
		return null;
	}
}
