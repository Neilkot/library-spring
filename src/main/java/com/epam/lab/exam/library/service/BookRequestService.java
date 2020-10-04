package com.epam.lab.exam.library.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import com.epam.lab.exam.library.dto.IdentityDTO;
import com.epam.lab.exam.library.dto.PendingRequestDTO;
import com.epam.lab.exam.library.dto.SubmitRequestDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.BookItem;
import com.epam.lab.exam.library.model.BookRequest;
import com.epam.lab.exam.library.model.BookRequestJournal;
import com.epam.lab.exam.library.model.BookRequestType;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.repository.AuthorRepository;
import com.epam.lab.exam.library.repository.BookItemRepository;
import com.epam.lab.exam.library.repository.BookRequestJournalRepository;
import com.epam.lab.exam.library.repository.BookRequestRepository;
import com.epam.lab.exam.library.repository.BookRequestTypeRepository;
import com.epam.lab.exam.library.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

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

	public void submit(SubmitRequestDTO dto) throws ClientRequestException {
		List<BookItem> findByBookId = bookItemRepository.findByBookId(dto.getBookId());
		if (findByBookId.isEmpty()) {
			throw ErrorType.BOOK_NOT_FOUND.ex();
		}
		BookItem bookItem = findByBookId.get(0);
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> ErrorType.USER_NOT_FOUND.ex());
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
		journal.setExpirationDate(journal.getBookRequest().getBookRequestType().getType().expiresAt(configService));
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
