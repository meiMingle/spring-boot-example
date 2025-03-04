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

package com.livk.commons.spring.context;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * SpringContextHolderTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("spring.data.redis.host=livk.com")
class SpringContextHolderTest {

	BeanTest bean = new BeanTest();

	@Test
	void getBean() {
		SpringContextHolder.registerBean(bean, "test");
		assertEquals(bean, SpringContextHolder.getBean("test"));
		assertEquals(bean, SpringContextHolder.getBean(BeanTest.class));
		assertEquals(bean, SpringContextHolder.getBean("test", BeanTest.class));
		if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
			context.removeBeanDefinition("test");
		}
	}

	@Test
	void getBeanProvider() {
		SpringContextHolder.registerBean(bean, "test");
		ResolvableType resolvableType = ResolvableType.forClass(BeanTest.class);
		assertEquals(bean, SpringContextHolder.getBeanProvider(BeanTest.class).getIfAvailable());
		assertEquals(bean, SpringContextHolder.getBeanProvider(resolvableType).getIfAvailable());
		if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
			context.removeBeanDefinition("test");
		}
	}

	@Test
	void getBeansOfType() {
		SpringContextHolder.registerBean(bean, "test");
		assertEquals(Map.of("test", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
		if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
			context.removeBeanDefinition("test");
		}
	}

	@Test
	void getProperty() {
		assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host"));
		assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host", String.class));
		assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host", String.class, "livk.cn"));
		assertEquals("livk.cn", SpringContextHolder.getProperty("spring.data.redisson.host", String.class, "livk.cn"));
	}

	@Test
	void resolvePlaceholders() {
		assertEquals("livk.com", SpringContextHolder.resolvePlaceholders("${spring.data.redis.host}"));
	}

	@Test
	@SuppressWarnings("unchecked")
	void registerBean() {
		SpringContextHolder.registerBean(bean, "test1");
		RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<BeanTest>) bean.getClass(), () -> bean);
		SpringContextHolder.registerBean(beanDefinition, "test2");
		assertEquals(Map.of("test1", bean, "test2", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
		if (SpringContextHolder.getApplicationContext() instanceof GenericApplicationContext context) {
			context.removeBeanDefinition("test1");
			context.removeBeanDefinition("test2");
		}
	}

	@Data
	static class BeanTest {

		private final Long id = 1L;

	}

}
