package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.RequestType;
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
public class BookRequestTypeRepositoryTest {
    @Autowired
    private BookRequestTypeRepository bookRequestTypeRepository;

    private static final RequestType ABONEMENT = RequestType.ABONEMENT;
    private static final Integer TYPE_ABONEMENT_ID = 1;

    @Test
    public void shouldFindByType() throws Exception {
        Optional<BookRequestType> type = bookRequestTypeRepository.findByType(ABONEMENT);
        assertTrue(type.isPresent());
        assertEquals(TYPE_ABONEMENT_ID, type.get().getId());
    }

}
