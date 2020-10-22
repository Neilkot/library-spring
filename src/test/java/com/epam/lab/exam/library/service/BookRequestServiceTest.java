package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.model.*;
import com.epam.lab.exam.library.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookRequestServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    BookItemRepository bookItemRepository;
    @Mock
    BookRequestRepository bookRequestRepository;
    @Mock
    BookRequestTypeRepository bookRequestTypeRepository;
    @Mock
    BookRequestJournalRepository bookRequestJournalRepository;
    @Mock
    ConfigService configService;
    @InjectMocks
    BookRequestService bookRequestService;

    @Test(expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenUserNotFoundOnSubmitting() throws Exception {
        bookRequestService.submit(new SubmitRequestDTO());
    }

    @Test(expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenSubmittingAndUserIsBlocked() throws Exception {
        SubmitRequestDTO dto = SubmitRequestDTO.builder().bookId(1).userId(1).build();
        User user = User.builder().isBlocked(true).build();
        when(userRepository.findById(dto.getUserId())).thenReturn(Optional.of(user));
        bookRequestService.submit(dto);
    }

    @Test (expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenSubmittingAndNoAvailableBook() throws Exception {
        User user = User.builder().isBlocked(false).build();
        SubmitRequestDTO dto = SubmitRequestDTO.builder().bookId(1).userId(1).build();

        when(userRepository.findById(dto.getUserId())).thenReturn(Optional.of(user));
        when(bookItemRepository.findAvailableByBookId(dto.getBookId())).thenReturn(new ArrayList<>());
        bookRequestService.submit(dto);
    }

    @Test
    public void shouldSubmitRequest() throws Exception {
        User user = User.builder().isBlocked(false).build();
        SubmitRequestDTO dto = SubmitRequestDTO.builder().bookId(1).userId(1).requestType(RequestType.ABONEMENT).build();
        BookItem bookItem = BookItem.builder().build();
        BookRequestType findByType = BookRequestType.builder().type(RequestType.ABONEMENT).build();
        when(userRepository.findById(dto.getUserId())).thenReturn(Optional.of(user));
        when(bookItemRepository.findAvailableByBookId(dto.getBookId())).thenReturn(Arrays.asList(bookItem));
        when(bookRequestTypeRepository.findByType(dto.getRequestType())).thenReturn(Optional.of(findByType));
        BookRequest bookRequest = BookRequest.builder().bookItem(bookItem).bookRequestType(findByType).user(user)
                .build();

        BookRequestJournal bookRequestJournal = BookRequestJournal.builder().bookRequest(bookRequest).createDate(Instant.now()).build();
        when(bookRequestRepository.save(bookRequest)).thenReturn(bookRequest);
        bookRequestService.submit(dto);
        verify(bookRequestRepository, times(1)).save(bookRequest);
//TODO: Instatn.now() mock
        //verify(bookRequestJournalRepository, times(1)).save(bookRequestJournal);
    }

    @Test (expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenApproveNonExistingBookRequest() throws Exception {
        bookRequestService.approve(new IdentityDTO());
    }

    @Test
    public void shouldApprove() throws Exception {
        IdentityDTO dto = IdentityDTO.builder().build();
        BookRequest bookRequest = BookRequest.builder().bookRequestType(BookRequestType.builder().type(RequestType.ABONEMENT).build()).build();
        BookRequestJournal journal = BookRequestJournal.builder().bookRequest(bookRequest).build();
        when(bookRequestJournalRepository.findByBookRequestId(dto.getId())).thenReturn(Optional.of(journal));
        when( configService.getExpirationDate(journal.getBookRequest().getBookRequestType().getType())).thenReturn(Instant.now());
        bookRequestService.approve(dto);
        verify(bookRequestJournalRepository, times(1)).save(journal);
    }

    @Test (expected = ClientRequestException.class)
    public void shouldThrowExceptionWhenDisapproveNonExistingBookRequest() throws Exception {
        bookRequestService.disapprove(new IdentityDTO());
    }

    @Test
    public void shouldDisapprove() throws Exception {
        IdentityDTO dto = IdentityDTO.builder().build();
        BookRequest bookRequest = BookRequest.builder().bookRequestType(BookRequestType.builder().id(2).build()).build();
        BookRequestJournal journal = BookRequestJournal.builder().id(1).bookRequest(bookRequest).build();
        when(bookRequestJournalRepository.findByBookRequestId(dto.getId())).thenReturn(Optional.of(journal));
        bookRequestService.disapprove(dto);
        verify(bookRequestJournalRepository, times(1)).deleteById(journal.getId());
        verify(bookRequestRepository, times(1)).deleteById(journal.getBookRequest().getId());
    }

    @Test (expected=ClientRequestException.class)
    public void shouldThrowExceptionWhenReturnBookWithNoBookRequestIdFound() throws Exception {
        bookRequestService.returnBook(new IdentityDTO());
    }

    @Test
    public void shouldReturnBook() throws Exception {
        IdentityDTO dto = IdentityDTO.builder().id(1).build();
        BookRequestJournal journal = BookRequestJournal.builder().build();
        when( bookRequestJournalRepository.findByBookRequestId(dto.getId())).thenReturn(Optional.of(journal));
        bookRequestService.returnBook(dto);
        verify(bookRequestJournalRepository, times(1)).save(journal);
    }



}
