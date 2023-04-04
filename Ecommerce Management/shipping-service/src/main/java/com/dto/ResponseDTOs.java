package com.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import com.model.User;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Component
public class ResponseDTOs {

	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime timestamp;
	private int HttpStatusCode;
	private String Details;
	private List<User> users;
	private ResponseDto responseDto;
}
