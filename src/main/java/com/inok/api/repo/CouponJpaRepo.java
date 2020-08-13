package com.inok.api.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inok.api.entity.Coupon;

public interface CouponJpaRepo extends JpaRepository<Coupon, Long> {

	Coupon findFirstByOrderByIdDesc();

	Optional<Coupon> findFirstByIssuedAndExpiryDateAfterOrderByExpiryDate(boolean b, LocalDate now);

	List<Coupon> findAllByIssued(boolean b);

	List<Coupon> findAllByIssuedAndExpiryDate(boolean b, LocalDate now);


}
