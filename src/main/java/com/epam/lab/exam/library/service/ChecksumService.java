package com.epam.lab.exam.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ChecksumService implements PasswordEncoder {

	@Autowired
	private ConfigService configService;

	public String makeChecksum(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance(configService.getChecksumAlgorithm());
			byte[] digest = md.digest(value.getBytes());
			return DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Couldn't generate checksum." + e.getMessage(), e);
		}
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return makeChecksum(String.valueOf(rawPassword));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equalsIgnoreCase(encode(rawPassword));
	}

}
