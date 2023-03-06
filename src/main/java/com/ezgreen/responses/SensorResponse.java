package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Sensor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SensorResponse extends EZGreenResponse
{
	List<Sensor> sensors;
}