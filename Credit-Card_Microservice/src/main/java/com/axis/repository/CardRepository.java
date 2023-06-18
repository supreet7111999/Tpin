package com.axis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axis.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>  {

	Card findCardByAccountId(String accountId);
	
}
