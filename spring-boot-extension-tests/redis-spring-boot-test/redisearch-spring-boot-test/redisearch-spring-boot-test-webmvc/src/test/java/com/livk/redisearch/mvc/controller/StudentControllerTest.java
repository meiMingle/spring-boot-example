/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.redisearch.mvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * StudentControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/student"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0:9].name", "livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6",
					"livk-7", "livk-8", "livk-9")
				.exists());

		mockMvc.perform(get("/student").param("query", "@class:{1班}"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0].name", "livk-0").exists());

		mockMvc.perform(get("/student").param("query", "livk"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0:9].name", "livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6",
					"livk-7", "livk-8", "livk-9")
				.exists());

		mockMvc.perform(get("/student").param("query", "女")).andExpect(status().isOk()).andDo(print());

		mockMvc.perform(get("/student").param("query", "是一个学生")).andExpect(status().isOk()).andDo(print());
	}

}
