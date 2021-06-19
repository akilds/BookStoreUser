package com.bookstore.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.dto.UserDTO;
import com.bookstore.exception.UserException;
import com.bookstore.model.UserData;
import com.bookstore.repository.UserRepository;
import com.bookstore.util.Email;
import com.bookstore.util.ExpireDateCheck;
import com.bookstore.util.Response;
import com.bookstore.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements IUserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private Email email;
	
	@Autowired
	private ExpireDateCheck edCheck;
	
	//Returns all user data present
	@Override
	public List<UserData> getAllUsers(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.info("Get All User Data");
			List<UserData> getAllUsers = userRepository.findAll();
			return getAllUsers;
		}else {
			log.error("User Token Is Not valid");
			throw new UserException(400, "User Token Is Not Valid");
		}	
	}

	//Checks for subscription reminder
	@Override
	public Response subsReminderCheck(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			if(isPresent.get().isSubscribe()) {
				LocalDateTime today = LocalDateTime.now();
				boolean check = edCheck.expireDateCheck(today,isPresent.get().getExpireDate());
				if(check) {
					log.info("Expiry Date Less Than A Month");
					return new Response(200, "Expiry Date Less Than A Month", token);
				}else {
					log.error("Expiry Date More Than A Month");
					throw new UserException(400, "Expiry Date More Than A Month");
				}
			}else {
				log.error("User Not Subscribed");
				throw new UserException(400, "User Not Subscribed");
			}
		}else {
			log.error("User Token Is Not valid");
			throw new UserException(400, "User Token Is Not Valid");
		}
	}
	
	//Adds a new user data
	@Override
	public Response addUser(UserDTO userDTO) {
		Optional<UserData> isPresent = userRepository.findByEmailId(userDTO.getEmailId());
		if(isPresent.isPresent()) {
			log.error("User Already Added");
			throw new UserException(400, "User Already Added");
		}else {
			log.info("Add User : " + userDTO);
			UserData user = modelmapper.map(userDTO, UserData.class);
			userRepository.save(user);
			String token = tokenUtil.createToken(user.getUserId());
			String text = "Please click on the below link to to receive otp : \n http://localhost:8080/user/sendOTP/"+token;
			String subject = "Mail Validation";
			email.sendMail(userDTO.getEmailId(),subject,text);
			return new Response(200, "User Data Added Successfully", token);
		}	
	}

	//Sends an OTP to user
	@Override
	public Response sendOTP(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.debug("OTP Sent!");
			Random rnd = new Random();
		    int number = rnd.nextInt(999999);
		    isPresent.get().setOtp(number);
			userRepository.save(isPresent.get());
			String text = "OTP : "+isPresent.get().getOtp();
			String subject = "OTP";
			email.sendMail(isPresent.get().getEmailId(),subject, text);
			return new Response(200, "OTP Sent successfully!", token);
		}
		else {
			log.error("User Token Is Not valid");
			throw new UserException(400, "User Token Is Not Valid");
		}
	}
	
	//Verifies the OTP received by user
	@Override
	public Response verifyOTP(String token, int otp) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			if(isPresent.get().getOtp()==otp) {
				log.debug("User: " + isPresent.get() + " Verified!");
				isPresent.get().setVerify(true);
				userRepository.save(isPresent.get());
				return new Response(200, "User Verified successfully!", token);
			}else {
				log.error("User Token Is Not valid");
				throw new UserException(400, "User Token Is Not Valid");
			}
		}
		else {
			log.error("User Token Is Not valid");
			throw new UserException(400, "User Token Is Not Valid");
		}
	}
	
	//Verifies the existence of user
	@Override
	public Response verifyUser(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.debug("User: " + isPresent.get() + " Verified!");
			isPresent.get().setPasswordCheck(true);
			userRepository.save(isPresent.get());
			return new Response(200, "User Verified successfully!", token);
		}
		else {
			log.error("User Token Is Not valid");
			throw new UserException(400, "User Token Is Not Valid");
		}
	}
	
	
	//Updates an existing user data//Verifies a user based on userId
	@Override
	public boolean verifyUserId(int userId) {
		List<UserData> users = userRepository.findAll();
		return users.stream().anyMatch(user -> user.getUserId()==userId);
	}
	
	//Updates an existing user
	@Override
	public Response updateUser(String token, UserDTO userDTO) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.info("Update User : " + userDTO);
			isPresent.get().updateUser(userDTO);
			userRepository.save(isPresent.get());
			return new Response(200, "User Data Updated Successfully", token);
		}else {
			log.error("User Doesnt Exist");
			throw new UserException(400, "User Doesnt Exist");
		}	
	}

	//Updates user subscription
	@Override
	public Response updateSubscription(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.info("Update Subscription");
			if(isPresent.get().isSubscribe()) {
				isPresent.get().setSubscribe(false);
				userRepository.save(isPresent.get());
				return new Response(200, "User UnSubscribed Successfully", token);
			}else {
				isPresent.get().setSubscribe(true);
				LocalDateTime today = LocalDateTime.now();
				isPresent.get().setExpireDate(today.plusYears(1));
				userRepository.save(isPresent.get());
				return new Response(200, "User Subscribed Successfully", token);
			}
		}else {
			log.error("User Doesnt Exist");
			throw new UserException(400, "User Doesnt Exist");
		}	
	}
	
	//User Login
	@Override
	public Response userLogin(String token, String emailId, String password) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			if(isPresent.get().getEmailId().equals(emailId)&&isPresent.get().getPassword().equals(password)) {
				return new Response(200, "User Logged In Successfully", token);
			}else {
				log.error("Incorrect EmailId or Password");
				throw new UserException(400, "Incorrect EmailId or Password");
			}
		}else {
			log.error("User Doesnt Exist");
			throw new UserException(400, "User Doesnt Exist");
		}	
	}
	
	//Forget Password
	@Override
	public Response forgotPassword(String token, String emailId) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			if(isPresent.get().getEmailId().equals(emailId)) {
				String text = "Please click on the below link to to receive otp : \n http://localhost:8080/user/verify/"+token;
				String subject = "Mail Validation";
				email.sendMail(emailId,subject,text);
				return new Response(200, "Verification Mail Sent Successfully", token);
			}else {
				log.error("InValid EmailId");
				throw new UserException(400, "InValid EmailId");
			}
		}else {
			log.error("User Doesnt Exist");
			throw new UserException(400, "User Doesnt Exist");
		}	
	}
	
	//Change Password
	@Override
	public Response changePassword(String token, String emailId, String password) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			if(isPresent.get().getEmailId().equals(emailId)&&isPresent.get().isPasswordCheck()==true) {
				isPresent.get().setPassword(password);
				isPresent.get().setPasswordCheck(false);
				userRepository.save(isPresent.get());
				return new Response(200, "Password Changed Successfully", token);
			}else {
				log.error("InValid EmailId");
				throw new UserException(400, "InValid EmailId");
			}
		}else {
			log.error("User Doesnt Exist");
			throw new UserException(400, "User Doesnt Exist");
		}	
	}
	
	//Deletes an existing user data
	@Override
	public Response deleteUser(String token) {
		int id = tokenUtil.decodeToken(token);
		Optional<UserData> isPresent = userRepository.findById(id);
		if(isPresent.isPresent()) {
			log.info("User Data Deleted");
			userRepository.delete(isPresent.get());
			return new Response(200, "User Data Deleted Successfully", token);
		}else {
			log.error("User Token Is Not Valid");
			throw new UserException(400, "User Token Is Not Valid");
		}
	}
}
