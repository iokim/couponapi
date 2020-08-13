package com.inok.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inok.api.service.SignService;
import com.inok.api.vo.CommonResponse;
import com.inok.api.vo.UserVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class SignController {
	
	private final SignService signService;
	
	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<CommonResponse> signup(@RequestBody UserVo userVo) {
		
		CommonResponse res = new CommonResponse();
		
		signService.signup(userVo);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<CommonResponse> signin(@RequestBody UserVo userVo) {
		CommonResponse res = new CommonResponse();
		
		String jwt;
		
		try {
			jwt = signService.signin(userVo);
			res.setResult(true);
			res.setMsg("로그인 성공");
			res.setData(jwt);
		} catch (Exception e) {
			res.setResult(false);
			res.setMsg("아이디, 비밀번호 다시 확인");
			return new ResponseEntity<CommonResponse>(res, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<CommonResponse>(res, HttpStatus.OK);
	}
}
