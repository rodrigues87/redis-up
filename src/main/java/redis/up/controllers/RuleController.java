package redis.up.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.up.dtos.RuleDTO;
import redis.up.dtos.RuleResponseDTO;
import redis.up.services.RuleService;

@RestController
@ConditionalOnProperty(name = "spring.redis.controller.enable", havingValue = "true", matchIfMissing = true)
@RequestMapping("/rules")
public class RuleController {
	
	private final RuleService ruleService;
	
	public RuleController(RuleService ruleService) {
		this.ruleService = ruleService;
	}
	
	@GetMapping
	public ResponseEntity<RuleResponseDTO> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(ruleService.findAll());
	}
	
	@PostMapping
	public ResponseEntity<Void> create(@RequestBody RuleDTO ruleDTO) {
		ruleService.save(ruleDTO);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{key}")
	public ResponseEntity<Object> findByKey(@PathVariable String key) {
		return ResponseEntity.ok().body(ruleService.findByKey(key));
	}
	
	@DeleteMapping("/{key}")
	public ResponseEntity<Void> delete(@PathVariable String key) {
		ruleService.delete(key);
		return ResponseEntity.ok().build();
	}
}
