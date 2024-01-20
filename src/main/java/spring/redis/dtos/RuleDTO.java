package redis.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class RuleDTO {
	private String key;
	private Object value;
}
