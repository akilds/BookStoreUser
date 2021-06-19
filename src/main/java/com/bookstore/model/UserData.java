package com.bookstore.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bookstore.dto.UserDTO;

import lombok.Data;

@Entity
@Table(name = "userTable")
public @Data class UserData {
    
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int userId;
	
	@Column
	private String firstName;
	private String lastName;
	private String dOB;
	private String mobileNo;
	private String emailId;
	private String password;
	private LocalDateTime registerDate = LocalDateTime.now();
	private LocalDateTime updatedDate;
	private boolean verify = false;
	private int otp;
	private LocalDateTime purchaseDate;
	private LocalDateTime expireDate;
	private boolean subscribe = false;
	private boolean passwordCheck = false;
	
	public UserData() {}
	
	public void updateUser(UserDTO userDTO) {
		this.firstName = userDTO.firstName;
		this.lastName = userDTO.lastName;
		this.dOB = userDTO.dOB;
		this.mobileNo = userDTO.mobileNo;
		this.emailId = userDTO.emailId;
		this.password = userDTO.password;
		this.updatedDate = LocalDateTime.now();
	}
}

