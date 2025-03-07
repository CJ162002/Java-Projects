package com.manager.smartcontactmanager.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manager.smartcontactmanager.Entity.User;

@Repository
public interface UserRepositry extends JpaRepository<User, Long> {

	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);

}
