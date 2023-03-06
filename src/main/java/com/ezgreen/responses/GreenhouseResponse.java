package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Greenhouse;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GreenhouseResponse extends EZGreenResponse
{
	List<Greenhouse> greenhouse;
}