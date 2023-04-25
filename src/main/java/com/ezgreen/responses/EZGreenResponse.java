package com.ezgreen.responses;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EZGreenResponse
{
	private HttpStatus statusCode;
	private String responseMessage;
}
