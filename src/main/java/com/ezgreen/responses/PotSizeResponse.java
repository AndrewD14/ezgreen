package com.ezgreen.responses;

import java.util.List;

import com.ezgreen.models.PotSize;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PotSizeResponse extends EZGreenResponse
{
	List<PotSize> potSizes;
}