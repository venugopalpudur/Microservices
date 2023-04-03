package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}
