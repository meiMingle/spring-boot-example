/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.http;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import com.livk.commons.spring.context.SpringAbstractImportSelector;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Http相关配置Selector进行IOC注入
 * <p>
 * 根据{@link EnableHttpClient}注解的value值加载对应的配置数据
 *
 * @author livk
 * @see EnableHttpClient
 */
public class HttpClientImportSelector extends SpringAbstractImportSelector<EnableHttpClient> {

	@Override
	protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		HttpClientType[] types = getValue(attributes);
		List<String> names = new ArrayList<>();
		for (HttpClientType type : types) {
			List<String> configurations = ImportCandidates.load(type.annotationType(), getBeanClassLoader())
				.getCandidates();
			names.addAll(configurations);
		}
		return names;
	}

	private HttpClientType[] getValue(AnnotationAttributes attributes) {
		Object value = attributes.get("value");
		if (!(value instanceof HttpClientType[]) && HttpClientType[].class.isArray()
				&& HttpClientType[].class.getComponentType().isInstance(value)) {
			Object array = Array.newInstance(HttpClientType[].class.getComponentType(), 1);
			Array.set(array, 0, value);
			value = array;
		}
		return (HttpClientType[]) value;
	}

}
