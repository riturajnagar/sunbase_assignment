package com.rj.sunbase.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rj.sunbase.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	@Query(value = "select * from user where username=?1", nativeQuery = true)
	 User findByUsername(String username);

}
