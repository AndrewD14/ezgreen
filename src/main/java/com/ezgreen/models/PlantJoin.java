package com.ezgreen.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name= "PlantJoin")
@Table(name="plant")
@Setter
@Getter
public class PlantJoin implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;

	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="number")
	private Integer number;
	
	@Column(name="high_moisture", nullable = false)
	private Double highMoisture;
	
	@Column(name="low_moisture", nullable = false)
	private Double lowMoisture;
	
	@Column(name="date_obtain")
	private LocalDate dateObtain;
		
	@Column(name="dead", nullable = false)
	private Integer dead;
	
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
	
	@ManyToOne
	@JoinColumn(name="pot_size_id")
	private PotSize potSize;
	
	@OneToOne
	@JoinColumn(name="sensor_id")
	private Sensor sensor;
	
	public PlantJoin()
	{
		
	}
	
	public PlantJoin(Long id, String name, Integer number, Double highMoisture,
			Double lowMoisture, LocalDate dateObtain, Integer dead, Integer delete,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.name = name;
		this.number = number;
		this.highMoisture = highMoisture;
		this.lowMoisture = lowMoisture;
		this.dateObtain = dateObtain;
		this.dead = dead;
		this.delete = delete;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
