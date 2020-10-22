package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	@Modifying
	@Query(value = "UPDATE users set is_blocked = ?2 where id = ?1", nativeQuery = true)
	int updateIsBlocked(Integer id, Boolean isBlocked);

	Optional<User> findByLogin(String login);

	List<User> findByRole(Role role, Pageable pageable);

	@Query(value= "SELECT COUNT(*) FROM users u JOIN roles r ON u.role_id = r.id WHERE r.type = 'LIBRARIAN'", nativeQuery=true)
	long countLibrarians();
}
