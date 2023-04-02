package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PlantType;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Relay;
import com.ezgreen.models.RelayType;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleDetailResponse extends EZGreenResponse
{
	Board board;
	Environment environment;
	Plant plant;
	PlantType plantType;
	PotSize potSize;
	Relay relay;
	List<Relay> relays;
	RelayType relayType;
	List<RelayType> relayTypes;
	Sensor sensor;
	List<Sensor> sensors;
	SensorType sensorType;
	List<SensorType> sensorTypes;
}
