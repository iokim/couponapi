package com.inok.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class User {

	@Id
	private String id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="password")
	private String password;
	
}
