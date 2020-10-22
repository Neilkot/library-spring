package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private static final RoleType ADMIN = RoleType.ADMIN;
    private static final Integer TYPE_ADMIN_ID = 3;

    @Test
   public void shouldReturnRoleByType() throws Exception {
        Optional<Role> role = roleRepository.findByType(ADMIN);
        assertTrue(role.isPresent());
        assertEquals(TYPE_ADMIN_ID, role.get().getId());

    }
}
