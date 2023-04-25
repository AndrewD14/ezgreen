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

@Entity(name= "Sensor")
@Table(name="sensor")
@Setter
@Getter
public class Sensor implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name="number", nullable = false)
	private Integer number;

	@Column(name="type_id", nullable = false)
	private Integer typeId;
	
	@Column(name="board_id", nullable = false)
	private Long boardId;
	
	@Column(name="port", nullable = false)
	private Integer port;
	
	@Column(name="low_calibration")
	private Double lowCalibration;
	
	@Column(name="high_calibration")
	private Double highCalibration;
	
	@Column(name="environment_id")
	private Integer environmentId;
	
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
	
	public Sensor()
	{
		
	}
	
	public Sensor(Long id, Integer number, Integer typeId, Long boardId, Integer port, Double lowCalibration,
			Double highCalibration, Integer environmentId, Integer delete,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.number = number;
		this.typeId = typeId;
		this.boardId = boardId;
		this.port = port;
		this.lowCalibration = lowCalibration;
		this.highCalibration = highCalibration;
		this.environmentId = environmentId;
		this.delete = delete;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
