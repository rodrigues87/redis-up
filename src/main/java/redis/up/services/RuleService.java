package redis.up.services;



import redis.up.dtos.RuleDTO;
import redis.up.dtos.RuleResponseDTO;

import java.util.List;

public interface RuleService {
	void save(RuleDTO ruleDTO);
	
	RuleResponseDTO findAll();
	
	Object findByKey(String key);
	
	void delete(String key);
	
	void delete(List<String> keys);
}
