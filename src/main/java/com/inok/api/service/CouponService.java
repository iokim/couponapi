package com.inok.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.inok.api.entity.Coupon;
import com.inok.api.entity.CouponUser;
import com.inok.api.entity.User;
import com.inok.api.repo.CouponJpaRepo;
import com.inok.api.repo.CouponUserJpaRepo;
import com.inok.api.repo.UserJpaRepo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
	
	private final CouponJpaRepo couponJpaRepo;
	private final CouponUserJpaRepo couponUserJpaRepo;
	private final UserJpaRepo userJpaRepo;

	public List<Coupon> createCoupons(long numbers) {
		
		// 쿠폰 생성
		List<String> couponsList = generate(numbers);
		
		List<Coupon> coupons = couponsList.stream()
								.map(item->Coupon.builder().couponNumber(item)
										.expiryDate(LocalDate.now().plusDays(3))
										.issued(false).build())
								.collect(Collectors.toList());
		
		coupons = couponJpaRepo.saveAll(coupons);
		
		return coupons;
	}
	
	public List<String> generate(long numbers) {
		List<String> coupons = new ArrayList<String>();
		
		Random random = new Random(System.currentTimeMillis());
		
		Coupon lastCoupon = couponJpaRepo.findFirstByOrderByIdDesc();
		
		long lastId = 0;
		
		if(lastCoupon != null) {
			lastId = lastCoupon.getId();
		}
		
		for(int i = 0; i < numbers ; i++ ) {
			String tmp = String.format("%012d", lastId+i+1);
			String coupon = random.nextInt(10) + tmp.substring(0, 4) + random.nextInt(10) + tmp.substring(4, 8) + random.nextInt(10) + tmp.substring(8, 12) + random.nextInt(10);
			coupons.add(coupon);
		}
		
		return coupons;
	}

	public String issueCoupon(String id) throws Exception {
		
		Coupon coupon = couponJpaRepo.findFirstByIssuedAndExpiryDateAfterOrderByExpiryDate(false, LocalDate.now().minusDays(1)).orElseThrow(Exception::new);
		
		User user = userJpaRepo.findById(id).orElseThrow(Exception::new);
		
		CouponUser couponUser = CouponUser.builder().coupon(coupon).user(user).used(false).build();
		
		couponUserJpaRepo.save(couponUser);
		
		coupon.setIssued(true);
		coupon.setIssueDate(LocalDateTime.now());
		
		return coupon.getCouponNumber();
	}

	public List<Coupon> getIssuedCoupons() {
		
		List<Coupon> coupons = couponJpaRepo.findAllByIssued(true);
		
		return coupons;
	}

	public void useCoupon(String couponNumber) throws Exception {
		CouponUser couponUser = couponUserJpaRepo.findOneByCoupon_CouponNumber(couponNumber).orElseThrow(Exception::new);
		
		if(couponUser.isUsed()) {
			throw new Exception();
		}
		
		couponUser.setUsed(true);
		couponUser.setUsageDate(LocalDateTime.now());
	}

	public void cancelCoupon(String couponNumber) throws Exception {
		CouponUser couponUser = couponUserJpaRepo.findOneByCoupon_CouponNumber(couponNumber).orElseThrow(Exception::new);
		couponUser.setUsed(false);
		couponUser.setUsageDate(null);
		
	}

	public List<Coupon> getCouponsExpiredToday() {
		
		List<Coupon> coupons = couponJpaRepo.findAllByIssuedAndExpiryDate(true,LocalDate.now());
		
		return coupons;
	}

	public List<CouponUser> notifyExpiryDate() {
		
		List<CouponUser> couponUsers = couponUserJpaRepo.findAllByUsedAndCoupon_ExpiryDate(false, LocalDate.now().plusDays(3));
		
		for(CouponUser item : couponUsers) {
			System.out.println(item.getUser().getId() + "님 쿠폰("+item.getCoupon().getCouponNumber()+")이 3일 후 만료됩니다.");
		}
		return couponUsers;
	}

}
