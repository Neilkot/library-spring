package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.*;
import com.epam.lab.exam.library.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookRequestService {

    private BookItemRepository bookItemRepository;
    private BookRequestJournalRepository bookRequestJournalRepository;
    private BookRequestTypeRepository bookRequestTypeRepository;
    private UserRepository userRepository;
    private BookRequestRepository bookRequestRepository;
    private ConfigService configService;
    private FeeService feeService;

    public BookRequestService(BookItemRepository bookItemRepository,
                              BookRequestJournalRepository bookRequestJournalRepository,
                              BookRequestTypeRepository bookRequestTypeRepository, UserRepository userRepository,
                              BookRequestRepository bookRequestRepository, ConfigService configService, FeeService feeService) {
        this.bookItemRepository = bookItemRepository;
        this.bookRequestJournalRepository = bookRequestJournalRepository;
        this.bookRequestTypeRepository = bookRequestTypeRepository;
        this.userRepository = userRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.configService = configService;
        this.feeService = feeService;
    }

    public List<PendingRequestDTO> getPending(int page, int size) {
        return bookRequestJournalRepository.getPendingNonBlockedReaderRequests(PageRequest.of(page, size)).stream()
                .map(PendingRequestDTO::from).collect(Collectors.toList());
    }

    public long countPending() {
        return bookRequestJournalRepository.countPending();
    }

    public List<BookRequestDTO> getApproved(int page, int size) {
        return feeService.enrichWithFee(bookRequestJournalRepository.getApprovedNonBlockedReaderRequests(PageRequest.of(page, size)).stream()
                .map(BookRequestDTO::from).collect(Collectors.toList()));
    }

    public long countApproved() {
        return bookRequestJournalRepository.countApproved();
    }

    public List<BookRequestDTO> getApproved(Integer userId, int page, int size) {
        return feeService.enrichWithFee(bookRequestJournalRepository.getUserApprovedRequests(userId, PageRequest.of(page, size)).stream()
                .map(BookRequestDTO::from).collect(Collectors.toList()));
    }

    @Transactional
    public void submit(SubmitRequestDTO dto) throws ClientRequestException {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> ErrorType.USER_NOT_FOUND.ex());
        if (user.getIsBlocked()) {
            throw ErrorType.FORBIDDEN.ex();
        }
        BookItem bookItem = bookItemRepository.findAvailableByBookId(dto.getBookId()).stream().findFirst().orElseThrow(() -> ErrorType.BOOK_NOT_FOUND.ex());
        BookRequestType findByType = bookRequestTypeRepository.findByType(dto.getRequestType()).get();
        BookRequest bookRequest = BookRequest.builder().bookItem(bookItem).bookRequestType(findByType).user(user)
                .build();
        bookRequest = bookRequestRepository.save(bookRequest);

        bookRequestJournalRepository
                .save(BookRequestJournal.builder().bookRequest(bookRequest).createDate(Instant.now()).build());
    }

    public void approve(IdentityDTO dto) throws ClientRequestException {
        BookRequestJournal journal = bookRequestJournalRepository.findByBookRequestId(dto.getId())
                .orElseThrow(() -> ErrorType.REQUEST_NOT_FOUND.ex());
        journal.setApproveDate(Instant.now());
        Instant expirationDate = configService.getExpirationDate(journal.getBookRequest().getBookRequestType().getType());
        journal.setExpirationDate(expirationDate);
        bookRequestJournalRepository.save(journal);
    }

    public void disapprove(IdentityDTO dto) throws ClientRequestException {
        BookRequestJournal journal = bookRequestJournalRepository.findByBookRequestId(dto.getId())
                .orElseThrow(() -> ErrorType.REQUEST_NOT_FOUND.ex());
        bookRequestJournalRepository.deleteById(journal.getId());
        bookRequestRepository.deleteById(journal.getBookRequest().getId());
    }

    public void returnBook(IdentityDTO dto) throws ClientRequestException {
        BookRequestJournal journal = bookRequestJournalRepository.findByBookRequestId(dto.getId())
                .orElseThrow(() -> ErrorType.REQUEST_NOT_FOUND.ex());
        journal.setReturnDate(Instant.now());
        bookRequestJournalRepository.save(journal);
    }

    public long countApprovedReaderRequests(Integer userId) {
        return bookRequestJournalRepository.countApprovedReaderRequests(userId);
    }
}
