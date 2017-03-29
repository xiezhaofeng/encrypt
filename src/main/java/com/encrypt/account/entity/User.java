package com.encrypt.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import com.encrypt.util.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private String name;
	private Integer age;
	@JsonIgnore
	private String ignore;
	
	public static void main(String[] args) throws JsonProcessingException {
		User user = new User("1234", 1, "dsfa");
		System.out.println(JSONUtils.toJackson(user));
	}
}
