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

@Entity(name= "Relay")
@Table(name="relay")
@Setter
@Getter
public class Relay implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;
	
	@Column(name="number", nullable = false)
	private Integer number;
	
	@Column(name="type", nullable = false)
	private String type;
	
	@Column(name="board_id", nullable = false)
	private Integer boardId;
	
	@Column(name="relay", nullable = false)
	private Integer relay;
	
	@Column(name="zone_id")
	private Integer zoneId;
	
	@Column(name="delete")
	private Integer delete;

	@Column(name = "created_by", nullable = false)
	private String createBy;
	
	@Column(name = "updated_by", nullable = false)
	private String updateBy;
	
	@Column(name = "created_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime createTs;
	
	@Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
	private LocalDateTime updateTs;
	
	public Relay()
	{
		
	}
	
	public Relay(Long id, Integer number, String type, Integer boardId, Integer relay, Integer zoneId, Integer delete,
			String createBy, String updateBy, LocalDateTime createTs, LocalDateTime updateTs)
	{
		super();
		
		this.id = id;
		this.number = number;
		this.type = type;
		this.boardId = boardId;
		this.relay = relay;
		this.zoneId = zoneId;
		this.delete = delete;
		this.createBy = createBy;
		this.updateBy = updateBy;
		this.createTs = createTs;
		this.updateTs = updateTs;
	}
}
