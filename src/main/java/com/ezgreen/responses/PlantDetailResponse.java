package com.ezgreen.responses;

import com.ezgreen.models.Plant;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Sensor;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantDetailResponse extends EZGreenResponse
{
	Plant plant;
	Sensor sensor;
	PotSize potSize;
}