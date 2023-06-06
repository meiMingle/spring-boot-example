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

package com.livk.redis.listener;

import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * LivkStreamListener
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LivkStreamListener
	implements StreamListener<String, ObjectRecord<String, String>>, InitializingBean, DisposableBean {

	private final UniversalRedisTemplate livkRedisTemplate;

	private StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer;

	@Override
	public void onMessage(ObjectRecord<String, String> message) {
		log.info("id:{}", message.getId().getSequence());
		log.info("value:{}", message.getValue());
		log.info("stream:{}", message.getStream());
	}

	@Override
	public void afterPropertiesSet() {
		if (Boolean.TRUE.equals(livkRedisTemplate.hasKey("livk-streamKey"))) {
			StreamInfo.XInfoGroups groups = livkRedisTemplate.opsForStream().groups("livk-streamKey");
			if (groups.isEmpty()) {
				livkRedisTemplate.opsForStream().createGroup("livk-streamKey", "livk-group");
			}
		} else {
			livkRedisTemplate.opsForStream().createGroup("livk-streamKey", "livk-group");
		}

		StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options
			= StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().batchSize(10)
			.executor(new ThreadPoolExecutor(4, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10)))
			.errorHandler(t -> log.error("ERROR:{}", t.getMessage())).pollTimeout(Duration.ZERO)
			.serializer(RedisSerializer.string()).targetType(String.class).build();
		this.listenerContainer = StreamMessageListenerContainer
			.create(Objects.requireNonNull(livkRedisTemplate.getConnectionFactory()), options);

		this.listenerContainer.receive(Consumer.from("livk-group", "livk"),
			StreamOffset.create("livk-streamKey", ReadOffset.lastConsumed()), this);
		this.listenerContainer.start();
	}

	@Override
	public void destroy() {
		this.listenerContainer.stop();
	}

}
