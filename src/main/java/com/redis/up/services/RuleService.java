package com.redis.up.services;



import com.redis.up.dtos.RuleDTO;
import com.redis.up.dtos.RuleResponseDTO;

import java.util.List;

public interface RuleService {
	void save(RuleDTO ruleDTO);
	
	RuleResponseDTO findAll();
	
	Object findByKey(String key);
	
	void delete(String key);
	
	void delete(List<String> keys);
}
