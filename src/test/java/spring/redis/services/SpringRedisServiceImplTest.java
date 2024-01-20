package spring.redis.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.redis.exceptions.CannotConvertDataException;
import spring.redis.exceptions.CannotReadValueAsStringException;
import spring.redis.services.imp.LiveloRedisServiceImpl;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LiveloRedisServiceImplTest {
	@Mock
	private RedisTemplate<String, String> redisTemplate;
	
	@Mock
	private ValueOperations<String, String> valueOperations;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@InjectMocks
	private LiveloRedisServiceImpl redisService;
	
	@BeforeEach
	void setUp() {
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	}
	
	@Test
	void saveTest() {
		String key = "testKey";
		String value = "testValue";
		
		redisService.save(key, value);
		
		verify(valueOperations, times(1)).set(key, value);
	}
	
	@Test
	void saveObjectTest() throws JsonProcessingException {
		String key = "testKey";
		Object object = new Object();
		String jsonObject = "{\"field\":\"value\"}";
		
		when(objectMapper.writeValueAsString(object)).thenReturn(jsonObject);
		
		assertDoesNotThrow(() -> redisService.save(key, object));
	}
	
	@Test
	void findByKeyTest() throws JsonProcessingException {
		String key = "testKey";
		String jsonObject = "{\"field\":\"value\"}";
		Object object = new Object();
		
		when(valueOperations.get(key)).thenReturn(jsonObject);
		when(objectMapper.readValue(jsonObject, Object.class)).thenReturn(object);
		
		Optional<Object> result = redisService.findByKey(key, Object.class);
		
		assertEquals(Optional.of(object), result);
	}
	
	@Test
	void shouldNotfindByKeyTest() throws JsonProcessingException {
		String key = "testKey";
		String jsonObject = "{\"field\":\"value\"}";
		
		when(valueOperations.get(key)).thenReturn(jsonObject);
		when(objectMapper.readValue(jsonObject, Object.class))
				.thenThrow(new InputCoercionException(null, "my mock exception", null, null));
		
		assertThrows(
				CannotConvertDataException.class, () -> redisService.findByKey(key, Object.class));
	}
	
	@Test
	void cantFindByKeyTest() throws JsonProcessingException {
		String key = "testKey";
		
		when(valueOperations.get(key)).thenReturn(null);
		when(objectMapper.readValue((String) null, Object.class)).thenReturn(Optional.empty());
		
		Optional<Object> result = redisService.findByKey(key, Object.class);
		
		assertEquals(Optional.empty(), result);
	}
	
	@Test
	void findByKeyWithRetryTest() throws JsonProcessingException {
		String key = "key";
		String keyToRetry = "keyToRetry";
		String jsonObject = "jsonObject";
		Object skuRuleParityResponseDTO = new Object();
		
		when(valueOperations.get(key)).thenReturn(jsonObject);
		when(valueOperations.get(keyToRetry)).thenReturn(jsonObject);
		
		when(objectMapper.readValue(jsonObject, Object.class)).thenReturn(skuRuleParityResponseDTO);
		
		when(redisService.findByKeyWithRetry(key, keyToRetry, Object.class)).thenReturn(Optional.of(skuRuleParityResponseDTO));
		
		Optional<Object> result = redisService.findByKeyWithRetry(key, keyToRetry, Object.class);
		
		assertEquals(Optional.of(Optional.of(skuRuleParityResponseDTO)), result);
	}
	
	@Test
	void cantFindByKeyWithRetryTest() throws JsonProcessingException {
		String key = "key";
		String keyToRetry = "keyToRetry";
		
		when(valueOperations.get(key)).thenReturn(null);
		when(objectMapper.readValue((String) null, Object.class)).thenReturn(Optional.empty());
		
		Optional<Object> result = redisService.findByKeyWithRetry(key, keyToRetry, Object.class);
		
		assertEquals(Optional.empty(), result);
	}
	
	@Test
	void deleteTest() {
		String key = "testKey";
		
		redisService.delete(key);
		
		verify(redisTemplate, times(1)).delete(key);
	}
	
	@Test
	void deleteMultipleKeysTest() {
		List<String> keys = List.of("key1", "key2", "key3");
		
		redisService.delete(keys);
		
		verify(redisTemplate, times(1)).delete(keys);
	}
	
	@Test
	void save_ShouldThrowCannotReadValueAsStringException_WhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
		// Arrange
		String key = "testKey";
		Object object = new Object();
		
		// Configurando o comportamento do mock para lançar uma exceção
		doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(any());
		
		// Act & Assert
		assertThrows(CannotReadValueAsStringException.class, () -> redisService.save(key, object));
	}
	@Test
	void findAllKeys_ShouldReturnAllKeysFromRedis() {
		// Arrange
		Set<String> mockKeys = Set.of("key1", "key2", "key3");
		
		// Configurando o comportamento do mock para retornar as chaves simuladas
		when(redisTemplate.keys("*")).thenReturn(mockKeys);
		
		// Act
		Set<String> resultKeys = redisService.findAllKeys();
		
		// Assert
		assertEquals(mockKeys, resultKeys);
	}
	
	@Test
	void findAllKeys_ShouldReturnEmptySet_WhenNoKeysInRedis() {
		// Configurando o comportamento do mock para retornar uma lista vazia de chaves
		when(redisTemplate.keys("*")).thenReturn(Collections.emptySet());
		
		// Act
		Set<String> resultKeys = redisService.findAllKeys();
		
		// Assert
		assertEquals(Collections.emptySet(), resultKeys);
	}
}
