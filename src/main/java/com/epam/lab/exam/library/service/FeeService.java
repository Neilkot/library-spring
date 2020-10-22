package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FeeService {

	@Autowired
	private ConfigService configService;

	public List<BookRequestDTO> enrichWithFee(List<BookRequestDTO> requests) {
		requests.forEach(this::enrichWithFee);
		return requests;
	}

	private void enrichWithFee(BookRequestDTO request) {
		String feeColumn = shouldCalculateFee(request) ? calculateFee(request.getExpirationDate()).toString() : "-";
		request.setFee(feeColumn);
	}

	private boolean shouldCalculateFee(BookRequestDTO request) {
		return request.getReturnDate() == null && request.getExpirationDate().isBefore(Instant.now());
	}

	private Float calculateFee(Instant expirationDate) {
		Float dailyFee = configService.getDailyFee();
		Instant now = Instant.now();
		ZonedDateTime current = ZonedDateTime.ofInstant(now, ZoneId.of("UTC"));
		ZonedDateTime expire = ZonedDateTime.ofInstant(expirationDate, ZoneId.of("UTC"));
		long daysExpired = ChronoUnit.DAYS.between(expire, current) + 1;
		BigDecimal bigDecimal = new BigDecimal(daysExpired * dailyFee);
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.floatValue();
	}
}
