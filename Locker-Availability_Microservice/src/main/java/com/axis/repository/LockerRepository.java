package com.axis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axis.model.Locker;
import com.axis.model.Size;

@Repository
public interface LockerRepository extends JpaRepository<Locker, Integer> {

	
    List<Locker> findLockerBySize(Size size);

    List<Locker> findLockerByBranchName(String branchName);
    
    List<Locker> findLockerByIsAvailable(boolean isAvailable);
    
	List<Locker> findLockerBySizeAndBranchName(Size size, String branchName);
	
	List<Locker> findLockerBySizeAndBranchNameAndIsAvailable(Size size, String branchName, boolean isAvailable);

}
