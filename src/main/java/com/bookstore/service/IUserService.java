package com.bookstore.service;

import java.util.List;

import javax.validation.Valid;

import com.bookstore.dto.UserDTO;
import com.bookstore.model.UserData;
import com.bookstore.util.Response;

public interface IUserService {

	List<UserData> getAllUsers(String token);

	Response addUser(@Valid UserDTO userDTO);

	Response updateUser(String token, @Valid UserDTO userDTO);

	Response deleteUser(String token);

	Response sendOTP(String token);

	Response verifyOTP(String token, int otp);

	Response updateSubscription(String token);

	Response subsReminderCheck(String token);

}
