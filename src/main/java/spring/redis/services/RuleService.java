package redis.services;

import redis.dtos.RuleDTO;
import redis.dtos.RuleResponseDTO;

import java.util.List;

public interface RuleService {
	void save(RuleDTO ruleDTO);
	
	RuleResponseDTO findAll();
	
	Object findByKey(String key);
	
	void delete(String key);
	
	void delete(List<String> keys);
}
