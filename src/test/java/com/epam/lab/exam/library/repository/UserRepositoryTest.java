package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final int PAGE = 0;
    private static final int SIZE = 25;
    private static final Integer USER_ID = 2;
    private static final String LOGIN = "admin";
    private static final int COUNT_LIBRARIAN_ROLE = 2;


    @Test
    @Transactional
    public void shouldUpdateReaderIsBlockedField() throws Exception {
        Optional<User> optionalUserBefore = userRepository.findById(USER_ID);
        assertTrue( optionalUserBefore.isPresent());
        User userBefore= optionalUserBefore.get();
       assertFalse(userBefore.getIsBlocked());
       userRepository.updateIsBlocked(USER_ID, !userBefore.getIsBlocked());
        //userRepository.updateIsBlocked(2, true);
        Iterator<User> iterator = userRepository.findAll().iterator();
        while( iterator.hasNext()){
            System.out.println("˜˜˜˜˜˜˜˜");
            System.out.println(iterator.next());
            System.out.println("˜˜˜˜˜˜˜˜");
        }
        Optional<User> byId = userRepository.findById(2);
        System.out.println("User after: " + byId.get().toString());
    }

    @Test
    public void shouldFindByLogin() throws Exception {
        Optional<User> user = userRepository.findByLogin(LOGIN);
        assertNotNull(user.get());
        assertEquals(LOGIN, user.get().getLogin());
    }

    @Test
    public void shouldFindByRole() throws Exception {
        Optional<Role> optionalRole = roleRepository.findByType(RoleType.LIBRARIAN);
        assertNotNull(optionalRole.get());
        Role role = optionalRole.get();
        List<User> roles = userRepository.findByRole(role, PageRequest.of(PAGE, SIZE));
        assertEquals(COUNT_LIBRARIAN_ROLE, roles.size() );
    }

    @Test
    public void shouldCountLibrarians() throws Exception {
        int librarians = (int) userRepository.countLibrarians();
        assertEquals(COUNT_LIBRARIAN_ROLE, librarians );
    }
}
