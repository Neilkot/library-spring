package com.epam.lab.exam.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Data;

@Data
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
}
