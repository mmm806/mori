package com.example.mori.domain.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mori.domain.user.dto.request.ChangePasswordRequest;
import com.example.mori.domain.user.dto.request.CreateUserRequest;
import com.example.mori.domain.user.dto.request.SignInRequest;
import com.example.mori.domain.user.dto.request.UpdateRoleRequest;
import com.example.mori.domain.user.dto.request.UpdateUserRequest;
import com.example.mori.domain.user.dto.request.UserSearchRequest;
import com.example.mori.domain.user.dto.response.UserResponseWithStats;
import com.example.mori.domain.user.dto.response.UserDtoCursorResponse;
import com.example.mori.domain.user.dto.response.UserResponse;
import com.example.mori.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request) {
		UserResponse userResponse = userService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> SignIn(@RequestBody @Valid SignInRequest request) {
		UserResponse userResponse = userService.signIn(request);
		return ResponseEntity.status(HttpStatus.OK).body(userResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseWithStats> getById(@PathVariable UUID id) {
		UserResponseWithStats response = userService.getById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserResponseWithStats> update(
		@PathVariable UUID id,
		@RequestBody @Valid UpdateUserRequest request
		) {
		UserResponseWithStats response = userService.update(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{id}/password")
	public ResponseEntity<Void> changePassword(
		@PathVariable UUID id,
		@RequestBody @Valid ChangePasswordRequest request
	) {
		userService.changePassword(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}


	@GetMapping
	public ResponseEntity<UserDtoCursorResponse>getAllUsers(
		@Valid @ModelAttribute UserSearchRequest request
	) {
		UserDtoCursorResponse response = userService.searchUsers(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{id}/role")
	public ResponseEntity<UserResponse> updateRole(
		@PathVariable UUID id,
		@RequestBody @Valid UpdateRoleRequest request
	) {
		UserResponse response = userService.updateUserRole(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}



}
