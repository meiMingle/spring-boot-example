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

package com.livk.auto.service.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.core.test.tools.SourceFile;
import org.springframework.core.test.tools.TestCompiler;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author livk
 */
class SpringAutoServiceProcessorTest {

	@Test
	public void testAutoConfiguration() {
		compile(SpringContext.class, "org.springframework.boot.autoconfigure.AutoConfiguration");
	}

	@Test
	public void testEnableAuto() {
		compile(SpringAutoContext.class, EnableAuto.class.getName());
	}

	private void compile(Class<?> type, String annotationName) {
		SpringAutoServiceProcessor serviceProcessor = new SpringAutoServiceProcessor();
		SourceFile sourceFile = SourceFile.forTestClass(type);
		TestCompiler testCompiler = TestCompiler.forSystem().withProcessors(serviceProcessor).withSources(sourceFile);
		testCompiler.compile(compiled -> {
			try {
				Enumeration<URL> resources = compiled.getClassLoader()
					.getResources("META-INF/spring/" + annotationName + ".imports");
				List<String> configList = new ArrayList<>();
				for (URL url : Collections.list(resources)) {
					InputStream inputStream = new UrlResource(url).getInputStream();
					String[] arr = new String(FileCopyUtils.copyToByteArray(inputStream)).split("\n");
					configList.addAll(Arrays.stream(arr).map(String::trim).toList());
				}
				Assertions.assertTrue(configList.contains(type.getName()));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
