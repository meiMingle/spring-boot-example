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

package com.livk.oss.aliyun.controller;

import com.aliyun.oss.OSS;
import com.livk.autoconfigure.oss.support.OSSTemplate;
import com.livk.commons.spring.context.SpringContextHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("oss")
@RequiredArgsConstructor
public class OssController {

	public final OSSTemplate ossTemplate;

	@PostConstruct
	public void init() {
		if (ossTemplate.exist("livk-cloud")) {
			ossTemplate.removeBucketAndObj("livk-cloud");
		}
	}

	@GetMapping("test")
	public HttpEntity<String> test() throws IOException {
		log.info("minioClient: {}", SpringContextHolder.getBean(OSS.class));
		InputStream stream = new ClassPathResource("test.jpg").getInputStream();
		ossTemplate.createBucket("livk-cloud");
		if (!ossTemplate.exist("livk-cloud", "test.jpg")) {
			ossTemplate.upload("livk-cloud", "test.jpg", stream);
		}
		String url = ossTemplate.getExternalLink("livk-cloud", "test.jpg");
		return ResponseEntity.ok(url);
	}

}
