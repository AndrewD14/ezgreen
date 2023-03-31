package com.ezgreen.models;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name= "Board")
@Table(name="board")
@Setter
@Getter
public class Board implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false, unique = true, updatable = false, insertable = false)
	private Long id;

	@Column(name="bus", nullable = false)
	private Integer bus;
	
	@Column(name="number", nullable = false)
	private Integer number;

	public Board()
	{
		
	}
	
	public Board(Long id, Integer bus, Integer number)
	{
		super();
		
		this.id = id;
		this.bus = bus;
		this.number = number;
	}
}
