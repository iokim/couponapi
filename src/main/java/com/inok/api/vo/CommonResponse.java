package com.inok.api.vo;

import lombok.Data;

@Data
public class CommonResponse {
	
	private boolean result;
	
	private String msg;
	
	private Object data;

}
