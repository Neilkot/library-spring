package com.epam.lab.exam.library.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@ActiveProfiles("test")
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void shouldSave() {
		Role role = Role.builder().type(RoleType.ADMIN).build();
		Role actual = roleRepository.save(role);
		System.err.println("~~~~~~~~~" + actual + "~~~~~~~~~" );
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(role.getType(), actual.getType());
	}

	@Test
	public void shouldFindByType() {
		Role role = roleRepository.save(Role.builder().type(RoleType.ADMIN).build());
		Role actual = roleRepository.findByType(RoleType.ADMIN).orElse(null);
		assertEquals(role, actual);
	}
}
