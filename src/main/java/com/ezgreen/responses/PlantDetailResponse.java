package com.ezgreen.responses;

import java.util.List;
import com.ezgreen.models.PlantFullDetail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlantDetailResponse extends EZGreenResponse
{
	List<PlantFullDetail> plants;
}