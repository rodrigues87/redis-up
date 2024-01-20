package spring.redis.services;



import spring.redis.dtos.RuleDTO;
import spring.redis.dtos.RuleResponseDTO;

import java.util.List;

public interface RuleService {
	void save(RuleDTO ruleDTO);
	
	RuleResponseDTO findAll();
	
	Object findByKey(String key);
	
	void delete(String key);
	
	void delete(List<String> keys);
}
