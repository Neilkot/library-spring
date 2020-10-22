package com.epam.lab.exam.library.service;

import com.epam.lab.exam.library.dto.BookRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeeServiceTest {

    @Mock
    ConfigService configService;
    @InjectMocks
    FeeService feeService;

    private static final int DAYS = 5;
    private static final Float DAY_FEE = 50F;
    private static final Float EXPECTED = DAY_FEE * DAYS;

    @Test
    public void shouldCalculateFee() throws Exception {
        LocalDateTime expirationDate =  LocalDateTime.now().minus(DAYS, ChronoUnit.DAYS);
        BookRequestDTO dto = BookRequestDTO.builder().expirationDate(expirationDate.toInstant(ZoneOffset.UTC)).build();
        List<BookRequestDTO> listDto = Arrays.asList(dto);
        when(configService.getDailyFee()).thenReturn(DAY_FEE );
        List<BookRequestDTO> bookRequestDTOS = feeService.enrichWithFee(listDto);
        Float actual = Float.parseFloat(bookRequestDTOS.get(0).getFee());
        assertEquals(EXPECTED, actual);
    }
}
