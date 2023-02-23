package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.Config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConfigResponse extends EZGreenResponse
{
	List<Config> configs;
}
