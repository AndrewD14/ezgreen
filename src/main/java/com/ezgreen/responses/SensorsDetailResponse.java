package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Plant;
import com.ezgreen.models.Sensor;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorsDetailResponse extends EZGreenResponse
{
	List<Sensor> sensors;
	List<Plant> plants;
}