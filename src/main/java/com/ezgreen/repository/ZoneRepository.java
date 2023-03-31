package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezgreen.models.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long>
{

}
