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
	
	@GetMapping("getallusers")
	public ResponseEntity<List<?>> getAllUsers(@RequestHeader String token) {
		log.info("Get All User Data");
		List<UserData> response = userService.getAllUsers(token);
		return new ResponseEntity<List<?>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/sendOTP/{token}")
	public ResponseEntity<Response> sendOTP(@PathVariable String token){
		Response userEntity = userService.sendOTP(token);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	@GetMapping("/verifyOTP")
	public ResponseEntity<Response> verifyOTP(@RequestHeader String token,
											  @RequestParam int otp){
		Response userEntity = userService.verifyOTP(token,otp);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	@GetMapping("/subsremindercheck")
	public ResponseEntity<Response> subsReminderCheck(@RequestHeader String token){
		Response userEntity = userService.subsReminderCheck(token);
		return new ResponseEntity<Response>(userEntity,HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Response> addUserData(@Valid @RequestBody UserDTO userDTO) {
		log.info("Create User Data : " + userDTO);
		Response response  = userService.addUser(userDTO);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Response> updateUserData(@RequestHeader String token,
			                                       @Valid @RequestBody UserDTO userDTO) {
		log.info("Update User Data : " + userDTO);
		Response response  = userService.updateUser(token, userDTO);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PutMapping("/updatesubscription")
	public ResponseEntity<Response> updateSubscription(@RequestHeader String token) {
		log.info("Update Subscription");
		Response response  = userService.updateSubscription(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteUserData(@RequestHeader String token) {
		log.info("User Data Deleted");
		Response response  = userService.deleteUser(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
