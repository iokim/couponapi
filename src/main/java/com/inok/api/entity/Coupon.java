package com.inok.api.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "coupon_number", nullable = false)
	private String couponNumber;
	
	@Column(name = "expiry_date", nullable = false)
	private LocalDate expiryDate;

	@Column(name = "issued", nullable = false)
	private boolean issued;

	@Column(name = "issue_date", nullable = true)
	private LocalDateTime issueDate;
	
}
