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

package com.livk.core.mapstruct;

import com.livk.core.mapstruct.converter.Converter;
import com.livk.core.mapstruct.converter.ConverterPair;
import com.livk.core.mapstruct.converter.MapstructRegistry;
import com.livk.core.mapstruct.repository.ConverterRepository;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class GenericMapstructService extends AbstractMapstructService implements MapstructRegistry {

	private final ConverterRepository converterRepository;

	@Override
	public <S, T> Converter<S, T> addConverter(ConverterPair converterPair, Converter<S, T> converter) {
		return converterRepository.computeIfAbsent(converterPair, converter);
	}

}
