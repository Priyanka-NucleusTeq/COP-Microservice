package com.user.services.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user.services.dbo.UserEntity;
import com.user.services.dto.SignInDto;
import com.user.services.dto.SignUpOutDto;
import com.user.services.dto.UserDetailsDto;
import com.user.services.exception.UnauthorizedException;
import com.user.services.exception.UserNotFoundException;
import com.user.services.service.UserService;
import com.user.services.validator.UserValidator;

@RestController
public class UserController {

	/**
	 * The UserService object.
	 */
	@Autowired
	private UserService userService;

	/**
	 * The userValidator object.
	 */
	@Autowired
	private UserValidator userValidator;

	/**
	 * The Logger object.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	/** This method is used for user signup.
	 * @param userEntity - userDetails for signup.
	 * @return - return the response details for signed-up user.
	 * @throws Exception 
	 */
	@PostMapping("/adduser")
	public SignUpOutDto userSignUp(@RequestBody UserEntity userEntity) throws Exception {
		LOGGER.info("Request received to user sign-up for {}", userEntity.toString());
		SignUpOutDto userDetails = userService.userSignUp(userEntity);
		LOGGER.info("User {} is registered successfully", userDetails.toString());
		return userDetails;
	}

	@PutMapping("/update/userdetails")
	public UserEntity updateUserDetails(@RequestBody UserDetailsDto userDetailsDto) throws UserNotFoundException {
		LOGGER.info("Request recieved to update the user details for {}", userDetailsDto.toString());
		userValidator.validateUser(userDetailsDto.getEmail());
		UserEntity  updatedDetails = userService.updateUserDetails(userDetailsDto);
		LOGGER.info("Successfully updated user details {}", updatedDetails);
		return updatedDetails;
	}

	@PostMapping("/fetch/userdetails")
	public SignUpOutDto fetchUserDetails(@RequestBody SignInDto signInDto) throws UserNotFoundException, UnauthorizedException {
		LOGGER.info("Request received to fetch user details for {}", signInDto.toString());
		userValidator.validateUser(signInDto.getEmail());
		SignUpOutDto userDetails = userService.fetchUserDetails(signInDto);
		LOGGER.info("Successfully fetched user details for {}", signInDto.toString());
		return userDetails;
	}

	@DeleteMapping("/delete/user/{userId}")
	public String deleteUser(@PathVariable Long userId) throws UserNotFoundException {
		LOGGER.info("Request received to delete the user for id {}", userId);
		userValidator.validateUserId(userId);
		String message = userService.deleteUser(userId);
		return message;
	}
}
