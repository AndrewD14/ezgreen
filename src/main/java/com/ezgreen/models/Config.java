package com.ezgreen.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name= "Config")
@Table(name="config")
@Setter
@Getter
public class Config implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Integer id;

	@Column(name="name")
	private String name;

	@Column(name = "created_by")
	private String createBy;
	
	@Column(name = "updated_by")
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime  updateTs;
	
	public Config()
	{
		
	}
	
	public Config(Integer id, String name, String createBy, String updateBy, LocalDateTime createTs,
			LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.name = name;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
