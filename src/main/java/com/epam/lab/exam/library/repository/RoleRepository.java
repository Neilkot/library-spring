package com.epam.lab.exam.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByType(@Param("type") RoleType type);
}
