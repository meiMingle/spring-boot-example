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

package com.livk.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * MqttProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(MqttProperties.MQTT)
public class MqttProperties {

	public static final String MQTT = "spring.mqtt";

	private String username;

	private String password;

	private String url;

	private int connectTimeout = 10;

	private int keepAliveInterval = 20;

	private Meta sender;

	private Meta receiver;

	@Data
	public static class Meta {

		private String clientId;

		private String defaultTopic;

	}

}
