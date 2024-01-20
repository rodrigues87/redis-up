package redis.services.imp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.dtos.RuleDTO;
import redis.dtos.RuleResponseDTO;
import redis.services.LiveloRedisService;
import redis.services.RuleService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RuleServiceImpl implements RuleService {
	
	private final LiveloRedisService liveloRedisService;
	
	public RuleServiceImpl(LiveloRedisService liveloRedisService) {
		this.liveloRedisService = liveloRedisService;
	}
	
	@Override
	public void save(RuleDTO ruleDTO) {
		liveloRedisService.save(ruleDTO.getKey(), ruleDTO.getValue());
	}
	
	@Override
	public RuleResponseDTO findAll() {
		Set<String> keys = liveloRedisService.findAllKeys();
		if (Objects.isNull(keys) || keys.isEmpty()) {
			return RuleResponseDTO.builder().rules(Collections.emptyList()).build();
		}
		return RuleResponseDTO
				.builder()
				.rules(keys.stream().map(key ->
						RuleDTO
								.builder()
								.key(key)
								.value(liveloRedisService.findByKey(key, Object.class))
								.build()).collect(Collectors.toList()))
				.build();
	}
	
	@Override
	public Object findByKey(String key) {
		return liveloRedisService.findByKey(key, Object.class);
	}
	
	@Override
	public void delete(String key) {
		liveloRedisService.delete(key);
	}
	
	@Override
	public void delete(List<String> keys) {
		liveloRedisService.delete(keys);
	}
}
