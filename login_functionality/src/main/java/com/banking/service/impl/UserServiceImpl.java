package com.banking.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.banking.dao.UserDao;
import com.banking.model.MessageResponse;
import com.banking.model.Role;
import com.banking.model.Roles;
import com.banking.model.Status;
import com.banking.model.User;
import com.banking.model.UserDTO;
import com.banking.service.RoleService;
import com.banking.service.UserService;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserDTO UserDTO;
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User createManager(UserDTO user) {
        User nUser = user.getUserFromDto(user);
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role managerRole = roleService.findByName("MANAGER");
        Role employeeRole = roleService.findByName("EMPLOYEE");
        Role customerRole = roleService.findByName("CUSTOMER");

        Set<Role> roleSet = new HashSet<>();
        if (managerRole != null) {
            roleSet.add(managerRole);
        }
        if (employeeRole != null) {
            roleSet.add(employeeRole);
        }
        if (customerRole != null) {
            roleSet.add(customerRole);
        }

        nUser.setRoles(roleSet);
        nUser.setRole(Roles.MANAGER);
        nUser.setAccountStatus(Status.Not_Created);
        return userDao.save(nUser);
    }

    @Override
    public User updateManager(UserDTO user) {
        User existingUser = userDao.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setName(user.getName());
//            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setPanNo(user.getPanNo());
            existingUser.setAccountCreated(user.isAccountCreated());
            existingUser.setAadharNumber(user.getAadharNumber());
            existingUser.setGender(user.getGender());

            Set<Role> roleSet = new HashSet<>();
            Role managerRole = roleService.findByName("MANAGER");
            Role employeeRole = roleService.findByName("EMPLOYEE");
            Role customerRole = roleService.findByName("CUSTOMER");

            if (managerRole != null) {
                roleSet.add(managerRole);
            }

            if (employeeRole != null) {
                roleSet.add(employeeRole);
            }

            if (customerRole != null) {
                roleSet.add(customerRole);
            }

            existingUser.setRoles(roleSet);

            return userDao.save(existingUser);
        }

        return null;
    }


    @Override
    public User getManagerById(long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when the user does not exist
            throw new NoSuchElementException("Manager with ID " + userId + " does not exist");
        }
    }

    @Override
    public String deleteManagerById(Long managerId) {
        Optional<User> manager = userDao.findById(managerId);

        if (manager.isPresent()) {
            User managerUser = manager.get();

            // Check if the manager does not have the "ADMIN" role
            boolean doesNotHaveAdminRole = managerUser.getRoles()
                    .stream()
                    .noneMatch(role -> role.getName().equals("ADMIN"));

            if (doesNotHaveAdminRole) {
                userDao.delete(managerUser);
                return "Manager Deleted Successfully";
            } else {
                return "Manager has the 'ADMIN' role and cannot be deleted";
            }
        }

        return "Manager Does Not Exist";
    }

    @Override
    public User createEmployee(UserDTO user) {
        User nUser = user.getUserFromDto(user);
        nUser.setPassword(bcryptEncoder.encode(user.getPassword()));

        Role employeeRole = roleService.findByName("EMPLOYEE");
        Role customerRole = roleService.findByName("CUSTOMER");

        Set<Role> roleSet = new HashSet<>();
        if (employeeRole != null) {
            roleSet.add(employeeRole);
        }
        if (customerRole != null) {
            roleSet.add(customerRole);
        }

        nUser.setRoles(roleSet);
        nUser.setRole(Roles.EMPLOYEE);
        nUser.setAccountStatus(Status.Not_Created);
        return userDao.save(nUser);
    }

    @Override
    public User updateEmployee(UserDTO user) {
        User existingUser = userDao.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setName(user.getName());
//            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setPanNo(user.getPanNo());
            existingUser.setAccountCreated(user.isAccountCreated());
            existingUser.setAadharNumber(user.getAadharNumber());
            existingUser.setGender(user.getGender());

            // Assuming the role names are still "EMPLOYEE" and "CUSTOMER"
            Set<Role> roleSet = new HashSet<>();
            Role employeeRole = roleService.findByName("EMPLOYEE");
            Role customerRole = roleService.findByName("CUSTOMER");

            if (employeeRole != null) {
                roleSet.add(employeeRole);
            }

            if (customerRole != null) {
                roleSet.add(customerRole);
            }

            existingUser.setRoles(roleSet);

            return userDao.save(existingUser);
        }

        return null;
    }

    @Override
    public User getEmployeeById(long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when the user does not exist
            throw new NoSuchElementException("Employee with ID " + userId + " does not exist");
        }
    }

    @Override
    public String deleteEmployeeById(Long employeeId) {
        Optional<User> employee = userDao.findById(employeeId);

        if (employee.isPresent()) {
            User employeeUser = employee.get();

            // Check if the employee does not have the "ADMIN" and "MANAGER" roles
            boolean doesNotHaveAdminAndManagerRoles = employeeUser.getRoles()
                    .stream()
                    .noneMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("MANAGER"));

            if (doesNotHaveAdminAndManagerRoles) {
                userDao.delete(employeeUser);
                return "Employee Deleted Successfully";
            } else {
                return "Employee has either the 'ADMIN' or 'MANAGER' role and cannot be deleted";
            }
        }

        return "Employee Does Not Exist";
    }

    @Override
    public ResponseEntity<?> save(UserDTO UserDTO) {

        User nUser = UserDTO.getUserFromDto(UserDTO);

        if (userDao.existsByUsername(UserDTO.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username already exists"));
        }

        if (userDao.existsByEmail(UserDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already exists"));
        }

        nUser.setPassword(bcryptEncoder.encode(UserDTO.getPassword()));

        Role role = roleService.findByName("CUSTOMER");

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        if (nUser.getEmail().split("@")[1].equals("admin.edu")) {
            role = roleService.findByName("ADMIN");
            roleSet.add(role);
            role = roleService.findByName("MANAGER");
            roleSet.add(role);
            role = roleService.findByName("EMPLOYEE");
            roleSet.add(role);
            
            
        }
        else {
        	nUser.setRole(Roles.CUSTOMER);
        	nUser.setAccountStatus(Status.Not_Created);
            userDao.save(nUser);
            return ResponseEntity.ok().body(nUser);


        }
        
        nUser.setRoles(roleSet);
        nUser.setRole(Roles.ADMIN);
        nUser.setAccountStatus(Status.Not_Created);
        userDao.save(nUser);

        return ResponseEntity.ok().body(nUser);
    }



    @Override
    public User updateCustomer(UserDTO user) {
        User existingUser = userDao.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setName(user.getName());
//            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setPanNo(user.getPanNo());
            existingUser.setAccountCreated(user.isAccountCreated());
            existingUser.setAadharNumber(user.getAadharNumber());
            existingUser.setGender(user.getGender());


            Set<Role> roleSet = new HashSet<>();
            Role customerRole = roleService.findByName("CUSTOMER");

            if (customerRole != null) {
                roleSet.add(customerRole);
            }

            existingUser.setRoles(roleSet);

            return userDao.save(existingUser);
        }

        return null;
    }

    // @Override
    // public User createCustomer(UserDTO user) {
    // User nUser = user.getUserFromDto();
    // nUser.setPassword(bcryptEncoder.encode(user.getPassword()));
    // Role role = roleService.findByName("CUSTOMER");
    // Set<Role> roleSet = new HashSet<>();
    // roleSet.add(role);
    // nUser.setRoles(roleSet);
    // return userDao.save(nUser);
    // }

    @Override
    public User getCustomerById(long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when the user does not exist
            throw new NoSuchElementException("Employee with ID " + userId + " does not exist");
        }
    }

    @Override
    public String deleteCustomerById(Long customerId) {
        Optional<User> customer = userDao.findById(customerId);

        if (customer.isPresent()) {
            User customerUser = customer.get();

            // Check if the customer has only the "CUSTOMER" role and not any other roles
            boolean hasOnlyCustomerRole = customerUser.getRoles()
                    .stream()
                    .allMatch(role -> role.getName().equals("CUSTOMER"));

            if (hasOnlyCustomerRole) {
            	customerUser.setAccountStatus(Status.Blocked);
            	userDao.save(customerUser);
//                userDao.delete(customerUser);
                return "Customer Deleted Successfully";
            } else {
                return "Customer has additional roles and cannot be deleted";
            }
        }

        return "Customer Does Not Exist";
    }

    @Override
    public User updateAdmin(UserDTO user) {
        User existingUser = userDao.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setName(user.getName());
//            existingUser.setPhone(user.getPhone());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setPanNo(user.getPanNo());
            existingUser.setAccountCreated(user.isAccountCreated());

            // Assuming the role names are still "ADMIN", "MANAGER", "EMPLOYEE", and "CUSTOMER"
            Set<Role> roleSet = new HashSet<>();
            Role adminRole = roleService.findByName("ADMIN");
            Role managerRole = roleService.findByName("MANAGER");
            Role employeeRole = roleService.findByName("EMPLOYEE");
            Role customerRole = roleService.findByName("CUSTOMER");

            if (adminRole != null) {
                roleSet.add(adminRole);
            }

            if (managerRole != null) {
                roleSet.add(managerRole);
            }

            if (employeeRole != null) {
                roleSet.add(employeeRole);
            }

            if (customerRole != null) {
                roleSet.add(customerRole);
            }

            existingUser.setRoles(roleSet);

            return userDao.save(existingUser);
        }

        return null;
    }


    @Override
    public User getAdminById(long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when the user does not exist
            throw new NoSuchElementException("Employee with ID " + userId + " does not exist");
        }
    }

    @Override
    public String deleteAdminById(Long adminId) {
        Optional<User> admin = userDao.findById(adminId);

        if (admin.isPresent()) {
            User adminUser = admin.get();

            // Check if the admin has all four roles
            boolean hasAllRoles = adminUser.getRoles().size() == 4
                    && adminUser.getRoles().stream()
                            .map(Role::getName)
                            .allMatch(
                                    roleName -> List.of("ADMIN", "MANAGER", "EMPLOYEE", "CUSTOMER").contains(roleName));

            if (hasAllRoles) {
            	adminUser.setAccountStatus(Status.Blocked);
            	userDao.save(adminUser);
//                userDao.delete(adminUser);
                return "Admin Deleted Successfully";
            } else {
                return "Admin does not have all four roles and cannot be deleted";
            }
        }

        return "Admin Does Not Exist";
    }

    @Override
	public boolean markAccountCreated(Long id) {
		
			User user = userDao.findById(id).get();
			user.setAccountCreated(true);
			user.setAccountStatus(Status.Created);
			userDao.save(user);
			return true;
		
	}
    
    @Override
    public Iterable<User> getUsersByRole(String role) {
        return userDao.findByRolesContaining(role);
    }

	
}
