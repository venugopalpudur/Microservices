package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.FileResource;

@Repository
public interface ResourceRepository extends JpaRepository<FileResource, Integer> {

	public FileResource findByResourceId(String uuid);
}
