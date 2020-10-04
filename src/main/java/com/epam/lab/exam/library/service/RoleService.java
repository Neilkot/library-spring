package com.epam.lab.exam.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role getByType(RoleType type) throws ClientRequestException {
		return roleRepository.findByType(type).orElseThrow(() -> new ClientRequestException(ErrorType.ROLE_NOT_FOUND));
	}

	public Role getReaderRole() throws ClientRequestException {
		return getByType(RoleType.LIBRARIAN);
	}

	public Role getLibrarianRole() throws ClientRequestException {
		return getByType(RoleType.LIBRARIAN);
	}

	public Role getRole(Integer id) throws ClientRequestException {
		return roleRepository.findById(id).orElseThrow(() -> new ClientRequestException(ErrorType.ROLE_NOT_FOUND));
	}

}
