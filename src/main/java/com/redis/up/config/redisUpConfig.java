package com.redis.up.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.up.services.SpringRedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import com.redis.up.services.imp.SpringRedisServiceImpl;

@Configuration
public class redisUpConfig {
	protected final RedisTemplate<String, String> redisTemplate;
	
	protected final ObjectMapper objectMapper;
	
	public redisUpConfig(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}
	
	@Bean
	public SpringRedisService springRedisService(){
		return new SpringRedisServiceImpl(redisTemplate, objectMapper);
	}
}
