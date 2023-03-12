package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Plant;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Sensor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlantsDetailResponse extends EZGreenResponse
{
	List<Plant> plants;
	List<Sensor> sensors;
	List<PotSize> potSizes;
}