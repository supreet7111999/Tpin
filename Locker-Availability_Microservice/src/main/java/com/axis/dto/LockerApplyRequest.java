package com.axis.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.axis.model.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockerApplyRequest {

	@Id
	private Integer id;
	
	@Enumerated(EnumType.STRING)
    private Size size;

    private int durationForAllocatingInYears;
	
	private String branchName;
	
    private String fromAccountId;
    
}
