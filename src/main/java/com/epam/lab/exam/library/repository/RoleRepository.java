package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByType(RoleType type);
}
