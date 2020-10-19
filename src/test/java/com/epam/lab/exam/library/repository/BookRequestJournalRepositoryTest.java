package com.epam.lab.exam.library.repository;


import com.epam.lab.exam.library.model.BookRequestJournal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql", "/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRequestJournalRepository {
    private static final int PAGE = 0;
    private static final int SIZE = 25;
    private static final Integer BOOK_REQUEST_ID = 1;

    @Autowired
    BookRequestJournalRepository bookRequestJournalRepository;

    @Test
    @Sql(value = {"/create-all-before.sql", "/create-not-approved-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnPendingNonBlockedUserRequests() throws Exception {
        Optional<BookRequestJournal> byBookRequestId = bookRequestJournalRepository.findByBookRequestId(1);
        assertNotNull(byBookRequestId.get());
    }
}
