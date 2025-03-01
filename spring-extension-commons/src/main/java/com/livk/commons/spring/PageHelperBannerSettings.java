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

package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.util.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * 用于移除默认PageHelper Banner图的相关设置
 * </p>
 *
 * @author livk
 */
@SpringFactories
public class PageHelperBannerSettings implements EnvironmentPostProcessor {

	private static final String PAGEHELPER_BANNER = "pagehelper.banner";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (ClassUtils.isPresent("com.github.pagehelper.PageInterceptor")) {
			Boolean pageHelperBannerEnable = environment.getProperty(PAGEHELPER_BANNER, Boolean.class, false);
			System.setProperty(PAGEHELPER_BANNER, pageHelperBannerEnable.toString());
		}
	}

}
