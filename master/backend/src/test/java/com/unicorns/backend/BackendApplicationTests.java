package com.unicorns.backend;

import com.unicorns.backend.controller.QuizController;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private QuizController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
