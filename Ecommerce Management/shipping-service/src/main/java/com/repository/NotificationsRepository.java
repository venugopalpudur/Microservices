package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer>{

}
