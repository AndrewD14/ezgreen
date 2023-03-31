package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PlantType;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Relay;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.ezgreen.models.Zone;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipleDetailResponse extends EZGreenResponse
{
	List<Board> boards;
	List<Environment> environments;
	List<Plant> plants;
	List<PlantType> plantTypes;
	List<PotSize> potSizes;
	List<Relay> relays;
	List<Sensor> sensors;
	List<SensorType> sensorTypes;
	List<Zone> zones;
}
