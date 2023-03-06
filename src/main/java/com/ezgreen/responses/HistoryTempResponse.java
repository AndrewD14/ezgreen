package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.HistoryTemp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryTempResponse extends EZGreenResponse
{
	List<HistoryTemp> historyTemps;
}