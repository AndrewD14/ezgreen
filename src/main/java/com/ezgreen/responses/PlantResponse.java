package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Plant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlantResponse extends EZGreenResponse
{
	List<Plant> plants;
}