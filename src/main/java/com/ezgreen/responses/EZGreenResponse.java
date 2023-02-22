package com.ezgreen.responses;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EZGreenResponse
{
	private HttpStatus statusCode;
	private String responseMessage;
}
