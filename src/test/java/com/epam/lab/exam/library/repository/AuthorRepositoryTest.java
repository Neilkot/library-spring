package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthorRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void shouldFindAuthorByName() throws Exception {
        Author martin = authorRepository.findByName("Robert Martin").get();
        assertNotNull(martin);
        assertEquals("Robert Martin", martin.getName());
    }
}

