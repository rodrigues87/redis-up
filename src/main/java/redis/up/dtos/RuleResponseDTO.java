package redis.up.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RuleResponseDTO {
	private List<RuleDTO> rules;
}
