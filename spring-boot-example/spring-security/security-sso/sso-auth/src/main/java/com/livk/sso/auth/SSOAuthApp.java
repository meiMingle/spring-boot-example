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

package com.livk.sso.auth;

import com.livk.commons.SpringLauncher;
import com.livk.sso.auth.mapper.UserMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>
 * SSOAuthApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SSOAuthApp {

	public static void main(String[] args) {
		SpringLauncher.run(args);
	}

	@Bean
	public ApplicationRunner applicationRunner(UserMapper userMapper, JdbcTemplate jdbcTemplate) {
		return args -> {
			if (userMapper.getByUserName("livk") == null) {
				jdbcTemplate.execute("INSERT INTO users (id, username, password)\n"
						+ "VALUES (1, 'livk', '$2a$10$LepUx6I/1y0Pc614ZqSK6eXvoMbNDjdjAKqV/GQ4C97b0pw/kiuBC')");
			}
		};
	}

}
