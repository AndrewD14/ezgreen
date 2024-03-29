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

@Entity(name= "PotSize")
@Table(name="pot_size")
@Setter
@Getter
public class PotSize implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name="name", nullable = false)
	private String name;

	@Column(name="size", nullable = false)
	private String size;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime updateTs;
	
	public PotSize()
	{
		
	}
	
	public PotSize(Long id, String name, String size, String createBy, String updateBy,
			LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();

		this.id = id;
		this.name = name;
		this.size = size;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
