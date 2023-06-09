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

package com.livk.autoconfigure.useragent.servlet;

import com.livk.autoconfigure.useragent.UserAgentHelper;
import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.domain.UserAgent;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * AbstractUserAgentHandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class UserAgentResolver implements HandlerMethodArgumentResolver {

	@Override
	public final boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserAgentInfo.class);
	}

	@Override
	public final Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		UserAgent agentContext = UserAgentContextHolder.getUserAgentContext();
		if (agentContext == null) {
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			Assert.notNull(request, "request not be null!");
			HttpHeaders headers = WebUtils.headers(request);
			agentContext = UserAgentHelper.convert(headers);
			UserAgentContextHolder.setUserAgentContext(agentContext);
		}
		return agentContext;
	}
}
