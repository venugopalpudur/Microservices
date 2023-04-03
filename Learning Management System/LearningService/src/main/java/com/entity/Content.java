/*package com.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "content")
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String paragraph;
	
	private String url;
	
	@OneToMany(targetEntity = FileResource.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "fr_fk", referencedColumnName = "id")
	private List<FileResource> fileResources;

	public Content(String paragraph, String url) {
		super();
		this.paragraph = paragraph;
		this.url = url;
	}
	
}*/
