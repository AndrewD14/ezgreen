package com.ezgreen.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlantFullDetail
{
	private Long id;
	private String name;
	private Integer number;
	private Double highMoisture;
	private Double lowMoisture;
	private LocalDate dateObtain;
	private Integer monitor;
	private Integer dead;
	private Integer delete;
	private String createBy;
	private String updateBy;
	private LocalDateTime createTs;
	private LocalDateTime updateTs;
	private String size;
	private Long sensorId;
	private String type;
	
	public PlantFullDetail()
	{
		
	}

	public PlantFullDetail(Long id, String name, Integer number, Double highMoisture, Double lowMoisture, LocalDate dateObtain,
			Integer monitor, Integer dead, Integer delete, String createBy, String updateBy, LocalDateTime createTs,
			LocalDateTime updateTs, String size, Long sensorId, String type)
	{
		super();
		
		this.id = id;
		this.name = name;
		this.number = number;
		this.highMoisture = highMoisture;
		this.lowMoisture = lowMoisture;
		this.dateObtain = dateObtain;
		this.monitor = monitor;
		this.dead = dead;
		this.delete = delete;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
		this.size = size;
		this.sensorId = sensorId;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return buildJSON().toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject buildJSON()
	{
		JSONObject json = new JSONObject();

		json.put("id", id);
		json.put("name", name);
		if(number != null) json.put("number", number);
		json.put("highMoisture", highMoisture);
		json.put("lowMoisture", lowMoisture);
		if(dateObtain != null) json.put("dateObtain", dateObtain.toString());
		json.put("monitor", monitor);
		json.put("dead", dead);
		json.put("delete", delete);
		json.put("createBy", createBy);
		json.put("updateBy", updateBy);
		json.put("createTs", createTs.toString());
		json.put("updateTs", updateTs.toString());
		json.put("size", size);
		if(sensorId != null) json.put("sensorId", sensorId);
		if(type != null) json.put("type", type);
		
		return json;
	}
}
