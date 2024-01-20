package spring.redis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import spring.redis.dtos.RuleDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	// Injetar uma instância mock do RuleService para simular o comportamento do serviço.
	
	@Test
	void findAll() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/rules"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void create() throws Exception {
		RuleDTO ruleDTO = RuleDTO.builder().key("key").value("value").build();
		String value = objectMapper.writeValueAsString(ruleDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/rules")
						.content(value)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	void findByKey() throws Exception {
		String key = "exampleKey";
		
		mockMvc.perform(MockMvcRequestBuilders.get("/rules/{key}", key))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void delete() throws Exception {
		String key = "exampleKey";
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/rules/{key}", key))
				.andExpect(status().isOk());
	}
}