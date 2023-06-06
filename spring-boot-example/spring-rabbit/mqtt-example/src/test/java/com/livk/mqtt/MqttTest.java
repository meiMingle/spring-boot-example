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

package com.livk.mqtt;

import com.livk.mqtt.handler.MqttSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * MqttTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest(classes = MqttApp.class)
public class MqttTest {

	@Autowired
	MqttSender mqttSender;

	@Test
	public void test() {
		for (int i = 0; i < 100; i++) {
			mqttSender.sendToMqtt("hello" + (i + 1));
		}
	}

}
