package redis.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.dtos.RuleDTO;
import redis.dtos.RuleResponseDTO;
import redis.services.imp.LiveloRedisServiceImpl;
import redis.services.imp.RuleServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RuleServiceImplTest {
	
	@Mock
	private LiveloRedisServiceImpl liveloRedisService;
	
	@InjectMocks
	private RuleServiceImpl ruleService;
	
	@Mock
	private RedisTemplate<String, String> redisTemplate;
	
	@MockBean
	private ObjectMapper objectMapper;
	
	@Mock
	private ValueOperations<String, String> valueOperations;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void save_ShouldCallLiveloRedisServiceSave() {
		// Arrange
		RuleDTO ruleDTO = RuleDTO.builder().value("value").key("key").build();
		
		// Act
		ruleService.save(ruleDTO);
		
		// Assert
		verify(liveloRedisService, times(1)).save(ruleDTO.getKey(), ruleDTO.getValue());
	}
	
	@Test
	void findAll_ShouldReturnEmptyResponseDTO_WhenNoKeysFound() {
		// Arrange
		when(liveloRedisService.findAllKeys()).thenReturn(Collections.emptySet());
		
		// Act
		RuleResponseDTO result = ruleService.findAll();
		
		// Assert
		assertNotNull(result);
		assertTrue(result.getRules().isEmpty());
	}
	
	@Test
	void findAll_ShouldReturnResponseDTO_WhenKeysFound() {
		// Arrange
		Set<String> mockKeys = Set.of("key1", "key2");
		when(liveloRedisService.findAllKeys()).thenReturn(mockKeys);
		
		
		// Mocking findByKey calls
		when(liveloRedisService.findByKey("key1", Object.class)).thenReturn(Optional.of("value1"));
		when(liveloRedisService.findByKey("key2", Object.class)).thenReturn(Optional.of("value2"));
		
		// Act
		RuleResponseDTO result = ruleService.findAll();
		
		// Assert
		assertEquals(2, result.getRules().size());
		
		result.getRules().forEach(ruleDTO -> {
			if (ruleDTO.getKey().equals("key1")) {
				assertEquals(Optional.of("value1"), ruleDTO.getValue());
			}
			if (ruleDTO.getKey().equals("key2")) {
				assertEquals(Optional.of("value2"), ruleDTO.getValue());
			}
		});
	}
	
	@Test
	void findByKey_ShouldCallLiveloRedisServiceFindByKey() {
		// Arrange
		String key = "key";
		
		// Act
		ruleService.findByKey(key);
		
		// Assert
		verify(liveloRedisService, times(1)).findByKey(key, Object.class);
	}
	
	@Test
	void delete_ShouldCallLiveloRedisServiceDelete() {
		// Arrange
		String key = "key";
		
		// Act
		ruleService.delete(key);
		
		// Assert
		verify(liveloRedisService, times(1)).delete(key);
	}
	
	@Test
	void delete_ShouldCallLiveloRedisServiceDeleteForMultipleKeys() {
		// Arrange
		List<String> keys = Arrays.asList("key1", "key2");
		
		// Act
		ruleService.delete(keys);
		
		// Assert
		verify(liveloRedisService, times(1)).delete(keys);
	}
	
}
