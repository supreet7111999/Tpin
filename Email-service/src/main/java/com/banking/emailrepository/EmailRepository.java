package com.banking.emailrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.emailmodel.EmailPojo;

@Repository
public interface EmailRepository extends JpaRepository<EmailPojo, Integer>{
	
}
