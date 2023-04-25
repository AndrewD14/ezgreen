package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.HistoryLuminosity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryLuminosityResponse extends EZGreenResponse
{
	List<HistoryLuminosity> historyLuminosities;
}