package com.bookstore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.dto.UserDTO;
import com.bookstore.model.UserData;
import com.bookstore.service.IUserService;
import com.bookstore.util.Response;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private IUserService userService;
	
	//Returns all the users present
	@GetMapping("getallusers")
	public ResponseEntity<List<?>> getAllUsers(@RequestHeader String token) {
		log.info("Get All User Data");
		List<UserData> response = userService.getAllUsers(token);
		return new ResponseEntity<List<?>>(response, HttpStatus.OK);
	}
	
	//Sends an OTP to the user's mail
	@GetMapping("/sendOTP/{token}")
	public ResponseEntity<Response> sendOTP(@PathVariable String token){
		Response userEntity = userService.sendOTP(token);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	//Verifies the OTP received by user
	@GetMapping("/verifyOTP")
	public ResponseEntity<Response> verifyOTP(@RequestHeader String token,
											  @RequestParam int otp){
		Response userEntity = userService.verifyOTP(token,otp);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	//Verifies the existence of user
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> verifyUser(@PathVariable String token){
		Response userEntity = userService.verifyUser(token);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
		
	//Verifies user based on userId 
	@GetMapping("/verifyuserid/{userId}")
	public boolean verifyUserId(@PathVariable int userId){
		boolean userCheck = userService.verifyUserId(userId);
		return userCheck;
	}
	
	//Sets up an reminder check for subscription
	@GetMapping("/subsremindercheck")
	public ResponseEntity<Response> subsReminderCheck(@RequestHeader String token){
		Response userEntity = userService.subsReminderCheck(token);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	//Creates a new user data
	@PostMapping("/create")
	public ResponseEntity<Response> addUserData(@Valid @RequestBody UserDTO userDTO) {
		log.info("Create User Data : " + userDTO);
		Response response  = userService.addUser(userDTO);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Updates an existing user data
	@PutMapping("/update")
	public ResponseEntity<Response> updateUserData(@RequestHeader String token,
			                                       @Valid @RequestBody UserDTO userDTO) {
		log.info("Update User Data : " + userDTO);
		Response response  = userService.updateUser(token, userDTO);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Updates the user subscription
	@PutMapping("/updatesubscription")
	public ResponseEntity<Response> updateSubscription(@RequestHeader String token) {
		log.info("Update Subscription");
		Response response  = userService.updateSubscription(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//User Login
	@PutMapping("/login")
	public ResponseEntity<Response> userLogin(@RequestHeader String token,
											  @RequestParam String emailId,
											  @RequestParam String password) {
		log.info("User Login");
		Response response  = userService.userLogin(token,emailId,password);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Forget Password
	@PutMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestHeader String token,
											  	   @RequestParam String emailId) {
		log.info("Forgot Password");
		Response response  = userService.forgotPassword(token,emailId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Changes the user password
	@PutMapping("/changepassword")
	public ResponseEntity<Response> changePassword(@RequestHeader String token,
											  	   @RequestParam String emailId,
											  	   @RequestParam String password) {
		log.info("Forgot Password");
		Response response  = userService.changePassword(token,emailId,password);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Deletes an existing user data
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteUserData(@RequestHeader String token) {
		log.info("User Data Deleted");
		Response response  = userService.deleteUser(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
