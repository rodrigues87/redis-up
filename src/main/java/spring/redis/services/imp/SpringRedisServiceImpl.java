package spring.redis.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import spring.redis.exceptions.CannotConvertDataException;
import spring.redis.exceptions.CannotReadValueAsStringException;
import spring.redis.services.LiveloRedisService;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class LiveloRedisServiceImpl implements LiveloRedisService {
	
	protected final RedisTemplate<String, String> redisTemplate;
	
	protected final ObjectMapper objectMapper;
	
	public LiveloRedisServiceImpl(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}
	
	public void save(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	@Override
	public void save(String key, Object object) {
		try {
			String jsonObject = objectMapper.writeValueAsString(object);
			save(key, jsonObject);
		} catch (JsonProcessingException e) {
			throw new CannotReadValueAsStringException(object);
		}
	}
	
	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	
	@Override
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}
	
	@Override
	public Set<String> findAllKeys() {
		return redisTemplate.keys("*");
	}
	
	
	@Override
	public <T> Optional<T> findByKey(String key, Class<T> className) {
		String jsonObject = redisTemplate.opsForValue().get(key);
		try {
			if (jsonObject != null) {
				T object = objectMapper.readValue(jsonObject, className);
				return Optional.of(object);
			} else {
				return Optional.empty();
			}
		} catch (JsonProcessingException e) {
			throw new CannotConvertDataException(className.getName());
		}
	}
	
	@Override
	public <T> Optional<T> findByKeyWithRetry(String key, String keyToRetry, Class<T> className) {
		Optional<T> objectFound = findByKey(key, className);
		if (objectFound.isPresent()) {
			return objectFound;
		} else {
			return findByKey(keyToRetry, className);
		}
	}
	
}
