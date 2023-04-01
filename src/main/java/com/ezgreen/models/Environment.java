package com.ezgreen.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name= "Environment")
@Table(name="environment")
@Setter
@Getter
public class Environment implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;

	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="sensor_type", nullable = false)
	private Long sensorType;
	
	@Column(name="low_desire")
	private Double lowDesire;
	
	@Column(name="highDesire")
	private Double highDesire;
	
	@Column(name="target")
	private Double target;
	
	@Column(name="humidity")
	private Double humidity;
	
	@Column(name = "time_start", columnDefinition = "TIME WITHOUT TIME ZONE")
	private LocalTime timeStart;
	
	@Column(name = "time_end", columnDefinition = "TIME WITHOUT TIME ZONE")
	private LocalTime timeEnd;
	
	@Column(name="delete", nullable = false)
	private Integer delete;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime updateTs;
	
	public Environment()
	{
		
	}
	
	public Environment(Long id, String name, Long sensorType, Double lowDesire, Double highDesire,
			Double target, Double humidity, LocalTime timeStart, LocalTime timeEnd, Integer delete,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.name = name;
		this.sensorType = sensorType;
		this.lowDesire = lowDesire;
		this.highDesire = highDesire;
		this.target = target;
		this.humidity = humidity;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.delete = delete;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
