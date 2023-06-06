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

package com.livk.autoconfigure.easyexcel.resolver;

import com.livk.autoconfigure.easyexcel.annotation.ExcelImport;
import com.livk.autoconfigure.easyexcel.annotation.ExcelParam;
import com.livk.autoconfigure.easyexcel.listener.ExcelMapReadListener;
import com.livk.autoconfigure.easyexcel.utils.EasyExcelSupport;
import com.livk.autoconfigure.easyexcel.utils.ExcelDataType;
import com.livk.commons.bean.util.BeanUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <p>
 * ExcelMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ExcelMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasMethodAnnotation(ExcelImport.class) &&
			parameter.hasParameterAnnotation(ExcelParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		ExcelImport excelImport = parameter.getMethodAnnotation(ExcelImport.class);
		ExcelParam excelParam = parameter.getParameterAnnotation(ExcelParam.class);
		if (Objects.nonNull(excelImport) && Objects.nonNull(excelParam)) {
			ExcelMapReadListener<?> listener = BeanUtils.instantiateClass(excelImport.parse());
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			InputStream in = getInputStream(request, excelParam.fileName());
			ExcelDataType dataType = ExcelDataType.match(parameter.getParameterType());
			Class<?> excelModelClass = dataType.getFunction().apply(ResolvableType.forMethodParameter(parameter));
			EasyExcelSupport.read(in, excelModelClass, listener, excelImport.ignoreEmptyRow());
			return listener.getData(dataType);
		}
		throw new IllegalArgumentException("Excel upload request resolver error, @ExcelData parameter type error");
	}

	private InputStream getInputStream(HttpServletRequest request, String fileName) {
		try {
			if (request instanceof MultipartRequest multipartRequest) {
				MultipartFile file = multipartRequest.getFile(fileName);
				Assert.notNull(file, "file not be null");
				return file.getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
