package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.HistoryWaterLevel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryWaterLevelResponse extends EZGreenResponse
{
	List<HistoryWaterLevel> historyWaterLevels;
}