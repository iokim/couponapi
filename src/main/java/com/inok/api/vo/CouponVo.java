package com.inok.api.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CouponVo {
	
	private long id;
	private String couponNumber;
	private LocalDate expiryDate;
	private boolean issued;
	private LocalDateTime issueDate;

}
