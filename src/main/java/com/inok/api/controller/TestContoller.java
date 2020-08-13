package com.inok.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContoller {

	@GetMapping(value = "")
	@ResponseBody
	public String test() {
		return "test";
	}
}
