package com.epam.lab.exam.library.repository;

import com.epam.lab.exam.library.model.BookRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql", "/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRequestRepositoryTest {
    @Autowired
    private BookRequestRepository bookRequestRepository;

    private static final Integer READER_ID = 1;
    private static final int PAGE = 0;
    private static final int SIZE = 25;
    private static final int COUNT_BOOK_REQUESTS = 1;
    private static final Integer BOOK_REQUEST_ID = 1;

    @Test
    public void shouldFindByUserId(){
        List<BookRequest> bookRequests = bookRequestRepository.findByUserId(READER_ID, PageRequest.of(PAGE, SIZE));
        assertEquals(COUNT_BOOK_REQUESTS, bookRequests.size());
        assertEquals(BOOK_REQUEST_ID, bookRequests.get(0).getId());
    }
}
