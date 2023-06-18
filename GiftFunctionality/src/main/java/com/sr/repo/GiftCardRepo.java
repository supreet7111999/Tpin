package com.sr.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sr.entity.GiftCard;

public interface GiftCardRepo extends JpaRepository<GiftCard,Integer> {
    
	@Query("select g from GiftCard g where g.userId= ?1")
	List<GiftCard> findAllByuserId(long userId);

}
