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

package com.livk.auth.server;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.nimbusds.jose.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * AuthServerAppTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthServerAppTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void testPassword() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("grant_type", "password");
		params.set("username", "livk");
		params.set("password", "123456");
		params.set("scope", "livk.read");
		String json = mockMvc.perform(post("/oauth2/token")
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode("livk-client:secret"))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.params(params))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("sub", "livk").exists())
			.andExpect(jsonPath("iss", "http://localhost:9000").exists())
			.andExpect(jsonPath("token_type", "Bearer").exists())
			.andExpect(jsonPath("client_id", "livk-client").exists())
			.andReturn()
			.getResponse()
			.getContentAsString();
		String accessToken = JsonMapperUtils.readTree(json).get("access_token").asText();
		assertNotNull(accessToken);
		assertFalse(accessToken.isBlank());
	}

	@Test
	public void testSms() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.set("grant_type", "sms");
		params.set("mobile", "18664960000");
		params.set("code", "123456");
		params.set("scope", "livk.read");
		String json = mockMvc.perform(post("/oauth2/token")
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode("livk-client:secret"))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.params(params))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("sub", "livk").exists())
			.andExpect(jsonPath("iss", "http://localhost:9000").exists())
			.andExpect(jsonPath("token_type", "Bearer").exists())
			.andExpect(jsonPath("client_id", "livk-client").exists())
			.andReturn()
			.getResponse()
			.getContentAsString();
		String accessToken = JsonMapperUtils.readTree(json).get("access_token").asText();
		assertNotNull(accessToken);
		assertFalse(accessToken.isBlank());
	}
}
