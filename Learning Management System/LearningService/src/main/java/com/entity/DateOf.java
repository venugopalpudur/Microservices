package com.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "date")
public class DateOf {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDateTime updatedTime;
	
	private long updateCount;

}
