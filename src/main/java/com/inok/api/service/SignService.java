package com.inok.api.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inok.api.entity.User;
import com.inok.api.repo.UserJpaRepo;
import com.inok.api.security.JwtTokenProvider;
import com.inok.api.vo.UserVo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {
	
	private final UserJpaRepo userJpaRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public void signup(UserVo userVo) {
		
		String pw = passwordEncoder.encode(userVo.getPassword());
		
		User user = User.builder()
				.id(userVo.getId())
				.name(userVo.getName())
				.password(pw).build();
		
		userJpaRepo.save(user);
	}

	public String signin(UserVo userVo) throws Exception {
		
		User user = userJpaRepo.findById(userVo.getId()).orElse(null);
		
		if (user == null || !passwordEncoder.matches(userVo.getPassword(), user.getPassword())) {
			throw new Exception();
		}

		String jwt = jwtTokenProvider.createToken(user.getId(), user.getName());
		
		return jwt;
	}

}
