package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Environment;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnvironmentResponse extends EZGreenResponse
{
	List<Environment> environments;
}