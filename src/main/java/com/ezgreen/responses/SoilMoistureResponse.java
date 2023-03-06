package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.SoilMoisture;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SoilMoistureResponse extends EZGreenResponse
{
	List<SoilMoisture> soilMoistures;
}