package com.ezgreen.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name= "HistorySoilMoisture")
@Table(name="history_soil_moisture")
@Setter
@Getter
public class HistorySoilMoisture implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name="plant_id", nullable = false)
	private Long plantId;
	
	@Column(name="sensor_id", nullable = false)
	private Long sensorId;
	
	@Column(name = "read", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime read;
	
	@Column(name="volt", nullable = false)
	private Double volt;
	
	@Column(name="percentage", nullable = false)
	private Double percentage;
	
	@Column(name="low_calibration", nullable = false)
	private Double lowCalibration;
	
	@Column(name="high_calibration", nullable = false)
	private Double highCalibration;
	
	@Column(name="low_desire", nullable = false)
	private Double lowDesire;
	
	@Column(name="high_desire", nullable = false)
	private Double highDesire;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime updateTs;
	
	public HistorySoilMoisture()
	{
		
	}
	
	public HistorySoilMoisture(Long id, Long plantId, Long sensorId, LocalDateTime read, Double volt, Double percentage,
			Double lowCalibration, Double highCalibration, Double lowDesire, Double highDesire,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.plantId = plantId;
		this.sensorId = sensorId;
		this.read = read;
		this.volt = volt;
		this.percentage = percentage;
		this.lowCalibration = lowCalibration;
		this.highCalibration = highCalibration;
		this.lowDesire = lowDesire;
		this.highDesire = highDesire;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
