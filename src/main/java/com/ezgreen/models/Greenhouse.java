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

@Entity(name= "Greenhouse")
@Table(name="greenhouse")
@Setter
@Getter
public class Greenhouse implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Integer id;

	@Column(name="location", nullable = false)
	private String location;
	
	@Column(name="sensor_id", nullable = false)
	private Integer sensorId;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  updateTs;
	
	public Greenhouse()
	{
		
	}
	
	public Greenhouse(Integer id, String location, Integer sensorId,
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
