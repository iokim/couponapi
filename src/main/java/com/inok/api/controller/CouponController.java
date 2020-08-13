package com.inok.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inok.api.entity.Coupon;
import com.inok.api.entity.CouponUser;
import com.inok.api.service.CouponService;
import com.inok.api.vo.CommonResponse;
import com.inok.api.vo.CouponVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("coupon")
public class CouponController {
	
	private final CouponService couponService;
	
	//TODO 1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관 
	// input : N
	@PostMapping(value="/coupons/{numbers}")
	public ResponseEntity<CommonResponse> createCoupons(@PathVariable long numbers) {
		CommonResponse res = new CommonResponse();
		
		couponService.createCoupons(numbers);
		
		res.setResult(true);
		res.setMsg("쿠폰 발급 성공");
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
		
	}
	//TODO 2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API
	// output : 쿠폰번호(XXXXX-XXXXXX-XXXXXXXX)
	@PostMapping("/issue/user/{id}")
	public ResponseEntity<CommonResponse> issueCoupon(@PathVariable String id) {
		CommonResponse res = new CommonResponse();
		
		try {
			String couponNumber = couponService.issueCoupon(id);
			res.setResult(true);
			res.setMsg("쿠폰이 지급되었습니다");
			res.setData(couponNumber);
		} catch (Exception e) {
			
			res.setResult(false);
			res.setMsg("쿠폰 지급에 실패했습니다.");
			return new ResponseEntity<CommonResponse>(res, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
	
	//TODO 3. 사용자에게 지급된 쿠폰을 조회
	@GetMapping("/issue/coupons")
	public ResponseEntity<CommonResponse> getIssuedCoupons() {
		CommonResponse res = new CommonResponse();
		
		List<Coupon> coupons = couponService.getIssuedCoupons();
		
		List<CouponVo> couponsVo = coupons.stream()
				.map(item-> CouponVo.builder().id(item.getId()).couponNumber(item.getCouponNumber()).expiryDate(item.getExpiryDate()).issued(item.isIssued()).issueDate(item.getIssueDate()).build())
				.collect(Collectors.toList());
		
		res.setResult(true);
		res.setMsg("지급된 쿠폰 목록입니다.");
		res.setData(couponsVo);
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
	
	//TODO 4. 지급된 쿠폰중 하나를 사용 (쿠폰 재사용은 불가)
	// input : 쿠폰번호
	@PatchMapping("/use/{couponNumber}")
	public ResponseEntity<CommonResponse> useCoupon(@PathVariable String couponNumber) {
		CommonResponse res = new CommonResponse();
		
		try {
			couponService.useCoupon(couponNumber);
			
			res.setResult(true);
			res.setMsg("쿠폰사용 성공");
		} catch (Exception e) {
			res.setResult(false);
			res.setMsg("쿠폰사용 실패");
			return new ResponseEntity<CommonResponse>(res, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
	
	//TODO 5. 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
	// input : 쿠폰번호
	@PatchMapping("/cancel/{couponNumber}")
	public ResponseEntity<CommonResponse> cancelCoupon(@PathVariable String couponNumber) {
		CommonResponse res = new CommonResponse();
		
		try {
			couponService.cancelCoupon(couponNumber);
			res.setResult(true);
			res.setMsg("쿠폰사용취소 성공");
		} catch (Exception e) {
			res.setResult(false);
			res.setMsg("쿠폰사용취소 실패");
			return new ResponseEntity<CommonResponse>(res, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
	
	//TODO 6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
	@GetMapping("/expired/coupons")
	public ResponseEntity<CommonResponse> getCouponsExpiredToday() {
		CommonResponse res = new CommonResponse();
		
		List<Coupon> coupons = couponService.getCouponsExpiredToday();
		
		List<CouponVo> couponsVo = coupons.stream()
				.map(item-> CouponVo.builder().id(item.getId()).couponNumber(item.getCouponNumber()).expiryDate(item.getExpiryDate()).issued(item.isIssued()).issueDate(item.getIssueDate()).build())
				.collect(Collectors.toList());
		
		res.setResult(true);
		res.setMsg("발급된 쿠폰 중 오늘 만료되는 쿠폰입니다.");
		res.setData(couponsVo);
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
	
	//TODO 7. 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송
	@GetMapping("/notify/expirydate")
	public ResponseEntity<CommonResponse> notifyExpiryDate() {
		CommonResponse res = new CommonResponse();
		
		List<CouponUser> couponUsers = couponService.notifyExpiryDate();
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
}
