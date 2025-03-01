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

package com.livk.spi;

import com.google.auto.service.AutoService;

import java.io.InputStream;

/**
 * <p>
 * MinioFileService
 * </p>
 *
 * @author livk
 */
@AutoService(FileService.class)
public class MinioFileService implements FileService {

	@Override
	public void upload(InputStream inputStream) {

	}

	@Override
	public byte[] download(final String filename) {
		return new byte[0];
	}

	@Override
	public String getType() {
		return this.getClass().getTypeName();
	}

}
