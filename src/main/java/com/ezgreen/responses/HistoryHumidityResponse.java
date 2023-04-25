package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.HistoryHumidity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryHumidityResponse extends EZGreenResponse
{
	List<HistoryHumidity> historyHumidities;
}