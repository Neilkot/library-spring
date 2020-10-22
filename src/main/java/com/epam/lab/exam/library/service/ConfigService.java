package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.model.RequestType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Service
public class ConfigService {

	@Value("${abonement.expiration.days}")
	private Integer abonementExpirationDays;

	@Value("${reading.area.expiration.hours}")
	private Integer readingAreaExpirationHours;

	@Value("${daily.fee}")
	private Float dailyFee;

	@Value("${default.page.size}")
	private Integer defaultPageSize;
	
	@Value("${checksum.algorithm}")
	private String checksumAlgorithm;

	public Instant getExpirationDate(RequestType requestType) {
		switch (requestType) {
			case ABONEMENT:
				return Instant.now().plus(getAbonementExpirationDays(), ChronoUnit.DAYS);
			case READING_AREA:
				return Instant.now().plus(getReadingAreaExpirationHours(), ChronoUnit.HOURS);
			default:
				throw new IllegalArgumentException("not supported " + this);
		}
	}
}
