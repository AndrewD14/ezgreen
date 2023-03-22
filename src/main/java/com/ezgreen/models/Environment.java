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

	@Column(name="location", nullable = false)
	private String location;
	
	@Column(name="sensor_id")
	private Integer sensorId;

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
	
	public Environment(Long id, String location, Integer sensorId,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.location = location;
		this.sensorId = sensorId;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
