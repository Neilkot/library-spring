package com.epam.lab.exam.library.repository;


import com.epam.lab.exam.library.model.BookRequestJournal;
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
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-all-before.sql", "/create-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRequestJournalRepositoryTest {
    private static final int PAGE = 0;
    private static final int SIZE = 25;
    private static final Integer BOOK_REQUEST_ID = 1;
    private static final Integer READER_ID = 1;
    private static final long COUNT_APPROVED_REQUESTS = 1;

    @Autowired
    private BookRequestJournalRepository bookRequestJournalRepository;

    @Test
    @Sql(value = {"/create-all-before.sql", "/create-not-approved-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shoulFindByBookRequestId() throws Exception {
        Optional<BookRequestJournal> byBookRequestId = bookRequestJournalRepository.findByBookRequestId(1);
        assertNotNull(byBookRequestId.get());
    }

    @Test
    @Sql(value = {"/create-all-before.sql", "/create-not-approved-book-request-before.sql", "/block-user-before.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldReturnPendingNonBlockedUserRequests() throws Exception {
        List<BookRequestJournal> pendingNonBlockedReaderRequests = bookRequestJournalRepository.getPendingNonBlockedReaderRequests(PageRequest.of(PAGE, SIZE));
        assertTrue( pendingNonBlockedReaderRequests.isEmpty());

    }

    @Test
    public void shouldReturnPendingNonBlockedReaderRequests() throws Exception {
        List<BookRequestJournal> pendingNonBlockedReaderRequests = bookRequestJournalRepository.getApprovedNonBlockedReaderRequests(PageRequest.of(PAGE, SIZE));
        assertFalse(pendingNonBlockedReaderRequests.isEmpty());
    }

    @Test
    public void shouldReturnReaderApprovedRequests() throws Exception {
        List<BookRequestJournal> userApprovedRequests = bookRequestJournalRepository.getUserApprovedRequests(READER_ID, PageRequest.of(PAGE, SIZE));
        assertFalse(userApprovedRequests.isEmpty());
    }


    @Test
    public void shouldCountApprovedReaderRequests() throws Exception {
        long actual = bookRequestJournalRepository.countApprovedReaderRequests(READER_ID);
        assertEquals(COUNT_APPROVED_REQUESTS , actual);
    }

    @Test
    @Sql(value = {"/create-all-before.sql", "/create-not-approved-book-request-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/create-all-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCountPendingReaderRequests() throws Exception {
        long actual = bookRequestJournalRepository.countPending();
        assertEquals(COUNT_APPROVED_REQUESTS , actual);
    }

    @Test
    public void shouldCountApprovedRequests() throws Exception {
        long actual = bookRequestJournalRepository.countApproved();
        assertEquals(COUNT_APPROVED_REQUESTS , actual);
    }


}
