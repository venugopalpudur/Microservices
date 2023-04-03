package com.entity;


import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "file_resource")
public class FileResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String resourceId = UUID.randomUUID().toString().replace("-", "");
	
	private String url;
	
	private String fileType;
	
	private long size;
	
	public FileResource(String name, String resourceId, String url, String fileType, long size) {
		super();
		this.name = name;
		this.resourceId = resourceId;
		this.url = url;
		this.fileType = fileType;
		this.size = size;
		//this.content = content;
	}
}





