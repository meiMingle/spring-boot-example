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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.livk.auto.service.annotation.SpringFactories;

/**
 * <p>
 * SpringFactoriesProcessor
 * </p>
 *
 * @author livk
 */
@AutoService(Processor.class)
public class SpringFactoriesProcessor extends CustomizeAbstractProcessor {

	private static final Class<SpringFactories> SUPPORT_CLASS = SpringFactories.class;

	private static final String SPRING_LOCATION = "META-INF/spring.factories";

	private static final String AOT_LOCATION = "META-INF/spring/aot.factories";

	private final SetMultimap<String, String> springFactoriesMap = Multimaps
		.synchronizedSetMultimap(LinkedHashMultimap.create());

	private final SetMultimap<String, String> aotFactoriesMap = Multimaps
		.synchronizedSetMultimap(LinkedHashMultimap.create());

	@Override
	protected Set<Class<?>> getSupportedAnnotation() {
		return Set.of(SUPPORT_CLASS);
	}

	@Override
	protected void generateConfigFiles() {
		generateConfigFiles(springFactoriesMap, SPRING_LOCATION);
		generateConfigFiles(aotFactoriesMap, AOT_LOCATION);
	}

	private void generateConfigFiles(Multimap<String, String> factoriesMap, String location) {
		if (!factoriesMap.isEmpty()) {
			try {
				FileObject resource = filer.getResource(out, "", location);
				Multimap<String, String> allImportMap = this.read(resource);
				for (Map.Entry<String, String> entry : factoriesMap.entries()) {
					allImportMap.put(entry.getKey(), entry.getValue());
				}
				FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", location);

				this.writeFile(allImportMap.asMap(), fileObject);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SUPPORT_CLASS);
		for (Element element : elements) {
			Optional<TypeElement> value = TypeElements.getAnnotationAttributes(element, SUPPORT_CLASS, "value");
			String provider = TypeElements.getBinaryName(value.orElseGet(() -> fromInterface(element)));
			if (provider == null || provider.isBlank()) {
				throw new IllegalArgumentException("current " + element + "missing @SpringFactories 'value'");
			}
			boolean aot = element.getAnnotation(SUPPORT_CLASS).aot();
			String serviceImpl = TypeElements.getBinaryName((TypeElement) element);
			if (aot) {
				aotFactoriesMap.put(provider, serviceImpl);
			}
			else {
				springFactoriesMap.put(provider, serviceImpl);
			}
		}
	}

	private TypeElement fromInterface(Element element) {
		if (element instanceof TypeElement typeElement) {
			List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
			if (interfaces != null && interfaces.size() == 1) {
				TypeMirror typeMirror = interfaces.getFirst();
				if (typeMirror instanceof DeclaredType declaredType) {
					return (TypeElement) declaredType.asElement();
				}
			}
		}
		return null;
	}

	/**
	 * 从文件读取某个接口的配置
	 * @param fileObject 文件信息
	 * @return set className
	 */
	private Multimap<String, String> read(FileObject fileObject) {
		try (BufferedReader reader = bufferedReader(fileObject)) {
			Properties properties = new Properties();
			properties.load(reader);
			Multimap<String, String> providers = LinkedHashMultimap.create();
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				String factoryTypeName = ((String) entry.getKey()).trim();
				String[] factoryImplementationNames = ((String) entry.getValue()).split(",");
				providers.putAll(factoryTypeName, Arrays.asList(factoryImplementationNames));
			}
			return providers;
		}
		catch (Exception e) {
			return LinkedHashMultimap.create();
		}
	}

	/**
	 * 将配置信息写入到文件
	 * @param allImportMap 供应商接口及实现类信息
	 * @param fileObject 文件信息
	 */
	private void writeFile(Map<String, ? extends Collection<String>> allImportMap, FileObject fileObject)
			throws IOException {
		try (BufferedWriter writer = bufferedWriter(fileObject)) {
			for (Map.Entry<String, ? extends Collection<String>> entry : allImportMap.entrySet()) {
				String providerInterface = entry.getKey();
				Collection<String> services = entry.getValue();
				writer.write(providerInterface);
				writer.write("=\\");
				writer.newLine();
				String[] serviceArrays = services.toArray(String[]::new);
				for (int i = 0; i < serviceArrays.length; i++) {
					writer.write("\t");
					writer.write(serviceArrays[i]);
					if (i != serviceArrays.length - 1) {
						writer.write(",\\");
					}
					writer.newLine();
				}
				writer.newLine();
			}
			writer.newLine();
			writer.flush();
		}
	}

}
