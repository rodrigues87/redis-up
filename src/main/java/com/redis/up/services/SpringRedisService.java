package com.redis.up.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SpringRedisService {
	
	void save(String key, Object object);
	
	<T> Optional<T> findByKey(String chaveRedis, Class<T> categoryRuleResponseDTOClass);
	
	<T> Optional<T> findByKeyWithRetry(String key, String keyToRetry, Class<T> className);
	
	void delete(String key);
	
	void delete(List<String> keys);
	
	Set<String> findAllKeys();
}
