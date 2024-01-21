package com.redis.up.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RuleDTO {
	private String key;
	private Object value;
}
