package com.epam.lab.exam.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.lab.exam.library.dto.LoginDTO;
import com.epam.lab.exam.library.dto.RegisterDTO;
import com.epam.lab.exam.library.dto.UserDTO;
import com.epam.lab.exam.library.exception.ClientRequestException;
import com.epam.lab.exam.library.exception.ErrorType;
import com.epam.lab.exam.library.model.Role;
import com.epam.lab.exam.library.model.RoleType;
import com.epam.lab.exam.library.model.User;
import com.epam.lab.exam.library.repository.RoleRepository;
import com.epam.lab.exam.library.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public UserDTO authenticateUser(LoginDTO dto) throws ClientRequestException {
		return userRepository.findByLogin(dto.getUsername())
				.filter(u -> u.getChecksum().equalsIgnoreCase(dto.getPassword())).map(UserDTO::from)
				.orElseThrow(() -> ErrorType.USER_NOT_FOUND.ex());
	}

	public UserDTO createReader(RegisterDTO dto) throws ClientRequestException {
		return createUser(dto, roleRepository.findByType(RoleType.READER).get());
	}

	public UserDTO createLibrarian(RegisterDTO dto)
			throws ClientRequestException {
		return createUser(dto, roleRepository.findByType(RoleType.LIBRARIAN).get());
	}

	@Transactional
	public void updateIsBlocked(Integer id, boolean isBlocked) {
		userRepository.updateIsBlocked(id, isBlocked);
	}

	public List<UserDTO> getReaders(int page, int size) {
		return getUsers(RoleType.READER, page, size);
	}

	public List<UserDTO> getLibrarians(int page, int size) {
		return getUsers(RoleType.LIBRARIAN, page, size);
	}

	private List<UserDTO> getUsers(RoleType roleType, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return userRepository.findByRole(roleRepository.findByType(roleType).get(), pageable).stream()
				.map(UserDTO::from).collect(Collectors.toList());
	}

	private UserDTO createUser(RegisterDTO dto, Role role) throws ClientRequestException {
		if (userRepository.findByLogin(dto.getUsername()).isPresent()) {
			log.info("Can't create user for already existing login={}", dto.getUsername());
			throw ErrorType.LOGIN_IN_USE.ex();
		}
		User user = User.builder().login(dto.getUsername()).checksum(dto.getPassword()).firstName(dto.getFirstName())
				.lastName(dto.getLastName()).role(role).isBlocked(false).build();
		user = userRepository.save(user);
		return UserDTO.from(user);
	}

	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	public Optional<User> getUser(Integer id) {
		return userRepository.findById(id);
	}

	public long countLibrarians() {
		return userRepository.countLibrarians();
	}

}
