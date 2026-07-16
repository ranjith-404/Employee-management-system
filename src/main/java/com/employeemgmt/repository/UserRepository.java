package com.employeemgmt.repository;

import com.employeemgmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmployee_EmpId(Integer empId);

    boolean existsByUsernameIgnoreCase(String username);
}
