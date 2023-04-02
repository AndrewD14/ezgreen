package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezgreen.models.Relay;

public interface RelayRepository extends JpaRepository<Relay, Long>
{
	
}
