package com.banking.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class UserDTO {
    private long id;
    private String username;
    private String password;
	private String email;
//    private String phone;
    private String name;
    private String address;
    private Date dateOfBirth;
    private long phoneNumber;
    private String panNo;
    private boolean isAccountCreated;
    private Set<RoleDTO> roles;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Enumerated(EnumType.STRING)
    private Status accountStatus;
    private Long aadharNumber;
    private Gender gender;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public boolean isAccountCreated() {
        return isAccountCreated;
    }

    public void setAccountCreated(boolean isAccountCreated) {
        this.isAccountCreated = isAccountCreated;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
    
    
    public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}
	

	public Long getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(Long aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	

	public Status getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Status accountStatus) {
		this.accountStatus = accountStatus;
	}

	public static User getUserFromDto(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
//        user.setPhone(userDTO.getPhone());
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPanNo(userDTO.getPanNo());
        user.setAccountCreated(userDTO.isAccountCreated());
        user.setRole(userDTO.getRole());
        user.setAadharNumber(userDTO.aadharNumber);
        user.setGender(userDTO.gender);
        user.setAccountStatus(userDTO.accountStatus);
        
        // Assuming a similar converter method exists for RoleDTO to Role conversion
//        Set<Role> roles = RoleDTO.getRolesFromDto(userDTO.getRoles());
//        user.setRoles(roles);
        
        return user;
    }

}
