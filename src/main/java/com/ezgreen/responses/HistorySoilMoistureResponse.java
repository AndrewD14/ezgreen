package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.HistorySoilMoisture;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistorySoilMoistureResponse extends EZGreenResponse
{
	List<HistorySoilMoisture> historySoilMoistures;
}