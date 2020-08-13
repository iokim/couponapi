package com.inok.api.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inok.api.entity.CouponUser;

public interface CouponUserJpaRepo extends JpaRepository<CouponUser, Long>{

	Optional<CouponUser> findOneByCoupon_CouponNumber(String couponNumber);

	List<CouponUser> findAllByUsedAndCoupon_ExpiryDate(boolean b, LocalDate plusDays);

}
