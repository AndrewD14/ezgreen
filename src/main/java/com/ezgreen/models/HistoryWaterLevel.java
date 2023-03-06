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

@Entity(name= "HistoryWaterLevel")
@Table(name="history_water_level")
@Setter
@Getter
public class HistoryWaterLevel implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Integer id;
	
	@Column(name = "read", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime read;
	
	@Column(name="cm", nullable = false)
	private Double cm;
	
	@Column(name="low_calibration", nullable = false)
	private Double lowCalibration;
	
	@Column(name="high_calibration", nullable = false)
	private Double highCalibration;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  updateTs;
	
	public HistoryWaterLevel()
	{
		
	}
	
	public HistoryWaterLevel(Integer id, LocalDateTime read, Double cm, Double lowCalibration, Double highCalibration,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.read = read;
		this.cm = cm;
		this.lowCalibration = lowCalibration;
		this.highCalibration = highCalibration;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
