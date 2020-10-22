package com.epam.lab.exam.library.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChecksumServiceTest {
    @Mock
    private ConfigService configService;
    @InjectMocks
    private ChecksumService checksumService;

    private final static String CHECKSUM = "C4CA4238A0B923820DCC509A6F75849B";

    @Test
    public void shouldMakeChecksum() throws Exception {
        when(configService.getChecksumAlgorithm()).thenReturn("MD5");
        String actual = checksumService.makeChecksum("1");
        assertEquals(CHECKSUM, actual);
    }
}
