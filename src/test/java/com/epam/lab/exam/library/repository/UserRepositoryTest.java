package com.epam.lab.exam.library.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@ActiveProfiles("test")
public class UserRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void shouldFindByRole() {
		Role role = roleRepository.save(Role.builder().type(RoleType.ADMIN).build());
		System.err.println("TAG saved: " + role);

		User user = User.builder().login("login").firstName("firstName").lastName("lastName").checksum("checksum")
				.role(role).build();
		user = userRepository.save(user);
		System.err.println("TAG users_role: " + user.getRole());
		List<User> users = userRepository.findByRole(user.getRole(), PageRequest.of(0, 1));
		assertEquals(1, users.size());
		assertEquals(user, users.get(0));
	}

}
