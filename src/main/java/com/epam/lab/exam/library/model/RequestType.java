package com.epam.lab.exam.library.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.epam.lab.exam.library.service.ConfigService;

public enum RequestType {
	ABONEMENT, READING_AREA;

	public Instant expiresAt(ConfigService configService) {
		switch (this) {
		case ABONEMENT:
			return Instant.now().plus(configService.getAbonementExpirationDays(), ChronoUnit.DAYS);
		case READING_AREA:
			return Instant.now().plus(configService.getReadingAreaExpirationHours(), ChronoUnit.HOURS);
		default:
			throw new IllegalArgumentException("not supported " + this);
		}
	}
}
