package com.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Service")
public class Services {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long sid;
	private String serviceName;
	private float fees;
	
	/*@ManyToOne(cascade = CascadeType.ALL, targetEntity = Patient.class)
    @JoinColumn(name = "pid", referencedColumnName = "pid")
	private Patient patient;*/
}
